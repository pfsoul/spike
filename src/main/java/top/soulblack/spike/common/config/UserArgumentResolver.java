package top.soulblack.spike.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;
import sun.security.provider.ConfigFile;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.service.SpikeUserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/27 15:34
 * @Version 1.0
 */

/**
 * 对参数含有user类的解析
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    SpikeUserService spikeUserService;

    /**
     * 判断所需参数是否需要user
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == SpikeUser.class;
    }

    /**
     * 根据redis找到用户详情并注入user
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
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
