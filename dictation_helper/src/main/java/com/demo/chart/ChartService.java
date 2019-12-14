package com.demo.chart;

import com.demo.utils.ChartUtil;
import org.jfree.chart.ChartFrame;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

public class ChartService {
    public void getChart(HttpServletResponse response,int uid){
        ChartUtil chartUtil=new ChartUtil();
        ChartFrame jf= chartUtil.LineChart(uid);
        //得到窗口内容面板
        Container content=jf.getContentPane();
        //创建缓冲图片对象
        BufferedImage img=new BufferedImage(
                jf.getWidth(),jf.getHeight(),BufferedImage.TYPE_INT_RGB);
        //得到图形对象
        Graphics2D g2d = img.createGraphics();
        //将窗口内容面板输出到图形对象中
        content.printAll(g2d);
        try {
            OutputStream outputStream= response.getOutputStream();
            ImageIO.write(img,"png",outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //释放图形对象
        g2d.dispose();
    }
}
