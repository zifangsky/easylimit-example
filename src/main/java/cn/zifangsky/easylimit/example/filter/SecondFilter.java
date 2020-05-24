package cn.zifangsky.easylimit.example.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 测试Filter2
 *
 * @author zifangsky
 * @date 2020/5/23
 * @since 1.0.0
 */
@Order(2)
@WebFilter(filterName="secondFilter", urlPatterns="/*")
public class SecondFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("***** second filter before *****");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("***** second filter after *****");
    }

    @Override
    public void destroy() {

    }
}
