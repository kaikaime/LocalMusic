package com.example.administrator.good.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.good.R;
import com.example.administrator.good.customview.SweepCustomView;
import com.example.administrator.good.database.DBDao;
import com.example.administrator.good.entity.MusicEntity;
import com.example.administrator.good.faceinter.ItemOnclikListener;
import com.example.administrator.good.faceinter.OnMusicItemClickListener;
import com.example.administrator.good.utils.Constom;
import com.example.administrator.good.utils.StringUtils;
import com.example.administrator.good.utils.ToolUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/23.
 */
public class MusicListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MusicEntity> mList;
    private OnMusicItemClickListener listener;
    private boolean [] parId ;
    public MusicListAdapter(Context context, ArrayList<MusicEntity> mList) {
        this.context = context;
        this.mList = mList;
        this.listener = (OnMusicItemClickListener) context;
        parId = ToolUtils.getPar(mList);
    }

    public void notifyChangedMusic(ArrayList<MusicEntity> mList){
        this.mList = mList;
        notifyDataSetChanged();
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
    public View getView(final int position, View conView, ViewGroup arg2) {
        ViewHolder mViewHolder = null;
        if(conView==null){
            mViewHolder = new ViewHolder();
            conView = View.inflate(context, R.layout.sweep_item, null);
            mViewHolder.tv_name = (TextView) conView.findViewById(R.id.tv_name);
            mViewHolder.tv_songer = (TextView) conView.findViewById(R.id.tv_songer);
            mViewHolder.tv_time = (TextView) conView.findViewById(R.id.tv_time);
            mViewHolder.sweepCustomView = (SweepCustomView) conView.findViewById(R.id.sweep_custom_view);
            conView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder) conView.getTag();
        }
        ToolUtils.closeSweep(parId, mViewHolder.sweepCustomView, position);
        final MusicEntity music = mList.get(position);
        mViewHolder.tv_name.setText(music.getMusicName());
        mViewHolder.tv_songer.setText((music.getDisplayName().contains("<unknown>")) == true ? "未知歌手" : music.getDisplayName());
        mViewHolder.tv_time.setText(StringUtils.reFormatDate(music.getMusicTime()));
        final View finalConView = conView;
        mViewHolder.sweepCustomView.setItemOnclikListener(position, new ItemOnclikListener() {
            @Override
            public void itemOnclik(int index,boolean isOnclick) {
                if (listener != null && isOnclick) {
                    listener.onItemClick(position, finalConView, music, Constom.objectPause);
                }
            }
        });

        //删除数据
        final ViewHolder finalMViewHolder = mViewHolder;
        mViewHolder.sweepCustomView.addDeleteListener(new SweepCustomView.OnDeleteListener() {
            @Override
            public void isDelete(boolean flag, boolean isOpen, SweepCustomView sweepCustomView) {
                if(flag && isOpen){
                   finalMViewHolder.sweepCustomView.close();
                   finalMViewHolder.sweepCustomView.addOpenListener(new SweepCustomView.OnOpenListener() {
                       @Override
                       public void isOpen(boolean flag, SweepCustomView sweepCustomView) {
                           //如果是关闭的直接删除数据
                           if (!flag && ToolUtils.deleteMusic(DBDao.deleteMusic(music))) {
                               ToolUtils.deleteIndexArray(parId,position, mList);
                               notifyDataSetChanged();
                           }
                       }
                   });
               }
            }
        });
        return conView;
    }


    class ViewHolder{
        SweepCustomView sweepCustomView;
        TextView tv_name;
        TextView tv_songer;
        TextView tv_time;
        TextView tv_musicPath;
        TextView tv_musicRating;
    }

}
