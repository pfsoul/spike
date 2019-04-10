package top.soulblack.spike.interceptor;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;
import top.soulblack.spike.common.CodeMsg;
import top.soulblack.spike.common.Result;
import top.soulblack.spike.common.config.UserContext;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.redis.RedisService;
import top.soulblack.spike.redis.key.AccessKey;
import top.soulblack.spike.service.SpikeUserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: 廉雪峰
 * @Date: 2019/4/10 18:35
 * @Version 1.0
 */
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SpikeUserService spikeUserService;

    @Autowired
    private RedisService redisService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            SpikeUser user = getUser(request, response);
            UserContext.setUser(user);

            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin) {
                if (user == null) {
                    render(response, CodeMsg.NOT_LOGIN);
                    return false;
                }
                key += "_" + user.getId();
            }
            Integer count = redisService.get(AccessKey.withExpire(seconds), key, Integer.class);
            AccessKey accessKey = AccessKey.withExpire(seconds);
            if (count == null) {
                redisService.set(accessKey, key, 1);
            } else if (count < maxCount) {
                redisService.incr(accessKey, key);
            } else {
                render(response, CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg codeMsg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(codeMsg));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private SpikeUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(SpikeUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, SpikeUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        SpikeUser user = spikeUserService.getByToken(response, token);
        return user;
    }


    /**
     * 获取全部cookie，找到所需要的cookie
     * @param request
     * @param cookieName
     * @return
     */
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null || request.getCookies().length <= 0) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
