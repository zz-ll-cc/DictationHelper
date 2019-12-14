package cn.edu.hebtu.software.listendemo.Untils;

public class CheckPwdFormatUtil {
    // 检查输入的 password 是否符合输入格式
    public static boolean checkPwdRight(String pwd) {
        if (pwd.length() >= 8 && pwd.length() <= 16) {
            boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
            boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
            boolean flag = true;
            for (int i = 0; i < pwd.length(); i++) { //循环遍历字符串
                // Log.e("tt",Character.isDigit(str.charAt(i))+""+Character.isLetter(str.charAt(i)));
                if (Character.isDigit(pwd.charAt(i))) { //用char包装类中的判断数字的方法判断每一个字符
                    isDigit = true;
                } else if (Character.isLetter(pwd.charAt(i))) { //用char包装类中的判断字母的方法判断每一个字符
                    isLetter = true;
                } else {
                    flag = false;
                }
            }
            return flag;
        } else return false;
    }
}
