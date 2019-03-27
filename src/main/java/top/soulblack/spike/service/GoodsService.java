package top.soulblack.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;
import top.soulblack.spike.mapper.GoodsMapper;
import top.soulblack.spike.model.vo.GoodsVo;

import java.util.List;


/**
 * @Author: 廉雪峰
 * @Date: 2019/3/27 20:28
 * @Version 1.0
 */
@Service
public class GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.listGoodsVo();
    }
}
