package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TbWalletMsig;
import com.ruoyi.system.domain.TbWalletMsigProposal;

/**
 * 多签钱包Service接口
 * 
 * @author system
 * @date 2022-09-14
 */
public interface ITbWalletMsigService 
{
    /**
     * 查询多签钱包
     * 
     * @param id 多签钱包ID
     * @return 多签钱包
     */
    public TbWalletMsig selectTbWalletMsigById(Long id);

    /**
     * 查询多签钱包列表
     * 
     * @param tbWalletMsig 多签钱包
     * @return 多签钱包集合
     */
    public List<TbWalletMsig> selectTbWalletMsigList(TbWalletMsig tbWalletMsig);

    /**
     * 新增多签钱包
     * 
     * @param tbWalletMsig 多签钱包
     * @return 结果
     */
    public int insertTbWalletMsig(TbWalletMsig tbWalletMsig);

    /**
     * 修改多签钱包
     * 
     * @param tbWalletMsig 多签钱包
     * @return 结果
     */
    public int updateTbWalletMsig(TbWalletMsig tbWalletMsig);

    /**
     * 批量删除多签钱包
     * 
     * @param ids 需要删除的多签钱包ID
     * @return 结果
     */
    public int deleteTbWalletMsigByIds(Long[] ids);

    /**
     * 删除多签钱包信息
     * 
     * @param id 多签钱包ID
     * @return 结果
     */
    public int deleteTbWalletMsigById(Long id);

    /**
     * 查询多签钱包
     *
     * @param account 多签钱包帐号
     * @return 多签钱包
     */
    public TbWalletMsig selectTbWalletMsigByAccount(String account);

    /**
     * 检查多签帐号信息，如果有变动，则更新
     * @param tbWalletMsig
     */
    public void checkWalletMsigInfo(TbWalletMsig tbWalletMsig);

}
