package cn.edu.hebtu.software.listendemo.Untils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private static SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd hh:mm");
    private static SimpleDateFormat sdf4 = new SimpleDateFormat("hh:mm");

    public static String getShowTime(Date createDate) {
        Date now = new Date();
        if (isSameDay(now,createDate)) {
            return sdf4.format(createDate);
        }
        else if (isSameYear(now,createDate)) {
            return sdf3.format(createDate);
        }
        else {
            return sdf2.format(createDate);
        }
    }

    public static boolean isSameDay(Date dt1, Date dt2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strDate1 = format.format(dt1);
        String strDate2 = format.format(dt2);
        return strDate1.equalsIgnoreCase(strDate2);
    }
    public static boolean isSameYear(Date dt1, Date dt2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String strDate1 = format.format(dt1);
        String strDate2 = format.format(dt2);
        return strDate1.equalsIgnoreCase(strDate2);
    }
}
