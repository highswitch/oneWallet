package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TbTransferMapper;
import com.ruoyi.system.domain.TbTransfer;
import com.ruoyi.system.service.ITbTransferService;

/**
 * 转帐Service业务层处理
 * 
 * @author system
 * @date 2022-09-14
 */
@Service
public class TbTransferServiceImpl implements ITbTransferService 
{
    @Autowired
    private TbTransferMapper tbTransferMapper;

    /**
     * 查询转帐
     * 
     * @param id 转帐ID
     * @return 转帐
     */
    @Override
    public TbTransfer selectTbTransferById(Long id)
    {
        return tbTransferMapper.selectTbTransferById(id);
    }

    /**
     * 查询转帐列表
     * 
     * @param tbTransfer 转帐
     * @return 转帐
     */
    @Override
    public List<TbTransfer> selectTbTransferList(TbTransfer tbTransfer)
    {
        return tbTransferMapper.selectTbTransferList(tbTransfer);
    }

    /**
     * 新增转帐
     * 
     * @param tbTransfer 转帐
     * @return 结果
     */
    @Override
    public int insertTbTransfer(TbTransfer tbTransfer)
    {
        return tbTransferMapper.insertTbTransfer(tbTransfer);
    }

    /**
     * 修改转帐
     * 
     * @param tbTransfer 转帐
     * @return 结果
     */
    @Override
    public int updateTbTransfer(TbTransfer tbTransfer)
    {
        return tbTransferMapper.updateTbTransfer(tbTransfer);
    }

    /**
     * 批量删除转帐
     * 
     * @param ids 需要删除的转帐ID
     * @return 结果
     */
    @Override
    public int deleteTbTransferByIds(Long[] ids)
    {
        return tbTransferMapper.deleteTbTransferByIds(ids);
    }

    /**
     * 删除转帐信息
     * 
     * @param id 转帐ID
     * @return 结果
     */
    @Override
    public int deleteTbTransferById(Long id)
    {
        return tbTransferMapper.deleteTbTransferById(id);
    }

    /**
     * 获取指定帐号的交易信息
     * @param account   帐号信息
     * @param type      类型：all 全部，into 转入，out 转出
     * @return
     */
    public List<TbTransfer> listByAccount(String account, String type){
        return tbTransferMapper.listByAccount(account, type);
    }
}
