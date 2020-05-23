package cn.edu.hebtu.software.listendemo.credit.task;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.CreditRecord;
import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.USAGE_STATS_SERVICE;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.SP_NAME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.USER_KEEP_KEY;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private TextView tvCreditSum;
    private int score;
    private User user;
    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<Map<String, String>> title;
    private static final int UPDATE_CRDICT = 900;
    private static final int GET_CREDICT_RECORD = 1000;
    private int positionp;
    private Handler handler;
    private Handler handler1;
    private SharedPreferences sp;
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
    private long studyTime = 0;
    private long studyMinute = 0;
    private long time1 = 0;
    private long time2 = 0;
    private int state = 0;
    private String[] tag;

    public TaskAdapter(String[] tag,long studyMinute, TextView tvCreditSum, int score, User user, Context context, List<Map<String, String>> title, SharedPreferences sp) {
        this.tvCreditSum = tvCreditSum;
        this.score = score;
        this.user = user;
        this.title = title;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.studyMinute = studyMinute;
        this.sp = sp;
        this.tag=tag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.activty_my_credit_task_item, parent, false));
    }

    private void updateSp(User user0) {
        sp.edit().putString(USER_KEEP_KEY, gson.toJson(user0)).commit();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        for (int i=0; i < 3; i++) {
//            if (tag[i].equals("false")) {
//                state=i;
//                break;
//            }
//        }
        this.positionp = position;
        Log.e("ViewHolder1", title.toString());
        Log.e("ViewHolder2", title.get(position).toString());
        holder.tv_credit_task.setText(title.get(position).get("task"));
        holder.tv_credit_task_name.setText(title.get(position).get("task_name"));
        if (title.get(position).get("task_content") != null) {
            if (title.get(position).get("task_content").equals("未听写")) {
                holder.tv_credit_task_content.setText("0分");
            } else {
                holder.tv_credit_task_content.setText(title.get(position).get("task_content"));
            }
        }
        if (title.get(position).get("tag").equals("true")) {
            holder.btn_add_credit.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_pressed));
            holder.btn_add_credit.setTextColor(context.getResources().getColor(R.color.black));
            holder.btn_add_credit.setEnabled(false);
            holder.btn_add_credit.setText("已领取");
        } else {
            holder.btn_add_credit.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_normal));
            holder.btn_add_credit.setTextColor(Color.parseColor("#ff8c00"));
            holder.btn_add_credit.setEnabled(true);
        }
        holder.btn_add_credit.setText(title.get(position).get("add_credit"));
        holder.btn_add_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("checkCredit3", title.get(position).get("tag"));
