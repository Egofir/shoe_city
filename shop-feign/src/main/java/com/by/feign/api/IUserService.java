package com.by.feign.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.by.entity.ResultEntity;
import com.by.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;

@FeignClient("shop-user")
public interface IUserService {

    @RequestMapping("/user/addUser")
    ResultEntity<?> addUser(@RequestBody User user);

    @RequestMapping("/user/getUserPage")
    Page<User> getUserPage(@RequestBody Map<String, Object> map);

    @RequestMapping("/user/getUserById/{id}")
    User getUserById(@PathVariable("id") Integer id);

    @RequestMapping("/user/updateUser")
    ResultEntity<?> updateUser(@RequestBody User user);

    @RequestMapping("/user/userBatchDel")
    ResultEntity<?> userBatchDel(@RequestParam("userIdList") ArrayList<Integer> userIdList);

    @RequestMapping("/user/getUserByUsername")
    User getUserByUsername(@RequestParam("username") String username);

    @RequestMapping("/user/getUserByEmail")
    User getUserByEmail(@RequestParam("emailStr") String emailStr);

}
