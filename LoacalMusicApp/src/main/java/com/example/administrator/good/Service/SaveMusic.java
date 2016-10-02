package com.example.administrator.good.Service;

import com.example.administrator.good.entity.MusicEntity;

import java.io.Serializable;

/**
 * Created by lvkaixue on 2016/9/24.
 */
public class SaveMusic implements Serializable{
    private static SaveMusic saveMusic;
    private boolean isSave;
    private int po;
    private MusicEntity entity;
    private SaveMusic(){}

    public static SaveMusic newInstanceSaveMusic() {
        if (saveMusic == null) {
            synchronized (SaveMusic.class) {
                if (saveMusic == null) {
                    saveMusic = new SaveMusic();
                }
            }
        }
        return saveMusic;
    }


    public MusicEntity getEntity() {
        return entity;
    }

    public void setEntity(MusicEntity entity) {
        this.entity = entity;
    }

    public boolean getSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }

    public int getPo() {
        return po;
    }

    public void setPo(int po) {
        this.po = po;
    }
}
