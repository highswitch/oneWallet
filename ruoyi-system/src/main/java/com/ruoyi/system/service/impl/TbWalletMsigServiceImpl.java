package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.system.domain.TbWalletMsigProposal;
import com.ruoyi.system.domain.vo.TransactionVo;
import com.ruoyi.system.domain.vo.WalletMsigDetailVo;
import org.apache.ibatis.ognl.IntHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TbWalletMsigMapper;
import com.ruoyi.system.domain.TbWalletMsig;
import com.ruoyi.system.service.ITbWalletMsigService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 多签钱包Service业务层处理
 * 
 * @author system
 * @date 2022-09-14
 */
@Service
public class TbWalletMsigServiceImpl implements ITbWalletMsigService 
{
    @Autowired
    private TbWalletMsigMapper tbWalletMsigMapper;

    /**
     * 查询多签钱包
     * 
     * @param id 多签钱包ID
     * @return 多签钱包
     */
    @Override
    public TbWalletMsig selectTbWalletMsigById(Long id)
    {
        return tbWalletMsigMapper.selectTbWalletMsigById(id);
    }

    /**
     * 查询多签钱包列表
     * 
     * @param tbWalletMsig 多签钱包
     * @return 多签钱包
     */
    @Override
    public List<TbWalletMsig> selectTbWalletMsigList(TbWalletMsig tbWalletMsig)
    {
        return tbWalletMsigMapper.selectTbWalletMsigList(tbWalletMsig);
    }

    /**
     * 新增多签钱包
     * 
     * @param tbWalletMsig 多签钱包
     * @return 结果
     */
    @Override
    public int insertTbWalletMsig(TbWalletMsig tbWalletMsig)
    {
        return tbWalletMsigMapper.insertTbWalletMsig(tbWalletMsig);
    }

    /**
     * 修改多签钱包
     * 
     * @param tbWalletMsig 多签钱包
     * @return 结果
     */
    @Override
    public int updateTbWalletMsig(TbWalletMsig tbWalletMsig)
    {
        return tbWalletMsigMapper.updateTbWalletMsig(tbWalletMsig);
    }

    /**
     * 批量删除多签钱包
     * 
     * @param ids 需要删除的多签钱包ID
     * @return 结果
     */
    @Override
    public int deleteTbWalletMsigByIds(Long[] ids)
    {
        return tbWalletMsigMapper.deleteTbWalletMsigByIds(ids);
    }

    /**
     * 删除多签钱包信息
     * 
     * @param id 多签钱包ID
     * @return 结果
     */
    @Override
    public int deleteTbWalletMsigById(Long id)
    {
        return tbWalletMsigMapper.deleteTbWalletMsigById(id);
    }

    /**
     * 查询多签钱包
     *
     * @param account 多签钱包帐号
     * @return 多签钱包
     */
    public TbWalletMsig selectTbWalletMsigByAccount(String account){
        return tbWalletMsigMapper.selectTbWalletMsigByAccount(account);
    }
    /**
     * 检查多签帐号信息，如果有变动，则更新
     * @param tbWalletMsig
     */
    public void checkWalletMsigInfo(TbWalletMsig tbWalletMsig){
        boolean isUpdate = false;
        TbWalletMsig updateWalletMsig = new TbWalletMsig();
        updateWalletMsig.setId(tbWalletMsig.getId());
        WalletMsigDetailVo detailVo = tbWalletMsig.getDetail();
        if (detailVo == null){
            return;
        }


    }
}
