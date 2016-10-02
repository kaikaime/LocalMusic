package com.example.administrator.good.view;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.good.R;
import com.example.administrator.good.adapter.MusicAllAdapter;
import com.example.administrator.good.entity.MusicEntity;
import com.example.administrator.good.faceinter.OnMusicItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class MusicView extends BaseFragment{
    private ListView mListView;
    private ArrayList<MusicEntity> musicList;
    public MusicView(ArrayList<MusicEntity> musicList ) {
        this.musicList = musicList;
    }

    @Override
    public View initView() {
        baseView = View.inflate(context, R.layout.per_item_view,null);
        mListView = (ListView) baseView.findViewById(R.id.item_el);
        mListView.setAdapter(new MusicAllAdapter(context, musicList));
        return baseView;
    }

}
