package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.TbWallet;
import com.ruoyi.system.domain.TbWalletMsigSigner;

/**
 * 多签钱包签署人Service接口
 * 
 * @author system
 * @date 2022-09-15
 */
public interface ITbWalletMsigSignerService 
{
    /**
     * 查询多签钱包签署人
     * 
     * @param id 多签钱包签署人ID
     * @return 多签钱包签署人
     */
    public TbWalletMsigSigner selectTbWalletMsigSignerById(Long id);

    /**
     * 查询多签钱包签署人列表
     * 
     * @param tbWalletMsigSigner 多签钱包签署人
     * @return 多签钱包签署人集合
     */
    public List<TbWalletMsigSigner> selectTbWalletMsigSignerList(TbWalletMsigSigner tbWalletMsigSigner);

    /**
     * 新增多签钱包签署人
     * 
     * @param tbWalletMsigSigner 多签钱包签署人
     * @return 结果
     */
    public int insertTbWalletMsigSigner(TbWalletMsigSigner tbWalletMsigSigner);

    /**
     * 修改多签钱包签署人
     * 
     * @param tbWalletMsigSigner 多签钱包签署人
     * @return 结果
     */
    public int updateTbWalletMsigSigner(TbWalletMsigSigner tbWalletMsigSigner);

    /**
     * 批量删除多签钱包签署人
     * 
     * @param ids 需要删除的多签钱包签署人ID
     * @return 结果
     */
    public int deleteTbWalletMsigSignerByIds(Long[] ids);

    /**
     * 删除多签钱包签署人信息
     * 
     * @param id 多签钱包签署人ID
     * @return 结果
     */
    public int deleteTbWalletMsigSignerById(Long id);

    /**
     * 批量插入多签钱包签署人
     * @param list
     * @return
     */
    public int bathcInsert(List<TbWallet> list, Long walletMsigId);
}
