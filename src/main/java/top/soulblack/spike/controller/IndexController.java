package top.soulblack.spike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.vo.GoodsVo;
import top.soulblack.spike.redis.RedisService;
import top.soulblack.spike.redis.key.GoodsKey;
import top.soulblack.spike.redis.key.IndexKey;
import top.soulblack.spike.service.GoodsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Create by 廉雪峰
 * Date: 2020/4/8 15:21
 * blog: www.soulblack.top
 * Description:
 */
@Controller
public class IndexController {
    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping("/index")
    @ResponseBody
    public String index(HttpServletRequest request, HttpServletResponse response, Model model, SpikeUser user){
        model.addAttribute("user", user);
        // 取缓存
        String html = redisService.get(IndexKey.getGoodList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        // 查询商品列表
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        // Springboot 2.0  1.0使用SpringWebContext
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        // 手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("index", webContext);
        if (!StringUtils.isEmpty(html)) {
            // 保存到缓存中
            redisService.set(IndexKey.getGoodList, "", html);
        }
        return html;
    }
}
