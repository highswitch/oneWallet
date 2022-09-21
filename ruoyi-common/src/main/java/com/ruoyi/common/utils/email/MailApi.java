package com.ruoyi.common.utils.email;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.MailConfig;
import com.ruoyi.common.utils.StringUtils;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

@Component
public class MailApi {
    private static Logger log = LoggerFactory.getLogger(MailApi.class);
    private String user;
    private String pwd;
    private Session session;
    @Autowired
    private MailConfig mailConfig;
    public MailApi() {

    }
    public void init(){
        log.info("邮件配置初始化~");
        init(mailConfig.getUser(), mailConfig.getPwd());
    }
    public void init(String user, String pwd){
        this.user = user;
        this.pwd = pwd;
        if (StringUtils.isBlank(this.user) || StringUtils.isBlank(this.pwd)){
            log.error("user 或 pwd为空，请检查配置信息~");
            return;
        }
        Properties prop = new Properties();
        //协议
        prop.setProperty("mail.transport.protocol", "smtp");
        //服务器
        prop.setProperty("mail.smtp.host", "smtp.exmail.qq.com");
        //端口
        prop.setProperty("mail.smtp.port", "465");
        //使用smtp身份验证
        prop.setProperty("mail.smtp.auth", "true");
        //使用SSL，企业邮箱必需！
        //开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (Exception e1) {
            log.error("异常", e1);
        }
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        //
        //获取Session对象
        this.session = Session.getInstance(prop, new Authenticator() {
            //此访求返回用户和密码的对象
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                PasswordAuthentication pa = new PasswordAuthentication(user, pwd);
                return pa;
            }
        });
        //设置session的调试模式，发布时取消
        this.session.setDebug(true);
    }

    /**
     * 发送邮件信息
     *
     * @param subject 主题
     * @param content 内容
     * @param address 目的email
     * @return
     */
    public boolean send(String subject, String content, String[] address) {
        if (this.session == null){
            init();
        }
        log.info("subject：" + subject);
        log.info("content：" + content);
        log.info("address：" + JSONObject.toJSONString(address));
        MimeMessage mimeMessage = new MimeMessage(this.session);
        try {
            mimeMessage.setFrom(new InternetAddress(user));
            for (String addr: address){
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(addr));
            }
            //设置主题
            mimeMessage.setSubject(subject);
            mimeMessage.setSentDate(new Date());
            //设置内容
            mimeMessage.setContent(content,"text/html;charset=UTF-8");
//            mimeMessage.setText(content);
            mimeMessage.saveChanges();
            //发送
            Transport.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            log.error("异常", e);
            return false;
        }
    }

    /**
     * 发送带附件的邮件信息
     *
     * @param subject 主题
     * @param content 内容
     * @param address 目的email
     * @return
     */
    public boolean sendAndAttachment(String subject, String content, String[] address, String filePath, String fileName) {
        if (this.session == null){
            log.error("邮件初始化异常~");
            return false;
        }
        MimeMessage mimeMessage = new MimeMessage(this.session);
        try {
            mimeMessage.setFrom(new InternetAddress(user));
            for (String addr: address){
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(addr));
            }
            //设置主题
            mimeMessage.setSubject(subject);
            mimeMessage.setSentDate(new Date());
            //添加附件
            Multipart multipart = new MimeMultipart();
            //正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setDataHandler(new DataHandler(content, "text/html;charset=UTF-8"));
            multipart.addBodyPart(contentPart);

            // 添加附件
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath);
            // 添加附件的内容
            messageBodyPart.setDataHandler(new DataHandler(source));
            // 添加附件的标题
            // 这⾥很重要，通过下⾯的Base64编码的转换可以保证你的中⽂附件标题名在发送时不会变成乱码

            messageBodyPart.setFileName(MimeUtility.encodeText(fileName));
            multipart.addBodyPart(messageBodyPart);
            // 将multipart对象放到message中
            mimeMessage.setContent(multipart);
            mimeMessage.saveChanges();
            //发送
            Transport.send(mimeMessage);
            return true;
        } catch (Exception e) {
            log.error("异常", e);
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        String user = "sys-no-reply@qzlink.com";
        String pwd = "Hai123456";
        MailApi api = new MailApi();
        api.init(user, pwd);
        String subject = "AIBOT量化电子账单（2022年5月10日）";
        String content = "尊敬的%s:<br>" +
                "    感谢您一直以来以我们的支持，以下是您的电子账单，如有疑问，欢迎与我们的客服团队咨询。";
//        api.send("测试邮件", "您的验证码为：11111，<br> 详细点击，http://www.baidu.com", new String[]{"1113003302@qq.com", "liyong@qzlink.com"});
        api.send(subject, String.format(content, "测试女士"), new String[]{"1113003302@qq.com"});
//        api.sendAndAttachment(subject, String.format(content,"测试女士"), new String[]{"1113003302@qq.com"}, "D:\\res\\1.wav", "测试附件.wav");

    }
}