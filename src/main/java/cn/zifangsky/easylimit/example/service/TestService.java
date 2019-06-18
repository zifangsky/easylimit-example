package cn.zifangsky.easylimit.example.service;

import cn.zifangsky.easylimit.example.model.Greeting;
import cn.zifangsky.easylimit.example.model.HelloMessage;
import cn.zifangsky.easylimit.example.model.SysUser;

/**
 * 测试
 *
 * @author zifangsky
 * @date 2019/5/29
 * @since 1.0.0
 */
public interface TestService {

    HelloMessage greeting(Greeting greeting, String ip);

    SysUser selectByUsername(String username);
}
