package com.ruoyi.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "fifoxmail")
public class FifoxMailConfig {
    private String subject;
    private String content;
    private Integer faultsNum;
    private Integer tryTimes;
    private BigDecimal minBalance;
    private String contentBalance;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFaultsNum() {
        return faultsNum;
    }

    public void setFaultsNum(Integer faultsNum) {
        this.faultsNum = faultsNum;
    }

    public Integer getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(Integer tryTimes) {
        this.tryTimes = tryTimes;
    }

    public BigDecimal getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(BigDecimal minBalance) {
        this.minBalance = minBalance;
    }

    public String getContentBalance() {
        return contentBalance;
    }

    public void setContentBalance(String contentBalance) {
        this.contentBalance = contentBalance;
    }
}
