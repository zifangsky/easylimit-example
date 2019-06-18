package cn.zifangsky.easylimit.example;

import cn.zifangsky.easylimit.example.mapper.SysFunctionMapper;
import cn.zifangsky.easylimit.example.mapper.SysRoleMapper;
import cn.zifangsky.easylimit.example.model.SysFunction;
import cn.zifangsky.easylimit.example.model.SysRole;
import cn.zifangsky.easylimit.session.impl.SimpleSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 测试基本SQL查询
 * @author zifangsky
 * @date 2019/02/28
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SQLTest {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysFunctionMapper functionMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testSelect(){
        Set<SysRole> set = roleMapper.selectByUserId(1L);

        System.out.println(set.size());
    }

    @Test
    public void testSelect2(){
        Set<SysFunction> set = functionMapper.selectByRoleId(1L);
        System.out.println(set.size());
    }

    @Test
    public void testRedis1(){
        HashOperations<String, Serializable, Object> opsForHash = redisTemplate.opsForHash();
        redisTemplate.delete("easylimit:session_cache");
    }

    @Test
    public void testRedis2(){
        HashOperations<String, Serializable, SimpleSession> opsForHash = redisTemplate.opsForHash();
        List<SimpleSession> list = opsForHash.values("easylimit:session_cache");
        System.out.println(list.size());
    }

}
