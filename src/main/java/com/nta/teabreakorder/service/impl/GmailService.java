package com.nta.teabreakorder.service.impl;


import com.nta.teabreakorder.model.User;
import com.nta.teabreakorder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

@Component
public class GmailService {

    @Value("${gmail.app.username}")
    private String username;

    @Value("${gmail.app.password}")
    private String password;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Properties prop = null;

    private Session session = null;

    @Autowired
    private UserRepository userRepository;

    public String resetEmail(String username) throws Exception {

        if (session == null) {
            initClient();
        }

        User user = userRepository.findByUsername(username).orElseThrow(() -> new Exception("User not found"));
        String newPass = generateNewPassword(6);
        System.out.println(newPass);

        Message message = new MimeMessage(session);
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(user.getEmail())
        );
        message.setFrom(new InternetAddress(username, "NTA - TEA BREAK ORDER"));
        message.setSubject("Forgot password ");
        message.setContent("<!doctype html>\n" +
                "<html lang=\"en-US\">\n" +
                "<head>\n" +
                "    <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/>\n" +
                "    <title>Reset Password Email Template</title>\n" +
                "    <meta name=\"description\" content=\"Reset Password Email Template.\">\n" +
                "    <style type=\"text/css\">\n" +
                "        a:hover {\n" +
                "            text-decoration: underline !important;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body marginheight=\"0\" topmargin=\"0\" marginwidth=\"0\" style=\"margin: 0px; background-color: #f2f3f8;\" leftmargin=\"0\">\n" +
                "<!--100% body table-->\n" +
                "<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#f2f3f8\"\n" +
                "       style=\"@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;\">\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <table style=\"background-color: #f2f3f8; max-width:670px;  margin:0 auto;\" width=\"100%\" border=\"0\"\n" +
                "                   align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                <tr>\n" +
                "                    <td style=\"height:80px;\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td style=\"text-align:center;\">\n" +
                "                        <a href=\"https://rakeshmandal.com\" title=\"logo\" target=\"_blank\">\n" +
                "                            <img width=\"200\"\n" +
                "                                 src=\"https://haymora.com/upload/images/cong_nghe_thong_tin/thang_3_-_2019/nitrotech_asia/nitro-techasia-logo.png\"\n" +
                "                                 title=\"logo\" alt=\"logo\">\n" +
                "                        </a>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <table width=\"95%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "                               style=\"max-width:670px;background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);\">\n" +
                "                            <tr>\n" +
                "                                <td style=\"height:40px;\">&nbsp;</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td style=\"padding:0 35px;\">\n" +
                "                                    <h6 style=\"color:#1e1e2d; font-weight:500; margin:0;font-size:22px;font-family:'Rubik',sans-serif;\">\n" +
                "                                        You have\n" +
                "                                        requested to reset your password</h6>\n" +
                "                                    <span\n" +
                "                                            style=\"display:inline-block; vertical-align:middle; margin:10px 0 10px; border-bottom:1px solid #cecece; width:100px;color: blue \"></span>\n" +
                "                                    <h3 style=\"color:#0000ff; font-weight:500; margin:0;font-size:28px;font-family:'Rubik',sans-serif;\">\n" +
                "                                        " + newPass + "</h3>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td style=\"height:40px;\">&nbsp;</td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                <tr>\n" +
                "                    <td style=\"height:20px;\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "\n" +
                "                <tr>\n" +
                "                    <td style=\"height:80px;\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "<!--/100% body table-->\n" +
                "</body>\n" +
                "</html>", "text/html");
        Transport.send(message);
        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);
        return user.getEmail().replaceAll("(?<=.{3}).(?=.*@)", "*");
    }

    private void initClient() {
        System.out.println("===============INIT GMAIL==============");
        prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    private String generateNewPassword(int size) {
        StringBuilder pass = new StringBuilder();
        int index = 0;
        Random random = new Random();
        do {
            pass.append(String.valueOf(random.nextInt(10)));
            index++;
        } while (index < size);

        return pass.toString();
    }

}
