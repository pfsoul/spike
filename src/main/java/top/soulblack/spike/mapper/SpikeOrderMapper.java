package top.soulblack.spike.mapper;

import top.soulblack.spike.model.SpikeOrder;

public interface SpikeOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SpikeOrder record);

    int insertSelective(SpikeOrder record);

    SpikeOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpikeOrder record);

    int updateByPrimaryKey(SpikeOrder record);
}