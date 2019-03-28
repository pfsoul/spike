package top.soulblack.spike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.User;
import top.soulblack.spike.model.vo.GoodsVo;
import top.soulblack.spike.service.GoodsService;
import top.soulblack.spike.service.SpikeUserService;
import top.soulblack.spike.service.UserService;

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

    @RequestMapping("/to_list")
    public String goodList(Model model, SpikeUser user) {
        model.addAttribute("user", user);
        // 查询商品列表
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, SpikeUser user, @PathVariable("goodsId") long goodsId) {
        //snowflake 算法
        if (user == null) {
            return "login";
        }
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
        model.addAttribute("user", user);
        model.addAttribute("goods", goods);
        model.addAttribute("spikeStatus", spikeStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}
