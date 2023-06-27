package com.by.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.by.entity.ResultEntity;
import com.by.entity.User;
import com.by.feign.api.IUserService;
import com.by.util.QueryMapUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/userController")
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping("/getUserPage")
    public Page<User> getUserPage(HttpServletRequest request) {
        Map<String, Object> map = QueryMapUtil.queryMap(request);

        // 调用service层查询数据
        return userService.getUserPage(map);
    }

    @RequestMapping("/addUser")
    public ResultEntity<?> addUser(User user) {
        return userService.addUser(user);
    }

    @RequestMapping("/getUserById/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @RequestMapping("/updateUser")
    public ResultEntity<?> updateUser(User user) {
        return userService.updateUser(user);
    }

    @RequestMapping("/userBatchDel")
    public ResultEntity<?> userBatchDel(@RequestParam("userIdList") ArrayList<Integer> userIdList) {
        return userService.userBatchDel(userIdList);
    }

}
