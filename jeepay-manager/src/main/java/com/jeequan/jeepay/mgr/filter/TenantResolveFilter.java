package com.jeequan.jeepay.mgr.filter;

import com.jeequan.jeepay.core.entity.Tenant;
import com.jeequan.jeepay.core.thread.TenantContext;
import com.jeequan.jeepay.service.impl.TenantService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.IOException;

@Component
public class TenantResolveFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(TenantResolveFilter.class);
    @Autowired
    private TenantService tenantService;

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    private static final String[] IGNORE_TENANT_PATH = {
            "/api/vercode",
            "/api/health",
            "/error",
            "/favicon.ico"
    };


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        for (String pattern : IGNORE_TENANT_PATH) {
            if (pathMatcher.match(pattern, uri)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String domain = req.getServerName(); // ★ 核心
        log.info("登录域名：{}",domain);

        Tenant tenant = tenantService.getByDomain(domain);

        if (tenant == null || tenant.getState() != 1) {
            throw new RuntimeException("非法租户域名：" + domain);
        }

        TenantContext.setTenantId(tenant.getTenantId());

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}

