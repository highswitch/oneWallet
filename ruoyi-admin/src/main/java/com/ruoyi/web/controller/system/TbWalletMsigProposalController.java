package com.ruoyi.web.controller.system;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TbWalletMsigProposal;
import com.ruoyi.system.service.ITbWalletMsigProposalService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 多签提案处理Controller
 * 
 * @author system
 * @date 2022-09-20
 */
@RestController
@RequestMapping("/system/tbWalletMsigProposal")
public class TbWalletMsigProposalController extends BaseController
{
    @Autowired
    private ITbWalletMsigProposalService tbWalletMsigProposalService;

    /**
     * 查询多签提案处理列表
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsigProposal:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbWalletMsigProposal tbWalletMsigProposal)
    {
        startPage();
        List<TbWalletMsigProposal> list = tbWalletMsigProposalService.selectTbWalletMsigProposalList(tbWalletMsigProposal);
        return getDataTable(list);
    }

    /**
     * 导出多签提案处理列表
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsigProposal:export')")
    @Log(title = "多签提案处理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TbWalletMsigProposal tbWalletMsigProposal)
    {
        List<TbWalletMsigProposal> list = tbWalletMsigProposalService.selectTbWalletMsigProposalList(tbWalletMsigProposal);
        ExcelUtil<TbWalletMsigProposal> util = new ExcelUtil<TbWalletMsigProposal>(TbWalletMsigProposal.class);
        return util.exportExcel(list, "tbWalletMsigProposal");
    }

    /**
     * 获取多签提案处理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsigProposal:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tbWalletMsigProposalService.selectTbWalletMsigProposalById(id));
    }

    /**
     * 新增多签提案处理
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsigProposal:add')")
    @Log(title = "多签提案处理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbWalletMsigProposal tbWalletMsigProposal)
    {
        return toAjax(tbWalletMsigProposalService.insertTbWalletMsigProposal(tbWalletMsigProposal));
    }

    /**
     * 修改多签提案处理
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsigProposal:edit')")
    @Log(title = "多签提案处理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbWalletMsigProposal tbWalletMsigProposal)
    {
        return toAjax(tbWalletMsigProposalService.updateTbWalletMsigProposal(tbWalletMsigProposal));
    }

    /**
     * 删除多签提案处理
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsigProposal:remove')")
    @Log(title = "多签提案处理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbWalletMsigProposalService.deleteTbWalletMsigProposalByIds(ids));
    }
}
