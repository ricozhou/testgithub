package mailmonitorone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CreateAndSendMail {
	private static final int serverPort = 12346;

	public static void main(String[] args) {
		//占用一个端口保证城西唯一启动避免重复发送
		try {
			ServerSocket ss=new ServerSocket(serverPort);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		TimerTask task=new TimerTask() {
			@Override
			public void run() {
				circulationSend();
			}
		};
		Timer timer=new Timer();
		long delay=300*1000;
		long intevalPeriod=3600*1000;
		timer.scheduleAtFixedRate(task, delay, intevalPeriod);
	}

	public static void circulationSend() {
		// 初始化
		InitMail initMail = new InitMail();
		// 收件人列表
		List<String> list = new ArrayList<String>();
		// 收件人地址
		list.add("2320095772@qq.com");
		// 创建邮件
		initMail.createMail();
		// 主题
		initMail.setSubject("imageMonitoring");
		// 发送给
		initMail.setTo(list);
		// 读取复杂文本
		// BufferedReader reader = null;
		// try {
		// reader = new BufferedReader(new InputStreamReader(new FileInputStream(new
		// File("404.html")), "utf-8"));
		// String html = "";
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// html += line;
		// }
		// initMail.addHtml(html);
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// if (reader != null) {
		// try {
		// reader.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }

		GetScreenShot gss = new GetScreenShot();
		String filePath = gss.getAndWriteImage();

		// 添加附件
		initMail.addAttach(new File(filePath));
		// 发送
		initMail.sendMail();
		// 间隔五秒然后删除
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gss.deleteFile(filePath);
	}
}
