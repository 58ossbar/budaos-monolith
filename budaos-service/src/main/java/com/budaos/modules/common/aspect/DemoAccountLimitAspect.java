package com.budaos.modules.common.aspect;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.ConstantProd;
import com.budaos.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.budaos.modules.evgl.trainee.vo.TevglTraineeInfoVo;
import com.budaos.utils.tool.StrUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
 * 演示账号 切面
 * @author huj
 * @create 2022-01-05 16:09
 * @email hujun@budaos.com
 */
@Aspect
@Component
public class DemoAccountLimitAspect {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TevglTraineeInfoMapper tevglTraineeInfoMapper;

    @Pointcut("@annotation(com.budaos.modules.common.annotation.DemoAccountLimit)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Assert.notNull(request, "request can not null");
        // 此处可以用token
        String token = getToken(request);
        if (StrUtils.isEmpty(token)) {
            return R.error("演示账号不允许该操作");
        }
        Object result;
        try {
            TevglTraineeInfoVo traineeInfo = tevglTraineeInfoMapper.selectTraineeVoByToken(token);
            if (traineeInfo == null) {
                return R.error("非法token");
            }
            if (ConstantProd.teacherDemoAccout.contains(traineeInfo.getMobile())) {
                return R.error("演示账号不允许该操作！");
            }
            result = pjp.proceed();
            return result;
        } catch (Exception e) {
            return R.error("系统开小差了！");
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
