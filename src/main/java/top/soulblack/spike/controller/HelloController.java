package top.soulblack.spike.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.soulblack.spike.common.Result;
import top.soulblack.spike.entity.User;
import top.soulblack.spike.service.UserService;


/**
 * @Author: 廉雪峰
 * @Date: 2019/3/23 16:23
 * @Version 1.0
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("name", "spike");
        return "hello";
    }

    @RequestMapping("/getid")
    @ResponseBody
    public Result<User> getById() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    /**
     * 测试事务
     */
    @RequestMapping("/getTX")
    @ResponseBody
    public Result<Boolean> getTX() {
        userService.tx();
        return Result.success(true);
    }
}
