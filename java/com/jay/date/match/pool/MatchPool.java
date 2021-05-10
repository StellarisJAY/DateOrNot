package com.jay.date.match.pool;

import com.jay.date.mapper.MatchingMapper;
import com.jay.date.match.MessageSender;
import com.jay.date.model.MatchedUserDTO;
import com.jay.date.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.PatternSyntaxException;

/**
 * 匹配池，所有的匹配都在匹配池中有单独的匹配线程完成
 * 1、单个匹配池对象只拥有一个队列，每个队列上限相同
 * 2、每个匹配池对应一个匹配线程，该线程负责池队列中所有用户的匹配
 * 3、每个用户有开始时间戳，超时会弹出匹配池
 * @author Jay
 */
public class MatchPool{
    /**
     * 匹配池ID，多匹配池模式下每个匹配池的唯一标识
     */
    private final int poolId;
    /**
     * 匹配队列，所有待匹配用户在队列中等待
     */
    private final Queue<MatchPoolUserInfo> queue;

    private final ThreadPoolExecutor executor;
    protected ReentrantLock lock;
    protected Condition waitSet;
    /**
     * 入口关闭阈值
     * 当队列中用户达到一定数量后，该匹配池将对外关闭
     * 通过阈值来保持匹配效率最高
     */

    public static int ENTRANCE_CLOSE_THRESHOLD = 100;

    private final Logger logger;

    /**
     * 单个用户匹配超时时间 60s = 60,000ms
     * 从用户入队开始计时，超过该时间视为匹配失败
     */
    private static final long MATCH_TIMEOUT = 60000L;

    /**
     * 标签匹配度阈值
     * 超过阈值则视为匹配成功
     */
    private static final double TAG_MATCH_RATIO = 0.7;

    /**
     * 返回码
     */
    private static final int TIMEOUT_CODE = 1;
    private static final int SUCCESS_CODE = 2;



