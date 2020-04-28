package cn.edu.hebtu.software.listendemo.credit.interf;

import android.graphics.Canvas;

import cn.edu.hebtu.software.listendemo.credit.model.Day;

/**
 * Created by ldf on 17/6/26.
 */

public interface IDayRenderer {

    void refreshContent();

    void drawDay(Canvas canvas, Day day);

    IDayRenderer copy();

}
