package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.FifoxMailConfig;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.email.MailApi;
import com.ruoyi.common.utils.file.FileQzlinkUtils;
import com.ruoyi.common.utils.http.HttpUtil;
import com.ruoyi.system.domain.TbFifoxMonitor;
import com.ruoyi.system.domain.plugin.fifox.ControlAddressVo;
import com.ruoyi.system.domain.plugin.fifox.FifoxVo;
import com.ruoyi.system.domain.plugin.fifox.SectorStatusVo;
import com.ruoyi.system.mapper.TbFifoxMonitorMapper;
import com.ruoyi.system.service.ITbFifoxMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * fifox监听Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-05-13
 */
@Service
public class TbFifoxMonitorServiceImpl implements ITbFifoxMonitorService 
{
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TbFifoxMonitorMapper tbFifoxMonitorMapper;
    @Autowired
    private FifoxMailConfig fifoxMailConfig;
    @Autowired
    private MailApi mailApi;

    private static Map<String, Integer> errorFaultsMap;
    /**
     * 查询fifox监听
     * 
     * @param id fifox监听主键
     * @return fifox监听
     */
    @Override
    public TbFifoxMonitor selectTbFifoxMonitorById(Long id)
    {
        return tbFifoxMonitorMapper.selectTbFifoxMonitorById(id);
    }

    /**
     * 查询fifox监听列表
     * 
     * @param tbFifoxMonitor fifox监听
     * @return fifox监听
     */
    @Override
    public List<TbFifoxMonitor> selectTbFifoxMonitorList(TbFifoxMonitor tbFifoxMonitor)
    {
        return tbFifoxMonitorMapper.selectTbFifoxMonitorList(tbFifoxMonitor);
    }

    /**
     * 新增fifox监听
     * 
     * @param tbFifoxMonitor fifox监听
     * @return 结果
     */
    @Override
    public int insertTbFifoxMonitor(TbFifoxMonitor tbFifoxMonitor)
    {
        return tbFifoxMonitorMapper.insertTbFifoxMonitor(tbFifoxMonitor);
    }

    /**
     * 修改fifox监听
     * 
     * @param tbFifoxMonitor fifox监听
     * @return 结果
     */
    @Override
    public int updateTbFifoxMonitor(TbFifoxMonitor tbFifoxMonitor)
    {
        return tbFifoxMonitorMapper.updateTbFifoxMonitor(tbFifoxMonitor);
    }

    /**
     * 批量删除fifox监听
     * 
     * @param ids 需要删除的fifox监听主键
     * @return 结果
     */
    @Override
    public int deleteTbFifoxMonitorByIds(Long[] ids)
    {
        return tbFifoxMonitorMapper.deleteTbFifoxMonitorByIds(ids);
    }

