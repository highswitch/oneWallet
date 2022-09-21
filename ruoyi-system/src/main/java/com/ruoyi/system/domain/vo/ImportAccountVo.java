package com.ruoyi.system.domain.vo;

public class ImportAccountVo {
    private String name;
    private String isPrivateKey;
    private String mnemonic;
    private String privateKey;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsPrivateKey() {
        return isPrivateKey;
    }

    public void setIsPrivateKey(String isPrivateKey) {
        this.isPrivateKey = isPrivateKey;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
