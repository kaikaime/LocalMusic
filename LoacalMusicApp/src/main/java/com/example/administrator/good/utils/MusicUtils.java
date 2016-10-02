package com.example.administrator.good.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.administrator.good.entity.MusicEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class MusicUtils {
    private final static Uri musicUri =MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private String musicPath;
    private String musicName;
    private String musicAlbum;
    private String musicArtist;
    private String musicAlbumKey;
    private String musicAlbumArtPath;
    private int size=0;
    private int musicTime;
    public  static HashMap<String,String> arrayList = new HashMap<>();
    private ArrayList<MusicEntity> musicList = new ArrayList<>();
    private static ContentResolver contentResolver;
    static {
        contentResolver = BaseApplication.getContext().getContentResolver();
    }

    public static HashMap<String,String> getArrayList(){
        return arrayList;
    }
    public  ArrayList<MusicEntity> loadSongs() {
        if (arrayList.size()>0) {
            arrayList.clear();
        }
        // 查找sdcard卡上的所有歌曲信息
        // 加入封装音乐信息的代码
        // 查询所有歌曲
        Cursor musicCursor = contentResolver.query(
                musicUri, null,
                MediaStore.Audio.Media.SIZE + ">80000", null, null);
        int musicColumnIndex;
        if (null != musicCursor && musicCursor.getCount() > 0) {
            for (musicCursor.moveToFirst(); !musicCursor.isAfterLast(); musicCursor
                    .moveToNext()) {
                MusicEntity entity = new MusicEntity();
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns._ID);
                entity.setMusicId(musicColumnIndex+"");
                int musicRating = musicCursor.getInt(musicColumnIndex);
                entity.setMusicRating(musicRating + "");
                entity.setId(size + "");
                size = size + 1;
                // 取得音乐播放路径
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
                musicPath = musicCursor.getString(musicColumnIndex);
                entity.setMusicPath(musicPath);
                // 取得音乐的名字
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
                musicName = musicCursor.getString(musicColumnIndex);
                entity.setMusicName(musicName);
                // 取得音乐的专辑名称
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);
                musicAlbum = musicCursor.getString(musicColumnIndex);
                entity.setMusicAlbum(musicAlbum);
                // 取得音乐的演唱者
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
                musicArtist = musicCursor.getString(musicColumnIndex);
                if (arrayList.get(musicArtist) == null) {
                    arrayList.put(musicArtist, musicArtist);
                }
                entity.setMusicArtist(musicArtist);
                // 取得歌曲对应的专辑对应的Key
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_KEY);
                musicAlbumKey = musicCursor.getString(musicColumnIndex);
                // 取得歌曲的大小
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);
                musicTime = musicCursor.getInt(musicColumnIndex);
                //
                // Time musicTime = new Time();
                // musicTime.set(musicTime);
                String readableTime = ":";
                int m = musicTime % 60000 / 1000;
                int o = musicTime / 60000;
                if (o == 0) {
                    readableTime = "00" + readableTime;
                } else if (0 < o && o < 10) {
                    readableTime = "0" + o + readableTime;
                } else {
                    readableTime = o + readableTime;
                }
                if (m < 10) {
                    readableTime = readableTime + "0" + m;
                } else {
                    readableTime = readableTime + m;
                }
                entity.setMusicTime(musicTime);
                //
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
                String path = musicCursor.getString(musicColumnIndex);
                entity.setPath(path);
                //
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
                String display = musicCursor.getString(musicColumnIndex);
                entity.setDisplayName(display);
                String[] argArr = {musicAlbumKey};
                Cursor albumCursor = contentResolver.query(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
                        MediaStore.Audio.AudioColumns.ALBUM_KEY + " = ?",
                        argArr, null);
                if (null != albumCursor && albumCursor.getCount() > 0) {
                    albumCursor.moveToFirst();
                    int albumArtIndex = albumCursor
                            .getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART);
                    musicAlbumArtPath = albumCursor.getString(albumArtIndex);
                    if (null != musicAlbumArtPath
                            && !"".equals(musicAlbumArtPath)) {
                        entity.setMusicAlbumImage(musicAlbumArtPath);
                    } else {
                        entity.setMusicAlbumImage(musicAlbumArtPath);
                    }
                } else {
                    // 没有专辑定义，给默认图片
                    entity.setMusicAlbumImage(musicAlbumArtPath);
                }
                musicList.add(entity);
            }
        }
        return musicList;

    }

    /**
     * 删除手机中的歌曲
     * @param context
     * @param entity
     * @return
     */
    private final static String whereMusicPath = MediaStore.Audio.AudioColumns.DATA+"=?";
    public static boolean dlMusic(MusicEntity entity){
        int delete = contentResolver.delete(musicUri,whereMusicPath,new String[]{entity.getMusicPath()});
        return delete>0?true:false;
    }

    /**
     * 删除艺术家，同时将当前艺术家所有的歌曲也删除
     * @return
     */
    public static boolean dlArtist(String mArtist,HashMap<String,List<MusicEntity>> listHashMap){
        List<MusicEntity> mList = null;
        Iterator<MusicEntity> it = null;
        if(listHashMap.size()>0){
            mList = listHashMap.get(mArtist);
            if (mList.size()>0) {
                it = mList.iterator();
                while (it.hasNext()){
                    MusicEntity entity = it.next();
                    System.out.println("entity: == : "+mArtist+" "+entity.getMusicPath());
                    dlMusic(entity);
                }
            }
        }
        int id = contentResolver.delete(musicUri,MediaStore.Audio.AudioColumns.ARTIST+"=?",new String[]{mArtist});
        return id==0?true:false;
    }
}
