package com.lyj.cache;

import com.lyj.cache.bean.Employee;
import com.lyj.cache.mapper.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot02CacheApplicationTests {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate; //操作k-v都是字符串的；

    @Autowired
    RedisTemplate redisTemplate;//操作k-v都是对象的；

    @Autowired
    RedisTemplate<Object, Employee> redisTemplate1;

    /**
     * Redis 常见的五大数据类型；
     * String(字符串)，list（列表），hash(散列)，Zset（有序）
     * stringRedisTemplate.opsForValue()[简化操作字符串的]
     * stringRedisTemplate.opsForHash()【简化操作hash】;
     * stringRedisTemplate.opsForSet()【简化操作set】;
     * stringRedisTemplate.opsForList()【简化操作List】;
     * stringRedisTemplate.opsForZSet()【简化操作Set】;
     */
    @Test
    public void test01(){
        //给redis中保存了一个数据
        //stringRedisTemplate.opsForValue().append("msg","hello");
//        String msg=stringRedisTemplate.opsForValue().get("msg");
//        System.out.println(msg);
//        stringRedisTemplate.opsForList().leftPush("mylist","1");
//        stringRedisTemplate.opsForList().leftPush("mylist","2");

    }
    @Test
    public void test02(){
        Employee empById=employeeMapper.getEmpById(1);
        //默认如果保存对象，使用jdk序列化机制，序列化后的数据保存在redis中；
        redisTemplate1.opsForValue().set("emp-01",empById);
        //1.将数据以json的方式保存；
        //(1).自己将对象转为json；
        //(2).redisTemplate有默认的序列化规则

    }
    @Test
    public void contextLoads() {
        Employee emp = employeeMapper.getEmpById(1);
        System.out.println(emp);
    }

}
