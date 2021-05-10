package com.jay.date.controller;

import com.jay.date.model.GroupInfoUserVO;
import com.jay.date.model.MatchedGroupVO;
import com.jay.date.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jay
 */
@RestController
@CrossOrigin
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/search")
    public List<MatchedGroupVO> searchGroup(@RequestParam("text") String groupName, @RequestParam("limit") Integer limit){
        try{
            return groupService.searchGroupByName(groupName, limit);
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    @GetMapping("/recommend")
    public List<MatchedGroupVO> recommendGroup(@RequestParam("userId") Integer userId, @RequestParam("limit") Integer limit){
        try{
            return groupService.recommendGroup(userId, limit);
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    @GetMapping("/member/chart")
    public List<GroupInfoUserVO> getGroupMembersForChart(@RequestParam("groupId") Integer groupId){
        try{
            return groupService.getGroupMembersForChart(groupId);
        }catch (IllegalArgumentException e){
            return null;
        }
    }
}
