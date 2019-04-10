package top.soulblack.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.soulblack.spike.mapper.UserMapper;
import top.soulblack.spike.model.OrderInfo;
import top.soulblack.spike.model.SpikeOrder;
import top.soulblack.spike.model.SpikeUser;
import top.soulblack.spike.model.User;
import top.soulblack.spike.model.vo.GoodsVo;
import top.soulblack.spike.redis.RedisService;
import top.soulblack.spike.redis.key.SpikeKey;
import top.soulblack.spike.util.MD5Util;
import top.soulblack.spike.util.UUIDUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/28 11:01
 * @Version 1.0
 */
@Service
public class SpikeService {

    @Autowired
    private SpikeGoodsService spikeGoodsService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo spike(SpikeUser user, GoodsVo goods) {
        //减库存 下订单，写入秒杀订单
        boolean success = spikeGoodsService.reduceStock(goods);
        if (success) {
            return orderInfoService.createOrder(user, goods);
        }
        setGoodsOver(goods.getId());
        return null;
    }


    public long getSpikeReesult(long id, long goodsId) {
        SpikeOrder order = orderInfoService.getSpikeOrderByUserIdGoodsId(id, goodsId);
        if (order != null) {
            return order.getOrderId();
        } else {
            // 判断是否秒杀成功 即物品是否卖光
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                // 秒杀失败
                return -1;
            } else {
                // 等待中
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SpikeKey.isGoodsOver, "" + goodsId, true);
    }


    private boolean getGoodsOver(long goodsId) {
        return redisService.exist(SpikeKey.isGoodsOver, "" + goodsId);
    }

    /**
     * 创造随机的秒杀路径
     * @param user
     * @param goodsId
     * @return
     */
    public String createSpikePath(SpikeUser user, long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(SpikeKey.getSpikePath, "" + user.getId() + "_" + goodsId, str);
        return str;
    }

    /**
     * 验证随机秒杀路径
     * @param path
     * @param user
     * @param goodsId
     * @return
     */
    public boolean checkPath(String path, SpikeUser user, long goodsId) {
        if (user == null || path == null) {
            return false;
        }
        String needPath = redisService.get(SpikeKey.getSpikePath, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(needPath);
    }

    /**
     * 创建验证码图片
     * @param user
     * @param goodsId
     * @return
     */
    public BufferedImage createVerifyCode(SpikeUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SpikeKey.getSpikeVerifyCode, user.getId()+"_"+goodsId, rnd);
        //输出图片
        return image;
    }

//    public static void main(String[] args) {
//        System.out.println(calc("3-42+1"));
//    }


    /**
     * 利用引擎计算表达式得到验证码结果
     * @param verifyCode
     * @return
     */
    private int calc(String verifyCode) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(verifyCode);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[]{'+', '-', '*'};

    /**
     * 生成表达式
     * @param rdm
     * @return
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    /**
     * 验证验证码
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    public boolean checkVerifyCode(SpikeUser user, long goodsId, int verifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }
        Integer codeOld = redisService.get(SpikeKey.getSpikeVerifyCode, user.getId() + "_" + goodsId, Integer.class);
        // 一次验证码只验证一次，后刷新重试
        redisService.delete(SpikeKey.getSpikeVerifyCode, user.getId() + "_" + goodsId);
        if (codeOld == null || codeOld - verifyCode != 0) {
            return false;
        }
        return true;
    }
}
