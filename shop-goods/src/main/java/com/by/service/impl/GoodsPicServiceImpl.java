package com.by.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.by.entity.GoodsPic;
import com.by.mapper.GoodPicMapper;
import com.by.service.IGoodsPicService;
import org.springframework.stereotype.Service;

@Service
public class GoodsPicServiceImpl extends ServiceImpl<GoodPicMapper, GoodsPic> implements IGoodsPicService {
}
