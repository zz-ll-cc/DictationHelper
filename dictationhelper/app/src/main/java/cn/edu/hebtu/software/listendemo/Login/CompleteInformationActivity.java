package cn.edu.hebtu.software.listendemo.Login;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.hebtu.software.listendemo.Entity.EventInfo;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Host.index.ListenIndexActivity;
import cn.edu.hebtu.software.listendemo.Mine.index.settings.EditMsgActivity;
import cn.edu.hebtu.software.listendemo.QiniuUtils.Auth;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

import static cn.edu.hebtu.software.listendemo.Untils.Constant.URL_HEAD_UPLOAD;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.URL_LOGIN_VERIFY;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.URL_UPDATE_USER;
//import static cn.edu.hebtu.software.listendemo.QiniuUtils.QiniuUtil.uploadImg2QiNiu;

public class CompleteInformationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.iv_login_register_user_header)
    ImageView ivHead;
    @BindView(R.id.tv_sethead)
    TextView tvSetHead;
    @BindView(R.id.et_login_regist_username)
    EditText etUserName;
    @BindView(R.id.spin_grade)
    Spinner spinGrade;
    @BindView(R.id.spin_year)
    Spinner spinYear;
    @BindView(R.id.tv_birth)
    TextView tvBirth;
    @BindView(R.id.spin_sex)
    Spinner spinSex;
    @BindView(R.id.btn_regist_finish)
    Button btnSubmit;
    @BindView(R.id.tv_selectBirth)
    TextView tvSelect;
    @BindView(R.id.iv_login_show_pwd)
    ImageView ivShow;

    private List<LocalMedia> selectResultList = null;
    private SharedPreferences sp;
    private User user;
    private Gson gson = new Gson();
    private DatePickerDialog dpd;
    private OkHttpClient okHttpClient;
    private static final int UPLOAD_QINIU_TRUE = 1000;
    private String headpicPath = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_QINIU_TRUE:
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    builder.addFormDataPart("fileUrl", msg.obj + "");
                    builder.addFormDataPart("uid", "" + user.getUid());
                    RequestBody requestBody = builder.build();
                    Request request = new Request.Builder()
                            .url(URL_HEAD_UPLOAD)
                            .post(requestBody)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new okhttp3.Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            //TODO 网络连接超时
                            EventInfo<String, String, User> eventInfo = new EventInfo();
                            Map<String, String> map = new HashMap<>();
                            map.put("status", "failUpload");
                            eventInfo.setContentMap(map);
                            EventBus.getDefault().post(eventInfo);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            EventInfo<String, String, User> eventInfo = new EventInfo();
                            Map<String, String> map = new HashMap<>();
                            map.put("status", "finishUpload");
                            eventInfo.setContentString(response.body().string());
                            eventInfo.setContentMap(map);
                            EventBus.getDefault().post(eventInfo);
                        }
                    });
                    tvSetHead.setText("正在上传....");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register_user_msg);

        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        okHttpClient = new OkHttpClient.Builder().connectTimeout(1000 * 60, TimeUnit.MILLISECONDS).build();

        ivHead.setOnClickListener(this::onClick);
        tvSkip.setOnClickListener(this::onClick);
        btnSubmit.setOnClickListener(this::onClick);
        tvSelect.setOnClickListener(this::onClick);
        ivShow.setOnClickListener(this::onClick);


        sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        String userJson = sp.getString(Constant.USER_KEEP_KEY, "");
        user = gson.fromJson(userJson, User.class);

