package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TbWalletBind;
import org.apache.ibatis.annotations.Param;

/**
 * 用户钱包绑定Mapper接口
 * 
 * @author system
 * @date 2022-09-19
 */
public interface TbWalletBindMapper 
{
    /**
     * 查询用户钱包绑定
     * 
     * @param id 用户钱包绑定ID
     * @return 用户钱包绑定
     */
    public TbWalletBind selectTbWalletBindById(Long id);

    /**
     * 查询用户钱包绑定列表
     * 
     * @param tbWalletBind 用户钱包绑定
     * @return 用户钱包绑定集合
     */
    public List<TbWalletBind> selectTbWalletBindList(TbWalletBind tbWalletBind);

    /**
     * 新增用户钱包绑定
     * 
     * @param tbWalletBind 用户钱包绑定
     * @return 结果
     */
    public int insertTbWalletBind(TbWalletBind tbWalletBind);

    /**
     * 修改用户钱包绑定
     * 
     * @param tbWalletBind 用户钱包绑定
     * @return 结果
     */
    public int updateTbWalletBind(TbWalletBind tbWalletBind);

    /**
     * 删除用户钱包绑定
     * 
     * @param id 用户钱包绑定ID
     * @return 结果
     */
    public int deleteTbWalletBindById(Long id);

    /**
     * 批量删除用户钱包绑定
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTbWalletBindByIds(Long[] ids);

    /**
     * 查询用户钱包绑定
     *
     * @param userId 用户钱包绑定ID
     * @return 用户钱包绑定
     */
    public TbWalletBind selectTbWalletBindByUserId(@Param("userId") Long userId);
}
