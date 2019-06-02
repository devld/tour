package me.devld.tour.controller;

import me.devld.tour.util.SecurityUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Aspect
@Component
public class RequestLogAop {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLogAop.class);

    @Around("@annotation(request)")
    public Object requestLogging(ProceedingJoinPoint pjp, Request request) throws Throwable {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String method = req.getMethod();
        String url = req.getRequestURI();
        String query = req.getQueryString();
        if (query != null) {
            url += "?" + query;
        }
        String api = request.value();
        Long userId = SecurityUtil.userIdOrNull();
        String remoteAddr = req.getRemoteAddr();

        String forwardFor = req.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(forwardFor)) {
            remoteAddr = forwardFor;
        }

        long t = -1;
        Object result = null;
        Throwable throwable = null;
        try {
            t = System.currentTimeMillis();
            result = pjp.proceed();
        } catch (Throwable th) {
            throwable = th;
        } finally {
            t = System.currentTimeMillis() - t;
        }

        LOGGER.info(
                "{} {}({}) {} {} {} {}ms {}",
                Instant.now().toString(), remoteAddr, userId, api, method, url, t,
                throwable == null ? "success" : (throwable.getClass().getName() + ":" + throwable.getMessage())
        );

        if (throwable != null) {
            throw throwable;
        }
        return result;
    }

}
