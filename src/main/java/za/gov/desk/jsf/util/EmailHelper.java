package za.gov.desk.jsf.util;

import java.io.PrintStream;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class EmailHelper
{
  public void sendEmail(Integer ref, String subject, String receiver, String msg)
  {
    Email email = new SimpleEmail();
    email.setHostName("smtp.googlemail.com");
    email.setSmtpPort(465);
    email.setAuthenticator(new DefaultAuthenticator("cashopsmail@gmail.com", "2011menus"));
    email.setSSLOnConnect(true);
    try
    {
      email.setFrom("kelvinashu@gmail.com");
      email.setSubject(subject);
      email.setMsg(msg);
      email.addTo(receiver);
      email.send();
    }
    catch (Exception e)
    {
      System.out.println("Excepton in Commons Email: " + e);
    }
  }
}