    /**
     * 匹配池构造器，完成初始化和匹配任务提交
     * @param poolId 池ID
     */
    public MatchPool(int poolId) {
        this.logger = LoggerFactory.getLogger(this.getClass());

        this.poolId = 1;
        this.queue = new LinkedList<>();
        this.executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(), r -> new Thread(r, "m-thread-"+poolId));
        // 启动匹配线程
        this.executor.execute(new MatchTask(this));
        lock = new ReentrantLock();
        waitSet = lock.newCondition();
        logger.info("匹配线程 " + poolId + "  已启动");
    }

    /**
     * 添加新用户到匹配池末尾
     * @param userInfo 用户信息
     * @return 添加状态
     */
    public boolean addLast(MatchPoolUserInfo userInfo){
        lock.lock();
        try{
            // 检查匹配池入口开闭
            if(queue.size() < ENTRANCE_CLOSE_THRESHOLD){
                queue.add(userInfo);
                logger.info("userId={} 进入匹配池 poolId={}, 匹配池活跃线程数：{}",
                        userInfo.getUserId(),
                        poolId, executor.getActiveCount());
                return true;
            }
            logger.info("poolId={} 已满，无法添加新用户", poolId);
            return false;
        }finally {
            lock.unlock();
        }
    }


    /**
     * 匹配任务，匹配线程执行
     * 每次取队首元素，用队首元素去和队列其他元素对比
     * 如果找到合适元素，则匹配成功
     * 如果没有，则将该元素移到队尾，将匹配权让给其他用户
     */
    static class MatchTask implements Runnable{
        /**
         * 匹配池对象
         * 该任务负责的匹配池
         */
        private final MatchPool pool;
        private final Logger logger;

        private static final int[] CONS_DAY_ARRAY = new int[] { 20, 19, 21, 20, 21, 22, 23,
                23, 23, 24, 23, 22 };

        private static final short[][] CONS_MAP = new short[][] {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
        };
        MatchTask(MatchPool pool) {
            this.pool = pool;
            this.logger = LoggerFactory.getLogger(this.getClass());
        }

        @Override
        public void run() {
            while(true){
                pool.lock.lock();
                try{
                    Queue<MatchPoolUserInfo> queue = pool.queue;
                    while(queue.isEmpty()){
                        pool.waitSet.await(200, TimeUnit.MILLISECONDS);
                    }
                    MatchPoolUserInfo first = queue.poll();

                    boolean matched = false;
                    boolean timeout = false;
                    if(first != null){
                        long currentTime = System.currentTimeMillis();
                        /*
                            该用户匹配已超时
                         */
                        if(currentTime - first.getTimestamp() > MATCH_TIMEOUT){
                            timeout = true;
                            MessageSender messageSender = SpringUtil.getBean(MessageSender.class);
                            // 发送超时反馈
                            messageSender.sendResponse(first.getUserId(), TIMEOUT_CODE, "匹配超时", null);
                            logger.info("userId={} 匹配超时，已发送反馈", first.getUserId());
                        }
                        /*
                            遍历队列，寻找第一个匹配的用户
                         */
                        Iterator<MatchPoolUserInfo> iterator = queue.iterator();
                        while(iterator.hasNext()){
                            currentTime = System.currentTimeMillis();
                            MatchPoolUserInfo currentUser = iterator.next();
                            // 发生重复，去除该用户
                            if(currentUser.getUserId().equals(first.getUserId())){
                                iterator.remove();
                                continue;
                            }
                            // 遍历的当前用户超时
                            if(currentTime - currentUser.getTimestamp() > MATCH_TIMEOUT){
                                MessageSender messageSender = SpringUtil.getBean(MessageSender.class);
                                // 发送超时反馈
                                messageSender.sendResponse(currentUser.getUserId(), TIMEOUT_CODE, "匹配超时", null);
                                logger.info("userId={} 匹配超时，已发送反馈", currentUser.getUserId());
                                iterator.remove();
                                continue;
                            }
                            // 匹配成功
                            if(match(currentUser, first)){
                                MessageSender messageSender = SpringUtil.getBean(MessageSender.class);
                                MatchingMapper matchingMapper = SpringUtil.getBean(MatchingMapper.class);
                                long queryStart = System.currentTimeMillis();
                                // 查询用户详细信息
                                MatchedUserDTO currentUserDTO = matchingMapper.getMatchedUserInfo(currentUser.getUserId());
                                MatchedUserDTO firstUserDTO = matchingMapper.getMatchedUserInfo(first.getUserId());
                                long queryEnd = System.currentTimeMillis();
                                logger.info("用户详细信息查询耗时：{}ms", queryEnd - queryStart);
                                // 发送匹配成功反馈
                                messageSender.sendResponse(first.getUserId(), SUCCESS_CODE, "匹配成功", currentUserDTO);
                                messageSender.sendResponse(currentUser.getUserId(), SUCCESS_CODE, "匹配成功", firstUserDTO);
                                logger.info("匹配成功: {} & {}, 最长等待时间={}ms", currentUser, first,
                                        Math.max(currentTime - currentUser.getTimestamp(), currentTime - first.getTimestamp()));
                                matched = true;
                                iterator.remove();
                                break;
                            }
                        }
                    }
                    /*
                        如果没有匹配成功或者没有超时，将该用户移到队尾
                     */
                    if(!matched && !timeout){
                        queue.add(first);
                    }
                }
                catch (InterruptedException e){
                    logger.info(e.getLocalizedMessage());
                    break;
                }
                finally {
                    pool.lock.unlock();
                }
            }
        }

        /**
         * 通过两个用户的信息计算出匹配度，返回是否成功匹配
         * @param userInfo1 用户1
         * @param userInfo2 用户2
         * @return 是否匹配成功
         */
        private boolean match(MatchPoolUserInfo userInfo1, MatchPoolUserInfo userInfo2){
            List<Integer> u1Tags = userInfo1.getTags();
            List<Integer> u2Tags = userInfo2.getTags();

            // 将标签排序，可以减少标签匹配复杂度
            u1Tags.sort(Comparator.comparingInt(t -> t));
            u2Tags.sort(Comparator.comparingInt(t -> t));

            int i = 0, j = 0;
            int countSameTags = 0;
            // 获取相同标签的个数
            while(i < u1Tags.size() && j < u2Tags.size()){
                int tag1 = u1Tags.get(i);
                int tag2 = u2Tags.get(j);
                if(tag1 > tag2){
                    j++;
                }
                else if(tag1 < tag2){
                    i++;
                }
                else{
                    countSameTags++;
                    i++;
                    j++;
                }
            }
            // 计算双方的标签匹配率
            double tagMatchRatio1 = (double) countSameTags / u1Tags.size();
            double tagMatchRatio2 = (double) countSameTags / u2Tags.size();
            if(tagMatchRatio1 >= TAG_MATCH_RATIO && tagMatchRatio2 >= TAG_MATCH_RATIO){
                return true;
            }
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String birthday1 = userInfo1.getBirthday();
                Date date1 = sdf.parse(birthday1);

                String birthday2 = userInfo1.getBirthday();
                Date date2 = sdf.parse(birthday2);


                int constellation1 = getConstellation(date1.getMonth(), date2.getDay());
                int constellation2 = getConstellation(date2.getMonth(), date2.getDay());
                // 星座是否匹配
                if(constellationMatch(constellation1, constellation2)){
                    return true;
                }
                // 年龄是否匹配
                return Math.abs(date1.getYear() - date2.getYear()) <= 3;
            }catch (PatternSyntaxException | ParseException e){
                logger.info(e.getMessage());
            }
            return false;
        }
        private boolean constellationMatch(int con1, int con2){
            if(con1 == con2){
                return true;
            }

            return false;
        }

        private int getConstellation(int month, int day) {
            return day < CONS_DAY_ARRAY[month - 1] ? month - 1 : month;
        }
    }


}
