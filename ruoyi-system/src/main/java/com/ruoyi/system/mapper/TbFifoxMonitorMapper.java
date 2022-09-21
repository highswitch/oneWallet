package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TbFifoxMonitor;

import java.util.List;

/**
 * fifox监听Mapper接口
 * 
 * @author ruoyi
 * @date 2022-05-13
 */
public interface TbFifoxMonitorMapper 
{
    /**
     * 查询fifox监听
     * 
     * @param id fifox监听主键
     * @return fifox监听
     */
    public TbFifoxMonitor selectTbFifoxMonitorById(Long id);

    /**
     * 查询fifox监听列表
     * 
     * @param tbFifoxMonitor fifox监听
     * @return fifox监听集合
     */
    public List<TbFifoxMonitor> selectTbFifoxMonitorList(TbFifoxMonitor tbFifoxMonitor);

    /**
     * 新增fifox监听
     * 
     * @param tbFifoxMonitor fifox监听
     * @return 结果
     */
    public int insertTbFifoxMonitor(TbFifoxMonitor tbFifoxMonitor);

    /**
     * 修改fifox监听
     * 
     * @param tbFifoxMonitor fifox监听
     * @return 结果
     */
    public int updateTbFifoxMonitor(TbFifoxMonitor tbFifoxMonitor);

    /**
     * 删除fifox监听
     * 
     * @param id fifox监听主键
     * @return 结果
     */
    public int deleteTbFifoxMonitorById(Long id);

    /**
     * 批量删除fifox监听
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbFifoxMonitorByIds(Long[] ids);
}
