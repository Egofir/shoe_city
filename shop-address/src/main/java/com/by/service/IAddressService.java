package com.by.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.by.entity.Address;

public interface IAddressService extends IService<Address> {
    void addAddress(Address address);
}
