package com.ruoyi.system.domain.vo;

import java.util.List;

/**
 * 多签帐号详细信息
 */
public class WalletMsigDetailVo {
    private String balance;
    private String spendable;
    private String threshold;
    private String transaction;
    private List<SignerVo> Signers;
    private List<TransactionVo> transactions;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getSpendable() {
        return spendable;
    }

    public void setSpendable(String spendable) {
        this.spendable = spendable;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public List<SignerVo> getSigners() {
        return Signers;
    }

    public void setSigners(List<SignerVo> signers) {
        Signers = signers;
    }

    public List<TransactionVo> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionVo> transactions) {
        this.transactions = transactions;
    }
}
