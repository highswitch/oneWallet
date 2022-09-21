package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TbWallet;

/**
 * 钱包Mapper接口
 * 
 * @author system
 * @date 2022-09-08
 */
public interface TbWalletMapper 
{
    /**
     * 查询钱包
     * 
     * @param id 钱包ID
     * @return 钱包
     */
    public TbWallet selectTbWalletById(Long id);

    /**
     * 查询钱包列表
     * 
     * @param tbWallet 钱包
     * @return 钱包集合
     */
    public List<TbWallet> selectTbWalletList(TbWallet tbWallet);

    /**
     * 新增钱包
     * 
     * @param tbWallet 钱包
     * @return 结果
     */
    public int insertTbWallet(TbWallet tbWallet);

    /**
     * 修改钱包
     * 
     * @param tbWallet 钱包
     * @return 结果
     */
    public int updateTbWallet(TbWallet tbWallet);

    /**
     * 删除钱包
     * 
     * @param id 钱包ID
     * @return 结果
     */
    public int deleteTbWalletById(Long id);

    /**
     * 批量删除钱包
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTbWalletByIds(Long[] ids);

    /**
     * 获取指定用户的帐号列表
     * @param tbWallet
     * @return
     */
    public List<TbWallet> getListByUser(TbWallet tbWallet);

    /**
     * 查询钱包
     *
     * @param accounts 钱包ID
     * @return 钱包
     */
    public List<TbWallet> selectTbWalletByAccounts(String[] accounts);
}
