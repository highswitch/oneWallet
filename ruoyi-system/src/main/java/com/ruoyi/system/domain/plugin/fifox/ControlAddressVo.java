package com.ruoyi.system.domain.plugin.fifox;

import java.math.BigDecimal;

public class ControlAddressVo {
    private String address;
    private BigDecimal balance;
    private BigDecimal realBalance;
    private static final BigDecimal BIG_DECIMAL = new BigDecimal("1000000000000000000");
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
        setRealBalance(this.balance.divide(BIG_DECIMAL, 18, BigDecimal.ROUND_CEILING));
    }

    public BigDecimal getRealBalance() {
        return realBalance;
    }

    public void setRealBalance(BigDecimal realBalance) {
        this.realBalance = realBalance;
    }

}
