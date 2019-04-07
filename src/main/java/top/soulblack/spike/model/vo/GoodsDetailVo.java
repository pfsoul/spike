package top.soulblack.spike.model.vo;

import top.soulblack.spike.model.SpikeUser;

/**
 * @Author: 廉雪峰
 * @Date: 2019/4/7 10:56
 * @Version 1.0
 */
public class GoodsDetailVo {
    private int spikeStatus;

    private int remainSeconds;

    private GoodsVo goods;

    private SpikeUser user;

    public SpikeUser getUser() {
        return user;
    }

    public void setUser(SpikeUser user) {
        this.user = user;
    }

    public int getSpikeStatus() {
        return spikeStatus;
    }

    public void setSpikeStatus(int spikeStatus) {
        this.spikeStatus = spikeStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "GoodsDetailVo{" +
                "spikeStatus=" + spikeStatus +
                ", remainSeconds=" + remainSeconds +
                ", goods=" + goods +
                ", user=" + user +
                '}';
    }
}
