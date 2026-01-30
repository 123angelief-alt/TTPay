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

import java.io.IOException;

@Component
public class TenantResolveFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(TenantResolveFilter.class);
    @Autowired
    private TenantService tenantService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
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

