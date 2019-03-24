package top.soulblack.spike.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.soulblack.spike.common.Result;
import top.soulblack.spike.entity.User;
import top.soulblack.spike.redis.RedisService;
import top.soulblack.spike.redis.key.UserKey;
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

    @Autowired
    private RedisService redisService;

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

    /**
     * 测试redis
     */
    @RequestMapping("/redisGet")
    @ResponseBody
    public Result<String> redisGet() {
        String str = redisService.get(UserKey.getById,""+1,String.class);
        return Result.success(str);
    }

    @RequestMapping("/redisSet")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1); user.setName("soulblack");
        boolean ret = redisService.set(UserKey.getById,""+1,user);
        return Result.success(ret);
    }
}
