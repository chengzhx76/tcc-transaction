/*
 * ========================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ========================================================================
 */
package org.mengyun.tcctransaction.unittest.repository;

import org.mengyun.tcctransaction.unittest.entity.SubAccount;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 子账户库.
 * Created by changmingxie on 12/3/15.
 */
@Repository
public class SubAccountRepository {

    private Map<Long, SubAccount> subAccountMap = new HashMap<Long, SubAccount>();

    {
    	/* 初始化3个子账户：余额为0 **/
        subAccountMap.put(1L, new SubAccount(1, 100));
        subAccountMap.put(2L, new SubAccount(2, 200));
        subAccountMap.put(3L, new SubAccount(3, 300));
    }

    /** 根据ID获取子账户信息 **/
    public SubAccount findById(Long id) {
        return subAccountMap.get(id);
    }
}
