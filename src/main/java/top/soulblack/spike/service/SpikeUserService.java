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
        // 先取缓存
        SpikeUser user = redisService.get(SpikeUserKey.getByID, "" + id, SpikeUser.class);
        if (user == null) {
            user = spikeUserMapper.getById(id);
            if (user != null) {
                redisService.set(SpikeUserKey.getByID, "" + id, user);
            }
        }
        return user;
    }

    /**
     * 更新密码
     * @param token
     * @param id
     * @param passwordNew
     * @return
     */
    public boolean updatePassword(String token, long id, String passwordNew) {
        // 取user
        SpikeUser user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 更新数据库
        SpikeUser toBeUpdate = new SpikeUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew,user.getSalt()));
        spikeUserMapper.updatePassword(toBeUpdate);
        // 处理缓存
        redisService.delete(SpikeUserKey.getByID, "" + id);
        // 更新token
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(SpikeUserKey.token, token, user);
        return true;
    }

    /**
     * 登录操作
     * @param response
     * @param loginVo
     * @return
     */
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

    /**
     * 注册账号
     * @param response
     * @param loginVo
     * @return
     */
    public String register(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 参数校验
        String mobile = loginVo.getMobile();
        String formpass = loginVo.getPassword();
        SpikeUser user = getById(Long.parseLong(mobile));
        if (user != null) {
            throw new GlobalException(CodeMsg.MOBILE_EXIST);
        }
        // 注册账号
        user = new SpikeUser();
        user.setPassword(formpass);
        user.setId(Long.parseLong(mobile));
        user.setNickname("测试账号");
        user.setSalt(MD5Util.SALT);
        System.out.println(user.toString());
        spikeUserMapper.insertUser(user);
        // 添加缓存
        redisService.set(SpikeUserKey.getByID, "" + user.getId(), user);
        // 生成一个cookie
        String token = UUIDUtil.uuid();
        addCookie(response,token, user);
        return token;
    }
}
