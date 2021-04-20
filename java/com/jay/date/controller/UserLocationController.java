package com.jay.date.controller;

import com.jay.date.model.NearbyUserVO;
import com.jay.date.service.GeoService;
import com.jay.date.service.NearbyUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Jay
 */
@RestController
@RequestMapping("/location")
@CrossOrigin
public class UserLocationController {

    private final GeoService geoService;
    private final NearbyUserService nearbyUserService;

    private final Logger logger;

    @Autowired
    public UserLocationController(GeoService geoService, NearbyUserService nearbyUserService) {
        this.geoService = geoService;
        this.nearbyUserService = nearbyUserService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    @PostMapping()
    public Boolean saveUserLocation(@RequestParam("userId") Integer userId,
                                    @RequestParam("latitude") Double latitude,
                                    @RequestParam("longitude") Double longitude){
        String member = userId.toString();
        return geoService.saveUserLocation(member, latitude, longitude);
    }

    @GetMapping("/nearby/{userId}")
    public List<NearbyUserVO> listNearbyUsers(@PathVariable("userId") Integer userId,
                                              @RequestParam("limit") Integer limit){
        return nearbyUserService.listNearbyUsers(userId, limit);
    }

    @GetMapping("/nearby")
    public List<NearbyUserVO> listNearbyUsers(@RequestParam(value = "latitude") Double latitude,
                                              @RequestParam(value = "longitude") Double longitude,
                                              @RequestParam(value = "limit") Integer limit){
        return nearbyUserService.listNearbyUsers(latitude, longitude, limit);
    }

    @GetMapping("/distance")
    public Double getDistanceBetween(@RequestParam(value = "userId") Integer userId,
                                     @RequestParam(value = "otherUserId") Integer otherUserId){
        try{
            return geoService.getDistanceBetween(userId, otherUserId);
        }
        catch (IllegalArgumentException e){
            logger.error("距离参数错误");
            return null;
        }
    }

    @GetMapping("/{userId}")
    public Map<String, Double> getUserLocation(@PathVariable("userId") Integer userId){
        try{
            return geoService.getUserLocation(userId);
        }
        catch (IllegalArgumentException e){
            logger.error("获取坐标参数错误");
            return null;
        }
    }


}
