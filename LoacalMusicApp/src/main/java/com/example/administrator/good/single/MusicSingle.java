package com.example.administrator.good.single;

import com.example.administrator.good.entity.MusicEntity;

import java.util.HashMap;

/**
 * Created by lvkaixue on 2016/9/23.
 */
public class MusicSingle {
    private static  HashMap<String,MusicEntity> hashMap;
    private MusicSingle(){}
    private static MusicSingle musicSingle;
    public static MusicSingle newInstanceMusicSingle(){
        if(musicSingle == null){
            synchronized (MusicSingle.class){
                if(musicSingle == null){
                    musicSingle = new MusicSingle();
                    hashMap = new HashMap<>();
                }
            }
        }
        return musicSingle;
    }

    public  HashMap<String,MusicEntity> getHashMap(){
        return hashMap;
    }
}
