/*
 * ========================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ========================================================================
 */
package org.mengyun.tcctransaction.sample.dubbo.order.api;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.mengyun.tcctransaction.sample.dubbo.order.domain.entity.Product;

/**
 * 下订单接口.
 * @author Administrator
 *
 */
public interface PlaceOrderService {
	
	/**
	 * 下订单。
	 * 
	 * @param payerUserId
	 *            付款者ID.
	 * @param shopId
	 *            店铺ID.
	 * @param productQuantities
	 *            产品数量
	 * @param redPacketPayAmount
	 *            红包支付金额。
	 */
    public String placeOrder(long payerUserId, long shopId, List<Pair<Long, Integer>> productQuantities, BigDecimal redPacketPayAmount);
    
    
    Product findProductById(long productId);
    
    List<Product> findProductByShopId(long shopId);
    
    
    BigDecimal getRedPacketAccountByUserId(long userId);
    
    BigDecimal getCapitalAccountByUserId(long userId);
    
    String getOrderStatusByMerchantOrderNo(String orderNo);

}
