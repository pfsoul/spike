package top.soulblack.spike.controller;

import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.soulblack.spike.common.CodeMsg;
import top.soulblack.spike.model.Goods;
import top.soulblack.spike.model.OrderInfo;
import top.soulblack.spike.model.SpikeOrder;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.vo.GoodsVo;
import top.soulblack.spike.service.GoodsService;
import top.soulblack.spike.service.OrderInfoService;
import top.soulblack.spike.service.SpikeService;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/28 10:48
 * @Version 1.0
 */
@Controller
@RequestMapping("/spike")
public class SpikeController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    SpikeService spikeService;

    /*
    qps 722.5
    5000 * 10
     */
    @RequestMapping("/do_spike")
    public String spike(Model model, SpikeUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return "login";
        }
        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.STOCK_EMPTY.getMsg());
            return "spike_fail";
        }
        // 判断是否已经秒杀到了
        SpikeOrder order = orderInfoService.getSpikeOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_SPIKE.getMsg());
            return "spike_fail";
        }
        // 减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = spikeService.spike(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }

}
