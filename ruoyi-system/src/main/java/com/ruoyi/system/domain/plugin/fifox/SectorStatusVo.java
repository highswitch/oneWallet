package com.ruoyi.system.domain.plugin.fifox;

/**
 * 节点状态
 */
public class SectorStatusVo {
    private String total;
    private String active;
    private String faults;
    private String recoveries;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getFaults() {
        return faults;
    }

    public void setFaults(String faults) {
        this.faults = faults;
    }

    public String getRecoveries() {
        return recoveries;
    }

    public void setRecoveries(String recoveries) {
        this.recoveries = recoveries;
    }
    public void setValues(String title, String val){
        switch (title){
            case "total":
                this.total = val;
                break;
            case "active":
                this.active = val;
                break;
            case "faults":
                this.faults = val;
                break;
            case "recoveries":
                this.recoveries = val;
                break;
            default:

        }
    }
}
