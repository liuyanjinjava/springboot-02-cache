package com.lyj.cache.config;


import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
 class FooGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {

        return method.getName();
    }
}