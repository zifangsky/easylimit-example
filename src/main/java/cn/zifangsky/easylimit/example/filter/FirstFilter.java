package cn.zifangsky.easylimit.example.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 测试Filter1
 *
 * @author zifangsky
 * @date 2020/5/23
 * @since 1.0.0
 */
@Order(1)
@WebFilter(filterName="firstFilter", urlPatterns="/*")
public class FirstFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("***** first filter before *****");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("***** first filter after *****");
    }

    @Override
    public void destroy() {

    }
}
