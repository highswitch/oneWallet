package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.TbWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TbWalletMsigSignerMapper;
import com.ruoyi.system.domain.TbWalletMsigSigner;
import com.ruoyi.system.service.ITbWalletMsigSignerService;

/**
 * 多签钱包签署人Service业务层处理
 * 
 * @author system
 * @date 2022-09-15
 */
@Service
public class TbWalletMsigSignerServiceImpl implements ITbWalletMsigSignerService 
{
    @Autowired
    private TbWalletMsigSignerMapper tbWalletMsigSignerMapper;

    /**
     * 查询多签钱包签署人
     * 
     * @param id 多签钱包签署人ID
     * @return 多签钱包签署人
     */
    @Override
    public TbWalletMsigSigner selectTbWalletMsigSignerById(Long id)
    {
        return tbWalletMsigSignerMapper.selectTbWalletMsigSignerById(id);
    }

    /**
     * 查询多签钱包签署人列表
     * 
     * @param tbWalletMsigSigner 多签钱包签署人
     * @return 多签钱包签署人
     */
    @Override
    public List<TbWalletMsigSigner> selectTbWalletMsigSignerList(TbWalletMsigSigner tbWalletMsigSigner)
    {
        return tbWalletMsigSignerMapper.selectTbWalletMsigSignerList(tbWalletMsigSigner);
    }

    /**
     * 新增多签钱包签署人
     * 
     * @param tbWalletMsigSigner 多签钱包签署人
     * @return 结果
     */
    @Override
    public int insertTbWalletMsigSigner(TbWalletMsigSigner tbWalletMsigSigner)
    {
        return tbWalletMsigSignerMapper.insertTbWalletMsigSigner(tbWalletMsigSigner);
    }

    /**
     * 修改多签钱包签署人
     * 
     * @param tbWalletMsigSigner 多签钱包签署人
     * @return 结果
     */
    @Override
    public int updateTbWalletMsigSigner(TbWalletMsigSigner tbWalletMsigSigner)
    {
        return tbWalletMsigSignerMapper.updateTbWalletMsigSigner(tbWalletMsigSigner);
    }

    /**
     * 批量删除多签钱包签署人
     * 
     * @param ids 需要删除的多签钱包签署人ID
     * @return 结果
     */
    @Override
    public int deleteTbWalletMsigSignerByIds(Long[] ids)
    {
        return tbWalletMsigSignerMapper.deleteTbWalletMsigSignerByIds(ids);
    }

    /**
     * 删除多签钱包签署人信息
     * 
     * @param id 多签钱包签署人ID
     * @return 结果
     */
    @Override
    public int deleteTbWalletMsigSignerById(Long id)
    {
        return tbWalletMsigSignerMapper.deleteTbWalletMsigSignerById(id);
    }
    /**
     * 批量插入多签钱包签署人
     * @param list          钱包列表
     * @param  walletMsigId 多签钱包ID
     * @return
     */
    public int bathcInsert(List<TbWallet> list, Long walletMsigId){
        List<TbWalletMsigSigner> signers = new ArrayList<>();
        Date date = new Date();
        for (int i = 0; i < list.size(); i++) {
            TbWalletMsigSigner signer = new TbWalletMsigSigner();
            signer.setUserId(list.get(i).getUserId());
            signer.setWalletMsigId(walletMsigId);
            signer.setWalletId(list.get(i).getId());
            signer.setGmtCreate(date);
            signer.setAccount(list.get(i).getAccount());
            signers.add(signer);
        }
        return tbWalletMsigSignerMapper.bathcInsert(signers);
    }
}
