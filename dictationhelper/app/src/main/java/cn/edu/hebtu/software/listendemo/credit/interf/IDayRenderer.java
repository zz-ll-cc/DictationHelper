package cn.edu.hebtu.software.listendemo.credit.interf;

import android.graphics.Canvas;
;
import cn.edu.hebtu.software.listendemo.credit.component.Day;

public interface IDayRenderer {

    void refreshContent();

    void drawDay(Canvas canvas, Day day);

    IDayRenderer copy();

}
