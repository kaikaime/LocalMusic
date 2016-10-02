package com.example.administrator.good.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.example.administrator.good.R;
import com.example.administrator.good.customview.SweepCustomView;
import com.example.administrator.good.database.DBDao;
import com.example.administrator.good.entity.MusicEntity;
import com.example.administrator.good.faceinter.ItemOnclikListener;
import com.example.administrator.good.faceinter.OnMusicItemClickListener;
import com.example.administrator.good.utils.Constom;
import com.example.administrator.good.utils.MusicUtils;
import com.example.administrator.good.utils.StringUtils;
import com.example.administrator.good.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class MusicAllAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MusicEntity> mList;
    private OnMusicItemClickListener listener;
    private boolean [] parId ;

    public MusicAllAdapter(Context context, ArrayList<MusicEntity> mList) {
        this.context = context;
        this.mList = mList;
        this.listener= (OnMusicItemClickListener) context;
        parId = ToolUtils.getPar(mList);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder mViewHolder=null;
        if(view==null){
            mViewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.sweep_item, null);
            mViewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            mViewHolder.tv_songer = (TextView) view.findViewById(R.id.tv_songer);
            mViewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            mViewHolder.sweepCustomView = (SweepCustomView) view.findViewById(R.id.sweep_custom_view);
            view.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder) view.getTag();
        }
        ToolUtils.closeSweep(parId, mViewHolder.sweepCustomView, position);
        final MusicEntity music = mList.get(position);
        mViewHolder.tv_name.setText(music.getMusicName());
        mViewHolder.tv_songer.setText((music.getDisplayName().contains("<unknown>")) == true ? "未知歌手" : music.getDisplayName());
        mViewHolder.tv_time.setText(StringUtils.reFormatDate(music.getMusicTime()));
        initAction(position, view, music);
        final View finalView = view;
        mViewHolder.sweepCustomView.setItemOnclikListener(position, new ItemOnclikListener() {
            @Override
            public void itemOnclik(int index, boolean isOnclick) {
                if (listener != null && isOnclick) {
                    listener.onItemClick(position, finalView, music, Constom.objectPause);
                }
            }
        });

        //删除数据
        final ViewHolder finalMViewHolder = mViewHolder;
        mViewHolder.sweepCustomView.addDeleteListener(new SweepCustomView.OnDeleteListener() {
            @Override
            public void isDelete(boolean flag, boolean isOpen, SweepCustomView sweepCustomView) {
                if (flag && isOpen) {
                    finalMViewHolder.sweepCustomView.close();
                    finalMViewHolder.sweepCustomView.addOpenListener(new SweepCustomView.OnOpenListener() {
                        @Override
                        public void isOpen(boolean flag, SweepCustomView sweepCustomView) {
                            //如果是关闭的直接删除数据
                            if (!flag && ToolUtils.deleteMusic(MusicUtils.dlMusic(music))&& DBDao.deleteMusic(music)) {
                                ToolUtils.deleteIndexArray(parId, position, mList);
                                notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });


        return view;
    }

    class ViewHolder{
        SweepCustomView sweepCustomView;
        TextView tv_name;
        TextView tv_songer;
        TextView tv_time;
        TextView tv_musicPath;
        TextView tv_musicRating;
    }


    private void initAction(int position, View view, MusicEntity music) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("MusicAllAdapter.onClick");
            }
        });
    }
}
