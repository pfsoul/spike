package top.soulblack.spike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.User;
import top.soulblack.spike.service.SpikeUserService;
import top.soulblack.spike.service.UserService;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/27 14:56
 * @Version 1.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SpikeUserService spikeUserService;

    @RequestMapping("/to_list")
    public String goodList(Model model, SpikeUser user) {
        model.addAttribute("user", user);
        return "goods_list";
    }
}
