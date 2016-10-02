package com.example.administrator.good.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.good.MainActivity;
import com.example.administrator.good.R;
import com.example.administrator.good.adapter.MusicAllAdapter;
import com.example.administrator.good.adapter.MusicListAdapter;
import com.example.administrator.good.database.DBDao;
import com.example.administrator.good.entity.MusicEntity;
import com.example.administrator.good.faceinter.OnMusicItemClickListener;
import com.example.administrator.good.faceinter.OnMusicItemRefrenListener;
import com.example.administrator.good.single.MusicSingle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 * 播放列表
 */
public class MusicListView extends BaseFragment   {
    private ListView mListView;
    private static ArrayList<MusicEntity> entityArrayList = new ArrayList<MusicEntity>();

    @Override
    public void initData() {
        //查询数据库中的数据
        entityArrayList = DBDao.getHanshMap();
    }
    @Override
    public View initView() {
        baseView = View.inflate(context, R.layout.play_item, null);
        mListView = (ListView) baseView.findViewById(R.id.item_el);
        mListView.setAdapter(new MusicListAdapter(context,entityArrayList));
        return baseView;
    }
}
