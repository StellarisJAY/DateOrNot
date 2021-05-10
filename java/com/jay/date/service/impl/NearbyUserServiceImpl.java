package com.jay.date.service.impl;

import com.jay.date.mapper.MatchingMapper;
import com.jay.date.model.MatchedUserDTO;
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

/**
 * @author Jay
 */
@Service
public class NearbyUserServiceImpl implements NearbyUserService {

    private final GeoService geoService;

    private final MatchingMapper matchingMapper;
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
    public NearbyUserServiceImpl(GeoService geoService, MatchingMapper matchingMapper) {
        this.geoService = geoService;
        this.matchingMapper = matchingMapper;
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

            MatchedUserDTO userDTO = matchingMapper.getMatchedUserInfo(uId);
            if(userDTO != null){
                String username = userDTO.getUser_name();
                int gender = userDTO.getSex();
                String birthday = userDTO.getBirthday();
                String description = userDTO.getIntroduction();
                String portraitUrl = userDTO.getHead_image_url();
                NearbyUserVO userVO = new NearbyUserVO(uId, username, gender, birthday, description, portraitUrl, distance, latitude, longitude);
                nearbyUsers.add(userVO);
            }
        });

        return nearbyUsers;
    }
}
