package com.lyj.cache.service;

import com.lyj.cache.bean.Department;
import com.lyj.cache.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DeptService {
    @Autowired
    DepartmentMapper departmentMapper;
    /**
     * 缓存的数据能存入redis；
     * 第二次从缓存中查询就不能反序列化回来
     * 存的是dept的json数据；CacheManager默认使用
     * edisTemplate<Object, Employee>操作redis
     * @param id
     * @return
     */
    @Cacheable(value = "dept",cacheManager ="deptCacheManager")
    public Department getDeptById(Integer id){
        System.out.println("查询部门"+id);
        Department dept = departmentMapper.getDeptById(id);
        return dept;
    }
}
