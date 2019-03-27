package top.soulblack.spike.mapper;

import top.soulblack.spike.model.SpikeGoods;

public interface SpikeGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SpikeGoods record);

    int insertSelective(SpikeGoods record);

    SpikeGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpikeGoods record);

    int updateByPrimaryKey(SpikeGoods record);
}