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

    @Update("update spike_goods set stock_count = stock_count - 1 where goods_id = #{goodsId}")
    int reduceStock(SpikeGoods g);
}