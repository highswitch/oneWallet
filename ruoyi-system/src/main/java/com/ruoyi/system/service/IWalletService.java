package com.ruoyi.system.service;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.config.WalletConfig;
import com.ruoyi.common.constant.WalletConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.LinuxCmdQzlinkUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.GroupTemplateUtils;
import com.ruoyi.system.domain.TbTransfer;
import com.ruoyi.system.domain.TbWallet;
import com.ruoyi.system.domain.TbWalletMsig;
import com.ruoyi.system.domain.TbWalletMsigProposal;
import com.ruoyi.system.domain.vo.SignerVo;
import com.ruoyi.system.domain.vo.TransactionVo;
import com.ruoyi.system.domain.vo.WalletMsigDetailVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

@Service
public class IWalletService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GroupTemplateUtils groupTemplateUtils;
    @Autowired
    private WalletConfig walletConfig;

    @Autowired
    private ITbWalletMsigProposalService tbWalletMsigProposalService;

    @Autowired
    private RedisCache redisCache;

    private long cmdTimeOut = 1000 * 60 * 5;

    public IWalletService(){

    }

    public String genMnemonic(){
        String folder = walletConfig.getFolder();
        String cmd = folder + "/fil-wallet wallet mnemonic --conf-path " + folder + "/conf/config.yaml";
        String ret = LinuxCmdQzlinkUtils.getInfo(cmd);
        String mnemonic = ret.substring(ret.lastIndexOf("\n")).trim();
        return mnemonic;
    }
    public TbWallet genMnemonicConfig(TbWallet tbWallet){
        tbWallet.setIsPrivate(0);
        return genConfig(tbWallet);
    }
    public TbWallet genConfig(TbWallet tbWallet){
        String folder = walletConfig.getFolder();
        String mnemonic = "";
        String key = "";
        String configFileName = "";
        if (tbWallet.getIsPrivate() == 0){
            logger.info("助记词~");
            mnemonic = String.join(" ", tbWallet.getMnemonic());
            logger.info("mnemonic:" + mnemonic);
            configFileName = genMnemonicConfigFileName();
        } else {
            logger.info("私匙~");
            key = tbWallet.getPrivateKey();
            configFileName = genPrivateKeyConfigFileName();
        }

        Map<String, String> p = new HashMap<>();
        p.put("key", key);
        p.put("mnemonic", mnemonic);

        if (!RuoYiConfig.isTest()){
            groupTemplateUtils.genFile("/moban/wallet-config.yaml", p, folder + "/conf/" + configFileName);
        }
        tbWallet.setConf(configFileName);
        return tbWallet;
    }
    private String genMnemonicConfigFileName(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return "mnemonic-" + uuid + ".yaml";
    }
    private String genPrivateKeyConfigFileName(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return "private-" + uuid + ".yaml";
    }
    /**
     * 生成帐号
     * @param tbWallet
     * @return
     */
    public TbWallet genWalletAccount(TbWallet tbWallet){
        if (RuoYiConfig.isTest()){
            logger.info("测试环境~");
            tbWallet.setWalletIndex(0L);
            tbWallet.setAccount("f1jgmtudjp33hpmmfd6ubammuckaturz3hca6vjpi");
            return tbWallet;
        }
        String folder = walletConfig.getFolder();
        StringBuffer cmdBuffer = new StringBuffer();
        cmdBuffer.append(folder + "/fil-wallet wallet generate --conf-path " + folder + "/conf/" + tbWallet.getConf());
        if (tbWallet.getIsPrivate() != 1){
            cmdBuffer.append(" --type " + tbWallet.getType());
        }
        String ret = LinuxCmdQzlinkUtils.getInfo(cmdBuffer.toString());
        tbWallet.setWalletIndex(0L);
        tbWallet.setAccount(ret.trim());
        return tbWallet;
    }

    /**
     * 转帐
     * @param tbWallet
     * @param transfer
     * @return
     */
    public TbTransfer transfer(TbWallet tbWallet, TbTransfer transfer){
        String folder = walletConfig.getFolder();
        StringBuffer cmdBuffer = new StringBuffer();
        cmdBuffer.append(folder + "/fil-wallet wallet transfer --conf-path " + folder + "/conf/" + tbWallet.getConf());
        if (tbWallet.getIsPrivate() == 0){
            cmdBuffer.append(" --index " + tbWallet.getWalletIndex());
        }
        transfer.setFormAccount(tbWallet.getAccount());
        transfer.setGmtCreate(new Date());
        cmdBuffer.append(" --from " + tbWallet.getAccount());
        cmdBuffer.append(" --to " + transfer.getToAccount());
        cmdBuffer.append(" --amount " + transfer.getAmount());
        String ret = "";
        if (RuoYiConfig.isTest()){
            ret = "https://filfox.info/en/message/bafy2bzaced63djvl4qz7nxqgllght7akkl426c6xs7x7kbvkdb3aujncny7xo";
        }else {
            ret = LinuxCmdQzlinkUtils.getInfo(cmdBuffer.toString(), 1000 * 20);
        }
        transfer.setMessageId(ret.trim());
        transfer.setGmtModity(new Date());
        return transfer;
    }
    /**
     * 获取钱包余额
     * @param tbWallet
     * @return
     */
    public TbWallet getWalletInfo(TbWallet tbWallet){
        if (RuoYiConfig.isTest()){
            tbWallet.setBalance("0");
            return tbWallet;
        }
        String folder = walletConfig.getFolder();
        String cmd = folder + "/fil-wallet wallet balance --conf-path " + folder + "/conf/" + tbWallet.getConf() + " --type " + tbWallet.getType() +
               " " + tbWallet.getAccount();
        String ret = LinuxCmdQzlinkUtils.getInfo(cmd).trim();
        if (ret.indexOf("(") != -1){
            tbWallet.setBalance("0");
            String warningTip = ret.substring(ret.indexOf("(") + 1, + ret.lastIndexOf(")"));
            tbWallet.setWarningTip(warningTip);
        } else {
            String[] strs = ret.split(" ");
            if (strs.length < 3){
                logger.error("余额信息异常：" + ret);
                tbWallet.setWarningTip("余额信息异常:" + ret);
            } else {
                tbWallet.setAccount(strs[0]);
                tbWallet.setBalance(strs[1]);
                tbWallet.setCurrency(strs[2]);
            }
        }
        return tbWallet;
    }

    /**
     * 生成多签帐号
     * @param tbWallet
     * @param tbWalletMsig
     * @return
     */
    public TbWalletMsig genMsigWalletAccount(TbWallet tbWallet, TbWalletMsig tbWalletMsig, String uuid){
        String formAccount = tbWallet.getAccount();
        tbWalletMsig.setFormAccount(formAccount);
        String signers = String.join(",", tbWalletMsig.getSignerList());
        tbWalletMsig.setGmtCreate(new Date());
        if (RuoYiConfig.isTest()){
            tbWalletMsig.setGmtModity(new Date());
            tbWalletMsig.setSigners(signers);
            tbWalletMsig.setAccountCode("f01920138");
            tbWalletMsig.setAccount("f2guw35mlo2bj3wpstmz25x6yn3kfsdt6e5urtzwa");
            return tbWalletMsig;
        }
        String folder = walletConfig.getFolder();
        StringBuffer cmdBuffer = new StringBuffer();
        cmdBuffer.append(folder + "/fil-wallet wallet msig --conf-path " + folder + "/conf/" + tbWallet.getConf());
        if (tbWallet.getIsPrivate() == 0){
            cmdBuffer.append(" --index " + tbWallet.getWalletIndex());
        }
        cmdBuffer.append(" create --from " + formAccount);
        cmdBuffer.append(" --required " + tbWalletMsig.getRequiredUserNum());
        for (String account: tbWalletMsig.getSignerList()){
            cmdBuffer.append(" " + account);
        }

        String ret = execTimeOutCmd(cmdBuffer.toString(), uuid, cmdTimeOut).trim();
        if (ret.equals("error")){
            return null;
        }
        for (String retChild: ret.split("\r\n")){
            logger.info("retChild:" + retChild);
            if (retChild.startsWith("Created new multisig:")){
                String cmdRet = retChild.substring(retChild.lastIndexOf(":") + 1).trim();
                String[] cmdRetChild = cmdRet.split(" ");
                tbWalletMsig.setAccountCode(cmdRetChild[0].trim());
                tbWalletMsig.setAccount(cmdRetChild[1].trim());
            }
        }
        tbWalletMsig.setGmtModity(new Date());
        return tbWalletMsig;
    }

    /**
     * 生成多签帐号
     * @param cmd
     * @param uuid
     * @param timeOut
     * @return
     */
    private String execTimeOutCmd(String cmd, String uuid, long timeOut){
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
            Map<String, String> map = new HashMap<>();
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            boolean processError = false;
            StringBuffer errBuffer = new StringBuffer();
            String errLine;
            while ((errLine = errorReader.readLine()) != null){
                errBuffer.append(errBuffer + "\r\n");
                if (errLine.indexOf("WARN") != -1){
                    processError = true;
                    String[] childs = errLine.trim().split("\t");
                    if (childs.length > 4){
                        redisCache.setCacheObject(uuid, "Error:" + childs[4]);
                    } else {
                        redisCache.setCacheObject(uuid, "Error:" + errLine);
                    }
                    break;
                }
                if (System.currentTimeMillis() - startL > timeOut){
                    logger.error("执行超时~");
                    break;
                }
            }
            if (processError){
                return "error";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer cmdRetBuffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null){
                cmdRetBuffer.append(line + "\r\n");
                if (System.currentTimeMillis() - startL > timeOut){
                    logger.error("执行超时~");
                    break;
                }
            }
            redisCache.deleteObject(uuid);
            String cmdRet = cmdRetBuffer.toString().trim();
            logger.info("cmdRet:" + cmdRet);
            return cmdRet;
        } catch (Exception e){
            logger.error("异常：", e);
            return "Error: 异常";
        }
    }

    /**
     * 获取多签帐号信息
     * @param tbWallet
     * @param tbWalletMsig
     * @return
     */
    public TbWalletMsig getWalletMsigInfo(TbWallet tbWallet, TbWalletMsig tbWalletMsig){
        String folder = walletConfig.getFolder();
        StringBuffer cmdBuffer = new StringBuffer();
        cmdBuffer.append(folder + "/fil-wallet wallet msig --conf-path " + folder + "/conf/" + tbWallet.getConf());
        if (tbWallet.getIsPrivate() == 0){
            cmdBuffer.append(" --index " + tbWallet.getWalletIndex());
        }
        cmdBuffer.append(" inspect " + tbWalletMsig.getAccount());
        String ret = "";
        if (RuoYiConfig.isTest()){
            ret = getTestDetail();
        } else {
            ret = LinuxCmdQzlinkUtils.getInfo(cmdBuffer.toString());
        }

        WalletMsigDetailVo detailVo = new WalletMsigDetailVo();
        List<SignerVo> signerVos = new ArrayList<>();
        List<TransactionVo> transactionVos = new ArrayList<>();
        boolean startSigners = false;
        boolean startTransaction = false;
        for (String childRet: ret.split("\r\n")){
            if (childRet.startsWith("Balance")){
                detailVo.setBalance(childRet.substring(childRet.indexOf(":") + 1).trim());
                continue;
            }
            if (childRet.startsWith("Spendable")){
                detailVo.setSpendable(childRet.substring(childRet.indexOf(":") + 1).trim());
                continue;
            }
            if (childRet.startsWith("Threshold")){
                detailVo.setThreshold(childRet.substring(childRet.indexOf(":") + 1).trim());
                continue;
            }
            if (childRet.startsWith("Signers")){
                startSigners = true;
                continue;
            }
            if (childRet.startsWith("ID")){
                continue;
            }
            if (childRet.startsWith("Transactions")){
                detailVo.setTransaction(childRet.substring(childRet.indexOf(":") + 1).trim());
                startSigners = false;
                startTransaction = true;
                continue;
            }
            if (startSigners){
                if (childRet.indexOf(" ") == -1){
                    continue;
                }
                childRet = clearMoreSpace(childRet);
                SignerVo vo = new SignerVo(childRet);
                signerVos.add(vo);
                detailVo.setSigners(signerVos);
                continue;
            }
            if (startTransaction){
                if (childRet.indexOf(" ") == -1){
                    continue;
                }
                childRet = clearMoreSpace(childRet);
                TransactionVo vo = new TransactionVo(childRet);
                transactionVos.add(vo);
                detailVo.setTransactions(transactionVos);
            }
        }
        tbWalletMsig.setDetail(detailVo);
        return tbWalletMsig;
    }

    /**
     * 测试多签结果
     * @return
     */
    private String getTestDetail(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Balance: 0 FIL\r\n");
        stringBuffer.append("Spendable: 0 FIL\r\n");
        stringBuffer.append("Threshold: 3 / 4\r\n");
        stringBuffer.append("Signers:\r\n");
        stringBuffer.append("ID         Address\r\n");
        stringBuffer.append("f01915717  f13oq3hxs22t2n7kn7kekkxgtdqqdpra64i5kjupa\r\n");
        stringBuffer.append("f01915720  f1phn3hlko4w7eb5haqtsq6p5gqps4kmtqbdjslci\r\n");
        stringBuffer.append("f01915721  f1tz4x6ds2pcjbqljc3dabaydxeumxj3qa4qm5o7i\r\n");
        stringBuffer.append("f01913265  f1f2jwo54aeupe4ow6boqf5zivx6a2mhzp6rg3eya\r\n");
        stringBuffer.append("Transactions:  0\r\n");
        stringBuffer.append("ID      State    Approvals  To                                         Value   Method           Params\r\n");
        stringBuffer.append("0       pending  1          f2nx3va7o4662avpuuqbzskla7us5wxxvevm6owxq  0 FIL   RemoveSigner(6)  8255013a761c67fc18378a9fe523e337c6633676\r\n");
        return stringBuffer.toString();
    }

    /**
     * 移除签署人
     * @param tbWallet
     * @param tbWalletMsig
     * @param account
     * @return
     */
    public boolean removeSigner(TbWallet tbWallet, TbWalletMsig tbWalletMsig, String account, String uuid){
        String folder = walletConfig.getFolder();
        StringBuffer cmdBuffer = new StringBuffer();
        cmdBuffer.append(folder + "/fil-wallet wallet msig --conf-path " + folder + "/conf/" + tbWallet.getConf());
        if (tbWallet.getIsPrivate() == 0){
            cmdBuffer.append(" --index " + tbWallet.getWalletIndex());
        }
        cmdBuffer.append(" remove-propose");
        cmdBuffer.append(" --from " + tbWallet.getAccount());
        cmdBuffer.append(" " + tbWalletMsig.getAccount());
        cmdBuffer.append(" " + account);

        msigCmdRun(tbWallet.getUserId(), tbWallet.getId(), tbWalletMsig.getId(),
                cmdBuffer.toString(), uuid, 1, WalletConstants.methodRemoveSigner);

        return true;
    }

    /**
     * 多签帐号修改阈值
     * @param tbWallet
     * @param tbWalletMsig
     * @return
     */
    public boolean editThreshold(TbWallet tbWallet, TbWalletMsig tbWalletMsig, String uuid){
        String folder = walletConfig.getFolder();
        StringBuffer cmdBuffer = new StringBuffer();
        cmdBuffer.append(folder + "/fil-wallet wallet msig --conf-path " + folder + "/conf/" + tbWallet.getConf());
        if (tbWallet.getIsPrivate() == 0){
            cmdBuffer.append(" --index " + tbWallet.getWalletIndex());
        }
        cmdBuffer.append(" threshold-propose");
        cmdBuffer.append(" --from " + tbWallet.getAccount());
        cmdBuffer.append(" " + tbWalletMsig.getAccount());
        cmdBuffer.append(" " + tbWalletMsig.getRequiredUserNum());

        msigCmdRun(tbWallet.getUserId(), tbWallet.getId(), tbWalletMsig.getId(),
                cmdBuffer.toString(), uuid, 1, WalletConstants.methodChangeNumApprovalsThreshold);

        return true;
    }
    public boolean cancelTask(TbWallet tbWallet, TbWalletMsig tbWalletMsig, String taskId, String uuid){
        String folder = walletConfig.getFolder();
        StringBuffer cmdBuffer = new StringBuffer();
        cmdBuffer.append(folder + "/fil-wallet wallet msig --conf-path " + folder + "/conf/" + tbWallet.getConf());
        if (tbWallet.getIsPrivate() == 0){
            cmdBuffer.append(" --index " + tbWallet.getWalletIndex());
        }
        cmdBuffer.append(" cancel");
        cmdBuffer.append(" --from " + tbWallet.getAccount());
        cmdBuffer.append(" " + tbWalletMsig.getAccount());
        cmdBuffer.append(" " + taskId);
        String ret = "";
        if (!RuoYiConfig.isTest()){
            ret = execTimeOutCmd(cmdBuffer.toString(), uuid, cmdTimeOut);
        } else {
            try {
                Thread.sleep(1000 * 5);
            } catch (Exception e){

            }
        }
        return true;
    }

    /**
     * 允许提案
     * @param tbWallet
     * @param tbWalletMsig
     * @param taskId
     * @param uuid
     * @return
     */
    public boolean approveTask(TbWallet tbWallet, TbWalletMsig tbWalletMsig, String taskId, String uuid){
        String folder = walletConfig.getFolder();
        StringBuffer cmdBuffer = new StringBuffer();
        cmdBuffer.append(folder + "/fil-wallet wallet msig --conf-path " + folder + "/conf/" + tbWallet.getConf());
        if (tbWallet.getIsPrivate() == 0){
            cmdBuffer.append(" --index " + tbWallet.getWalletIndex());
        }
        cmdBuffer.append(" transfer-approve");
        cmdBuffer.append(" --from " + tbWallet.getAccount());
        cmdBuffer.append(" " + tbWalletMsig.getAccount());
        cmdBuffer.append(" " + taskId);
        String ret = "";
        if (!RuoYiConfig.isTest()){
            ret = execTimeOutCmd(cmdBuffer.toString(), uuid, cmdTimeOut);
        } else {
            try {
                Thread.sleep(1000 * 5);
            } catch (Exception e){

            }
        }
        return true;
    }
    /**
     * 多签帐号添加签署人
     * @param tbWallet
     * @param tbWalletMsig
     * @return
     */
    public boolean addSigners(TbWallet tbWallet, TbWalletMsig tbWalletMsig, String signer, String uuid){
        String folder = walletConfig.getFolder();
        StringBuffer cmdBuffer = new StringBuffer();
        cmdBuffer.append(folder + "/fil-wallet wallet msig --conf-path " + folder + "/conf/" + tbWallet.getConf());
        if (tbWallet.getIsPrivate() == 0){
            cmdBuffer.append(" --index " + tbWallet.getWalletIndex());
        }
        cmdBuffer.append(" add-propose");
        cmdBuffer.append(" --from " + tbWallet.getAccount());
        cmdBuffer.append(" " + tbWalletMsig.getAccount());
        cmdBuffer.append(" " + signer);

        msigCmdRun(tbWallet.getUserId(), tbWallet.getId(), tbWalletMsig.getId(),
                cmdBuffer.toString(), uuid, 1, WalletConstants.methodAddSigner);

        return true;
    }
    /**
     * 多签帐号添加签署人
     * @param tbWallet
     * @param tbWalletMsig
     * @return
     */
    public boolean editSigners(TbWallet tbWallet, TbWalletMsig tbWalletMsig, String oldSigner, String signer, String uuid){
        String folder = walletConfig.getFolder();
        StringBuffer cmdBuffer = new StringBuffer();
        cmdBuffer.append(folder + "/fil-wallet wallet msig --conf-path " + folder + "/conf/" + tbWallet.getConf());
        if (tbWallet.getIsPrivate() == 0){
            cmdBuffer.append(" --index " + tbWallet.getWalletIndex());
        }
        cmdBuffer.append(" swap-propose");
        cmdBuffer.append(" --from " + tbWallet.getAccount());
        cmdBuffer.append(" " + tbWalletMsig.getAccount());
        cmdBuffer.append(" " + oldSigner);
        cmdBuffer.append(" " + signer);

        msigCmdRun(tbWallet.getUserId(), tbWallet.getId(), tbWalletMsig.getId(),
                cmdBuffer.toString(), uuid, 1, WalletConstants.methodSwapSigner);

        String ret = "";
        if (!RuoYiConfig.isTest()){
            ret = execTimeOutCmd(cmdBuffer.toString(), uuid, cmdTimeOut);
        } else {
            try {
                Thread.sleep(1000 * 5);
            } catch (Exception e){

            }
        }
        return true;
    }
    private String clearMoreSpace(String cmd){
        String[] strings = cmd.split(" ");
        StringBuffer stringBuffer = new StringBuffer();
        for (String str: strings) {
            if (StringUtils.isNotBlank(str)){
                stringBuffer.append(str + " ");
            }
        }
        return stringBuffer.toString().trim();
    }

    /**
     * 执行多签命令，和解析结果
     * @param userId
     * @param walletId
     * @param walletMsigId
     * @param cmd
     * @param uuid
     */
    private void msigCmdRun(long userId, long walletId, long walletMsigId, String cmd, String uuid, int isOwner,
                            String msigMethod){
        String ret = "";
        TbWalletMsigProposal proposal = new TbWalletMsigProposal(userId, walletId, walletMsigId,
                "", msigMethod);
        if (!RuoYiConfig.isTest()){
            ret = execTimeOutCmd(cmd, uuid, cmdTimeOut);
            String[] rets = ret.split("\r\n");
            for (String child: rets){
                if (child.startsWith("https")){
                    proposal.setMsgUrl(child.trim());
                    proposal.setMsgId(child.substring(child.lastIndexOf("/") + 1).trim());
                    continue;
                }
                if (child.startsWith("txId")){
                    proposal.setTaskId(child.substring(child.lastIndexOf(":") + 1).trim());
                    continue;
                }
            }
            proposal.setIsOwner(isOwner);
            proposal.setGmtModity(new Date());
        }
        tbWalletMsigProposalService.insertTbWalletMsigProposal(proposal);
    }
    public static void main(String[] args) {
        String str = "2022-09-15T10:14:04.381+0800\tWARN\tfil-wallet\tfil_new_version/main.go:28\tunknown address network";
        String cmdRet = str.substring(str.lastIndexOf(":") + 1).trim();
        System.out.println("cmdRet:" + cmdRet);
        String[] cmdRetChild = cmdRet.split(" ");
        System.out.println("cmdRetChild:" + JSONObject.toJSONString(cmdRetChild));
        System.out.println("accountCode:" + cmdRetChild[0]);
        System.out.println("account:" + cmdRetChild[1]);
    }
}
