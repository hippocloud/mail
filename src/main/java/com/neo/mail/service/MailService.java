package com.neo.mail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;


@Service
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.mail.username}")
    private String from;
    @Autowired(required=true)
    private JavaMailSender mailSender;

    //简单文本邮件
    public void sendSimpleMail(String to,String subject,String content){

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        simpleMailMessage.setFrom(from);

        mailSender.send(simpleMailMessage);
    }
    //html邮件
    public void sendHtmlMail(String to,String subject,String content)throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content,true);
        mailSender.send(message);
    }
    //附件邮件
    public void sendAttachmentMail(String to,String subject,String content,String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content,true);

        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = file.getFilename();
        helper.addAttachment(fileName,file);
        mailSender.send(message);
    }
    //带图片邮件
    public void sendInlinResourceMail(String to,String subject,String content,String rscPath,String rscId)   {

        logger.info("发送静态邮件开始：{},{},{},{},{}",to,subject,content,rscPath,rscId);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);

            FileSystemResource img = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId,img);
            mailSender.send(message);
            logger.info("静态文件发送成功。");
        }catch (MessagingException e){
            logger.error("静态文件发送失败",e);
        }

    }
}
