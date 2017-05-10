/*
 * ====================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ====================================================================
 */
package org.mengyun.tcctransaction;

import java.util.Date;
import java.util.List;

import org.mengyun.tcctransaction.api.TransactionXid;

/**
 * 事务库接口.
 * Created by changmingxie on 11/12/15.
 */
public interface TransactionRepository {

	/**
	 * 创建事务日志记录.
	 * @param transaction
	 */
    int create(Transaction transaction);

    /**
     * 更新事务日志记录.
     * @param transaction
     */
    int update(Transaction transaction);

    /**
     * 删除事务日志记录.
     * @param transaction
     */
    int delete(Transaction transaction);

    /**
     * 根据xid查找事务日志记录.
     * @param xid
     * @return
     */
    Transaction findByXid(TransactionXid xid);

    /**
     * 找出所有未处理事务日志（从某一时间点开始）.
     * @return
     */
    List<Transaction> findAllUnmodifiedSince(Date date);
}
