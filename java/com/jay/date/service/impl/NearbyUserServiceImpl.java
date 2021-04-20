package com.jay.date.service.impl;

import com.jay.date.model.NearbyUserVO;
import com.jay.date.service.GeoService;
import com.jay.date.service.NearbyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Metrics;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Jay
 */
@Service
public class NearbyUserServiceImpl implements NearbyUserService {

    private final GeoService geoService;

    /**
     * 默认返回 2km
     */
    private static final Double DEFAULT_RADIUS = 1.0;
    private static final Metric RADIUS_METRIC = Metrics.KILOMETERS;
    /**
     * 最大获取数量：20
     */
    private static final int MAX_LIMIT = 20;

    @Autowired
    public NearbyUserServiceImpl(GeoService geoService) {
        this.geoService = geoService;
    }

    @Override
    public List<NearbyUserVO> listNearbyUsers(Integer userId, Integer limit) {
        if(userId == null || limit == null || limit <= 0){
            throw new IllegalArgumentException();
        }
        String member = userId.toString();
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoService.listLocationsInRadius(member,
                DEFAULT_RADIUS, RADIUS_METRIC, limit > MAX_LIMIT ? MAX_LIMIT : limit);

        return geoResultToVO(results);
    }

    @Override
    public List<NearbyUserVO> listNearbyUsers(Double latitude, Double longitude, Integer limit) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoService.listLocationsInRadius(latitude,
                longitude, DEFAULT_RADIUS, RADIUS_METRIC, limit > MAX_LIMIT ? MAX_LIMIT : limit);
        return geoResultToVO(results);
    }

    /**
     * 将redis geo的结果转换成页面显示结果
     * @param results redis geo结果集
     * @return 附近用户VO集合
     */
    private List<NearbyUserVO> geoResultToVO(GeoResults<RedisGeoCommands.GeoLocation<String>> results){
        if(results == null){
            return null;
        }
        List<NearbyUserVO> nearbyUsers = new ArrayList<>();
        results.forEach((result)->{
            double distance = result.getDistance().getValue();
            Integer uId = Integer.valueOf(result.getContent().getName());
            double latitude = result.getContent().getPoint().getY();
            double longitude = result.getContent().getPoint().getX();

            // 从缓存获取各种数据
            String username = "张三";
            Integer age = 20;
            Integer gender = new Random().nextInt(2);
            String description = "hello world hello world hello ...";
            NearbyUserVO userVO = new NearbyUserVO(uId, username, gender, age, description, distance, latitude, longitude);
            nearbyUsers.add(userVO);
        });

        return nearbyUsers;
    }
}
