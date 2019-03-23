package top.soulblack.spike.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/23 16:23
 * @Version 1.0
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("name", "spike");
        return "hello";
    }
}
