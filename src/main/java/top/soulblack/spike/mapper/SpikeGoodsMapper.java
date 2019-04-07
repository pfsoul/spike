package top.soulblack.spike.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import top.soulblack.spike.model.SpikeGoods;

@Mapper
public interface SpikeGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SpikeGoods record);

    int insertSelective(SpikeGoods record);

    SpikeGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpikeGoods record);

    int updateByPrimaryKey(SpikeGoods record);

    //@Update("update spike_goods set stock_count = stock_count - 1 where stock_count > 0 and goods_id = #{goodsId}")
    @Update("update spike_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    public int reduceStock(SpikeGoods g);
}