/*
 * ====================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/course/view/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ====================================================================
 */
package org.mengyun.tcctransaction.recover;

/**
 * 事务恢复配置接口.
 * Created by changming.xie on 6/1/16.
 */
public interface RecoverConfig {

	/**
	 * 获取最大重试次数
	 * @return
	 */
    public int getMaxRetryCount();

    /**
     * 获取需要执行事务恢复的持续时间.
     * @return
     */
    public int getRecoverDuration();

    /**
     * 获取定时任务规则表达式.
     * @return
     */
    public String getCronExpression();
}
