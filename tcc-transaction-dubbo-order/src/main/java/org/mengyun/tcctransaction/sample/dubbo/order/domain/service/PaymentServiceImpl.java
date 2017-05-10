/*
 * ========================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ========================================================================
 */
package org.mengyun.tcctransaction.sample.dubbo.order.domain.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mengyun.tcctransaction.Compensable;
import org.mengyun.tcctransaction.sample.dubbo.capital.api.CapitalTradeOrderService;
import org.mengyun.tcctransaction.sample.dubbo.capital.api.dto.CapitalTradeOrderDto;
import org.mengyun.tcctransaction.sample.dubbo.order.domain.entity.Order;
import org.mengyun.tcctransaction.sample.dubbo.order.domain.repository.OrderRepository;
import org.mengyun.tcctransaction.sample.dubbo.redpacket.api.RedPacketTradeOrderService;
import org.mengyun.tcctransaction.sample.dubbo.redpacket.api.dto.RedPacketTradeOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by changming.xie on 4/1/16.
 */
@Service
public class PaymentServiceImpl {
	
	private static final Log LOG = LogFactory.getLog(PaymentServiceImpl.class);

    @Autowired
    CapitalTradeOrderService capitalTradeOrderService;

    @Autowired
    RedPacketTradeOrderService redPacketTradeOrderService;

    @Autowired
    OrderRepository orderRepository;

    /*
     * 如果事务日志没有成功提交，那么整个TCC事务将会需要恢复，
     * 如果是在CONFIRMING阶段出异常，则恢复Job将继续启动事务的Confirm操作过程，
     * 如果是在TRYING阶段出异常，则恢复Job将启动事务的Cancel操作过程。
     */

	/**
	 * 付款.
	 * 
	 * @param order
	 *            订单信息.
	 * @param redPacketPayAmount
	 *            红包支付金额
	 * @param capitalPayAmount
	 *            资金帐户支付金额.
	 */
    @Compensable(confirmMethod = "confirmMakePayment", cancelMethod = "cancelMakePayment")
    @Transactional
    public void makePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {
    	
    	LOG.debug("==>order try make payment called");
    	
    	LOG.debug("==>redPacketPayAmount：" + redPacketPayAmount.doubleValue());
    	LOG.debug("==>capitalPayAmount：" + capitalPayAmount.doubleValue());

        order.pay(redPacketPayAmount, capitalPayAmount);
        orderRepository.updateOrder(order);
        
        LOG.debug("==try capitalTradeOrderService.record(null, buildCapitalTradeOrderDto(order) begin");
        // 资金帐户交易订单记录（因为此方法中有TransactionContext参数，因此也会被TccTransactionContextAspect拦截处理）
        String result = capitalTradeOrderService.record(null, buildCapitalTradeOrderDto(order));
        LOG.debug("==try capitalTradeOrderService.record(null, buildCapitalTradeOrderDto(order) end, result:" + result);
        
        LOG.debug("==>try redPacketTradeOrderService.record(null, buildRedPacketTradeOrderDto(order)) begin");
		// 红包帐户交易订单记录
        String result2 = redPacketTradeOrderService.record(null, buildRedPacketTradeOrderDto(order));
        LOG.debug("==>try redPacketTradeOrderService.record(null, buildRedPacketTradeOrderDto(order)) end, result:" + result2);
        
        LOG.debug("==>order try end");
        
    }

    /**
	 * 确认付款.
	 * @param order
	 * @param redPacketPayAmount
	 * @param capitalPayAmount
	 */
    public void confirmMakePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {

    	LOG.debug("==>order confirm make payment called, set status : CONFIRMED");
        order.confirm(); // 设置订单状态为CONFIRMED

        orderRepository.updateOrder(order);
    }

    /**
	 * 取消付款.
	 * @param order
	 * @param redPacketPayAmount
	 * @param capitalPayAmount
	 */
    public void cancelMakePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {

    	LOG.debug("==>order cancel make payment called, set status : PAY_FAILED");

        order.cancelPayment();

        orderRepository.updateOrder(order);
    }


    /**
	 * 构建资金帐户支付订单Dto
	 * @param order
	 * @return
	 */
    private CapitalTradeOrderDto buildCapitalTradeOrderDto(Order order) {
    	LOG.debug("==>buildCapitalTradeOrderDto(Order order)");
        CapitalTradeOrderDto tradeOrderDto = new CapitalTradeOrderDto();
        tradeOrderDto.setAmount(order.getCapitalPayAmount());
        tradeOrderDto.setMerchantOrderNo(order.getMerchantOrderNo());
        tradeOrderDto.setSelfUserId(order.getPayerUserId());
        tradeOrderDto.setOppositeUserId(order.getPayeeUserId());
        tradeOrderDto.setOrderTitle(String.format("order no:%s", order.getMerchantOrderNo()));

        return tradeOrderDto;
    }

    /**
	 * 构建红包帐户支付订单Dto
	 * @param order
	 * @return
	 */
    private RedPacketTradeOrderDto buildRedPacketTradeOrderDto(Order order) {
    	LOG.debug("==>buildRedPacketTradeOrderDto(Order order)");
        RedPacketTradeOrderDto tradeOrderDto = new RedPacketTradeOrderDto();
        tradeOrderDto.setAmount(order.getRedPacketPayAmount());
        tradeOrderDto.setMerchantOrderNo(order.getMerchantOrderNo());
        tradeOrderDto.setSelfUserId(order.getPayerUserId());
        tradeOrderDto.setOppositeUserId(order.getPayeeUserId());
        tradeOrderDto.setOrderTitle(String.format("order no:%s", order.getMerchantOrderNo()));

        return tradeOrderDto;
    }
}
