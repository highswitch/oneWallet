package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.TbWallet;
import com.ruoyi.system.service.ITbWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TbWalletBindMapper;
import com.ruoyi.system.domain.TbWalletBind;
import com.ruoyi.system.service.ITbWalletBindService;

/**
 * 用户钱包绑定Service业务层处理
 * 
 * @author system
 * @date 2022-09-19
 */
@Service
public class TbWalletBindServiceImpl implements ITbWalletBindService 
{
    @Autowired
    private TbWalletBindMapper tbWalletBindMapper;

    @Autowired
    private ITbWalletService tbWalletService;

    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 查询用户钱包绑定
     * 
     * @param id 用户钱包绑定ID
     * @return 用户钱包绑定
     */
    @Override
    public TbWalletBind selectTbWalletBindById(Long id)
    {
        return tbWalletBindMapper.selectTbWalletBindById(id);
    }

    /**
     * 查询用户钱包绑定列表
     * 
     * @param tbWalletBind 用户钱包绑定
     * @return 用户钱包绑定
     */
    @Override
    public List<TbWalletBind> selectTbWalletBindList(TbWalletBind tbWalletBind)
    {
        return tbWalletBindMapper.selectTbWalletBindList(tbWalletBind);
    }

    /**
     * 新增用户钱包绑定
     * 
     * @param tbWalletBind 用户钱包绑定
     * @return 结果
     */
    @Override
    public int insertTbWalletBind(TbWalletBind tbWalletBind)
    {
        return tbWalletBindMapper.insertTbWalletBind(tbWalletBind);
    }

    /**
     * 修改用户钱包绑定
     * 
     * @param tbWalletBind 用户钱包绑定
     * @return 结果
     */
    @Override
    public int updateTbWalletBind(TbWalletBind tbWalletBind)
    {
        return tbWalletBindMapper.updateTbWalletBind(tbWalletBind);
    }

    /**
     * 批量删除用户钱包绑定
     * 
     * @param ids 需要删除的用户钱包绑定ID
     * @return 结果
     */
    @Override
    public int deleteTbWalletBindByIds(Long[] ids)
    {
        return tbWalletBindMapper.deleteTbWalletBindByIds(ids);
    }

    /**
     * 删除用户钱包绑定信息
     * 
     * @param id 用户钱包绑定ID
     * @return 结果
     */
    @Override
    public int deleteTbWalletBindById(Long id)
    {
        return tbWalletBindMapper.deleteTbWalletBindById(id);
    }

    /**
     * 查询用户钱包绑定
     *
     * @param userId 用户钱包绑定ID
     * @return 用户钱包绑定
     */
    public TbWalletBind selectTbWalletBindByUserId(Long userId){
        TbWalletBind tbWalletBind = tbWalletBindMapper.selectTbWalletBindByUserId(userId);
        if (tbWalletBind == null){
            TbWallet tbWallet = getFirstWallet(userId);
            if (tbWallet == null){
                return null;
            }
            tbWalletBind = new TbWalletBind();
            tbWalletBind.setUserId(userId);
            tbWalletBind.setWalletId(tbWallet.getId());
            tbWalletBind.setGmtCreate(new Date());
            tbWalletBind.setWalletName(tbWallet.getName());
            tbWalletBind.setWalletAccount(tbWallet.getAccount());
            insertTbWalletBind(tbWalletBind);
            return tbWalletBind;
        }
        logger.info("tbWalletBind:" + JSONObject.toJSONString(tbWalletBind));
        if (!StringUtils.isNotBlank(tbWalletBind.getWalletName())){
            logger.info("钱包信息为空~");
            TbWallet tbWallet = getFirstWallet(userId);
            if (tbWallet == null){
                logger.info("未查询到钱包账户信息");
                return null;
            }
            tbWalletBind.setWalletId(tbWallet.getId());
            tbWalletBind.setWalletName(tbWallet.getName());
            tbWalletBind.setWalletAccount(tbWallet.getAccount());
            tbWalletBind.setGmtModity(new Date());
            updateTbWalletBind(tbWalletBind);
        }
        logger.info("tbWalletBind - 1:" + JSONObject.toJSONString(tbWalletBind));
        return tbWalletBind;
    };
    private TbWallet getFirstWallet(long userId){
        TbWallet tbWallet = new TbWallet();
        tbWallet.setUserId(userId);
        List<TbWallet> tbWallets = tbWalletService.selectTbWalletList(tbWallet);
        if (tbWallets == null || tbWallets.size() == 0){
            return null;
        }
        return tbWallets.get(0);
    }
}
