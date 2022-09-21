package com.ruoyi.system.domain.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionVo {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String id;
    private String state;
    private String approvals;
    private String to;
    private String value;
    private String method;
    private String params;
    private int isOwner;
    private boolean proposal;
    public TransactionVo(){
        this.proposal = false;
    }
    public TransactionVo(String str){
        logger.info("TransactionVo:" + str);
        String[] strs = str.split(" ");
        this.id = strs[0];
        this.state = strs[1];
        this.approvals = strs[2];
        this.to = strs[3];
        this.value = strs[4] + " " + strs[5];
        this.method = strs[6];
        this.params = strs[7];
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getApprovals() {
        return approvals;
    }

    public void setApprovals(String approvals) {
        this.approvals = approvals;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public boolean isProposal() {
        return proposal;
    }

    public void setProposal(boolean proposal) {
        this.proposal = proposal;
    }
}
