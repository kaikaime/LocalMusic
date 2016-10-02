package com.example.administrator.good.view;

import android.content.Context;
import android.view.View;

import com.example.administrator.good.entity.MusicEntity;
import com.example.administrator.good.faceinter.OnMusicItemClickListener;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public abstract class BaseView {
    public View view;
    public Context context;

    public BaseView(Context context){
        this.context = context;
        view = initView();
    }

    public abstract View initView() ;

    public void setData(List<MusicEntity> musicList){}

    public View getView(){
        return view;
    }
}
