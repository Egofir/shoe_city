package com.by.feign.api;

import com.by.entity.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("shop-address")
public interface IAddressService {

    @RequestMapping("/addressController/getAddressListByUid")
    List<Address> getAddressListByUid(@RequestParam("uid") Integer uid);

    @RequestMapping("/addressController/getAddressById")
    Address getAddressById(@RequestParam("addressId") Integer addressId);

}
