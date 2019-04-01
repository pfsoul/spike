package top.soulblack.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import top.soulblack.spike.common.CodeMsg;
import top.soulblack.spike.common.exception.GlobalException;
import top.soulblack.spike.mapper.SpikeUserMapper;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.vo.LoginVo;
import top.soulblack.spike.redis.RedisService;
import top.soulblack.spike.redis.key.SpikeUserKey;
import top.soulblack.spike.util.MD5Util;
import top.soulblack.spike.util.UUIDUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/25 14:49
 * @Version 1.0
 */
@Service
public class SpikeUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private SpikeUserMapper spikeUserMapper;

    @Autowired
    private RedisService redisService;

    public SpikeUser getById(Long id) {
        return spikeUserMapper.getById(id);
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 参数校验
        String mobile = loginVo.getMobile();
        String formpass = loginVo.getPassword();
        SpikeUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 验证密码
        String dbPassword = user.getPassword();
        String slatDB = user.getSalt();
        String password = MD5Util.formPassToDBPass(formpass, slatDB);
        if (!dbPassword.equals(password)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        // 生成一个cookie
        String token = UUIDUtil.uuid();
        addCookie(response,token, user);
        return token;

    }

    /**
     * 从redis获取用户信息
     * @param response
     * @param token
     * @return
     */
    public SpikeUser getByToken(HttpServletResponse response,String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        SpikeUser user = redisService.get(SpikeUserKey.token, token, SpikeUser.class);
        if(user == null)
            return null;
        // 延长有效期
        // 生成一个cookie
        addCookie(response,token, user);
        return user;
    }

    /**
     * 增加分布式session
     * @param response
     * @param user
     */
    private void addCookie(HttpServletResponse response,String token, SpikeUser user) {
        redisService.set(SpikeUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SpikeUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