    /**
     * 删除fifox监听信息
     * 
     * @param id fifox监听主键
     * @return 结果
     */
    @Override
    public int deleteTbFifoxMonitorById(Long id)
    {
        return tbFifoxMonitorMapper.deleteTbFifoxMonitorById(id);
    }
    /**
     * fifox任务监控
     */
    public void fifoxMonitor(){
        TbFifoxMonitor tbFifoxMonitor = new TbFifoxMonitor();
        tbFifoxMonitor.setStatus(1);
        List<TbFifoxMonitor> list = selectTbFifoxMonitorList(tbFifoxMonitor);
        if (list == null || list.size() == 0){
            logger.info("无fifox监控列表~");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
           judgeDataSendMail(list.get(i));
        }

    };
    private void judgeDataSendMail(TbFifoxMonitor monitor){
        String url = monitor.getUrl();
        List<String> emails = monitor.getEmailList();
        List<String> nodes = monitor.getNodeList();
        if (!StringUtils.isNotBlank(url) || !(url.startsWith("http://") || url.startsWith("https://"))){
            logger.error("url地址错误~" + JSONObject.toJSONString(monitor));
            return;
        }
        if (emails == null || emails.size() == 0){
            logger.error("邮件配置为空~" + JSONObject.toJSONString(monitor));
            return;
        }
        if (nodes == null || nodes.size() == 0){
            logger.error("节点配置为空~" + JSONObject.toJSONString(monitor));
            return;
        }
        boolean sendMail = false;
        StringBuffer content = new StringBuffer();
        String contentMoBan = fifoxMailConfig.getContent();
        for (int i = 0; i < nodes.size(); i++) {
            String fifoxUrl = url + nodes.get(i);
            Map<String, String> headers = new HashMap<>();
            String data = "";
            String ret = getFifoxHtmlData(fifoxUrl, headers, data, 0);
            if (!StringUtils.isNotBlank(ret)){
                sendMail = true;
                logger.info("节点数据获取失败");
                content.append("获取节点信息失败~异常地址：" + fifoxUrl + "<br>");
                continue;
            }
            FifoxVo bean = parsFifoxData(ret, fifoxUrl);
            logger.info("bean:" + JSONObject.toJSONString(bean));
            if (bean == null){
                sendMail = true;
                logger.error("获取节点信息异常~");
                content.append("获取节点信息异常~异常地址：" + fifoxUrl + "<br>");
                continue;
            }
            SectorStatusVo sectorStatusVo = bean.getSectorStatusVo();
            if (sectorStatusVo == null){
                sendMail = true;
                logger.error("节点状态获取失败~");
                content.append("节点状态获取失败~节点地址：" + fifoxUrl + "<br>");
                continue;
            }
            int faults = 0;
            try {
                faults = Integer.valueOf(sectorStatusVo.getFaults());
            } catch (Exception e){
                logger.error("异常", e);
            }
            if (faults == 0){
                logger.info("节点无异常~");
                clearErrorFaultsMap(fifoxUrl);
            }
            if (faults > fifoxMailConfig.getFaultsNum()){
                if (!judgeErrorFaultsMap(fifoxUrl, faults)){
                    continue;
                }
                logger.error("节点状态异常，需检查" + JSONObject.toJSONString(bean));
                sendMail = true;
                content.append(String.format(contentMoBan, fifoxUrl, JSONObject.toJSONString(sectorStatusVo)) + "<br>");
                continue;
            } else {
                logger.info("未超过故障数阈值，不处理~" + faults + "," + fifoxMailConfig.getFaultsNum());
            }
            // 判断余额
            List<ControlAddressVo> controlAddressVos = bean.getControlAddresses();
            if (controlAddressVos == null || controlAddressVos.size() == 0){
//                sendMail = true;
                logger.error("节点余额获取失败~" );
                continue;
            }

            ControlAddressVo controlAddressVo = controlAddressVos.get(0);
            if (controlAddressVo.getRealBalance().compareTo(fifoxMailConfig.getMinBalance()) < 0){
                logger.info("小于最小余额阈值，发送邮件，" + JSONObject.toJSONString(controlAddressVo));
                sendMail = true;
                content.append(String.format(contentMoBan, fifoxUrl, JSONObject.toJSONString(controlAddressVo)) + "<br>");
                continue;
            } else {
                logger.info("未超过最低余额阈值，不处理~" + controlAddressVo.getBalance() + "," + controlAddressVo.getRealBalance());
            }
        }
        if (sendMail){
            mailApi.init();
            mailApi.send(fifoxMailConfig.getSubject(), content.toString(), (String[]) emails.toArray());
        }

    }

