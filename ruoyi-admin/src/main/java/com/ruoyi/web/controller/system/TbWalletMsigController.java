package com.ruoyi.web.controller.system;

import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.AjaxErrorMsg;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.LinuxCmdQzlinkUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.TbWallet;
import com.ruoyi.system.domain.TbWalletBind;
import com.ruoyi.system.domain.TbWalletMsigProposal;
import com.ruoyi.system.domain.vo.TransactionVo;
import com.ruoyi.system.service.*;
import org.apache.ibatis.ognl.IntHashMap;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TbWalletMsig;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 多签钱包Controller
 * 
 * @author system
 * @date 2022-09-14
 */
@RestController
@RequestMapping("/system/tbWalletMsig")
public class TbWalletMsigController extends BaseController
{
    @Autowired
    private ITbWalletMsigService tbWalletMsigService;

    @Autowired
    private ITbWalletService tbWalletService;

    @Autowired
    private IWalletService walletService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ITbWalletMsigSignerService tbWalletMsigSignerService;

    @Autowired
    private ITbWalletMsigProposalService tbWalletMsigProposalService;

    @Autowired
    private ITbWalletBindService tbWalletBindService;
    /**
     * 查询多签钱包列表
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsig:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbWalletMsig tbWalletMsig)
    {
        startPage();
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();

        TbWalletBind tbWalletBind = tbWalletBindService.selectTbWalletBindByUserId(user.getUserId());
        TbWallet tbWallet = tbWalletService.selectTbWalletById(tbWalletBind.getWalletId());
        tbWalletMsig.setBindAccount(tbWallet.getAccount());

        List<TbWalletMsig> list = tbWalletMsigService.selectTbWalletMsigList(tbWalletMsig);
        return getDataTable(list);
    }

    /**
     * 导出多签钱包列表
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsig:export')")
    @Log(title = "多签钱包", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TbWalletMsig tbWalletMsig)
    {
        List<TbWalletMsig> list = tbWalletMsigService.selectTbWalletMsigList(tbWalletMsig);
        ExcelUtil<TbWalletMsig> util = new ExcelUtil<TbWalletMsig>(TbWalletMsig.class);
        return util.exportExcel(list, "tbWalletMsig");
    }

    /**
     * 获取多签钱包详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tbWalletMsigService.selectTbWalletMsigById(id));
    }

    /**
     * 新增多签钱包
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsig:add')")
    @Log(title = "多签钱包", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbWalletMsig tbWalletMsig)
    {
        TbWallet wallet = tbWalletService.selectTbWalletById(tbWalletMsig.getFormWalletId());
        if (wallet == null){
           return AjaxResult.error(AjaxErrorMsg.notFindWallet);
        }
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        String uuid = LinuxCmdQzlinkUtils.genCacheUUID();
        redisCache.setCacheObject(uuid, "多签帐号创建中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                tbWalletMsig.setSigners(String.join("," , tbWalletMsig.getSignerList()));
                tbWalletMsig.setUserId(user.getUserId());
                TbWalletMsig walletMsig = walletService.genMsigWalletAccount(wallet, tbWalletMsig, uuid);
                if (walletMsig == null){
                    logger.error("命令执行异常~");
                    return;
                }
                tbWalletMsigService.insertTbWalletMsig(tbWalletMsig);

                final String account = walletMsig.getAccount();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        logger.info("创建多签签署人信息~");
                        TbWalletMsig inWalletMsig = tbWalletMsigService.selectTbWalletMsigByAccount(account);
                        String signers = inWalletMsig.getSigners();
                        List<TbWallet> wallets = tbWalletService.selectTbWalletByAccounts(signers.split(","));
                        tbWalletMsigSignerService.bathcInsert(wallets, inWalletMsig.getId());
                    }
                }).start();

                logger.info("多签帐号创建完成~");
                redisCache.deleteObject(uuid);
            }
        }).start();

        return AjaxResult.success(uuid);
    }

    /**
     * 修改多签钱包
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsig:edit')")
    @Log(title = "多签钱包", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbWalletMsig tbWalletMsig)
    {
        return toAjax(tbWalletMsigService.updateTbWalletMsig(tbWalletMsig));
    }

    /**
     * 删除多签钱包
     */
    @PreAuthorize("@ss.hasPermi('system:tbWalletMsig:remove')")
    @Log(title = "多签钱包", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbWalletMsigService.deleteTbWalletMsigByIds(ids));
    }
    @GetMapping("/getTbWalletMsigDetail")
    public AjaxResult getTbWalletMsigDetail(@RequestParam("id") long id, @RequestParam("walletId") long walletId){
        TbWalletMsig tbWalletMsig = tbWalletMsigService.selectTbWalletMsigById(id);
        if (tbWalletMsig == null){
            return AjaxResult.error(AjaxErrorMsg.notFindWalletMsig);
        }
        TbWallet tbWallet = tbWalletService.selectTbWalletById(walletId);
        if (tbWallet == null){
            return AjaxResult.error(AjaxErrorMsg.notFindWallet);
        }
        walletService.getWalletMsigInfo(tbWallet, tbWalletMsig);
        updateTaskStatus(tbWalletMsig, walletId);

        tbWalletMsigService.checkWalletMsigInfo(tbWalletMsig);
        return AjaxResult.success(tbWalletMsig);
    }
    @GetMapping("/removeSigner")
    public AjaxResult removeSigner(@RequestParam("id") long id, @RequestParam("account") String account,
                                   @RequestParam("walletId") long walletId){
        TbWalletMsig tbWalletMsig = tbWalletMsigService.selectTbWalletMsigById(id);
        TbWallet tbWallet = tbWalletService.selectTbWalletById(walletId);
        String uuid = LinuxCmdQzlinkUtils.genCacheUUID();
        redisCache.setCacheObject(uuid, "任务处理中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                walletService.removeSigner(tbWallet, tbWalletMsig, account, uuid);
                redisCache.deleteObject(uuid);
            }
        }).start();

        return AjaxResult.success(uuid);
    }
    @GetMapping("/cancelTask")
    public AjaxResult cancelTask(@RequestParam("id") long id, @RequestParam("taskId") String taskId,
                                 @RequestParam("walletId") long walletId){
        TbWalletMsig tbWalletMsig = tbWalletMsigService.selectTbWalletMsigById(id);
        TbWallet tbWallet = tbWalletService.selectTbWalletById(walletId);
        String uuid = LinuxCmdQzlinkUtils.genCacheUUID();
        redisCache.setCacheObject(uuid, "任务处理中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                walletService.cancelTask(tbWallet, tbWalletMsig, taskId, uuid);
                redisCache.deleteObject(uuid);
            }
        }).start();

        return AjaxResult.success(uuid);
    }
    @GetMapping("/editThreshold")
    public AjaxResult editThreshold(@RequestParam("id") long id, @RequestParam("threshold") int threshold,
                                    @RequestParam("walletId") long walletId){
        TbWalletMsig tbWalletMsig = tbWalletMsigService.selectTbWalletMsigById(id);
        String uuid = LinuxCmdQzlinkUtils.genCacheUUID();
        redisCache.setCacheObject(uuid, "任务处理中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                TbWallet tbWallet = tbWalletService.selectTbWalletById(walletId);
                walletService.editThreshold(tbWallet, tbWalletMsig, uuid);
                redisCache.deleteObject(uuid);
            }
        }).start();
        return AjaxResult.success(uuid);
    }
    @GetMapping("/addSigners")
    public AjaxResult addSigners(@RequestParam("id") long id, @RequestParam("signer") String signer,
                                 @RequestParam("walletId") long walletId){
        TbWalletMsig tbWalletMsig = tbWalletMsigService.selectTbWalletMsigById(id);
        TbWallet tbWallet = tbWalletService.selectTbWalletById(walletId);
        String uuid = LinuxCmdQzlinkUtils.genCacheUUID();
        redisCache.setCacheObject(uuid, "任务处理中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                walletService.addSigners(tbWallet, tbWalletMsig, signer, uuid);
                redisCache.deleteObject(uuid);
            }
        }).start();

        return AjaxResult.success(uuid);
    }
    @GetMapping("/editSigners")
    public AjaxResult editSigners(@RequestParam("id") long id, @RequestParam("signer") String signer,
                                  @RequestParam("oldSigner") String oldSigner, @RequestParam("walletId") long walletId){
        TbWalletMsig tbWalletMsig = tbWalletMsigService.selectTbWalletMsigById(id);
        TbWallet tbWallet = tbWalletService.selectTbWalletById(walletId);
        String uuid = LinuxCmdQzlinkUtils.genCacheUUID();
        redisCache.setCacheObject(uuid, "任务处理中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                walletService.editSigners(tbWallet, tbWalletMsig, oldSigner, signer, uuid);
                redisCache.deleteObject(uuid);
            }
        }).start();

        return AjaxResult.success(uuid);
    }

    @GetMapping("/approveTask")
    public AjaxResult approveTask(@RequestParam("id") long id, @RequestParam("taskId") String taskId,
                                 @RequestParam("walletId") long walletId, @RequestParam("method") String method){
        TbWalletMsig tbWalletMsig = tbWalletMsigService.selectTbWalletMsigById(id);
        TbWallet tbWallet = tbWalletService.selectTbWalletById(walletId);
        String uuid = LinuxCmdQzlinkUtils.genCacheUUID();
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();

        redisCache.setCacheObject(uuid, "任务处理中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                TbWalletMsigProposal tbWalletMsigProposal = new TbWalletMsigProposal(user.getUserId(), walletId, id,
                        taskId, method);
                walletService.approveTask(tbWallet, tbWalletMsig, taskId, uuid);
                redisCache.deleteObject(uuid);
                tbWalletMsigProposal.setGmtModity(new Date());
                tbWalletMsigProposalService.insertTbWalletMsigProposal(tbWalletMsigProposal);

            }
        }).start();

        return AjaxResult.success(uuid);
    }
    /**
     * 更新多签提案信息
     * @return
     */
    private void updateTaskStatus(TbWalletMsig tbWalletMsig, long walletId){
        logger.info("更新任务状态");
        if (tbWalletMsig.getDetail() == null || tbWalletMsig.getDetail().getTransactions() == null){
            logger.info("任务列表为空");
            return;
        }
        TbWalletMsigProposal proposal = new TbWalletMsigProposal();
        proposal.setWalletId(walletId);
        proposal.setWalletMsigId(tbWalletMsig.getId());
        proposal.setStatus(0);
        logger.info("proposal:" + JSONObject.toJSONString(proposal));
        List<TbWalletMsigProposal> proposals = tbWalletMsigProposalService.selectTbWalletMsigProposalList(proposal);
        if (proposals == null || proposals.size() == 0){
            logger.info("授权信息列表为空~");
            return;
        }
        Map<String, TbWalletMsigProposal> proposalMap = new HashMap<>();
        for (int i = 0; i < proposals.size(); i++) {
            proposalMap.put(proposals.get(i).getTaskId() + "-" + proposals.get(i).getMethod(), proposals.get(i));
        }
        logger.info("proposalMap:" + JSONObject.toJSONString(proposalMap));
        List<TransactionVo> transactionals = tbWalletMsig.getDetail().getTransactions();
        for (int i = 0; i < transactionals.size(); i++) {
            String key = transactionals.get(i).getId() + "-" + transactionals.get(i).getMethod();
            if (proposalMap.containsKey(key)){
                transactionals.get(i).setProposal(true);
                transactionals.get(i).setIsOwner(proposalMap.get(key).getIsOwner());
                proposalMap.remove(key);
            }
        }
        tbWalletMsig.getDetail().setTransactions(transactionals);
        if (proposalMap.size() > 0){
            logger.info("更新已完成的提案状态~");
            List<Long> ids = new ArrayList<>();
            for (String key: proposalMap.keySet()){
                ids.add(proposalMap.get(key).getId());
            }
            tbWalletMsigProposalService.updateStatusForIds(ids, 1);
        }
    }
}
