package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TbTransfer;
import org.apache.ibatis.annotations.Param;

/**
 * 转帐Mapper接口
 * 
 * @author system
 * @date 2022-09-14
 */
public interface TbTransferMapper 
{
    /**
     * 查询转帐
     * 
     * @param id 转帐ID
     * @return 转帐
     */
    public TbTransfer selectTbTransferById(Long id);

    /**
     * 查询转帐列表
     * 
     * @param tbTransfer 转帐
     * @return 转帐集合
     */
    public List<TbTransfer> selectTbTransferList(TbTransfer tbTransfer);

    /**
     * 新增转帐
     * 
     * @param tbTransfer 转帐
     * @return 结果
     */
    public int insertTbTransfer(TbTransfer tbTransfer);

    /**
     * 修改转帐
     * 
     * @param tbTransfer 转帐
     * @return 结果
     */
    public int updateTbTransfer(TbTransfer tbTransfer);

    /**
     * 删除转帐
     * 
     * @param id 转帐ID
     * @return 结果
     */
    public int deleteTbTransferById(Long id);

    /**
     * 批量删除转帐
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTbTransferByIds(Long[] ids);

    /**
     * 获取指定帐号的交易信息
     * @param account   帐号信息
     * @param type      类型：all 全部，into 转入，out 转出
     * @return
     */
    public List<TbTransfer> listByAccount(@Param("account") String account, @Param("type") String type);
}