//                if (title.get(position).get("tag").equals("true")) {
//                    Toast.makeText(context, "今天已领取，明天再来！", Toast.LENGTH_LONG).show();
//                }
                switch (position) {
                    case 0:
                        switch (title.get(position).get("task")) {
                            case "学习10分钟":
                                if (studyMinute >= 1) {
                                    updateCredit(user.getUid(), 2);
                                    handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);
                                            switch (msg.what) {
                                                case UPDATE_CRDICT:
                                                    Log.e("msg1", msg.obj + "");
                                                    if (!msg.obj.equals("0")) {
                                                        User user1 = gson.fromJson(msg.obj + "", User.class);
                                                        Toast.makeText(context, "领取成功", Toast.LENGTH_LONG).show();
                                                        tvCreditSum.setText(user.getUserCredit() + "分");
                                                        title.get(position).put("task", "学习30分钟");
                                                        title.get(position).put("add_credit","+3积分");
                                                        title.get(position).put("tag", "false");
                                                         notifyDataSetChanged();
//                                                        holder.btn_add_credit.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_pressed));
//                                                        holder.btn_add_credit.setTextColor(context.getResources().getColor(R.color.black));
//                                                        holder.btn_add_credit.setEnabled(false);
                                                        List<UnLock> unLocks = null;
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(msg.obj+"");
                                                            String unLockList = jsonObject.get("unlockList").toString();
                                                            Type type = new TypeToken<List<UnLock>>(){}.getType();
                                                            unLocks = new Gson().fromJson(unLockList,type);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        user1.setUnLockList(unLocks);
                                                        user.setUnLockList(unLocks);
                                                        user.setUserCredit(user1.getUserCredit());
                                                        //updateSp(user);
                                                        context.getSharedPreferences(SP_NAME,MODE_PRIVATE).edit().putString(USER_KEEP_KEY,msg.obj+"").commit();
//                                                EventBus.getDefault().post(user);
                                                    } else {
                                                        Toast.makeText(context, "领取失败", Toast.LENGTH_LONG).show();
                                                    }

                                                    break;
                                            }
                                        }
                                    };
                                } else {
                                    Toast.makeText(context, "学习时长不够，赶快去学习吧！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "学习30分钟":
                                if (studyMinute >= 30) {
                                    updateCredit(user.getUid(), 3);
                                    handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);
                                            switch (msg.what) {
                                                case UPDATE_CRDICT:
                                                    Log.e("msg2", msg.obj + "");
                                                    if (!msg.obj.equals("0")) {
                                                        User user1 = gson.fromJson(msg.obj + "", User.class);
                                                        Toast.makeText(context, "领取成功", Toast.LENGTH_LONG).show();
//                                                        holder.btn_add_credit.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_pressed));
//                                                        holder.btn_add_credit.setTextColor(context.getResources().getColor(R.color.black));
//                                                        holder.btn_add_credit.setEnabled(false);
                                                        title.get(position).put("task","学习60分钟");
                                                        title.get(position).put("tag","false");
                                                        title.get(position).put("add_credit","+5积分");
                                                        notifyDataSetChanged();
                                                        tvCreditSum.setText(user.getUserCredit() + "分");
//                                                EventBus.getDefault().post(user);
                                                        List<UnLock> unLocks = null;
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(msg.obj+"");
                                                            String unLockList = jsonObject.get("unlockList").toString();
                                                            Type type = new TypeToken<List<UnLock>>(){}.getType();
                                                            unLocks = new Gson().fromJson(unLockList,type);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        user1.setUnLockList(unLocks);
                                                        user.setUnLockList(unLocks);
                                                        user.setUserCredit(user1.getUserCredit());
                                                        //updateSp(user);
                                                        context.getSharedPreferences(SP_NAME,MODE_PRIVATE).edit().putString(USER_KEEP_KEY,msg.obj+"").commit();
                                                    } else {
                                                        Toast.makeText(context, "领取失败", Toast.LENGTH_LONG).show();
                                                    }
                                                    break;
                                            }
                                        }
                                    };
                                } else {
                                    Toast.makeText(context, "学习时长不够，赶快去学习吧！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "学习60分钟":
                                if (studyMinute >= 60) {
                                    updateCredit(user.getUid(), 4);
                                    handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);
                                            switch (msg.what) {
                                                case UPDATE_CRDICT:
                                                    Log.e("msg3", msg.obj + "");
                                                    if (!msg.obj.equals("0")) {
                                                        User user1 = gson.fromJson(msg.obj + "", User.class);
                                                        Toast.makeText(context, "领取成功", Toast.LENGTH_LONG).show();
                                                        tvCreditSum.setText(user.getUserCredit() + "分");
                                                        title.get(position).put("task","学习60分钟");
                                                        title.get(position).put("add_credit","+5积分");
                                                        title.get(position).put("tag","true");
                                                        holder.btn_add_credit.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_pressed));
                                                        holder.btn_add_credit.setTextColor(context.getResources().getColor(R.color.black));
                                                        holder.btn_add_credit.setEnabled(false);
                                                        holder.btn_add_credit.setText("已领取");
//                                                EventBus.getDefault().post(user);
                                                        List<UnLock> unLocks = null;
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(msg.obj+"");
                                                            String unLockList = jsonObject.get("unlockList").toString();
                                                            Type type = new TypeToken<List<UnLock>>(){}.getType();
                                                            unLocks = new Gson().fromJson(unLockList,type);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        user1.setUnLockList(unLocks);
                                                        user.setUnLockList(unLocks);
                                                        user.setUserCredit(user1.getUserCredit());
//                                                        updateSp(user);
                                                        context.getSharedPreferences(SP_NAME,MODE_PRIVATE).edit().putString(USER_KEEP_KEY,msg.obj+"").commit();
                                                    } else {
                                                        Toast.makeText(context, "领取失败", Toast.LENGTH_LONG).show();
                                                    }
                                                    break;
                                            }
                                        }
                                    };
                                } else {
                                    Toast.makeText(context, "学习时长不够，赶快去学习吧！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                        break;
                    case 1:
                        if (!title.get(position).get("task_content").equals("未听写")) {
                            Log.e("msg5", title.get(position).get("task_content") + "");
                            updateCredit(user.getUid(), 1);
                            handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    switch (msg.what) {
                                        case UPDATE_CRDICT:
                                            Log.e("msg4", msg.obj + "");
                                            if (!msg.obj.equals("0")) {
                                                User user1 = gson.fromJson(msg.obj + "", User.class);
                                                Toast.makeText(context, "领取成功", Toast.LENGTH_LONG).show();
                                                holder.btn_add_credit.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_pressed));
                                                holder.btn_add_credit.setTextColor(context.getResources().getColor(R.color.black));
                                                holder.btn_add_credit.setEnabled(false);
                                                holder.btn_add_credit.setText("已领取");
                                                tvCreditSum.setText(user.getUserCredit() + "分");
                                                title.get(position).put("tag","true");
//                                                EventBus.getDefault().post(user);
                                                List<UnLock> unLocks = null;
                                                try {
                                                    JSONObject jsonObject = new JSONObject(msg.obj+"");
                                                    String unLockList = jsonObject.get("unlockList").toString();
                                                    Type type = new TypeToken<List<UnLock>>(){}.getType();
                                                    unLocks = new Gson().fromJson(unLockList,type);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                user1.setUnLockList(unLocks);
                                                user.setUnLockList(unLocks);
                                                user.setUserCredit(user1.getUserCredit());
//                                                updateSp(user);
                                                context.getSharedPreferences(SP_NAME,MODE_PRIVATE).edit().putString(USER_KEEP_KEY,msg.obj+"").commit();
                                            } else {
                                                Toast.makeText(context, "领取失败", Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                    }
                                }
                            };
                        } else {
                            Toast.makeText(context, "快去听写领积分吧！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
//                switch (position) {
//                    case 0:
//                        if (studyMinute >= 10) {
//                            updateCredit(user.getUid(), 2);
//                            handler = new Handler() {
//                                @Override
//                                public void handleMessage(Message msg) {
//                                    super.handleMessage(msg);
//                                    switch (msg.what) {
//                                        case UPDATE_CRDICT:
//                                            Log.e("msg1", msg.obj + "");
//                                            if (!msg.obj.equals("0")) {
//                                                User user = gson.fromJson(msg.obj + "", User.class);
//                                                Toast.makeText(context, "领取成功", Toast.LENGTH_LONG).show();
//                                                tvCreditSum.setText(user.getUserCredit() + "分");
//                                                holder.btn_add_credit.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_pressed));
//                                                holder.btn_add_credit.setTextColor(context.getResources().getColor(R.color.black));
//                                                holder.btn_add_credit.setEnabled(false);
//                                                updateSp(user);
////                                                EventBus.getDefault().post(user);
//                                            } else {
//                                                Toast.makeText(context, "领取失败", Toast.LENGTH_LONG).show();
//                                            }
//
//                                            break;
//                                    }
//                                }
//                            };
//                        } else {
//                            Toast.makeText(context, "学习时长不够，赶快去学习吧！", Toast.LENGTH_LONG).show();
//                        }
//                        break;
//                    case 1:
//                        if (studyMinute >= 30) {
//                            updateCredit(user.getUid(), 3);
//                            handler = new Handler() {
//                                @Override
//                                public void handleMessage(Message msg) {
//                                    super.handleMessage(msg);
//                                    switch (msg.what) {
//                                        case UPDATE_CRDICT:
//                                            Log.e("msg2", msg.obj + "");
//                                            if (!msg.obj.equals("0")) {
//                                                User user = gson.fromJson(msg.obj + "", User.class);
//                                                Toast.makeText(context, "领取成功", Toast.LENGTH_LONG).show();
//                                                holder.btn_add_credit.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_pressed));
//                                                holder.btn_add_credit.setTextColor(context.getResources().getColor(R.color.black));
//                                                holder.btn_add_credit.setEnabled(false);
//                                                tvCreditSum.setText(user.getUserCredit() + "分");
//                                                updateSp(user);
////                                                EventBus.getDefault().post(user);
//                                            } else {
//                                                Toast.makeText(context, "领取失败", Toast.LENGTH_LONG).show();
//                                            }
//                                            break;
//                                    }
//                                }
//                            };
//                        } else {
//                            Toast.makeText(context, "学习时长不够，赶快去学习吧！", Toast.LENGTH_LONG).show();
//                        }
//                        break;
//                    case 2:
//                        if (studyMinute >= 60) {
//                            updateCredit(user.getUid(), 4);
//                            handler = new Handler() {
//                                @Override
//                                public void handleMessage(Message msg) {
//                                    super.handleMessage(msg);
//                                    switch (msg.what) {
//                                        case UPDATE_CRDICT:
//                                            Log.e("msg3", msg.obj + "");
//                                            if (!msg.obj.equals("0")) {
//                                                User user = gson.fromJson(msg.obj + "", User.class);
//                                                Toast.makeText(context, "领取成功", Toast.LENGTH_LONG).show();
//                                                tvCreditSum.setText(user.getUserCredit() + "分");
//                                                holder.btn_add_credit.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_pressed));
//                                                holder.btn_add_credit.setTextColor(context.getResources().getColor(R.color.black));
//                                                holder.btn_add_credit.setEnabled(false);
//                                                updateSp(user);
////                                                EventBus.getDefault().post(user);
//                                            } else {
//                                                Toast.makeText(context, "领取失败", Toast.LENGTH_LONG).show();
//                                            }
//                                            break;
//                                    }
//                                }
//                            };
//                        } else {
//                            Toast.makeText(context, "学习时长不够，赶快去学习吧！", Toast.LENGTH_LONG).show();
//                        }
//                        break;
//                    case 3:
//                        if (!title.get(position).get("task_content").equals("未听写")) {
//                            Log.e("msg5", title.get(position).get("task_content") + "");
//                            updateCredit(user.getUid(), 1);
//                            handler = new Handler() {
//                                @Override
//                                public void handleMessage(Message msg) {
//                                    super.handleMessage(msg);
//                                    switch (msg.what) {
//                                        case UPDATE_CRDICT:
//                                            Log.e("msg4", msg.obj + "");
//                                            if (!msg.obj.equals("0")) {
//                                                User user = gson.fromJson(msg.obj + "", User.class);
//                                                Toast.makeText(context, "领取成功", Toast.LENGTH_LONG).show();
//                                                holder.btn_add_credit.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_pressed));
//                                                holder.btn_add_credit.setTextColor(context.getResources().getColor(R.color.black));
//                                                holder.btn_add_credit.setEnabled(false);
//                                                tvCreditSum.setText(user.getUserCredit() + "分");
//                                                updateSp(user);
////                                                EventBus.getDefault().post(user);
//                                            } else {
//                                                Toast.makeText(context, "领取失败", Toast.LENGTH_LONG).show();
//                                            }
//                                            break;
//                                    }
//                                }
//                            };
//                        } else {
//                            Toast.makeText(context, "快去听写领积分吧！", Toast.LENGTH_LONG).show();
//                        }
//                        break;
//                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (title != null) {
            return title.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_credit_task;
        TextView tv_credit_task_name;
        TextView tv_credit_task_content;
        Button btn_add_credit;

        ViewHolder(View view) {
            super(view);
            tv_credit_task = (TextView) view.findViewById(R.id.tv_credit_task);
            tv_credit_task_name = (TextView) view.findViewById(R.id.tv_credit_task_name);
            tv_credit_task_content = (TextView) view.findViewById(R.id.tv_credit_task_content);
            btn_add_credit = (Button) view.findViewById(R.id.btn_add_credit);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }

    //更新用户积分
    public void updateCredit(int userId, int code) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("id", userId + "").add("code", code + "").build();
        Request request = new Request.Builder().url(Constant.URL_UPDATE_CREDIT).post(fb).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            /**
             * 未完待续
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("response", "" + json);
                if (response == null || response.equals("")) {
                    Message message = new Message();
                    message.what = UPDATE_CRDICT;
                    message.obj = "0";
                    handler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.what = UPDATE_CRDICT;
                    message.obj = json;
                    handler.sendMessage(message);
                }

            }
        });
    }

    //获取今天领取积分的记录
    public void getCreditRecord(int userId) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("id", userId + "").build();
        Request request = new Request.Builder().url(Constant.URL_CREDIT_RECORD).post(fb).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            /**
             * 未完待续
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("checkCredit", "" + json);
                if (response != null || !response.equals("")) {
                    Message message = new Message();
                    message.what = GET_CREDICT_RECORD;
                    message.obj = json;
                    handler1.sendMessage(message);
                }

            }
        });
    }

}
