package top.soulblack.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.soulblack.spike.common.CodeMsg;
import top.soulblack.spike.common.exception.GlobalException;
import top.soulblack.spike.mapper.SpikeUserMapper;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.vo.LoginVo;
import top.soulblack.spike.util.MD5Util;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/25 14:49
 * @Version 1.0
 */
@Service
public class SpikeUserService {

    @Autowired
    private SpikeUserMapper spikeUserMapper;

    public SpikeUser getById(Long id) {
        return spikeUserMapper.getById(id);
    }

    public boolean login(LoginVo loginVo) {
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
        return true;

    }
}