    /**
     * 获取节点状态数据
     * @param fifoxUrl
     * @param headers
     * @param data
     * @param le
     * @return
     */
    private String getFifoxHtmlData(String fifoxUrl, Map<String,String> headers, String data, int le){
        String ret = HttpUtil.httpPost(fifoxUrl, headers, data);
        if (ret.indexOf("504 Gateway Time-out") != -1){
            logger.error("获取节点信息异常，重新获取：" + le + ",ret:" + ret);
            if (le >= 3){
                logger.error("重试三次，返回空字符串");
                return  "";
            }
            try {
                Thread.sleep(fifoxMailConfig.getTryTimes() * 1000);
                le = le + 1;
                getFifoxHtmlData(fifoxUrl, headers, data, le);
            } catch (Exception e){
                logger.error("异常", e);
            }
        }
        return ret;
    }
    /**
     * 解析fifox监控数据
     * @param data
     * @return
     */
    public FifoxVo parsFifoxData(String data, String url){
        FifoxVo fifoxVo = new FifoxVo();
        try {
            SectorStatusVo sectorStatusVo = parsFifoxSectorStatus(data, url);
            fifoxVo.setSectorStatusVo(sectorStatusVo);
            List<ControlAddressVo> controlAddressVos = parsFifoxControlAddress(data, url);
            fifoxVo.setControlAddresses(controlAddressVos);
        } catch (Exception e){
            logger.error("data:" + data);
            logger.error("异常:", e);
            return null;
        }
        return fifoxVo;
    }
    public SectorStatusVo parsFifoxSectorStatus(String data, String url){
        SectorStatusVo bean = new SectorStatusVo();
        try {
            String json = data.substring(data.indexOf("Sector Status:") + 14, data.indexOf("</span></div></div></div></div></div>"));
            logger.info("json:" + json);
            json = FileQzlinkUtils.delHTMLTag(json);
            logger.info("json:" + json);
            String[] strs = json.split(",  ");
            for (String str: strs){
                str = str.replaceAll(",", "");
                String title = str.substring(str.indexOf(" ") + 1);
                String val = str.substring(0, str.indexOf(" "));
                bean.setValues(title, val);
            }
        } catch (Exception e) {
            logger.error("异常:" + url, e);
            logger.error("data:" + data);
            return null;
        }
        return bean;
    }
    public List<ControlAddressVo> parsFifoxControlAddress(String data, String url){
        List<ControlAddressVo> list = new ArrayList<>();
        try {
            String controlAddressesData = data.substring(data.indexOf("controlAddresses:") + 17, data.indexOf(",peerId"));
            logger.info("controlAddressesData:" + controlAddressesData);
            list = JSONObject.parseArray(controlAddressesData, ControlAddressVo.class);
        } catch (Exception e){
            logger.info("获取节点余额异常：" + url);
            return null;
        }
        return list;
    }

    /**
     * 判断节点异常数
     * 大于之前的节点数，返回true，表示需要发邮件，
     * 小于或等于之前的节点数，返回false，表示不需要发邮件
     * @param fifoxUrl
     * @param faults
     * @return
     */
    public boolean judgeErrorFaultsMap(String fifoxUrl, int faults){
        if (errorFaultsMap == null){
            errorFaultsMap = new HashMap<>();
            logger.info("errorFaultsMap为空，初始化，发送邮件~" + fifoxUrl + "," + faults);
            errorFaultsMap.put(fifoxUrl, faults);
            return true;
        }
        if (!errorFaultsMap.containsKey(fifoxUrl)){
            logger.info("新增异常节点，发送邮件~" + fifoxUrl + "," + faults);
            errorFaultsMap.put(fifoxUrl, faults);
            return true;
        }
        int oldFaults = errorFaultsMap.get(fifoxUrl);
        logger.info("fifoxUrl:" + fifoxUrl + "," + faults);
        if (faults > oldFaults ){
            logger.info("节点异常增加，发送邮件~" + fifoxUrl + "," + faults + ", old:" + oldFaults);
            errorFaultsMap.put(fifoxUrl, faults);
            return true;
        }
        logger.info("节点状态未变更，不发送邮件，oldFaults：" + oldFaults + ",faults:" + faults);
        return false;
    }

    /**
     * 清理旧异常节点西系
     * @param fifoxUrl
     */
    public void clearErrorFaultsMap(String fifoxUrl){
        if (errorFaultsMap == null){
            errorFaultsMap = new HashMap<>();
            return;
        }
        if (errorFaultsMap.containsKey(fifoxUrl)){
            logger.info("节点已恢复正常，清理旧数据信息~" + fifoxUrl + "," + errorFaultsMap.get(fifoxUrl));
            errorFaultsMap.remove(fifoxUrl);
        }
    }

    public static void main(String[] args) {
        String url = "https://api.filscan.io:8700/rpc/v1?0=f3vtaovsjfnfr3zym3rgnhcgqwfudvb37nkkszaxiybuztm5bv2rtmrnx5bhdpxqcdaiawlchzk5ik3yuq7ssa";
        String ret = HttpUtil.httpPost(url, new HashMap<>(), "");
        System.out.println(ret);
    }
}
