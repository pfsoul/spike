package top.soulblack.spike.controller;

import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.soulblack.spike.common.CodeMsg;
import top.soulblack.spike.common.Result;
import top.soulblack.spike.model.Goods;
import top.soulblack.spike.model.OrderInfo;
import top.soulblack.spike.model.SpikeOrder;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.vo.GoodsVo;
import top.soulblack.spike.rabbitmq.MQSender;
import top.soulblack.spike.rabbitmq.SpikeMessage;
import top.soulblack.spike.redis.RedisService;
import top.soulblack.spike.redis.key.GoodsKey;
import top.soulblack.spike.redis.key.SpikeKey;
import top.soulblack.spike.service.GoodsService;
import top.soulblack.spike.service.OrderInfoService;
import top.soulblack.spike.service.SpikeService;
import top.soulblack.spike.util.MD5Util;
import top.soulblack.spike.util.UUIDUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/28 10:48
 * @Version 1.0
 */
@Controller
@RequestMapping("/spike")
public class SpikeController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private SpikeService spikeService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(GoodsKey.getSpikeGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }
    }


    /*
    qps 722.5
    5000 * 10

    qps 919.4
    5000 * 10
     */
    @RequestMapping(value = "/{path}/do_spike", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> spike(SpikeUser user, @RequestParam("goodsId") long goodsId, @PathVariable("path") String path) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        // 验证path
        boolean check = spikeService.checkPath(path, user, goodsId);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        // 如果卖完则表示该商品卖完，即卖完以后均不访问redis
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.STOCK_EMPTY);
        }
        // 判断是否已经秒杀到了
        SpikeOrder order = orderInfoService.getSpikeOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_SPIKE);
        }
        // 预减库存
        long stock = redisService.decr(GoodsKey.getSpikeGoodsStock, "" + goodsId);
        if (stock < 0) {
            // 如果卖完则表示该商品卖完，即10个以后均不访问redis
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.STOCK_EMPTY);
        }
        // 入队
        SpikeMessage spikeMessage = new SpikeMessage();
        spikeMessage.setGoodsId(goodsId);
        spikeMessage.setUser(user);
        mqSender.sendSpikeMessage(spikeMessage);
        return Result.success(0);  // 排队中
    }

    /**
     * 轮询结果
     * 返回orderId:成功
     * -1：秒杀失败
     * 0：排队中
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> result(SpikeUser user,@RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        long result = spikeService.getSpikeReesult(user.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> path(SpikeUser user,@RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        String path = spikeService.createSpikePath(user, goodsId);
        return Result.success(path);
    }


}
