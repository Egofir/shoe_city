package com.by.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.by.aop.annotation.LoginUser;
import com.by.entity.Address;
import com.by.entity.ResultEntity;
import com.by.entity.User;
import com.by.service.IAddressService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/addressController")
public class AddressController {

    @Resource
    private IAddressService addressService;

    @RequestMapping("/getAddressListByUid")
    public List<Address> getAddressListByUid(Integer uid) {

        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);

        return addressService.list(queryWrapper);
    }

    @RequestMapping("/addAddress")
    @LoginUser
    public ResultEntity<?> addAddress(User user, Address address) {
        address.setUid(user.getId());
        addressService.addAddress(address);
        return ResultEntity.success();
    }

    @RequestMapping("/getAddressById")
    public Address getAddressById(Integer addressId) {
        return addressService.getById(addressId);
    }


}
