package com.by.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.by.entity.ResultEntity;
import com.by.entity.User;
import com.by.service.IUserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping("/addUser")
    public ResultEntity<?> addUser(@RequestBody User user) {
        return ResultEntity.responseClient(userService.save(user), "添加用户失败");
    }

    @RequestMapping("/getUserPage")
    public Page<User> getUserPage(@RequestBody Map<String, Object> map) {

        // 1、从map中取出需要的参数
        Object current = map.get("current");
        Object size = map.get("size");
        Object username = map.get("username");
        Object email = map.get("email");
        Object phone = map.get("phone");

        // 2、创建分页对象
        Page<User> page = new Page<>();
        if (!StringUtils.isEmpty(current)) {
            page.setCurrent(Integer.parseInt(current.toString()));
        }
        if (!StringUtils.isEmpty(size)) {
            page.setSize(Integer.parseInt(size.toString()));
        }

        // 3、创建条件查询对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(username)) {
            queryWrapper.like("username", username.toString());
        }
        if (!StringUtils.isEmpty(email)) {
            queryWrapper.like("email", email.toString());
        }
        if (!StringUtils.isEmpty(phone)) {
            queryWrapper.like("phone", phone.toString());
        }

        // 4、查询数据
        return userService.page(page, queryWrapper);
    }

    @RequestMapping("/getUserById/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @RequestMapping("/updateUser")
    public ResultEntity<?> updateUser(@RequestBody User user) {
        return ResultEntity.responseClient(userService.updateById(user), "更新用户信息失败");
    }

    @RequestMapping("/userBatchDel")
    public ResultEntity<?> userBatchDel(@RequestParam("userIdList") ArrayList<Integer> userIdList) {
        return ResultEntity.responseClient(userService.removeBatchByIds(userIdList),
                "删除用户失败");
    }

    @RequestMapping("/getUserByUsername")
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userService.getOne(queryWrapper);
    }

    @RequestMapping("/getUserByEmail")
    public User getUserByEmail(String emailStr) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", emailStr);
        return userService.getOne(queryWrapper);
    }

}
