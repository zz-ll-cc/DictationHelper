package cn.edu.hebtu.software.listendemo.Untils;

import android.graphics.Point;

public class Constant {
    // 筛选书籍部分
    public static final int VERSION_ALL = 1;    // 查询书籍时，版本号 选择查询全部
    public static final int GRADE_ALL = 1;      // 查询书籍时，年级号 选择查询全部
    public static final String VERSION = "2";
    public static final String GRADE = "3";

    // SharedPreferences 部分
    public static final String SP_NAME = "用户";      // SharedPreferences 名称
    public static final String BIND_KEY = "bind";   // 绑定教材 key
    public static final String DEFAULT_BING_MAP = "{}"; // 默认绑定的map
    public static final int DEFAULT_BIND_ID = -1;   // 绑定教材 默认值
    public static final String COLLECT_KEY = "collectList";    // 收藏教材 key
    public static final String DEFAULT_COLLECT_LIST = "{}";     // 收藏教材 默认值
    public static final String USER_KEEP_KEY = "user";      // 存放登录用户信息 key
    public static final String DEFAULT_KEEP_USER = "{}";        // 登陆用户信息默认值
    public static final String AUTO_LOGIN_KEY = "autoLogin";    // 存放自动登陆 key
    public static final boolean DEFAULT_LOGIN_KEY = false;      // 自动登陆的默认值
    public static final String BOOK_JSON = "";//最近读过的一本书

    public static final String BASE_URL = "http://114.55.101.120:80/";//"http://192.168.43.26:8080/";//

    // URL 链接部分
    public static final String URL_BOOKS_FIND_ALL = BASE_URL + "book/all";   // 获取全部书的信息
    public static final String URL_BOOKS_FIND_BY_VER_AND_GRA = BASE_URL + "book/findbyversionandgrade";  // 通过Version与grade一起查书
    public static final String URL_VERSIONS_FIND_ALL = BASE_URL + "version/all";     // 查询所有 Version
    public static final String URL_GRADES_FIND_ALL = BASE_URL + "grade/all"; // 查询所有 Grade
    public static final String URL_WORDS_FIND_BY_BOOK_AND_UNIT = BASE_URL + "word/findwordsbybookandunit";   // 根据 Book 与 Unit 查单词
    public static final String URL_LOGIN_VERIFY = BASE_URL + "user/login"; //手机验证码登录
    public static final String URL_SAVE_RECORD = BASE_URL + "record/save";//保存听写记录http:
    public static final String URL_GET_ACCOUNT = BASE_URL + "word/getsumbybid";//根据bid获取这本书的单词总数


    public static final String URL_HEAD_UPLOAD = BASE_URL + "user/uploadhead";//头像上传
    public static final String URL_UPDATE_USER = BASE_URL + "user/updateuser";//更新用户详细信息
    public static final String URL_UPD_PWD = BASE_URL + "user/updatepwd";       // 修改密码
    // Intent 传递 ====  接收部分
    public static final String HOST_CON_DETAIL_BOOK = "book";   // 在Host -> BookDetail 时传递 book Entity
    public static final String DETAIL_CON_RECITE_OR_DICTATION = "chooseWords";  // 在Detail -> 听写/背诵 时传递 List<Word> (jsonStr) 使用
    public static final String RECITE_CON_DICTATION = "wordlist";   // 在 背诵 -> 听写时使用
    public static final String NEWWORD_CON_LEARNWORD_LEARN = "learnNewWord";//在NewWordActivity——LearnWordActivity传递List<Word>使用
    public static final String WRONGWORD_CON_LEARNWORD_LEARN = "learnWrongWord";//在WrongWordActivity——LearnWordActicity传递list<WrongWord>使用
    public static final String NEWWORD_CON_LEARNWORD_DICTATION = "listenNewWord";//在NewWordActivity——LearnWordActivity传递List<Word>使用
    public static final String WRONGWORD_CON_LEARNWORD_DICTATION = "listenWrongWord";//在WrongWordActivity——LearnWordActicity传递list<WrongWord>使用

    // 拼写
    public static final String NO_STYLE = "未拼写";           // 默写时，输入值为空时默认值
    public static final int SPELL_TRUE = 1;             // 拼写正确
    public static final int SPELL_FALSE = 0;            // 拼写错误

    // 登录/注册
    public static final int REGISTER_STEP_ONE = 1;      // 完成填写手机号
    public static final int REGISTER_STEP_TWO = 2;      // 完成填写个人信息
    public static final String REGISTER_FRAGMENT_STEP_ONE_ID = "phone"; // 第一个fragment，填写手机号等信息
    public static final String REGISTER_FRAGMENT_STEP_TWO_ID = "message";   // 第二个fragment，完善个人信息
    public static final String REGISTER_FINISH = "finish";      // 注册成功后，返回信息
    public static final int LOGIN_BY_VERIFY = 1;    //使用验证码登录
    public static final int LOGIN_BY_PASSWORD = 2;  //使用密码登录

    public static final int LOGIN_PHONE_UNREGIST = 1;       //未注册
    public static final int LOGIN_PHONE_REGISTED = 2;       //已注册

    public static final int LOGIN_PASSWORD_SUCCESS = 1;     //成功
    public static final int LOGIN_PASSWORD_UNREGIST = 2;    //账号未注册
    public static final int LOGIN_PASSWORD_WRONGPASS = 3;   //密码错误


    // 个人中心显示
    public static final String TV_IS_VIP_SHOW = "正在享受VIP服务";
    public static final String TV_NOT_VIP_SHOW = "尚未开通VIP";

    public static final int PWD_TYPE_SET = 0;   // 设置密码
    public static final int PWD_TYPE_UPD_OLD = 1;   // 通过旧密码修改密码
    public static final int PWD_TYPE_UPD_PHONE = 2; // 通过手机验证修改密码

    public static Point point;//获取屏幕的大小

    //学习记录
    public static final String URL_GETRECORD_TOLINECHART_FIVE = BASE_URL + "dayrecord/getdata";
    public static final String URL_GETRECORD_TOLINECHART_MONTH = BASE_URL + "dayrecord/getdata";
    public static final int WORD_FIVE = 1;
    public static final int WORD_MONTH = 2;
    public static final int ACCURENCY_FIVE = 3;
    public static final int ACCURENCY_MONTH = 4;

   //头像上传
    public static final String ACCESSKEY = "ITGiHJmwEZeBfn6HNC76VWk5PahCoK7J9q7W36Qv";//此处填你自己的AccessKey
    public static final String SECRETKEY = "pWk_nnowhVKlz3YVTGH4XAVcjtXprOYHQdFnts6_";//此处填你自己的SecretKey
    public static final String BUCKET = "zin";

    // 意见反馈
    public static final String FEEDBACK_KEEP_SP_NAME = "Feedback_keep";
    public static final String FEEDBACK_POST_KEEP_CONTENT_KEY = "contentKeep";
    public static final String FEEDBACK_POST_KEEP_TYPE_KEY = "typeKeep";
    public static final String CON_POST_FEEDBACK_IP = BASE_URL+"mail/report";
}
