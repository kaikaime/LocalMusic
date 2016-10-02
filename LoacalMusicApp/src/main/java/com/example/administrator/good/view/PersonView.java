package com.example.administrator.good.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.good.MainActivity;
import com.example.administrator.good.R;
import com.example.administrator.good.adapter.MusicAllAdapter;
import com.example.administrator.good.adapter.MusicPandApdapter;
import com.example.administrator.good.entity.MusicEntity;
import com.example.administrator.good.faceinter.OnMusicItemClickListener;
import com.example.administrator.good.utils.MusicUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class PersonView extends BaseFragment implements OnMusicItemClickListener{
    private ExpandableListView expandableListView;
    private  HashMap<String,List<MusicEntity>> listHashMap = new HashMap<>();
    private  HashMap<String,String> title = new HashMap<>();
    private  ArrayList<String> titArray = new ArrayList<>();
    private MusicPandApdapter musicPandApdapter;
    private List<MusicEntity> musicList;
    public PersonView(List<MusicEntity> musicList) {
        this.musicList = musicList;
    }

    @Override
    public void initData() {
        notifyData();
    }

    @Override
    public View initView() {
            baseView = View.inflate(context, R.layout.expand_ist_view,null);
            expandableListView = (ExpandableListView) baseView.findViewById(R.id.expand);
        if(musicPandApdapter == null){
            musicPandApdapter = new MusicPandApdapter(context, titArray, listHashMap);
            musicPandApdapter.setOnMusicItemClickListener(this);
            expandableListView.setAdapter(musicPandApdapter);
        }else{
            musicPandApdapter.notifyChangedData(titArray,listHashMap);
        }
        return baseView;
    }

    @Override
    public void notifyData() {
        if ((title = MusicUtils.getArrayList())!=null) {
            Iterator<String> it = title.keySet().iterator();
            while (it.hasNext()){
                ArrayList<MusicEntity> list = new ArrayList<>();
                String artist = it.next();
                titArray.add(artist);
                for(int i = 0;i<musicList.size();i++){
                    if (artist.equals(musicList.get(i).getMusicArtist())) {
                        list.add(musicList.get(i));
                    }
                }
                listHashMap.put(artist, list);
            }
        }
    }

    @Override
    public void onItemClick(int index, View view, Object object, int cup) {
        ((MainActivity) context).receive(0, ((MusicEntity) object));
    }
}
