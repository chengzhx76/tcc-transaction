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
import org.mengyun.tcctransaction.Compensable;
import org.mengyun.tcctransaction.unittest.client.AccountRecordServiceProxy;
import org.mengyun.tcctransaction.unittest.entity.AccountStatus;
import org.mengyun.tcctransaction.unittest.entity.SubAccount;
import org.mengyun.tcctransaction.unittest.repository.SubAccountRepository;
import org.mengyun.tcctransaction.unittest.utils.UnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by changmingxie on 10/25/15.
 */
@Service
public class AccountServiceImpl implements AccountService {


    @Autowired
    AccountRecordServiceProxy accountRecordServiceProxy;

    @Autowired
    SubAccountRepository subAccountRepository;

    @Override
    @Compensable(confirmMethod = "transferFromConfirm", cancelMethod = "transferFromCancel")
    public void transferFrom(TransactionContext transactionContext, long accountId, int amount) {
        System.out.println("transferFrom called");
        SubAccount subAccount = subAccountRepository.findById(accountId);
        subAccount.setStatus(AccountStatus.TRANSFERING.getId());
        subAccount.setBalanceAmount(subAccount.getBalanceAmount() - amount);
        accountRecordServiceProxy.record(null, accountId, amount);
    }

    /** 向子账户转账，状态为“转账中：TRANSFERING” */
    @Override
    @Compensable(confirmMethod = "transferToConfirm", cancelMethod = "transferToCancel")
    public void transferTo(TransactionContext transactionContext, long accountId, int amount) {

        System.out.println("transferTo called");
        SubAccount subAccount = subAccountRepository.findById(accountId);
        subAccount.setStatus(AccountStatus.TRANSFERING.getId());
        subAccount.setBalanceAmount(subAccount.getBalanceAmount() + amount);
    }

    @Override
    @Compensable(confirmMethod = "transferFromConfirm", cancelMethod = "transferFromCancel")
    public void transferFromWithMultipleTier(TransactionContext transactionContext, long accountId, int amount) {
        System.out.println("transferFromWithMultipleTier called");
        SubAccount subAccount = subAccountRepository.findById(accountId);
        subAccount.setStatus(AccountStatus.TRANSFERING.getId());
        subAccount.setBalanceAmount(subAccount.getBalanceAmount() + amount);
        accountRecordServiceProxy.record(null, accountId, amount);
    }

    @Override
    @Compensable(confirmMethod = "transferToConfirm", cancelMethod = "transferToCancel")
    public void transferToWithMultipleTier(TransactionContext transactionContext, long accountId, int amount) {

        System.out.println("transferToWithMultipleTier called");

        SubAccount subAccount = subAccountRepository.findById(accountId);
        subAccount.setStatus(AccountStatus.TRANSFERING.getId());
        subAccount.setBalanceAmount(subAccount.getBalanceAmount() + amount);

        accountRecordServiceProxy.record(null, accountId, amount);
    }


    @Override
    @Compensable(confirmMethod = "transferToConfirmWithNoTransactionContext", cancelMethod = "transferToCancelWithNoTransactionContext")
    public void transferToWithNoTransactionContext(long accountId, int amount) {

        System.out.println("transferToWithNoTransactionContext called");
        SubAccount subAccount = subAccountRepository.findById(accountId);
        subAccount.setStatus(AccountStatus.TRANSFERING.getId());
        subAccount.setBalanceAmount(subAccount.getBalanceAmount() + amount);
        accountRecordServiceProxy.record(null, accountId, amount);
    }


    public void transferFromConfirm(TransactionContext transactionContext, long accountId, int amount) {
        System.out.println("transferFromConfirm called");
        SubAccount subAccount = subAccountRepository.findById(accountId);
        subAccount.setStatus(AccountStatus.NORMAL.getId());
    }

    public void transferFromCancel(TransactionContext transactionContext, long accountId, int amount) {
        System.out.println("transferFromCancel called");
        SubAccount subAccount = subAccountRepository.findById(accountId);

        if (subAccount.getStatus() == AccountStatus.TRANSFERING.getId()) {

            subAccount.setStatus(AccountStatus.NORMAL.getId());
            subAccount.setBalanceAmount(subAccount.getBalanceAmount() + amount);
        }
    }

    /** 向子账户转账确认，状态改为“常规状态：NORMAL” */
    public void transferToConfirm(TransactionContext transactionContext, long accountId, int amount) {
        System.out.println("transferToConfirm called");

        if(UnitTest.CONFIRMING_EXCEPTION) {
            throw new RuntimeException("transferToConfirm confirm failed.");
        }

        SubAccount subAccount = subAccountRepository.findById(accountId);
        subAccount.setStatus(AccountStatus.NORMAL.getId());
    }

    /** 向子账户转账取消，状态改为“常规状态：NORMAL” */
    public void transferToCancel(TransactionContext transactionContext, long accountId, int amount) {
        System.out.println("transferToCancel called");

        SubAccount subAccount = subAccountRepository.findById(accountId);

        if (subAccount.getStatus() == AccountStatus.TRANSFERING.getId()) {

            subAccount.setStatus(AccountStatus.NORMAL.getId());
            subAccount.setBalanceAmount(subAccount.getBalanceAmount() - amount);
        }
    }

    public void transferToConfirmWithNoTransactionContext(long accountId, int amount) {
        System.out.println("transferToConfirmWithNoTransactionContext called");
        SubAccount subAccount = subAccountRepository.findById(accountId);
        subAccount.setStatus(AccountStatus.NORMAL.getId());
    }

    public void transferToCancelWithNoTransactionContext(long accountId, int amount) {
        System.out.println("transferToCancelWithNoTransactionContext called");

        SubAccount subAccount = subAccountRepository.findById(accountId);

        if (subAccount.getStatus() == AccountStatus.TRANSFERING.getId()) {

            subAccount.setStatus(AccountStatus.NORMAL.getId());
            subAccount.setBalanceAmount(subAccount.getBalanceAmount() - amount);
        }
    }

}
