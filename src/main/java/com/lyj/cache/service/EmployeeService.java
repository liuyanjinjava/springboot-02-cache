package com.lyj.cache.service;

import com.lyj.cache.bean.Employee;
import com.lyj.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "emp",cacheManager = "empCacheManager")
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 将方法的结果进行缓存，以后再要相同的数据，直接从缓存中获取，不用调用方法；
     * CacheManger管理多个Cache组件，对缓存的真正的crud操作在cache组件中；
     * 每一个缓存组件有自己唯一一个名字，几个属性：
     *      CacheNames/value：指定缓存组件的名字；
     *      key：缓存数据使用的key，可以用来指定，默认是使用方法参数的值
     *      simplekeyGenerate默认的生成策略；如果没有参数；key=new Simplekey
     *      如果有一个参数，就是key=参数的值；
     *      如果有多个参数，就是key=new simplekey（params）
     *      cacheManger:指定缓存管理器，或者cacheResolver指定获取解析器；
     *      condition：指定符合条件的情况下才缓存；
     *      unless：否定缓存，当unless指定的条件为true时，方法的返回值就不会被缓存；
     *      可以获取到结果进行判断；
     *  原理：
     *   1.自动配置：CacheAutoConfiguration 缓存的自动配置类；
     *   2.缓存的配置类如下；
     *  0 = "org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration"
     *  1 = "org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration"
     *  2 = "org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration"
     *  3 = "org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration"
     *  4 = "org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration"
     *  5 = "org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration"
     *  6 = "org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration"
     *  7 = "org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration"
     *  8 = "org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration"
     *  9 = "org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration"
     *  3） 默认是SimpleCacheConfigruration生效的；
     *  4） 给容器中注册了一个CacheManager： ConcurrentMapCacheManager
     *  5） 可以获取和创建ConcurrentMapCache类型的缓存组件；他的作用将数据保存在ConCurrentMap中；
     *
     *  运行的流程是什么：
     *  1.方法运行之前，先去查询Cache(缓存组件)，按照CacheNames指定的名字获取；
     *  （CacheManger先获取相应的缓存）
     *  第一次获取缓存如果没有cache组件会自动创建出来；
     *  2、去Cache中查找缓存的内容，使用一个key，默认就是方法的参数；
     *   key是按照某种策略生成的，默认就是使用keyGenerate生成的，默认使用SimpleKeyGenerate生成key
     *  3.没有查到缓存就调用目标方法；
     *  4.将目标方法返回的结果放进缓存中；
     *
     *  一句话：@Cacheable方法执行之前先来检查缓存中有没有这个数据，默认按照参数的值作为key去查询缓存，如果没有
     *  就运行方法，将结果放入缓存中；
     *  核心：使用CacheManger【ConcurrentMapCacheManager】按照名字得到Cache【ConCurrentMap】组件；
     *  开发中使用的是缓存中间件；redis，memcache，ehcache
     *  三.整合redis作为缓存；
     *  redis是一个开源（BSD许可的），内存中的数据结构存储系统，它可以用作数据库，缓存，消息中间件；
     * @param id
     * @return
     */
    @Cacheable(cacheNames = {"emp"}/*,keyGenerator = "fooGenerator",condition ="#a0>1",unless = "#a0==2"*/)
    public Employee getEmp(Integer id){

        System.out.println("查询"+id+"员工");
        return employeeMapper.getEmpById(id);

    }

    /**
     * @CachePut:既调用方法，又更新缓存数据；
     * 修改了数据库的某个数据，同时更新缓存；
     * 1.先调用目标方法，
     * 2.将目标方法的结果缓存起来；
     * @param employee
     * @return
     * 测试步骤：
     * 1.先来查询一号员工，再来更新一号员工；
     * updateEmp:Employee
     * [id=1, lastName=zhangsan, email=zhangsan@163.com, gender=1, dId=1]
     * 2.以后查询还是之前的结果；
     * 3.更新员工
     * 方法运行以后给缓存中放数据的；
     */
   @CachePut(value ={"emp"},key = "#result.id")
    public Employee updateEmp(Employee employee){
       System.out.println("updateEmp:"+employee);
       employeeMapper.updateEmp(employee);
       return employee;
    }
    /**
     * @CacheEvict:缓存清除，
     * key:指定要清除的数据；
     * allentries,删除所有的缓存，即使有key和allEntries的配置也是allEntries
     * 优先或覆盖 allEntries = true；
     * beforeInvocation()缓存的清除是否在方法之前执行；
     * 默认代表缓存清除操作是在方法执行之后执行的；如果出现异常；缓存就不会清掉；
     */
    @CacheEvict(/*value = "emp",*/beforeInvocation = true/*key = "#id",*/)
    public void deleteEmp(Integer id){
        System.out.println("deleteEmp:"+id);
        //employeeMapper.deleteEmpById(id);
       int i=10/0;

    }
    //定义复杂的缓存规则；
    @Caching(
            cacheable = {
               @Cacheable(/*value = "emp",*/key = "#lastName")
            },
            put = {
                    @CachePut(/*value = "emp",*/key = "#result.id"),
                    @CachePut(/*value = "emp",*/key = "#result.email")

            }
    )
    public Employee getEmpByLastName(String lastName){
     return   employeeMapper.getEmpByLastName(lastName);
    }
}
