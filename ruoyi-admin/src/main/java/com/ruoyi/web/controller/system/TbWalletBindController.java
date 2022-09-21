package com.ruoyi.web.controller.system;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.constant.AjaxErrorMsg;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.TbWallet;
import com.ruoyi.system.service.ITbWalletService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TbWalletBind;
import com.ruoyi.system.service.ITbWalletBindService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户钱包绑定Controller
 * 
 * @author system
 * @date 2022-09-19
 */
@RestController
@RequestMapping("/system/tbWalletBind")
public class TbWalletBindController extends BaseController
{
    @Autowired
    private ITbWalletBindService tbWalletBindService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ITbWalletService tbWalletService;
    /**
     * 查询用户钱包绑定列表
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletBind:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbWalletBind tbWalletBind)
    {
        startPage();
        List<TbWalletBind> list = tbWalletBindService.selectTbWalletBindList(tbWalletBind);
        return getDataTable(list);
    }

    /**
     * 导出用户钱包绑定列表
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletBind:export')")
    @Log(title = "用户钱包绑定", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TbWalletBind tbWalletBind)
    {
        List<TbWalletBind> list = tbWalletBindService.selectTbWalletBindList(tbWalletBind);
        ExcelUtil<TbWalletBind> util = new ExcelUtil<TbWalletBind>(TbWalletBind.class);
        return util.exportExcel(list, "tbWalletBind");
    }

    /**
     * 获取用户钱包绑定详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletBind:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tbWalletBindService.selectTbWalletBindById(id));
    }

    /**
     * 新增用户钱包绑定
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletBind:add')")
    @Log(title = "用户钱包绑定", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbWalletBind tbWalletBind)
    {
        return toAjax(tbWalletBindService.insertTbWalletBind(tbWalletBind));
    }

    /**
     * 修改用户钱包绑定
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletBind:edit')")
    @Log(title = "用户钱包绑定", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbWalletBind tbWalletBind)
    {
        return toAjax(tbWalletBindService.updateTbWalletBind(tbWalletBind));
    }

    /**
     * 删除用户钱包绑定
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletBind:remove')")
    @Log(title = "用户钱包绑定", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbWalletBindService.deleteTbWalletBindByIds(ids));
    }

    /**
     * 获取钱包绑定信息
     * @return
     */
    @GetMapping("/getWalletBindInfo")
    public AjaxResult getWalletBindInfo(){
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        TbWalletBind tbWalletBind = tbWalletBindService.selectTbWalletBindByUserId(user.getUserId());
        return AjaxResult.success(tbWalletBind);
    }
    @GetMapping("/switchWalletAccount")
    public AjaxResult switchWalletAccount(@RequestParam("walletId") long walletId){
        TbWallet tbWallet = tbWalletService.selectTbWalletById(walletId);
        if (tbWallet == null){
            return AjaxResult.error(AjaxErrorMsg.notFindWallet);
        }
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        TbWalletBind tbWalletBind = tbWalletBindService.selectTbWalletBindByUserId(user.getUserId());
        TbWalletBind updateWalletBind = new TbWalletBind();
        boolean isUpdate = true;
        if (tbWalletBind == null){
            isUpdate = false;
            updateWalletBind.setUserId(user.getUserId());
            updateWalletBind.setGmtCreate(new Date());
        } else {
            updateWalletBind.setId(tbWalletBind.getId());
            updateWalletBind.setGmtModity(new Date());
        }
        updateWalletBind.setWalletId(walletId);
        if (isUpdate){
            tbWalletBindService.updateTbWalletBind(updateWalletBind);
        } else {
            tbWalletBindService.insertTbWalletBind(updateWalletBind);
        }
        return AjaxResult.success();
    }
}
