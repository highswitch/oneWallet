package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.system.service.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TbWalletMapper;
import com.ruoyi.system.domain.TbWallet;
import com.ruoyi.system.service.ITbWalletService;

/**
 * 钱包Service业务层处理
 * 
 * @author system
 * @date 2022-09-08
 */
@Service
public class TbWalletServiceImpl implements ITbWalletService 
{
    @Autowired
    private TbWalletMapper tbWalletMapper;

    @Autowired
    private IWalletService walletService;
    /**
     * 查询钱包
     * 
     * @param id 钱包ID
     * @return 钱包
     */
    @Override
    public TbWallet selectTbWalletById(Long id)
    {
        return tbWalletMapper.selectTbWalletById(id);
    }

    /**
     * 查询钱包列表
     * 
     * @param tbWallet 钱包
     * @return 钱包
     */
    @Override
    public List<TbWallet> selectTbWalletList(TbWallet tbWallet)
    {
        return tbWalletMapper.selectTbWalletList(tbWallet);
    }

    /**
     * 新增钱包
     * 
     * @param tbWallet 钱包
     * @return 结果
     */
    @Override
    public int insertTbWallet(TbWallet tbWallet)
    {
        tbWallet.setGmtCreate(new Date());
        return tbWalletMapper.insertTbWallet(tbWallet);
    }

    /**
     * 修改钱包
     * 
     * @param tbWallet 钱包
     * @return 结果
     */
    @Override
    public int updateTbWallet(TbWallet tbWallet)
    {
        return tbWalletMapper.updateTbWallet(tbWallet);
    }

    /**
     * 批量删除钱包
     * 
     * @param ids 需要删除的钱包ID
     * @return 结果
     */
    @Override
    public int deleteTbWalletByIds(Long[] ids)
    {
        return tbWalletMapper.deleteTbWalletByIds(ids);
    }

    /**
     * 删除钱包信息
     * 
     * @param id 钱包ID
     * @return 结果
     */
    @Override
    public int deleteTbWalletById(Long id)
    {
        return tbWalletMapper.deleteTbWalletById(id);
    }

    /**
     * 获取指定用户的帐号列表
     * @param tbWallet
     * @return
     */
    public List<TbWallet> getListByUser(TbWallet tbWallet){
        return tbWalletMapper.getListByUser(tbWallet);
    }
    /**
     * 查询钱包
     *
     * @param accounts 钱包ID
     * @return 钱包
     */
    public List<TbWallet> selectTbWalletByAccounts(String[] accounts){
        return tbWalletMapper.selectTbWalletByAccounts(accounts);
    };
}
