package cn.edu.hebtu.software.listendemo.QiniuUtils;


import android.util.Log;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QiniuUtil {
    private static String AccessKey = "ITGiHJmwEZeBfn6HNC76VWk5PahCoK7J9q7W36Qv";//此处填你自己的AccessKey
    private static String SecretKey = "pWk_nnowhVKlz3YVTGH4XAVcjtXprOYHQdFnts6_";//此处填你自己的SecretKey
    private static String bucket = "zin";
    private static final String TAG = "MainActivity";
//    private ImageView avatar;
//    private Uri imageUri;
//    private static final int REQUEST_CAPTURE = 2;
//    private static final int REQUEST_PICTURE = 5;
//    private static final int RESULT_CROP = 7;
//    private static final int GALLERY_ACTIVITY_CODE = 9;
//    private Button fromCarame;
//    private Button fromGarllary;
//    private Button upload;
//    private Uri localUri = null;
    private static String headpicPath=null;

    //保存图像
//    private void storeImage(Bitmap image) {
//        File pictureFile = getOutputMediaFile();
//        if (pictureFile == null) {
//            Log.d(TAG,
//                    "Error creating media file, check storage permissions: ");// e.getMessage());
//            return;
//        }
//        try {
//            FileOutputStream fos = new FileOutputStream(pictureFile);
//            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            Log.d(TAG, "File not found: " + e.getMessage());
//        } catch (IOException e) {
//            Log.d(TAG, "Error accessing file: " + e.getMessage());
//        }
//    }


    //上传图片
    public static String uploadImg2QiNiu(String picPath) {
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "icon_" + sdf.format(new Date());
        uploadManager.put(picPath, key, Auth.create(AccessKey, SecretKey).uploadToken(bucket), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                // info.error中包含了错误信息，可打印调试
                // 上传成功后将key值上传到自己的服务器
                if (info.isOK()) {
                    Log.e( "token===" ,Auth.create(AccessKey, SecretKey).uploadToken(bucket));
                    headpicPath ="http://cdn.zin4ever.top/"+key;
                    Log.e("complete" , headpicPath);
                }else{
                    Log.e("error",info.error);
                }
            }
        }, null);
        return headpicPath;
    }


}
