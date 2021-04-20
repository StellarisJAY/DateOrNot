package com.jay.date.service;

import com.jay.date.model.NearbyUserVO;

import java.util.List;

/**
 * @author Jay
 */
public interface NearbyUserService {

    /**
     * 列出附近用户
     * @param userId 用户id
     * @param limit 上限
     * @return 附近用户集合
     */
    List<NearbyUserVO> listNearbyUsers(Integer userId, Integer limit);


    /**
     * 列出给定坐标附近的用户
     * @param latitude 纬度
     * @param longitude 经度
     * @param limit 数量上限
     * @return 附近用户集合
     */
    List<NearbyUserVO> listNearbyUsers(Double latitude, Double longitude, Integer limit);
}
