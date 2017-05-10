/*
 * ========================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ========================================================================
 */
package org.mengyun.tcctransaction.unittest.entity;

/**
 * 账户记录.
 * Created by changmingxie on 12/3/15.
 */
public class AccountRecord {

	/** 账户ID **/
    private long accountId;

    /** 余额 **/
    private  volatile int balanceAmount;

    /** 状态ID,默认：1 **/
    private volatile int statusId = AccountStatus.NORMAL.getId();

    public AccountRecord(long accountId, int balanceAmount) {
        this.accountId = accountId;
        this.balanceAmount = balanceAmount;
    }

    public long getAccountId() {
        return accountId;
    }

    public int getBalanceAmount() {
        return balanceAmount;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setBalanceAmount(int balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public int getStatusId() {
        return statusId;
    }
}
/*
 * ========================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ========================================================================
 */