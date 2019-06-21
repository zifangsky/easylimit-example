package cn.zifangsky.easylimit.example.controller;

import cn.zifangsky.easylimit.example.model.Greeting;
import cn.zifangsky.easylimit.example.model.HelloMessage;
import cn.zifangsky.easylimit.example.model.SysUser;
import cn.zifangsky.easylimit.example.service.TestService;
import cn.zifangsky.easylimit.permission.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 测试使用
 *
 * @author zifangsky
 * @date 2017/12/5
 * @since 1.0.0
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Resource(name = "testServiceImpl")
    private TestService testService;


    @PostMapping(value = "/greeting", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public HelloMessage greeting(@RequestBody Greeting greeting, HttpServletRequest request){
        return testService.greeting(greeting, request.getRemoteHost());
    }

    @ResponseBody
    @RequiresPermissions("/aaa/bbb")
    @RequestMapping(value = "/selectByUsername", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SysUser selectByUsername(String username) {
        return testService.selectByUsername(username);
    }

}
