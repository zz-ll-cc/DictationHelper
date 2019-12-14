package com.demo.utils;

import com.demo.dayrecord.DayRecordService;
import com.demo.record.RecordService;
import javafx.scene.chart.CategoryAxis;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.LineNumberReader;
import java.util.Date;
import java.util.List;

public class ChartUtil {

    public static ChartFrame BarChart(){
        return null;
    }
    public static ChartFrame LineChart(int uid){
        StandardChartTheme mChartTheme = new StandardChartTheme("CN");
        mChartTheme.setLargeFont(new Font("黑体", Font.BOLD, 20));
        mChartTheme.setExtraLargeFont(new Font("宋体", Font.PLAIN, 15));
        mChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 26));
        ChartFactory.setChartTheme(mChartTheme);
        CategoryDataset mDataset = GetDataset(uid);
        JFreeChart mChart = ChartFactory.createLineChart(
                null,//图名字
                "日期",//横坐标
                "正确率",//纵坐标
                mDataset,//数据集
                //PlotOrientation.VERTICAL,
                PlotOrientation.VERTICAL,
                false, // 显示图例
                true, // 采用标准生成器
                false);// 是否生成超链接
        CategoryPlot mPlot = (CategoryPlot)mChart.getPlot();
        mPlot.setDomainGridlinesVisible(true);  // x轴网格是否可见
        mPlot.setRangeGridlinesVisible(true);   // y轴网格是否可见
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) mPlot.getRenderer();
        renderer.setSeriesPaint(0, new Color(154, 207, 136));//折线颜色
        renderer.setDefaultItemLabelFont(new Font("宋体", Font.PLAIN, 28));
        renderer.setDefaultShapesVisible(true); //加转折点
        renderer.setDefaultItemLabelsVisible(true);
        //设置曲线显示各数据点的值
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
//下面三句是对设置折线图数据标示的关键代码
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelFont(new Font("Dialog", 2, 18) );
        renderer.setDefaultItemLabelPaint(new Color(154, 207, 136));
        mPlot.setRenderer(renderer);
        mPlot.setBackgroundPaint(Color.DARK_GRAY);
       // mPlot.setDrawSharedDomainAxis(false);
        //mPlot.setAnchorValue(10);
       // mPlot.setRangeGridlinePaint(Color.BLUE);//背景底部横虚线
       // mPlot.setOutlinePaint(Color.RED);//边界线
        ChartFrame mChartFrame = new ChartFrame("折线图", mChart);
        mChartFrame.pack();
//        mChartFrame.setVisible(true);
        return mChartFrame;
    }

    private static CategoryDataset GetDataset(int uid) {
        DayRecordService service=new DayRecordService();
        List<Double> arrs=service.getArr(uid);
        List<String> dates=service.getDate(uid);
        System.out.println(arrs);
        System.out.println(dates);
        DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
        for (int i=0;i<arrs.size();i++){
            mDataset.addValue(arrs.get(i),"you",dates.get(i));
        }
        System.out.println(mDataset);
        return mDataset;
    }


}
