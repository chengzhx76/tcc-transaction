/*
 * ====================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ====================================================================
 */
package org.mengyun.tcctransaction.repository.helper;

import org.mengyun.tcctransaction.Transaction;
import org.mengyun.tcctransaction.serializer.ObjectSerializer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by changming.xie on 9/15/16.
 */
public class TransactionSerializer {

    public static byte[] serialize(ObjectSerializer serializer, Transaction transaction) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("GLOBAL_TX_ID", transaction.getXid().getGlobalTransactionId());
        map.put("BRANCH_QUALIFIER", transaction.getXid().getBranchQualifier());
        map.put("STATUS", transaction.getStatus().getId());
        map.put("TRANSACTION_TYPE", transaction.getTransactionType().getId());
        map.put("RETRIED_COUNT", transaction.getRetriedCount());
        map.put("CREATE_TIME", transaction.getCreateTime());
        map.put("LAST_UPDATE_TIME", transaction.getLastUpdateTime());
        map.put("VERSION", transaction.getVersion());
        map.put("CONTENT", serializer.serialize(transaction));

        return serializer.serialize(map);
    }

    public static Transaction deserialize(ObjectSerializer serializer, byte[] value) {

        Map<String, Object> map = (Map<String, Object>) serializer.deserialize(value);

        byte[] content = (byte[]) map.get("CONTENT");
        Transaction transaction = (Transaction) serializer.deserialize(content);
        transaction.resetRetriedCount((Integer) map.get("RETRIED_COUNT"));
        transaction.setLastUpdateTime((Date) map.get("LAST_UPDATE_TIME"));
        transaction.setVersion((Long) map.get("VERSION"));
        return transaction;
    }
}
