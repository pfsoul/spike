package top.soulblack.spike.mapper;

import org.apache.ibatis.annotations.*;
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

    @Update("update spike_user set password = #{password} where id = #{id}")
    void updatePassword(SpikeUser toBeUpdate);

    @Insert("insert into spike_user (id, nickname, password, salt) values (#{id}, #{nickname}, #{password}, #{salt}) ")
    int insertUser(SpikeUser record);
}