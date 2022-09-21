package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TbWalletMsigProposal;
import org.apache.ibatis.annotations.Param;

/**
 * 多签提案处理Mapper接口
 * 
 * @author system
 * @date 2022-09-20
 */
public interface TbWalletMsigProposalMapper 
{
    /**
     * 查询多签提案处理
     * 
     * @param id 多签提案处理ID
     * @return 多签提案处理
     */
    public TbWalletMsigProposal selectTbWalletMsigProposalById(Long id);

    /**
     * 查询多签提案处理列表
     * 
     * @param tbWalletMsigProposal 多签提案处理
     * @return 多签提案处理集合
     */
    public List<TbWalletMsigProposal> selectTbWalletMsigProposalList(TbWalletMsigProposal tbWalletMsigProposal);

    /**
     * 新增多签提案处理
     * 
     * @param tbWalletMsigProposal 多签提案处理
     * @return 结果
     */
    public int insertTbWalletMsigProposal(TbWalletMsigProposal tbWalletMsigProposal);

    /**
     * 修改多签提案处理
     * 
     * @param tbWalletMsigProposal 多签提案处理
     * @return 结果
     */
    public int updateTbWalletMsigProposal(TbWalletMsigProposal tbWalletMsigProposal);

    /**
     * 删除多签提案处理
     * 
     * @param id 多签提案处理ID
     * @return 结果
     */
    public int deleteTbWalletMsigProposalById(Long id);

    /**
     * 批量删除多签提案处理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTbWalletMsigProposalByIds(Long[] ids);

    /**
     * 更新已完成的提案处理状态
     * @param ids
     * @param status
     * @return
     */
    public int updateStatusForIds(@Param("ids") List<Long> ids, @Param("status") int status);
}
