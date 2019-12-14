package com.demo.chart;

import com.demo.utils.ChartUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import org.jfree.chart.ChartFrame;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

public class ChartController extends Controller {
    @Inject
    private ChartService service;
    public void getChart(){
        int uid=getInt("uid");
        HttpServletResponse response=getResponse();
        service.getChart(response,uid);
    }
}
