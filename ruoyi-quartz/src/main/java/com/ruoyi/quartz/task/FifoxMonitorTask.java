package com.ruoyi.quartz.task;

import com.ruoyi.system.service.ITbFifoxMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("FifoxMonitorTask")
public class FifoxMonitorTask {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ITbFifoxMonitorService tbFifoxMonitorService;
    public void index(){
        logger.info("fifox监控任务启动~");
        tbFifoxMonitorService.fifoxMonitor();
    }
}
