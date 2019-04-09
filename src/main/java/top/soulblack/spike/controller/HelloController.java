package top.soulblack.spike.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.soulblack.spike.common.Result;
import top.soulblack.spike.model.User;
import top.soulblack.spike.rabbitmq.MQSender;
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

    @Autowired
    private MQSender mqSender;

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
     * 测试rabbitmq
     */
    @RequestMapping("/mq/headers")
    @ResponseBody
    public Result<String> headers() {
        mqSender.headersSend("hello soulblack");
        return Result.success("hello");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout() {
        mqSender.fanoutSend("hello soulblack");
        return Result.success("hello");
    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic() {
        mqSender.topicSend("hello soulblack");
        return Result.success("hello");
    }

    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq() {
        mqSender.send("hello soulblack");
        return Result.success("hello");
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
