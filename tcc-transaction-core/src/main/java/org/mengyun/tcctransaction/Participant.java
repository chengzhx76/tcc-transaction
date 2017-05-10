/*
 * ====================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ====================================================================
 */
package org.mengyun.tcctransaction;

import org.apache.log4j.Logger;
import org.mengyun.tcctransaction.api.TransactionXid;

import java.io.Serializable;

/**
 * 事务参与者.
 * Created by changmingxie on 10/27/15.
 */
public class Participant implements Serializable {
	
	static final Logger LOG = Logger.getLogger(Participant.class.getSimpleName());

    private static final long serialVersionUID = 4127729421281425247L;
    
    private TransactionXid xid;

    private Terminator terminator;

    public Participant() {

    }

    public Participant(TransactionXid xid, Terminator terminator) {
        this.xid = xid;
        this.terminator = terminator;
    }

    /**
     * 回滚参与者事务（在Transaction中被调用）
     */
    public void rollback() {
    	LOG.debug("==>Participant.rollback()");
        terminator.rollback();
    }

    /**
     * 提交参与者事务（在Transaction中被调用）.
     */
    public void commit() {
    	LOG.debug("==>Participant.rollback()");
        terminator.commit();
    }

}
