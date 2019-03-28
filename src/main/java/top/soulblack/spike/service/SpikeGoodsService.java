package top.soulblack.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.soulblack.spike.mapper.SpikeGoodsMapper;
import top.soulblack.spike.model.SpikeGoods;
import top.soulblack.spike.model.vo.GoodsVo;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/28 11:22
 * @Version 1.0
 */
@Service
public class SpikeGoodsService {

    @Autowired
    private SpikeGoodsMapper spikeGoodsMapper;

    public void reduceStock(GoodsVo goods) {
        SpikeGoods g = new SpikeGoods();
        g.setGoodsId(goods.getId());
        spikeGoodsMapper.reduceStock(g);
    }
}
