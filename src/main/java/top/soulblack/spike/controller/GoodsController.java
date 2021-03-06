package top.soulblack.spike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;
import top.soulblack.spike.common.Result;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.User;
import top.soulblack.spike.model.vo.GoodsDetailVo;
import top.soulblack.spike.model.vo.GoodsVo;
import top.soulblack.spike.redis.RedisService;
import top.soulblack.spike.redis.key.GoodsKey;
import top.soulblack.spike.service.GoodsService;
import top.soulblack.spike.service.SpikeUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /*
    5000*10 渲染 吞吐量766.8 异常率77.95  8g4核
    页面缓存 吞吐量176.1 异常率57.35      1核1G服务器
    不渲染 吞吐量691.7 异常率67.35        8g4核
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    // 页面缓存
    public String goodList(HttpServletRequest request, HttpServletResponse response, Model model, SpikeUser user) {
        model.addAttribute("user", user);
        // 取缓存
        String html = redisService.get(GoodsKey.getGoodList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        // 查询商品列表
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        // Springboot 2.0  1.0使用SpringWebContext
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        // 手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", webContext);
        if (!StringUtils.isEmpty(html)) {
            // 保存到缓存中
            redisService.set(GoodsKey.getGoodList, "", html);
        }
        return html;
        //return "goods_list";
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response,
                                        Model model, SpikeUser user, @PathVariable("goodsId") long goodsId) {

        // 手动渲染
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int spikeStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {
            // 秒杀还没开始，倒计时
            spikeStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        }else if (now > endAt){
            // 秒杀已经结束
            spikeStatus = 2;
            remainSeconds = -1;
        }else {
            // 秒杀进行中
            spikeStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setSpikeStatus(spikeStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setUser(user);
        return Result.success(goodsDetailVo);
    }
}
