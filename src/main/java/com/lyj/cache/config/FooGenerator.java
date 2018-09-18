package com.lyj.cache.config;


import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
 class FooGenerator implements KeyGenerator {
    //主键自动生成策略
    //测试一下是否自动提交
    //测试一下github是否已经提交；
    @Override
    public Object generate(Object target, Method method, Object... params) {

        return method.getName();
    }
}