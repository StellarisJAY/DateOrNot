package com.jay.date.service;

import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.Map;

/**
 * 提供对redis缓存geo操作的方法
 * 仅提供底层方法，不实现具体业务逻辑
 * @author Jay
 * @version 1.0
 */
public interface GeoService {

    /**
     * 保存member的地理信息
     * @param member member
     * @param latitude 纬度 double
     * @param longitude 经度 double
     * @return 操作状态
     */
    Boolean saveUserLocation(String member, Double latitude, Double longitude);

    /**
     * 列出member周围一定范围内的所有缓存项
     * @param member member
     * @param distance 范围
     * @param metric 距离单位
     * @param limit 获取数量
     * @return 结果集
     */
    GeoResults<RedisGeoCommands.GeoLocation<String>> listLocationsInRadius(String member, Double distance,
                                                                           Metric metric, int limit);

    /**
     * 列出给定坐标附近一定范围内的所有用户
     * @param latitude 纬度
     * @param longitude 经度
     * @param distance 距离
     * @param metric 距离单位
     * @param limit 获取数量
     * @return 结果集
     */
    GeoResults<RedisGeoCommands.GeoLocation<String>> listLocationsInRadius(Double latitude, Double longitude,
                                                                           Double distance, Metric metric, int limit);

    /**
     * 获取两个用户之间的距离
     * @param userId 用户id
     * @param otherUserId 另一个用户id
     * @return 距离（单位米）
     */
    Double getDistanceBetween(Integer userId, Integer otherUserId);

    /**
     * 获取用户坐标
     * @param userId 用户id
     * @return 坐标点
     */
    Map<String, Double> getUserLocation(Integer userId);


    Boolean deleteUserLocation(Integer userId);
}
