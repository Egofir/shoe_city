package com.by.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.by.entity.Appraisement;
import com.by.mapper.AppMapper;
import com.by.service.IAppService;
import org.springframework.stereotype.Service;

@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, Appraisement> implements IAppService {
}
