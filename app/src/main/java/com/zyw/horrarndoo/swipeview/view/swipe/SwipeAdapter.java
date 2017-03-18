package com.zyw.horrarndoo.swipeview.view.swipe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zyw.horrarndoo.swipeview.R;
import com.zyw.horrarndoo.swipeview.utils.ToastUtils;
import com.zyw.horrarndoo.swipeview.utils.UIUtils;
import com.zyw.horrarndoo.swipeview.view.swipe.SwipeLayout.OnSwipeStateChangeListener;

import java.util.List;

/**
 * Created by Horrarndoo on 2017/3/17.
 */

public class SwipeAdapter extends BaseAdapter implements OnSwipeStateChangeListener {
    private Context mContext;
    private List<String> list;
    private MyClickListener myClickListener;
    private SwipeLayoutManager swipeLayoutManager;

    public SwipeAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        init();
    }

    private void init() {
        myClickListener = new MyClickListener();
        swipeLayoutManager = SwipeLayoutManager.getInstance();
    }

    public void setList(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = UIUtils.inflate(R.layout.list_item_swipe);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);

        holder.tv_content.setText(list.get(position));
        holder.tv_overhead.setOnClickListener(myClickListener);
        holder.tv_overhead.setTag(position);
        holder.tv_delete.setOnClickListener(myClickListener);
        holder.tv_delete.setTag(position);
        holder.sv_layout.setOnSwipeStateChangeListener(this);
        holder.sv_layout.setTag(position);

        holder.sv_layout.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast("item click : " + position);
                swipeLayoutManager.closeUnCloseSwipeLayout();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tv_content, tv_overhead, tv_delete;
        SwipeLayout sv_layout;

        public ViewHolder(View convertView) {
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            tv_overhead = (TextView) convertView.findViewById(R.id.tv_overhead);
            tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            sv_layout = (SwipeLayout) convertView.findViewById(R.id.sv_layout);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Integer position = (Integer) v.getTag();
            switch (v.getId()) {
                case R.id.tv_overhead:
                    //ToastUtils.showToast("position : " + position + " overhead is clicked.");
                    swipeLayoutManager.closeUnCloseSwipeLayout(false);
                    if(onSwipeControlListener != null){
                        onSwipeControlListener.onOverhead(position, list.get(position));
                    }
                    break;
                case R.id.tv_delete:
                    //ToastUtils.showToast("position : " + position + " delete is clicked.");
                    swipeLayoutManager.closeUnCloseSwipeLayout(false);
                    if(onSwipeControlListener != null){
                        onSwipeControlListener.onDelete(position, list.get(position));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onOpen(SwipeLayout swipeLayout) {
        //ToastUtils.showToast(swipeLayout.getTag() + "onOpen.");
    }

    @Override
    public void onClose(SwipeLayout swipeLayout) {
        //ToastUtils.showToast(swipeLayout.getTag() + "onClose.");
    }

    @Override
    public void onStartOpen(SwipeLayout swipeLayout) {
        //            ToastUtils.showToast("onStartOpen.");
    }

    @Override
    public void onStartClose(SwipeLayout swipeLayout) {
        //            ToastUtils.showToast("onStartClose.");
    }

    private OnSwipeControlListener onSwipeControlListener;

    public void setOnSwipeControlListener(OnSwipeControlListener onSwipeControlListener){
        this.onSwipeControlListener = onSwipeControlListener;
    }

    /**
     * overhead 和 delete点击事件接口
     */
    public interface OnSwipeControlListener{
        void onOverhead(int position, String itemTitle);

        void onDelete(int position, String itemTitle);
    }
}
