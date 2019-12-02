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
    public static final int DEFAULT_BIND_ID = -1;   // 绑定教材 默认值
    public static final String COLLECT_KEY = "collectList";    // 收藏教材 key
    public static final String DEFAULT_COLLECT_LIST = "[]";     // 收藏教材 默认值
    public static final String USER_KEEP_KEY = "user";      // 存放登录用户信息 key
    public static final String DEFAULT_KEEP_USER = "[]";        // 登陆用户信息默认值
    public static final String AUTO_LOGIN_KEY = "autoLogin";    // 存放自动登陆 key
    public static final boolean DEFAULT_LOGIN_KEY = false;      // 自动登陆的默认值

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

    // Intent 传递 ====  接收部分
    public static final String HOST_CON_DETAIL_BOOK = "book";   // 在Host -> BookDetail 时传递 book Entity
    public static final String DETAIL_CON_RECITE_OR_DICTATION = "chooseWords";  // 在Detail -> 听写/背诵 时传递 List<Word> (jsonStr) 使用
    public static final String RECITE_CON_DICTATION = "wordlist";   // 在 背诵 -> 听写时使用

    // 拼写
    public static final String NO_STYLE = "未拼写";           // 默写时，输入值为空时默认值
    public static final int SPELL_TRUE = 1;             // 拼写正确
    public static final int SPELL_FALSE = 0;            // 拼写错误

    // 注册
    public static final int REGISTER_STEP_ONE = 1;      // 完成填写手机号
    public static final int REGISTER_STEP_TWO = 2;      // 完成填写个人信息
    public static final String REGISTER_FRAGMENT_STEP_ONE_ID = "phone"; // 第一个fragment，填写手机号等信息
    public static final String REGISTER_FRAGMENT_STEP_TWO_ID = "message";   // 第二个fragment，完善个人信息
    public static final String REGISTER_FINISH = "finish";      // 注册成功后，返回信息
}
