package com.by.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.by.entity.Address;
import com.by.mapper.AddressMapper;
import com.by.service.IAddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Override
    public void addAddress(Address address) {
        baseMapper.addAddress(address);
    }

}
