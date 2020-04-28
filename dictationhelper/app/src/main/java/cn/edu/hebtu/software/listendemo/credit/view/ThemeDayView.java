package cn.edu.hebtu.software.listendemo.credit.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.credit.Utils.State;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarDate;
import cn.edu.hebtu.software.listendemo.credit.interf.IDayRenderer;


public class ThemeDayView extends DayView {
    private TextView dateTv;
    private ImageView marker;
    private View selectedBackground;
    private View todayBackground;
    private final CalendarDate today = new CalendarDate();

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public ThemeDayView(Context context, int layoutResource) {
        super(context, layoutResource);
        dateTv = (TextView) findViewById(R.id.date);
        marker = (ImageView) findViewById(R.id.maker);
        selectedBackground = findViewById(R.id.selected_background);
        selectedBackground = findViewById(R.id.selected_background);
        todayBackground = findViewById(R.id.today_background);
    }

    @Override
    public void refreshContent() {
        CalendarDate date = day.getDate();
        Log.e("tt3",date.getDay()+"");
        State state = day.getState();
        if (date != null) {
            if (date.equals(today)) {
                dateTv.setText("今");
                todayBackground.setVisibility(VISIBLE);
            } else {
                dateTv.setText(date.day + "");
                todayBackground.setVisibility(GONE);
            }
        }
        if (state == State.SELECT) {
            selectedBackground.setVisibility(VISIBLE);
        } else {
            selectedBackground.setVisibility(GONE);
        }
        super.refreshContent();
    }

    @Override
    public IDayRenderer copy() {
        return new ThemeDayView(context, layoutResource);
    }
}
