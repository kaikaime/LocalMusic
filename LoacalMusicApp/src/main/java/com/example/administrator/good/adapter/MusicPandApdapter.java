package com.example.administrator.good.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.administrator.good.MainActivity;
import com.example.administrator.good.R;
import com.example.administrator.good.customview.SweepCustomView;
import com.example.administrator.good.entity.MusicEntity;
import com.example.administrator.good.faceinter.OnMusicItemClickListener;
import com.example.administrator.good.utils.Constom;
import com.example.administrator.good.utils.MusicUtils;
import com.example.administrator.good.utils.StringUtils;
import com.example.administrator.good.utils.ToolUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lvkaixue on 2016/9/24.
 */
public class MusicPandApdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> titArray;
    private HashMap<String,List<MusicEntity>> listHashMap;
    private OnMusicItemClickListener listener;
    private boolean [] parId ;
    public MusicPandApdapter(Context context, ArrayList<String> titArray, HashMap<String, List<MusicEntity>> listHashMap) {
        this.context = context;
        this.titArray = titArray;
        this.listHashMap = listHashMap;
        parId = ToolUtils.getPar(titArray);

    }

    public void setOnMusicItemClickListener(OnMusicItemClickListener listener){
        this.listener = listener;
    }
    public void notifyChangedData( ArrayList<String> titArray, HashMap<String, List<MusicEntity>> listHashMap){
        this.titArray = titArray;
        this.listHashMap = listHashMap;
    }
    @Override
    public int getGroupCount() {
        return titArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(titArray.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(titArray.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupClassViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder= new GroupClassViewHolder();
            convertView = View.inflate(context, R.layout.expandlist,null);
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.text1);
            viewHolder.sweepCustomView = (SweepCustomView) convertView.findViewById(R.id.sweep_custom_view);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (GroupClassViewHolder) convertView.getTag();
        }

        final String musicName = titArray.get(groupPosition);
        viewHolder.text1.setText(StringUtils.splitString(musicName));
        ToolUtils.closeSweep(parId, viewHolder.sweepCustomView, groupPosition);

        //删除数据
        final GroupClassViewHolder finalMViewHolder = viewHolder;
        viewHolder.sweepCustomView.addDeleteListener(new SweepCustomView.OnDeleteListener() {
            @Override
            public void isDelete(boolean flag, boolean isOpen, SweepCustomView sweepCustomView) {
                if (flag && isOpen) {
                    finalMViewHolder.sweepCustomView.close();
                    finalMViewHolder.sweepCustomView.addOpenListener(new SweepCustomView.OnOpenListener() {
                        @Override
                        public void isOpen(boolean flag, SweepCustomView sweepCustomView) {

                        }
                    });
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildClassViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder= new ChildClassViewHolder();
            convertView = View.inflate(context, R.layout.expandlist2,null);
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.text1);
            viewHolder.text2= (TextView) convertView.findViewById(R.id.text2);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ChildClassViewHolder) convertView.getTag();
        }
        final MusicEntity entity =  listHashMap.get(titArray.get(groupPosition)).get(childPosition);
        viewHolder.text1.setText(entity.getMusicName());
        viewHolder.text2.setText(StringUtils.reFormatTime(entity.getMusicTime()));

        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(childPosition, finalConvertView,entity,0);
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupClassViewHolder{
        TextView text1;
        SweepCustomView sweepCustomView;
    }
    class ChildClassViewHolder{
        TextView text1;
        TextView text2;
    }
}
