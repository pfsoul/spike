package top.soulblack.spike.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.soulblack.spike.common.CodeMsg;
import top.soulblack.spike.common.Result;
import top.soulblack.spike.model.vo.LoginVo;
import top.soulblack.spike.service.SpikeUserService;
import top.soulblack.spike.util.ValidatorUtil;

import javax.validation.Valid;


/**
 * @Author: 廉雪峰
 * @Date: 2019/3/25 11:29
 * @Version 1.0
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SpikeUserService spikeUserService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(@Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        // 登录
        spikeUserService.login(loginVo);
        return Result.success(true);
    }
}
