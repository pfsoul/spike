package top.soulblack.spike.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.soulblack.spike.model.SpikeUser;

@Mapper
public interface SpikeUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SpikeUser record);

    int insertSelective(SpikeUser record);

    SpikeUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpikeUser record);

    int updateByPrimaryKey(SpikeUser record);

    @Select("select * from spike_user where id = #{id}")
    SpikeUser getById(@Param("id") Long id);
}