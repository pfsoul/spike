package top.soulblack.spike.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.soulblack.spike.model.User;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/24 13:15
 * @Version 1.0
 */

@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    public User getById(@Param("id") int id);

    @Insert("insert into user(id, name)values(#{id},#{name})")
    public int insert(User user);
}
