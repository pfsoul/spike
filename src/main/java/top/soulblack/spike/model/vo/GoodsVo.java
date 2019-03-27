package top.soulblack.spike.model.vo;

import top.soulblack.spike.model.Goods;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: 廉雪峰
 * @Date: 2019/3/27 20:29
 * @Version 1.0
 */
public class GoodsVo extends Goods {

    private BigDecimal spikePrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

    public BigDecimal getSpikePrice() {
        return spikePrice;
    }

    public void setSpikePrice(BigDecimal spikePrice) {
        this.spikePrice = spikePrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
