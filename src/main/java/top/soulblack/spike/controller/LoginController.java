package top.soulblack.spike.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.soulblack.spike.common.Result;
import top.soulblack.spike.common.config.UserContext;
import top.soulblack.spike.model.vo.LoginVo;
import top.soulblack.spike.service.SpikeUserService;

import javax.servlet.http.HttpServletResponse;
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
    public String toLogin(Model model) {
        model.addAttribute("user", UserContext.getUser());
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        // 登录
        String token = spikeUserService.login(response, loginVo);
        return Result.success(token);
    }

    @RequestMapping("/do_register")
    @ResponseBody
    public Result<String> doRegister(HttpServletResponse response, @Valid LoginVo loginVo){
        log.info(loginVo.toString());
        //注册
        String token = spikeUserService.register(response, loginVo);
        return Result.success(token);
    }
}
