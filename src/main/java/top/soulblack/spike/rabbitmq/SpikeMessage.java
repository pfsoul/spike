package top.soulblack.spike.rabbitmq;

import top.soulblack.spike.model.SpikeUser;

/**
 * @Author: 廉雪峰
 * @Date: 2019/4/9 15:13
 * @Version 1.0
 */
public class SpikeMessage {
    private SpikeUser user;
    private long goodsId;

    public SpikeUser getUser() {
        return user;
    }

    public void setUser(SpikeUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
