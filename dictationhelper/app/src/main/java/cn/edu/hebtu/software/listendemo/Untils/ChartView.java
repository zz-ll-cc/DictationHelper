package cn.edu.hebtu.software.listendemo.Untils;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.view.View;

import cn.edu.hebtu.software.listendemo.R;

public class ChartView extends View {
    public int XPoint = 80;    //原点的X坐标
    //public int YPoint = Constant.point.y / 2-10;     //原点的Y坐标
    public int YPoint = Constant.point.y /3;     //原点的Y坐标
    public int XScale = 55;     //X的刻度长度
    public int YScale = 40;     //Y的刻度长度
    public int XLength = Constant.point.x - 150;        //X轴的长度
    public int YLength = Constant.point.y / 2 - 450;        //Y轴的长度
    public String[] XLabel;    //X的刻度
    public String[] YLabel;    //Y的刻度
    public String[] Data;      //数据
    public String Title;    //显示的标题
    private String danwei;//单位

    public ChartView(Context context) {
        super(context);
    }

    public void SetInfo(String[] XLabels, String[] YLabels, String[] AllData, String strTitle,String danwei) {
        XLabel = XLabels;
        YLabel = YLabels;
        Data = AllData;
        Title = strTitle;
        XScale = XLength / AllData.length;//实际X的刻度长度
        YScale = YLength / YLabels.length;
        this.danwei=danwei;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);//重写onDraw方法

        canvas.drawColor(Color.WHITE);//设置背景颜色

        //标题
        Paint titlePaint=new Paint();
        titlePaint.setColor(Color.RED);
        titlePaint.setTextSize(60);
        titlePaint.setStrokeWidth(3);

        //轴线
        Paint xyPaint = new Paint();
        xyPaint.setStyle(Paint.Style.STROKE);
        xyPaint.setAntiAlias(true);//去锯齿
        xyPaint.setColor(Color.BLACK);//颜色
        xyPaint.setTextSize(20);
        xyPaint.setStrokeWidth(2);

        //单位
        Paint dwPaint = new Paint();
        dwPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        dwPaint.setAntiAlias(true);//去锯齿
        dwPaint.setColor(Color.BLACK);//颜色
        dwPaint.setTextSize(40);
        dwPaint.setStrokeWidth(2);

        //文字
        Paint textpaint = new Paint();
        textpaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textpaint.setAntiAlias(true);//去锯齿
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(20);  //设置轴文字大小

        //折线
        Paint linepaint = new Paint();
        linepaint.setStyle(Paint.Style.STROKE);
        linepaint.setAntiAlias(true);//去锯齿
        linepaint.setColor(getResources().getColor(R.color.orange));//颜色
        linepaint.setTextSize(20);
        linepaint.setStrokeWidth(10);

        //折线拐点
        Paint linepoint = new Paint();
        linepoint.setStyle(Paint.Style.FILL_AND_STROKE);
        linepoint.setAntiAlias(true);//去锯齿
        linepoint.setColor(getResources().getColor(R.color.orange));//颜色
        linepoint.setTextSize(60);
        linepoint.setStrokeWidth(25);

        //设置Y轴
        canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, xyPaint);   //轴线
        for (int i = 0; i * YScale < YLength; i++) {
            //canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i * YScale, duPaint);  //刻度
            try {
                canvas.drawText(YLabel[i], XPoint -65, YPoint - i * YScale + 5, textpaint);  //文字
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength + 6, linepaint);  //箭头
        canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength + 6, linepaint);
       //设置字体的大小角度等
        drawText(canvas, danwei, XPoint - 50, YPoint - YLength + YScale - 100, dwPaint, -30);

        //设置X轴
        canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, xyPaint);   //轴线
        for (int i = 0; i * XScale < XLength; i++) {
            //canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale, YPoint - 5, duPaint);  //刻度
            try {
                //canvas.drawText(XLabel[i], XPoint + i * XScale - 10, YPoint + 20, linepaint); // 文字
                drawText(canvas, XLabel[i], XPoint + i * XScale, YPoint +50, textpaint, -15); // 文字
                // 数据值
                if (i > 0 && YCoord(Data[i - 1]) != -999 && YCoord(Data[i]) != -999) // 保证有效数据
                    canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(Data[i - 1]), XPoint + i * XScale, YCoord(Data[i]), linepaint);
                    canvas.drawCircle(XPoint + i * XScale, YCoord(Data[i]), 2, linepoint);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint - 3, linepaint);    //箭头
        canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint + 3, linepaint);
        //设置标题位置
        canvas.drawText(Title, XLength / 2 - 100, 80, titlePaint);
    }

    //设置文字显示方向
    void drawText(Canvas canvas, String text, float x, float y, Paint paint, float angle) {
        if (angle != 0) {
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if (angle != 0) {
            canvas.rotate(-angle, x, y);
        }
    }

    //计算绘制时的Y坐标，无数据时返回-999
    private int YCoord(String y0){
        int y;
        try {
            y = Integer.parseInt(y0);
        } catch (Exception e) {
            e.printStackTrace();
            return -999;    //出错则返回-999
        }
        try {
            return YPoint - y * YScale / Integer.parseInt(YLabel[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return y;
    }

}