//        if(user !=null && user.getUheadPath() != null){
//            //如果用户是有头像的
//            Glide.with(this)
//                    .load(user.getUheadPath())
//                    .into(ivHead);
//        }
        etUserName.setText(user.getUname());


        StatusBarUtil.statusBarLightMode(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_skip:
                //点击跳过
                Intent intent = new Intent(CompleteInformationActivity.this, ListenIndexActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.iv_login_register_user_header:
                //点击头像，先判断有无权限
                askPermission();
                //回调使用，使用图片选择器

                break;

            case R.id.btn_regist_finish:
                //点击提交
                Log.e("spinner内容", "" + spinSex.getSelectedItemPosition());
                //0保密，1男，2女
                if (!tvBirth.getText().toString().isEmpty()) {
                    try {
                        Date date = new SimpleDateFormat("yyyy年MM月dd").parse(tvBirth.getText().toString());
                        Calendar ca = Calendar.getInstance();
                        ca.setTime(date);
                        int day = ca.get(Calendar.DAY_OF_MONTH);
                        int month = ca.get(Calendar.MONTH);
                        int year = ca.get(Calendar.YEAR);
                        user.setUbirth(year + "-" + (month + 1) + "-" + day);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                if (!etUserName.getText().toString().equals(user.getUname()) && !etUserName.getText().toString().isEmpty()) {
                    user.setUname(etUserName.getText().toString());
                }
                switch (spinSex.getSelectedItemPosition()) {
                    case 0:
                        user.setUsex("保密");
                        break;
                    case 1:
                        user.setUsex("男");
                        break;
                    case 2:
                        user.setUsex("女");
                        break;
                }
                user.setUgrade(spinGrade.getSelectedItemPosition());
                updateUser();
                break;

            case R.id.tv_selectBirth:
                //点击选择日期
                Calendar now = Calendar.getInstance();
                dpd = new DatePickerDialog(this, this::onDateSet, 2016 - spinYear.getSelectedItemPosition(), 0, 1);
                dpd.show();
                break;
        }
    }

    private void updateUser() {
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), gson.toJson(user));
        Log.e("user", "" + gson.toJson(user));

        Request request = new Request.Builder().url(URL_UPDATE_USER).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //TODO 网络连接超时
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                EventInfo<String, String, User> eventInfo = new EventInfo();
                Map<String, String> map = new HashMap<>();
                map.put("status", "finishSubmit");
                eventInfo.setContentMap(map);
                EventBus.getDefault().post(eventInfo);
            }
        });

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        startSelectPic();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "没有授予权限不能设置头像哦~", Toast.LENGTH_SHORT).show();
    }

    //回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectResultList = PictureSelector.obtainMultipleResult(data);
            for (LocalMedia localMedia : selectResultList) {
                Log.e("Path:", "" + localMedia.getCutPath());
            }
            uploadFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private void askPermission() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, perms)) {
            startSelectPic();
        } else {
            EasyPermissions.requestPermissions(CompleteInformationActivity.this, "程序必须的权限", 4513, perms);
        }
    }

    private void startSelectPic() {
        PictureSelector.create(CompleteInformationActivity.this)
                .openGallery(PictureMimeType.ofAll())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_QQ_style)
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)//图片列表点击缩放效果
                .enableCrop(true)// 是否裁剪 true or false
                .withAspectRatio(1, 1)//一比一剪裁
                .rotateEnabled(false)//禁止旋转
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }


    //上传文件
    private void uploadFile() {
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //表单参数
//        builder.addFormDataPart("uid", "" + user.getUid());
        /**
         * pictureType中，图片是image/jpeg，视频是video/mp4
         *
         * 文件名可以通过path的/分割字符串如何提出最后一段就是文件名
         *
         */
        for (LocalMedia localMedia : selectResultList) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            Log.e("Cutpath", "" + localMedia.getCutPath());
            if (localMedia.getPictureType().equals("image/jpeg")) {
                builder.addFormDataPart("type", "jpeg");
            } else if (localMedia.getPictureType().equals("video/mp4")) {
                builder.addFormDataPart("type", "mp4");
            } else {
                //剪裁的话就不是jpeg了
//                return;
            }
            //获取剪裁后的路径，而不是getPath
            String[] args = localMedia.getCutPath().split("/");
            //builder.addFormDataPart("file",""+args[args.length-1],fileBody);
            Log.e("path", args[args.length - 1]);
            String headpicPath = uploadImg2QiNiu(localMedia.getCutPath());
//            builder.addFormDataPart("fileUrl", headpicPath + "");
        }


//        RequestBody requestBody = builder.build();
//        Request request = new Request.Builder()
//                .url(URL_HEAD_UPLOAD)
//                .post(requestBody)
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //TODO 网络连接超时
//                EventInfo<String,String,User> eventInfo = new EventInfo();
//                Map<String ,String> map = new HashMap<>();
//                map.put("status","failUpload");
//                eventInfo.setContentMap(map);
//                EventBus.getDefault().post(eventInfo);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                EventInfo<String,String,User> eventInfo = new EventInfo();
//                Map<String ,String> map = new HashMap<>();
//                map.put("status","finishUpload");
//                eventInfo.setContentString(response.body().string());
//                eventInfo.setContentMap(map);
//                EventBus.getDefault().post(eventInfo);
//            }
//        });
//        tvSetHead.setText("正在上传....");

    }

    public synchronized String uploadImg2QiNiu(String picPath) {
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "icon_" + sdf.format(new Date());
        uploadManager.put(picPath, key, Auth.create(Constant.ACCESSKEY, Constant.SECRETKEY).uploadToken(Constant.BUCKET), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                // info.error中包含了错误信息，可打印调试
                // 上传成功后将key值上传到自己的服务器
                if (info.isOK()) {
                    Log.e("token===", Auth.create(Constant.ACCESSKEY, Constant.SECRETKEY).uploadToken(Constant.BUCKET));
                    headpicPath = "http://cdn.zin4ever.top/" + key;
                    Log.e(" headpicPath", headpicPath);
                    Message message = new Message();
                    message.what = UPLOAD_QINIU_TRUE;
                    message.obj = headpicPath;
                    headpicPath = "";
                    handler.sendMessage(message);
                } else {
                    Log.e("error", info.error);
                }
            }
        }, null);
        return headpicPath;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMethod(EventInfo<String, String, User> eventInfo) {
        Map<String, String> map = eventInfo.getContentMap();

        switch (map.get("status").toString()) {
            case "finishUpload":
                Log.e("receiveMethod", "" + eventInfo.getContentString());
                user = gson.fromJson(eventInfo.getContentString(), User.class);
                sp.edit().putString(Constant.USER_KEEP_KEY, gson.toJson(user)).commit();

                tvSetHead.setText("上传成功");
                Glide.with(this)
                        .load(user.getUheadPath())
                        .into(ivHead);
//                PictureFileUtils.deleteCacheDirFile(CompleteInformationActivity.this);
                break;

            case "finishSubmit":


                Log.e("finishSubmit", "" + sp.getString(Constant.USER_KEEP_KEY, ""));
                sp.edit().putString(Constant.USER_KEEP_KEY, gson.toJson(user)).commit();
                Intent intent = new Intent(CompleteInformationActivity.this, ListenIndexActivity.class);
                startActivity(intent);
                finish();
                break;

            case "failUpload":
                tvSetHead.setText("服务器繁忙");
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
        if (year != (1 - spinYear.getSelectedItemPosition())) {
            int position = 2016 - year;
            spinYear.setSelection(position);
        }
        tvBirth.setText(date);
    }
}
