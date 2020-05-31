package cn.zifangsky.easylimit.example.config;

import cn.zifangsky.easylimit.DefaultWebSecurityManager;
import cn.zifangsky.easylimit.SecurityManager;
import cn.zifangsky.easylimit.cache.Cache;
import cn.zifangsky.easylimit.cache.impl.DefaultRedisCache;
import cn.zifangsky.easylimit.enums.ProjectModeEnums;
import cn.zifangsky.easylimit.example.easylimit.CustomRealm;
import cn.zifangsky.easylimit.example.mapper.SysFunctionMapper;
import cn.zifangsky.easylimit.example.mapper.SysRoleMapper;
import cn.zifangsky.easylimit.example.mapper.SysUserMapper;
import cn.zifangsky.easylimit.filter.impl.support.DefaultFilterEnums;
import cn.zifangsky.easylimit.filter.impl.support.FilterRegistrationFactoryBean;
import cn.zifangsky.easylimit.permission.aop.PermissionsAnnotationAdvisor;
import cn.zifangsky.easylimit.realm.Realm;
import cn.zifangsky.easylimit.session.SessionDAO;
import cn.zifangsky.easylimit.session.SessionIdFactory;
import cn.zifangsky.easylimit.session.SessionManager;
import cn.zifangsky.easylimit.session.impl.AbstractWebSessionManager;
import cn.zifangsky.easylimit.session.impl.MemorySessionDAO;
import cn.zifangsky.easylimit.session.impl.support.CookieWebSessionManager;
import cn.zifangsky.easylimit.session.impl.support.RandomCharacterSessionIdFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

/**
 * EasyLimit框架的配置
 *
 * @author zifangsky
 * @date 2019/5/28
 * @since 1.0.0
 */
@Configuration
public class EasyLimitConfig {

    /**
     * 配置缓存
     */
    @Bean
    public Cache cache(RedisTemplate<String, Object> redisTemplate){
        return new DefaultRedisCache(redisTemplate);
    }

    /**
     * 配置Realm
     */
    @Bean
    public Realm realm(SysUserMapper sysUserMapper, SysRoleMapper sysRoleMapper, SysFunctionMapper sysFunctionMapper, Cache cache){
        CustomRealm realm = new CustomRealm(sysUserMapper, sysRoleMapper, sysFunctionMapper);
        //缓存主体信息
        realm.setEnablePrincipalInfoCache(true);
        realm.setPrincipalInfoCache(cache);

        //缓存角色、权限信息
        realm.setEnablePermissionInfoCache(true);
        realm.setPermissionInfoCache(cache);

        return realm;
    }

    /**
     * 配置Session的存储方式
     */
    @Bean
    public SessionDAO sessionDAO(Cache cache){
        return new MemorySessionDAO();
    }

    /**
     * 配置session管理器
     */
    @Bean
    public AbstractWebSessionManager sessionManager(SessionDAO sessionDAO){
//        CookieInfo cookieInfo = new CookieInfo("custom_session_id");
        AbstractWebSessionManager sessionManager = new CookieWebSessionManager(/*cookieInfo*/);
        sessionManager.setSessionDAO(sessionDAO);

        //设置session超时时间为1小时
        sessionManager.setGlobalTimeout(1L);
        sessionManager.setGlobalTimeoutChronoUnit(ChronoUnit.HOURS);

        //设置定时校验的时间为2分钟
        sessionManager.setSessionValidationInterval(2L);
        sessionManager.setSessionValidationUnit(TimeUnit.MINUTES);

        //设置sessionId的生成方式
//        SessionIdFactory sessionIdFactory = new SnowFlakeSessionIdFactory(1L, 1L);
        SessionIdFactory sessionIdFactory = new RandomCharacterSessionIdFactory();
        sessionManager.setSessionIdFactory(sessionIdFactory);

        return sessionManager;
    }

    /**
     * 认证、权限、session等管理的入口
     */
    @Bean
    public SecurityManager securityManager(Realm realm, SessionManager sessionManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(realm, sessionManager);
        //踢出当前用户的旧会话
        securityManager.setKickOutOldSessions(true);

        return securityManager;
    }

    /**
     * 将filter添加到Spring管理
     */
    @Bean
    public FilterRegistrationFactoryBean filterRegistrationFactoryBean(SecurityManager securityManager){
        //添加指定路径的权限校验
        LinkedHashMap<String, String[]> patternPathFilterMap = new LinkedHashMap<>();
        patternPathFilterMap.put("/css/**", new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
        patternPathFilterMap.put("/layui/**", new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
        patternPathFilterMap.put("/index.html", new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
        patternPathFilterMap.put("/test/greeting", new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
//        patternPathFilterMap.put("/test/selectByUsername", new String[]{"perms[/aaa/bbb]"});
        //其他路径需要登录才能访问
        patternPathFilterMap.put("/**/*.html", new String[]{DefaultFilterEnums.LOGIN.getFilterName()});

        FilterRegistrationFactoryBean factoryBean = new FilterRegistrationFactoryBean(ProjectModeEnums.DEFAULT, securityManager, patternPathFilterMap);

        //设置几个登录、未授权等相关URL
        factoryBean.setLoginUrl("/login.html");
        factoryBean.setLoginCheckUrl("/check");
        factoryBean.setUnauthorizedUrl("/error.html");

        return factoryBean;
    }

    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("filterRegistrationFactoryBean");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * 添加对权限注解的支持
     */
    @Bean
    public PermissionsAnnotationAdvisor permissionsAnnotationAdvisor(){
        return new PermissionsAnnotationAdvisor("execution(* cn.zifangsky..controller..*.*(..))");
    }


}
