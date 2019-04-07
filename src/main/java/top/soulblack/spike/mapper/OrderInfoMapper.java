package top.soulblack.spike.mapper;

import org.apache.ibatis.annotations.*;
import top.soulblack.spike.model.OrderInfo;
import top.soulblack.spike.model.SpikeOrder;

@Mapper
public interface OrderInfoMapper {
    int deleteByPrimaryKey(Long id);


    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    @Select("select * from spike_order where user_id = #{userId} and goods_id = #{goodsId}")
    SpikeOrder getSpikeOrderByUserIdGoodsId(@Param("userId") Long userId,@Param("goodsId") long goodsId);

    @Insert("insert into spike_order (user_id, order_id, goods_id) values (#{userId}, #{orderId}, #{goodsId})")
    void insertSpikeOrder(SpikeOrder spikeOrder);

    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderById(@Param("orderId")long orderId);
}