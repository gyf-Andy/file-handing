package com.gyf.tools.verify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

/**
 * @author 郭云飞
 * @date 2022/3/15-17:52
 * @Description TODO
 */
@Component
public class SendEmail {

    @Autowired
    private JavaMailSender JavaMailSender;

    private static JavaMailSender mailSender;

    //这里必须初始化，不然会报空指针异常
    @PostConstruct
    public void init(){

        mailSender = JavaMailSender;
        System.out.println("mailSender初始化");
    }

    @Value("${spring.mail.username}")
    private String from;

//    @Autowired
//    private Environment environment;

    //application.properties中已配置的值
    //@Value("${spring.mail.username}")
    //private String myEmail;

    /**
     * 给前端输入的邮箱，发送验证码
     * @param email
     * @param session
     * @return
     */
    public boolean sendMimeMail( String email, HttpSession session) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("文件处理验证码邮件");//主题
            //生成随机数
            String code = new RandomCode().randomCode();
            System.out.println(code);

            //将随机数放置到session中
            session.setAttribute("email",email);
            session.setAttribute(email,code); //将用户的账号作为验证码的键存入session中

            mailMessage.setText("您收到的验证码是："+code);//内容

            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom("2241307402@qq.com");//发送右键的邮箱

            mailSender. send(mailMessage);
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


}
