package mailmonitorone;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class GetScreenShot {
	private BufferedImage image = null;

	public String getAndWriteImage() {
		// 获取屏幕尺寸
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		// 截取最大截图
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		image = robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String name=sdf.format(new Date());
		String filePath=name+".jpg";
		String str = System.getProperty("user.home");
		File f=new File(str+"\\12138");
		//创建并隐藏
		if(!f.exists()) {
			f.mkdirs();
			String s="attrib +H \""+f.getAbsolutePath()+"\"";
			try {
				Runtime.getRuntime().exec(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		filePath=f.getAbsolutePath()+"\\"+filePath;
		System.out.println();
		//写入
		try {
			ImageIO.write(image, "jpg", new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}
	
	public boolean deleteFile(String filePath) {
		File file=new File(filePath);
		String path=file.getAbsolutePath();
		if(file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}
}
