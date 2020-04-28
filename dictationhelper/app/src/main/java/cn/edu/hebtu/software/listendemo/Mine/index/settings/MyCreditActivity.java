package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.edu.hebtu.software.listendemo.R;

public class MyCreditActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
    private SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");// HH:mm
    private long studyTime = 0;
    private long time1 = 0;
    private long time2 = 0;

    private LinearLayout lltime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_credit);
        findViews();
        //startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

        Calendar beginCal = Calendar.getInstance();
        beginCal.add(Calendar.HOUR_OF_DAY, -1);
        Calendar endCal = Calendar.getInstance();
        long startTime = beginCal.getTimeInMillis();
        long endTime = endCal.getTimeInMillis();



//        ArrayList<UsageEvents.Event> mEventList=getEventList(this,startTime,endTime);
//        if(mEventList.size()>0){
//            Log.e("mEventList","not NULL");
//        }else{
//            Log.e("mEventList","NULL");
//            textView.setText("0分钟");
//        }

    }

    private void findViews() {
        lltime=findViewById(R.id.ll_my_credit_time);
        //textView = findViewById(R.id.my_point);
    }

//    @SuppressWarnings("ResourceType")
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public  ArrayList<UsageEvents.Event> getEventList(Context context, long startTime, long endTime){
//
//        ArrayList<UsageEvents.Event> mEventList = new ArrayList<>();
//
//        Log.e("TAG"," EventUtils-getEventList()   Range start:" + startTime);
//        Log.e("TAG"," EventUtils-getEventList()   Range end:" +endTime);
//        Log.e("TAG"," EventUtils-getEventList()   Range start:" + dateFormat.format(startTime));
//        Log.e("TAG"," EventUtils-getEventList()   Range end:" + dateFormat.format(endTime));
//        if(context!=null) {
//           // if (context.getSystemService("usagestats") != null) {
//                UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
//                UsageEvents events = mUsmManager.queryEvents(startTime, endTime);
//                while (events.hasNextEvent()) {
//                    UsageEvents.Event e = new UsageEvents.Event();
//                    events.getNextEvent(e);
//                    if (e != null && (e.getEventType() == 1 || e.getEventType() == 2)) {
//                        //Log.e("TAG", " EventUtils-getEventList()  " + e.getTimeStamp() + "   event:" + e.getClassName() + "   type = " + e.getEventType());
//                        if(e.getClassName().equals("cn.edu.hebtu.software.listendemo.Host.learnWord.LearnWordActivity")){
//                            Log.e("TAG", " EventUtils-getEventList()  " + dateFormat.format(e.getTimeStamp()) + "   event:" + e.getClassName() + "   type = " + e.getEventType());
//                            if(e.getEventType()==1){
//                                time1=e.getTimeStamp();
//                            }else{
//                                time2=e.getTimeStamp();
//                                studyTime=studyTime+(time2-time1);
//                                String time2Str=dateFormat.format(time2);
//                                String time1Str=dateFormat.format(time1);
//                                String studyTimeStr=dateFormat.format(studyTime);
//                                try {
//                                    Date begin=dateFormat.parse(time1Str);
//                                    Date end = dateFormat.parse(time2Str);
//                                    long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
//                                    long day1=between/(24*3600);
//                                    long hour1=between%(24*3600)/3600;
//                                    long minute1=between%3600/60;
//                                    long second1=between%60/60;
//                                    Log.e("TAG","study"+day1+"天"+hour1+"小时"+minute1+"分钟"+second1+"秒");
//                                    Date studyTime = dateFormat.parse(studyTimeStr);
//                                    long studytime=(studyTime.getTime())/1000;
//                                    long day2=studytime/(24*3600);
//                                    long hour2=studytime%(24*3600)/3600;
//                                    long minute2=studytime%3600/60;
//                                    long second2=studytime%60/60;
//                                    Log.e("TAG","studytime"+day2+"天"+hour2+"小时"+minute2+"分钟"+second2+"秒");
//                                    if(hour2!=0){
//                                        textView.setText(hour2+"小时"+minute2+"分钟");
//                                    }else{
//                                        textView.setText(minute2+"分钟");
//                                    }
//
//                                } catch (ParseException e1) {
//                                    e1.printStackTrace();
//                                }
////                                Log.e("TAG",  "time2："+time2 +" "+dateFormat.format(time2)+" time1："+time1+" "+dateFormat.format(time1)+" 差："+(time2-time1)+" "+dateFormat.format(time2-time1)+" studyTime " +dateFormat1.format(time2-time1));
////                                Log.e("TAG", " studyTime " + dateFormat1.format(studyTime));
//                            }
//                        }
//                        mEventList.add(e);
//                    }
//                }
//          //  }
//        }
//        return mEventList;
//    }


    @Override
    public void onClick(View v) {

    }
}
