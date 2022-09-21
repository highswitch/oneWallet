package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.TbWallet;
import com.ruoyi.system.service.ITbWalletService;
import com.ruoyi.system.service.IWalletService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TbTransfer;
import com.ruoyi.system.service.ITbTransferService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 转帐Controller
 * 
 * @author system
 * @date 2022-09-14
 */
@RestController
@RequestMapping("/system/tbTransfer")
public class TbTransferController extends BaseController
{
    @Autowired
    private ITbTransferService tbTransferService;

    @Autowired
    private ITbWalletService tbWalletService;

    @Autowired
    private IWalletService walletService;

    @Autowired
    private TokenService tokenService;
    /**
     * 查询转帐列表
     */
    @PreAuthorize("@ss.hasPermi('system:tbTransfer:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbTransfer tbTransfer)
    {
        startPage();
        List<TbTransfer> list = tbTransferService.selectTbTransferList(tbTransfer);
        return getDataTable(list);
    }

    /**
     * 导出转帐列表
     */
    @PreAuthorize("@ss.hasPermi('system:tbTransfer:export')")
    @Log(title = "转帐", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TbTransfer tbTransfer)
    {
        List<TbTransfer> list = tbTransferService.selectTbTransferList(tbTransfer);
        ExcelUtil<TbTransfer> util = new ExcelUtil<TbTransfer>(TbTransfer.class);
        return util.exportExcel(list, "tbTransfer");
    }

    /**
     * 获取转帐详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tbTransfer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tbTransferService.selectTbTransferById(id));
    }

    /**
     * 新增转帐
     */
    @PreAuthorize("@ss.hasPermi('system:tbTransfer:add')")
    @Log(title = "转帐", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbTransfer tbTransfer)
    {
        TbWallet tbWallet = tbWalletService.selectTbWalletById(tbTransfer.getWalletId());
        if (tbWallet == null){
            return AjaxResult.error("为查询到相应账户信息");
        }
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        tbTransfer.setUserId(tbWallet.getUserId());
        tbTransfer.setOperationUserId(user.getUserId());

        walletService.transfer(tbWallet, tbTransfer);
        return toAjax(tbTransferService.insertTbTransfer(tbTransfer));
    }

    /**
     * 修改转帐
     */
    @PreAuthorize("@ss.hasPermi('system:tbTransfer:edit')")
    @Log(title = "转帐", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbTransfer tbTransfer)
    {
        return toAjax(tbTransferService.updateTbTransfer(tbTransfer));
    }

    /**
     * 删除转帐
     */
    @PreAuthorize("@ss.hasPermi('system:tbTransfer:remove')")
    @Log(title = "转帐", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbTransferService.deleteTbTransferByIds(ids));
    }

    @GetMapping("/listByAccount")
    public AjaxResult listByAccount(@RequestParam("account") String account, @RequestParam("type") String type){
        List<TbTransfer> list = tbTransferService.listByAccount(account, type);
        return AjaxResult.success(list);
    }
}
