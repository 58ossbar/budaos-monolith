package com.budaos.modules.common.aspect;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.ConstantProd;
import com.budaos.modules.common.annotation.NoRepeatSubmit;
import com.budaos.modules.redis.StringRedisService;
import com.budaos.utils.tool.StrUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 防重复提交 切面
 * @author huj
 * @create 2021-12-25 14:52
 * @email 1552281464@qq.com
 */
@Aspect
@Component
public class RepeatSubmitAspect {

    private Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private StringRedisService stringRedisService;

    @Pointcut("@annotation(com.budaos.modules.common.annotation.NoRepeatSubmit)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Assert.notNull(request, "request can not null");
        // 此处可以用token
        String token = getToken(request);
        String key = token + "-" + request.getServletPath();
        log.debug("防重复提交 key => {}", key);
        NoRepeatSubmit annotation = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(NoRepeatSubmit.class);
        long expire = annotation.value();
        // 超时时间：10秒，最好设为常量
        String time = String.valueOf(System.currentTimeMillis() + expire);
        // 加锁
        boolean islock = stringRedisService.secKilllock(key, time);
        if (islock) {
            Object result;
            try {
                result = pjp.proceed();
            } finally {
                // 解锁
                stringRedisService.unlock(key, time);
            }
            return result;
        } else {
            return R.error("重复请求,请稍后再试");
        }
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(ConstantProd.TOKEN_KEY_NAME);
        if (StrUtils.isEmpty(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    switch(cookie.getName()){
                        case ConstantProd.TOKEN_KEY_NAME:
                        case "token":
                            token = cookie.getValue();
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return token;
    }

}
