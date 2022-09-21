package com.ruoyi.common.utils;

import com.ruoyi.common.core.redis.RedisCache;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.UUID;

/**
 * linux命令接口
 */
public class LinuxCmdQzlinkUtils {
    private static long defaultTimeOut = 1000 * 60 * 5;
    private static Logger logger = LoggerFactory.getLogger(LinuxCmdQzlinkUtils.class);
    private static final String cacheKey = "linux-cmd";
    /**
     * 获取执行结果
     * @param cmd   命令
     * @return
     */
    public static String getInfo(String cmd){
        return getInfo(cmd, defaultTimeOut);
    }
    /**
     * 带回执的定时任务
     * @param cmd           命令
     * @param timeOut       超时时间：毫秒
     * @return
     */
    public static String getInfo(String cmd, long timeOut, RedisCache redisCache, String uuid){
        try {
            logger.info("cmd:" + cmd);
            if (System.getProperty("os.name").startsWith("Windows")) {
                logger.error("windows系统:");
                return "Error: 仅支持linux系统";
            }
            if (!StringUtils.isNotBlank(cmd)){
                logger.error("命令为空");
                return "Error: 命令不可为空";
            }
            long startL = System.currentTimeMillis();
            StringBuffer ret = new StringBuffer();
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream in = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null){
                ret.append(line + "\r\n");
                redisCache.setCacheObject(uuid, ret.toString());
                if (System.currentTimeMillis() - startL > timeOut){
                    break;
                }
            }
            redisCache.deleteObject(uuid);
            String cmdRet = ret.toString().trim();
            logger.info("cmdRet:" + cmdRet);
            return cmdRet;
        } catch (Exception e){
            logger.error("异常：", e);
            return "Error: 异常";
        }
    }
    /**
     *
     * @param cmd           命令
     * @param timeOut       超时时间：毫秒
     * @return
     */
    public static String getInfo(String cmd, long timeOut){
        try {
            logger.info("cmd:" + cmd);
            if (System.getProperty("os.name").startsWith("Windows")) {
                logger.error("windows系统:");
                return "Error: 仅支持linux系统";
            }
            if (!StringUtils.isNotBlank(cmd)){
                logger.error("命令为空");
                return "Error: 命令不可为空";
            }
            long startL = System.currentTimeMillis();
            StringBuffer ret = new StringBuffer();
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream in = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null){
                ret.append(line + "\r\n");
                if (System.currentTimeMillis() - startL > timeOut){
                    logger.error("命令执行超时~");
                    break;
                }
            }
            String cmdRet = ret.toString().trim();
            logger.info("cmdRet:" + cmdRet);
            return cmdRet;
        } catch (Exception e){
            logger.error("异常：", e);
            return "Error: 异常";
        }
    }

    /**
     * 执行命令
     * @param agrs
     */
    public static void cmd(String[] agrs){
        try {
            String cmd = "";
            if (agrs == null || agrs.length == 0){
                System.err.println("命令为空");
                return;
            }
            if (agrs.length == 1){
                cmd = agrs[0];
            } else {
                cmd = String.join(" ", agrs);
            }
            getInfo(cmd);
        } catch (Exception e){
            logger.error("异常：", e);
        }
    }
    public static String genCacheUUID(){
        return "cmd-" + UUID.randomUUID().toString().replaceAll("-", "");
    }
    public static void main(String[] args) {
        LinuxCmdQzlinkUtils.cmd(args);
    }
}
