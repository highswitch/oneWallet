package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TbWalletMsigProposalMapper;
import com.ruoyi.system.domain.TbWalletMsigProposal;
import com.ruoyi.system.service.ITbWalletMsigProposalService;

/**
 * 多签提案处理Service业务层处理
 * 
 * @author system
 * @date 2022-09-20
 */
@Service
public class TbWalletMsigProposalServiceImpl implements ITbWalletMsigProposalService 
{
    @Autowired
    private TbWalletMsigProposalMapper tbWalletMsigProposalMapper;

    /**
     * 查询多签提案处理
     * 
     * @param id 多签提案处理ID
     * @return 多签提案处理
     */
    @Override
    public TbWalletMsigProposal selectTbWalletMsigProposalById(Long id)
    {
        return tbWalletMsigProposalMapper.selectTbWalletMsigProposalById(id);
    }

    /**
     * 查询多签提案处理列表
     * 
     * @param tbWalletMsigProposal 多签提案处理
     * @return 多签提案处理
     */
    @Override
    public List<TbWalletMsigProposal> selectTbWalletMsigProposalList(TbWalletMsigProposal tbWalletMsigProposal)
    {
        return tbWalletMsigProposalMapper.selectTbWalletMsigProposalList(tbWalletMsigProposal);
    }

    /**
     * 新增多签提案处理
     * 
     * @param tbWalletMsigProposal 多签提案处理
     * @return 结果
     */
    @Override
    public int insertTbWalletMsigProposal(TbWalletMsigProposal tbWalletMsigProposal)
    {
        return tbWalletMsigProposalMapper.insertTbWalletMsigProposal(tbWalletMsigProposal);
    }

    /**
     * 修改多签提案处理
     * 
     * @param tbWalletMsigProposal 多签提案处理
     * @return 结果
     */
    @Override
    public int updateTbWalletMsigProposal(TbWalletMsigProposal tbWalletMsigProposal)
    {
        return tbWalletMsigProposalMapper.updateTbWalletMsigProposal(tbWalletMsigProposal);
    }

    /**
     * 批量删除多签提案处理
     * 
     * @param ids 需要删除的多签提案处理ID
     * @return 结果
     */
    @Override
    public int deleteTbWalletMsigProposalByIds(Long[] ids)
    {
        return tbWalletMsigProposalMapper.deleteTbWalletMsigProposalByIds(ids);
    }

    /**
     * 删除多签提案处理信息
     * 
     * @param id 多签提案处理ID
     * @return 结果
     */
    @Override
    public int deleteTbWalletMsigProposalById(Long id)
    {
        return tbWalletMsigProposalMapper.deleteTbWalletMsigProposalById(id);
    }

    /**
     * 更新已完成的提案处理状态
     * @param ids
     * @param status
     * @return
     */
    public int updateStatusForIds(List<Long> ids, int status){
        return tbWalletMsigProposalMapper.updateStatusForIds(ids, status);
    }
}
