package cn.edu.hebtu.software.listendemo.Mine.index.notify;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyMessageReceiver extends JPushMessageReceiver {

    // 处理消息
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        // 当通知消息到达时回调
        Log.e("接受到通知", "通知标题：" + notificationMessage.notificationTitle +
                " 通知内容：" + notificationMessage.notificationContent +
                " 附加字段：" + notificationMessage.notificationExtras);
        // 附加字段以 JSON 格式传过来
    }

    // 用户点击通知时 , 回调
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        // 启动固定界面
        Intent intent = new Intent(context, NotifyActivity.class);
        intent.putExtra("extras",notificationMessage.notificationExtras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   // 如果在活动栈顶，就不用再新建/启动
        context.startActivity(intent);
    }

    // 程序接收到自定义消息时，回调
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);

    }
}
