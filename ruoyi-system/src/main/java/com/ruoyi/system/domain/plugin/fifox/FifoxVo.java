package com.ruoyi.system.domain.plugin.fifox;

import java.util.List;

/**
 * 节点监控数据
 */
public class FifoxVo {
    private SectorStatusVo sectorStatusVo;
    private List<ControlAddressVo> controlAddresses;

    public SectorStatusVo getSectorStatusVo() {
        return sectorStatusVo;
    }

    public void setSectorStatusVo(SectorStatusVo sectorStatusVo) {
        this.sectorStatusVo = sectorStatusVo;
    }

    public List<ControlAddressVo> getControlAddresses() {
        return controlAddresses;
    }

    public void setControlAddresses(List<ControlAddressVo> controlAddresses) {
        this.controlAddresses = controlAddresses;
    }
}
