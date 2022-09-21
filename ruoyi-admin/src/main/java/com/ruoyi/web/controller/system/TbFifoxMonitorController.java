package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.TbFifoxMonitor;
import com.ruoyi.system.service.ITbFifoxMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * fifox监听Controller
 * 
 * @author ruoyi
 * @date 2022-05-13
 */
@RestController
@RequestMapping("/system/monitor")
public class TbFifoxMonitorController extends BaseController
{
    @Autowired
    private ITbFifoxMonitorService tbFifoxMonitorService;

    /**
     * 查询fifox监听列表
     */
    @PreAuthorize("@ss.hasPermi('system:monitor:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbFifoxMonitor tbFifoxMonitor)
    {
        startPage();
        List<TbFifoxMonitor> list = tbFifoxMonitorService.selectTbFifoxMonitorList(tbFifoxMonitor);
        return getDataTable(list);
    }

    /**
     * 导出fifox监听列表
     */
    @PreAuthorize("@ss.hasPermi('system:monitor:export')")
    @Log(title = "fifox监听", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult export(HttpServletResponse response, TbFifoxMonitor tbFifoxMonitor)
    {
        List<TbFifoxMonitor> list = tbFifoxMonitorService.selectTbFifoxMonitorList(tbFifoxMonitor);
        ExcelUtil<TbFifoxMonitor> util = new ExcelUtil<TbFifoxMonitor>(TbFifoxMonitor.class);
        return util.exportExcel(list, "fifox监听数据");
    }

    /**
     * 获取fifox监听详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:monitor:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tbFifoxMonitorService.selectTbFifoxMonitorById(id));
    }

    /**
     * 新增fifox监听
     */
    @PreAuthorize("@ss.hasPermi('system:monitor:add')")
    @Log(title = "fifox监听", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbFifoxMonitor tbFifoxMonitor)
    {
        List<String> nodeList = tbFifoxMonitor.getNodeList();
        String nodes = "";
        if (nodeList != null && nodeList.size() > 0){
            nodes = String.join(",", nodeList);
        }
        List<String> mailList = tbFifoxMonitor.getEmailList();
        String emails = "";
        if (mailList != null && mailList.size() > 0) {
            emails = String.join(",", mailList);
        }
        tbFifoxMonitor.setNodes(nodes);
        tbFifoxMonitor.setEmails(emails);
        tbFifoxMonitor.setGmtCreate(new Date());
        return toAjax(tbFifoxMonitorService.insertTbFifoxMonitor(tbFifoxMonitor));
    }

    /**
     * 修改fifox监听
     */
    @PreAuthorize("@ss.hasPermi('system:monitor:edit')")
    @Log(title = "fifox监听", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbFifoxMonitor tbFifoxMonitor)
    {
        List<String> nodeList = tbFifoxMonitor.getNodeList();
        String nodes = "";
        if (nodeList != null && nodeList.size() > 0){
            nodes = String.join(",", nodeList);
        }
        List<String> mailList = tbFifoxMonitor.getEmailList();
        String emails = "";
        if (mailList != null && mailList.size() > 0) {
            emails = String.join(",", mailList);
        }
        tbFifoxMonitor.setNodes(nodes);
        tbFifoxMonitor.setEmails(emails);
        tbFifoxMonitor.setGmtModified(new Date());
        return toAjax(tbFifoxMonitorService.updateTbFifoxMonitor(tbFifoxMonitor));
    }

    /**
     * 删除fifox监听
     */
    @PreAuthorize("@ss.hasPermi('system:monitor:remove')")
    @Log(title = "fifox监听", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbFifoxMonitorService.deleteTbFifoxMonitorByIds(ids));
    }
}
