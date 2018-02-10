package mailmonitorone;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class InitMail {
	// 配置需要的mail量
	private static final String PROTOCOL = "mail.transport.protocol";
	private static final String HOST = "mail.smtp.host";
	private static final String PORT = "mail.smtp.port";
	private static final String USERNAME = "mail.smtp.user";
	private static final String PASSWORD = "mail.smtp.password";
	private static final String AUTH = "mail.smtp.auth";

	private static final String PROTOCOL_1 = "smtp";
	private static final String HOST_1 = "smtp.163.com";
	private static final String PORT_1 = "25";
	private static final String USERNAME_1 = "15606218315@163.com";
	private static final String PASSWORD_1 = "rico840310";
	private static final String AUTH_1 = "true";

	// 参数
	private static Properties props = new Properties();
	private static Authenticator authenticator;

	private Session session;
	protected Message message;
	// java mail 中用于存放不同部分邮件内容的容器
	private Multipart multipart; 

	public InitMail() {
		init();
	}

	private void init() {
		multipart = new MimeMultipart();
		props.put(PROTOCOL, PROTOCOL_1);
		props.put(HOST, HOST_1);
		props.put(PORT, PORT_1);
		props.put(AUTH, AUTH_1);
		authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME_1, PASSWORD_1);
			}
		};
	}
	
	//获取本机相关信息
	public String getLocalHost() {
		String ip=null;
		try {
			ip=InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
	
	//创建一封邮件
	public void createMail() {
		//根据配置创建会话对象, 用于和邮件服务器交互
		session = Session.getInstance(props, authenticator);
		message = new MimeMessage(session);
			// 设置邮件的发送地址
			try {
				message.setFrom(new InternetAddress(USERNAME_1,getLocalHost(),"UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//发送邮件
	public void sendMail() {
		try {
			// 设置邮件内容
			message.setContent(multipart);
			// 发送邮件
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	//接收地址
	public void setTo(List<String> tos) {
		try {
			Address[] to = new Address[tos.size()];
			for (int i = 0; i < tos.size(); i++) {
				to[i] = new InternetAddress(tos.get(i));
			}
			message.setRecipients(RecipientType.TO, to);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}


	//邮件标题
	public void setSubject(String subject) {
		try {
			message.setSubject(subject);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	//邮件文本
	public void addContent(String content) {
		try {
			BodyPart bodyPart = new MimeBodyPart();
			bodyPart.setText(content);
			multipart.addBodyPart(bodyPart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	//邮件内容中增加附件
	private void addAttach(File attach, String header) {
		try {
			BodyPart bodyPart = new MimeBodyPart();
			DataSource dataSource = new FileDataSource(attach);
			bodyPart.setDataHandler(new DataHandler(dataSource));
			bodyPart.setFileName(attach.getName());
			if (header != null) {
				bodyPart.setHeader("Content-ID", "<" + header + ">");
			}
			multipart.addBodyPart(bodyPart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	//单独附件
	public void addAttach(File attach) {
		addAttach(attach, null);
	}

	//邮件中添加HTML
	public void addHtml(String html) {
		try {
			BodyPart bodyPart = new MimeBodyPart();
			bodyPart.setContent(html, "text/html;charset=utf8");
			multipart.addBodyPart(bodyPart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	//添加图片
	//不好使
	public void addImage(File image) {
		try {
			String header = UUID.randomUUID().toString();
			String img = "<img src=\"cid:" + header + "\">";
			addHtml(img);
			addAttach(image, header);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
