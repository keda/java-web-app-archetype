package ${package}.email;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ${package}.mail.EmailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/beans/mail-beans.xml")
public class EmailServiceTest {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EmailService emailService;
	
	@Test
	public void test() throws MessagingException {
		
	    Map<String, Object> vars = new HashMap<String, Object>();
	    vars.put("name", "Shaojz");
	    vars.put("subscriptionDate", new Date());
	    vars.put("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
	    vars.put("imageResourceName", null); // so that we can reference it from HTML
	    
		emailService.sendMailWithInline("735966818@qq.com", "jinzhou.shao@amssy.com", "测试邮件", "email-inlineimage.html", vars, Locale.CHINESE);
		logger.info("email send success!");
	}

}
