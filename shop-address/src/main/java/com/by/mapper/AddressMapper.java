package com.by.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.by.entity.Address;

public interface AddressMapper extends BaseMapper<Address> {

    void addAddress(Address address);

}
