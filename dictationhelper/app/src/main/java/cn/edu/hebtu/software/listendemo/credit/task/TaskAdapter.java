package cn.edu.hebtu.software.listendemo.credit.task;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.R;


/**
 * Created by ldf on 17/6/14.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<Map<String,String>> title;

    public TaskAdapter(Context context,List<Map<String,String>> title) {
        this.title=title;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.activty_my_credit_task_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("ViewHolder1",title.toString());
        Log.e("ViewHolder2",title.get(position).toString());
        holder.tv_credit_task.setText(title.get(position).get("task"));
        holder.tv_credit_task_name.setText(title.get(position).get("task_name"));
        holder.tv_credit_task_content.setText(title.get(position).get("task_content"));
        holder.btn_add_credit.setText(title.get(position).get("add_credit"));
    }

    @Override
    public int getItemCount() {
        if(title!=null)
        {
            return title.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_credit_task;
        TextView tv_credit_task_name;
        TextView tv_credit_task_content;
        Button btn_add_credit;

        ViewHolder(View view) {
            super(view);
            tv_credit_task = (TextView) view.findViewById(R.id.tv_credit_task);
            tv_credit_task_name=(TextView) view.findViewById(R.id.tv_credit_task_name);
            tv_credit_task_content=(TextView) view.findViewById(R.id.tv_credit_task_content);
            btn_add_credit=(Button)view.findViewById(R.id.btn_add_credit);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }
}

//package cn.edu.hebtu.software.listendemo.credit;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import cn.edu.hebtu.software.listendemo.R;
//
///**
// * Created by ldf on 17/6/14.
// */
//
//public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ViewHolder> {
//
//    private final LayoutInflater layoutInflater;
//    private final Context context;
//    private String[] titles;
//
//    public ExampleAdapter(Context context) {
//        titles = context.getResources().getStringArray(R.array.titles);
//        this.context = context;
//        layoutInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(layoutInflater.inflate(R.layout.item, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.textView.setText(titles[position]);
//    }
//
//    @Override
//    public int getItemCount() {
//        return titles == null ? 0 : titles.length;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView textView;
//
//        ViewHolder(View view) {
//            super(view);
//            textView = (TextView) view.findViewById(R.id.text_view);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("ViewHolder", "onClick--> position = " + getPosition());
//                }
//            });
//        }
//    }
//}

//package cn.edu.hebtu.software.calendardemo2.task;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import cn.edu.hebtu.software.calendardemo2.R;
//
//
//public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
//
//    private final LayoutInflater layoutInflater;
//    private final Context context;
//    private String[] titles;
//
//    public TaskAdapter(Context context) {
//        titles = context.getResources().getStringArray(R.array.titles);
//        this.context = context;
//        layoutInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(layoutInflater.inflate(R.layout.item, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.textView.setText(titles[position]);
//    }
//
//    @Override
//    public int getItemCount() {
//        return titles == null ? 0 : titles.length;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView textView;
//
//        ViewHolder(View view) {
//            super(view);
//            textView = (TextView) view.findViewById(R.id.text_view);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("ViewHolder", "onClick--> position = " + getPosition());
//                }
//            });
//        }
//    }
//}
