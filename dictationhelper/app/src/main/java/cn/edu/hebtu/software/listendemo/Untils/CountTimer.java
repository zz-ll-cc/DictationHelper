package cn.edu.hebtu.software.listendemo.Untils;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

public class CountTimer extends CountDownTimer {
    public static final int TIME_COUNT = 60000;//时间防止从59s开始显示（以倒计时60s为例子）
    private TextView btn;
    private String endText;

    public CountTimer(TextView btn) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endText = "获取验证码";
    }

    /**
     * @param btn     倒计时文本控件
     * @param endText 结束时显示的文字
     */
    public CountTimer(TextView btn, String endText) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endText = endText;
    }

    /**
     * @param millisInFuture    倒计时总时间（如60S，120s等）
     * @param countDownInterval 渐变时间（每次倒计1s）
     * @param btn               点击的按钮(因为Button是TextView子类，为了通用我的参数设置为TextView）
     * @param endText           倒计时结束后，按钮对应显示的文字
     */
    public CountTimer(long millisInFuture, long countDownInterval, TextView btn, String endText) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.endText = endText;
    }


    // 计时完毕时触发
    @Override
    public void onFinish() {
        btn.setText(endText);
        btn.setEnabled(true);
    }

    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setEnabled(false);
        btn.setText(millisUntilFinished / 1000 + " 秒");
    }
}
