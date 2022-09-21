package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.TbWallet;
import com.ruoyi.system.domain.TbWalletBind;
import com.ruoyi.system.domain.vo.ImportAccountVo;
import com.ruoyi.system.service.ITbWalletBindService;
import com.ruoyi.system.service.ITbWalletService;
import com.ruoyi.system.service.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 钱包Controller
 * 
 * @author system
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/system/tbWallet")
public class TbWalletController extends BaseController
{
    @Autowired
    private ITbWalletService tbWalletService;

    @Autowired
    private IWalletService walletService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ITbWalletBindService tbWalletBindService;
    /**
     * 查询钱包列表
     */
    @DataScope(userAlias = "b")
    @PreAuthorize("@ss.hasPermi('system:tbWallet:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbWallet tbWallet)
    {
        startPage();
        List<TbWallet> list = tbWalletService.selectTbWalletList(tbWallet);
        return getDataTable(list);
    }

    /**
     * 导出钱包列表
     */
    @PreAuthorize("@ss.hasPermi('system:tbWallet:export')")
    @Log(title = "钱包", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TbWallet tbWallet)
    {
        List<TbWallet> list = tbWalletService.selectTbWalletList(tbWallet);
        ExcelUtil<TbWallet> util = new ExcelUtil<TbWallet>(TbWallet.class);
        return util.exportExcel(list, "tbWallet");
    }

    /**
     * 获取钱包详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tbWallet:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tbWalletService.selectTbWalletById(id));
    }

    /**
     * 新增钱包
     */
    @PreAuthorize("@ss.hasPermi('system:tbWallet:add')")
    @Log(title = "钱包", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbWallet tbWallet)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        tbWallet.setUserId(user.getUserId());
        walletService.genMnemonicConfig(tbWallet);
        walletService.genWalletAccount(tbWallet);
        return toAjax(tbWalletService.insertTbWallet(tbWallet));
    }

    /**
     * 修改钱包
     */
    @PreAuthorize("@ss.hasPermi('system:tbWallet:edit')")
    @Log(title = "钱包", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbWallet tbWallet)
    {
        return toAjax(tbWalletService.updateTbWallet(tbWallet));
    }

    /**
     * 删除钱包
     */
    @PreAuthorize("@ss.hasPermi('system:tbWallet:remove')")
    @Log(title = "钱包", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbWalletService.deleteTbWalletByIds(ids));
    }

    /**
     * 生成助记词
     * @return
     */
    @GetMapping("/genMnemonic")
    public AjaxResult genMnemonic(){
        String mnemonic = "";
        if (RuoYiConfig.isTest()){
            mnemonic = "old receive portion color rocket submit fluid lion easily chaos pulp renew";
        } else {
            mnemonic = walletService.genMnemonic();
        }
        return AjaxResult.success("success", mnemonic);
    }
    @GetMapping("/info")
    public AjaxResult info(long walletId){
        TbWallet tbWallet = tbWalletService.selectTbWalletById(walletId);
        walletService.getWalletInfo(tbWallet);
        return AjaxResult.success(tbWallet);
    }
    @PostMapping("/importAccount")
    public AjaxResult importAccount(@RequestBody TbWallet tbWallet){
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        tbWallet.setUserId(user.getUserId());
        walletService.genConfig(tbWallet);
        walletService.genWalletAccount(tbWallet);

        tbWalletService.insertTbWallet(tbWallet);
        return AjaxResult.success();
    }
    @GetMapping("/getListByUser")
    public AjaxResult getListByUser(){
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        TbWallet tbWallet = new TbWallet();
        tbWallet.setUserId(user.getUserId());
        List<TbWallet> list = tbWalletService.getListByUser(tbWallet);
        return AjaxResult.success(list);
    }
    @GetMapping("/getBindWalletInfo")
    public AjaxResult getBindWalletInfo(){
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();

        TbWalletBind tbWalletBind = tbWalletBindService.selectTbWalletBindByUserId(user.getUserId());

        TbWallet tbWallet = tbWalletService.selectTbWalletById(tbWalletBind.getWalletId());
        walletService.getWalletInfo(tbWallet);

        return AjaxResult.success(tbWallet);
    }
}

