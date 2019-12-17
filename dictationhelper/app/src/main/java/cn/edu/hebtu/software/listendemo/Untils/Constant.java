package cn.edu.hebtu.software.listendemo.Untils;

public class Constant {
    // 筛选书籍部分
    public static final int VERSION_ALL = 1;    // 查询书籍时，版本号 选择查询全部
    public static final int GRADE_ALL = 1;      // 查询书籍时，年级号 选择查询全部
    public static final String VERSION="2";
    public static final String GRADE="3";

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

    // URL 链接部分
    public static final String URL_BOOKS_FIND_ALL = "http://47.94.171.160:8080/book/all";   // 获取全部书的信息
    public static final String URL_BOOKS_FIND_BY_VERSION = "http://47.94.171.160:8080/book/findbyversion";  // 只通过 Version 查询书
    public static final String URL_BOOKS_FIND_BY_GRADE = "http://47.94.171.160:8080/book/findbygrade";      // 只通过 Grade 查询书
    public static final String URL_BOOKS_FIND_BY_VER_AND_GRA = "http://47.94.171.160:8080/book/findbyversionandgrade";  // 通过Version与grade一起查书
    public static final String URL_UNITS_FIND_ALL = "http://47.94.171.160:8080/unit/all";   // 查询所有 Unit
    public static final String URL_VERSIONS_FIND_ALL = "http://47.94.171.160:8080/version/all";     // 查询所有 Version
    public static final String URL_GRADES_FIND_ALL = "http://47.94.171.160:8080/grade/all"; // 查询所有 Grade
    public static final String URL_WORDS_FIND_ALL = "http://47.94.171.160:8080/word/findallwords";  // 查询所有单词
    public static final String URL_WORDS_FIND_BY_BOOK = "http://47.94.171.160:8080/word/findwordsbybook";   // 根据 Book 查单词
    public static final String URL_WORDS_FIND_BY_BOOK_AND_UNIT = "http://47.94.171.160:8080/word/findwordsbybookandunit";   // 根据 Book 与 Unit 查单词
    public static final String URL_USER_LOGIN = "http://47.94.171.160:8080/user/loginByPP";      // 登陆验证
    public static final String URL_CAN_REGISTER = "";       // 发送手机号是否已被注册请求
    public static final String URL_LOGIN_VERIFY = "http://47.94.171.160:8080/user/login"; //手机验证码登录
    public static final String URL_SAVE_RECORD = "http://47.94.171.160:8080/record/save";//保存听写记录http:
    public static final String URL_GET_RECORD = "http://47.94.171.160:8080/chart/getChart";//保存听写记录http://47.94.171.160:8080/chart/getChart?uid=10020
    public static final String URL_GET_ACCOUNT = "http://47.94.171.160:8080/word/getsumbybid";//根据bid获取这本书的单词总数




    public static final String URL_HEAD_UPLOAD = "http://47.94.171.160:8080/user/uploadhead";//头像上传
    public static final String URL_UPDATE_USER = "http://47.94.171.160:8080/user/updateuser";//更新用户详细信息
    public static final String URL_UPD_PWD = "http://47.94.171.160:8080/user/updatepwd";       // 修改密码
    // Intent 传递 ====  接收部分
    public static final String HOST_CON_DETAIL_BOOK = "book";   // 在Host -> BookDetail 时传递 book Entity
    public static final String DETAIL_CON_RECITE_OR_DICTATION = "chooseWords";  // 在Detail -> 听写/背诵 时传递 List<Word> (jsonStr) 使用
    public static final String RECITE_CON_DICTATION = "wordlist";   // 在 背诵 -> 听写时使用
    public static final String NEWWORD_CON_LEARNWORD_LEARN="learnNewWord";//在NewWordActivity——LearnWordActivity传递List<Word>使用
    public static final String WRONGWORD_CON_LEARNWORD_LEARN="learnWrongWord";//在WrongWordActivity——LearnWordActicity传递list<WrongWord>使用
    public static final String NEWWORD_CON_LEARNWORD_DICTATION="listenNewWord";//在NewWordActivity——LearnWordActivity传递List<Word>使用
    public static final String WRONGWORD_CON_LEARNWORD_DICTATION="listenWrongWord";//在WrongWordActivity——LearnWordActicity传递list<WrongWord>使用

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
    public static final String URL_GETRECORD_TOLINECHART_FIVE="http://47.94.171.160:8080/dayrecord/getdata";
    public static final String URL_GETRECORD_TOLINECHART_MONTH="http://47.94.171.160:8080/dayrecord/getdata";
    public static final int WORD_FIVE=1;
    public static final int WORD_MONTH=2;
    public static final int ACCURENCY_FIVE=3;
    public static final int ACCURENCY_MONTH=4;


}
