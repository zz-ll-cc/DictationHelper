package cn.edu.hebtu.software.listendemo.Untils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.MemoryFile;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class ReadManager {

    Context context;
    String mFilepath;

    //语音合成对象
    private SpeechSynthesizer mTts;
    //默认发音人
    private String voicer = "xiaoyan";

    String texts = "";

    MemoryFile memoryFile;

    public volatile long mTotalSize = 0;

    private Vector<byte[]> container = new Vector<>();

    public ReadManager(Context context, String mFilepath) {
        this.mFilepath = mFilepath;
        this.context = context;
    }

    public void pronounce(String texts){
        this.texts = texts;
//        File file = new File(mFilepath+"/"+texts+".pcm");
//        if(file.exists()){
//            //播放音频
//
//        }else{
            SpeechUtility.createUtility(context, SpeechConstant.APPID + "=5de5adc2");
            mTts = SpeechSynthesizer.createSynthesizer(context,mTtsInitListener);
//        }

    }

    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //支持实时音频返回，仅在synthesizeToUri条件下支持
        mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
        //	mTts.setParameter(SpeechConstant.TTS_BUFFER_TIME,"1");
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.pcm");
    }

    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int i) {
            Log.d("tag","initListener init() code = " + i);
            if(i != ErrorCode.SUCCESS){
                Log.w("初始化失败,错误码：",i + "");
            }else{
                //初始化成功，之后可以调用startSpeaking方法
                //
                setParam();
                int code = mTts.startSpeaking(texts,mTtsListener);
                String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
                Log.e("path",""+path);
            }
        }
    };


    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            Log.e("onSpeakBegin","onSpeakBegin");
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            Log.e("MscSpeechLog_", "percent =" + i);
        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
            Log.e("MscSpeechLog_", "percent =" + i);
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            Log.e("onCompleted", "onCompleted " );
            if(speechError == null){
                for(int i = 0 ; i<container.size();i++){
                    try {
                        writeToFile(container.get(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            mTts.stopSpeaking();
            mTts.destroy();
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            if(SpeechEvent.EVENT_TTS_BUFFER == i){
                byte[] buf = bundle.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
                container.add(buf);
            }
        }
    };


    private void writeToFile(byte[] data) throws IOException {
        if (data == null || data.length == 0)
            return;
        try {
            if(memoryFile == null)
            {
                Log.e("MscSpeechLog_","ffffffffff");
                mFilepath = mFilepath+"/"+texts+".pcm";
                memoryFile = new MemoryFile(mFilepath,1920000);
                memoryFile.allowPurging(false);
            }
            memoryFile.writeBytes(data, 0, (int)mTotalSize, data.length);
            mTotalSize += data.length;
        } finally {
        }
    }
}
