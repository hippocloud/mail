package com.neo.mail.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;



@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Resource
    TemplateEngine templateEngine;

    @Test
    public void sendSimpleMail() {
        mailService.sendSimpleMail("506107396@qq.com","这是我的一封邮件","哈哈，2018年9月24日");

    }
    @Test
    public void sendHtmlMail() throws MessagingException {
        String content = "<html>\n"+
                "<body>\n"+
                "<h3> hello world,这是一封html邮件！</h3>\n"+
                "</body>\n"+
                "</html>";

        mailService.sendHtmlMail("ityouknow@126.com","这是我的一封邮件",content);
    }

    @Test
    public void sendAttachmentMail() throws MessagingException {
        String filePath = "C:\\Users\\Hippo\\Desktop\\hhh\\timg.jpg";
        mailService.sendAttachmentMail("ityouknow@126.com","这是我的一封邮件","附件邮件",filePath);
    }

    @Test
    public void sendInlinResourceMail()   {
        String imgPath = "C:\\Users\\Hippo\\Desktop\\hhh\\timg.jpg";
        String rscId = "neo001";
        String content = "<html>\n"+
                "<body>\n"+
                "<h3> hello world,这是一封带图片的邮件！</h3>\n"+
                "</body>\n"+
                "</html>";
        mailService.sendInlinResourceMail("506107396@qq.com","这是我的一封邮件",content,imgPath,rscId);
    }

    @Test
    public void testTemplateMailTest() throws MessagingException {
        Context context = new Context();
        context.setVariable("id","007");
        String emailContent = templateEngine.process("emailTemplate",context);//第一个参数传模板的名字，第二个传context
        mailService.sendHtmlMail("506107396@qq.com","这是我的一封模板邮件",emailContent);
    }
}