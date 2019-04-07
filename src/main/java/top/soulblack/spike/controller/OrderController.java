package top.soulblack.spike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.soulblack.spike.common.CodeMsg;
import top.soulblack.spike.common.Result;
import top.soulblack.spike.model.OrderInfo;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.vo.GoodsVo;
import top.soulblack.spike.model.vo.OrderVo;
import top.soulblack.spike.service.GoodsService;
import top.soulblack.spike.service.OrderInfoService;

/**
 * @Author: 廉雪峰
 * @Date: 2019/4/7 15:47
 * @Version 1.0
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    // 可以写一个拦截器来拦截判断user是否存在
    public Result<OrderVo> detail(SpikeUser user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        OrderInfo orderInfo = orderInfoService.getOrderById(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIT);
        }
        long goodsId = orderInfo.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderVo vo = new OrderVo();
        vo.setGoodsVo(goodsVo);
        vo.setOrderInfo(orderInfo);
        return Result.success(vo);
    }
}
