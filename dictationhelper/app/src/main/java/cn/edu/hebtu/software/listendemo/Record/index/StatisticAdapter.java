package cn.edu.hebtu.software.listendemo.Record.index;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.edu.hebtu.software.listendemo.R;

public class StatisticAdapter extends BaseQuickAdapter<List<String>,BaseViewHolder> {

    private Context context;

    public StatisticAdapter(int layoutResId, @Nullable List data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, List<String> item) {
        Glide.with(context)
                .load(item)
                .into((ImageView) helper.getView(R.id.iv_statistic));
    }
}
