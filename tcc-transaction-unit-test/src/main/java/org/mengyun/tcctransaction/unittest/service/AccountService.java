/*
 * ========================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ========================================================================
 */
package org.mengyun.tcctransaction.unittest.service;

import org.mengyun.tcctransaction.api.TransactionContext;

/**
 * Created by changmingxie on 10/25/15.
 */
public interface AccountService {

	/** 向子账户转账，状态为“转账中” */
    void transferTo(TransactionContext transactionContext, long accountId, int amount);

    /** 向子账户转账确认，状态改为“常规状态：NORMAL” */
    void transferToConfirm(TransactionContext transactionContext, long accountId, int amount);

    /** 向子账户转账取消，状态改为“常规状态：NORMAL” */
    void transferToCancel(TransactionContext transactionContext, long accountId, int amount);

    void transferToWithNoTransactionContext(long accountId, int amount);

    void transferToConfirmWithNoTransactionContext(long accountId, int amount);

    void transferToCancelWithNoTransactionContext(long accountId, int amount);

    void transferFrom(TransactionContext transactionContext, long accountId, int amount);

    void transferFromConfirm(TransactionContext transactionContext, long accountId, int amount);

    void transferFromCancel(TransactionContext transactionContext, long accountId, int amount);

    void transferToWithMultipleTier(TransactionContext transactionContext, long accountId, int amount);

    void transferFromWithMultipleTier(TransactionContext transactionContext, long accountId, int amount);
}
