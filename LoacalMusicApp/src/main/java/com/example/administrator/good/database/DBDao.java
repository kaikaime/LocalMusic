package com.example.administrator.good.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.good.entity.MusicEntity;
import com.example.administrator.good.utils.ToolUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lvkaixue on 2016/9/24.
 */
public class DBDao {
    private static DBUtils dbUtils;
    private static ArrayList<MusicEntity> arrayList;
    static {
        dbUtils = new DBUtils(ToolUtils.getContext());
        arrayList = new ArrayList<MusicEntity>();
    }

    /**
     * 保存播放过的音乐
     * @param entity
     * @return
     */
    public static boolean addMusic(MusicEntity entity){
        SQLiteDatabase database = dbUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBUtils.ColumnTable.music_rating,entity.getMusicRating());
        values.put(DBUtils.ColumnTable.music_name,entity.getMusicName());
        values.put(DBUtils.ColumnTable.music_path,entity.getMusicPath());
        values.put(DBUtils.ColumnTable.music_album,entity.getMusicAlbum());
        values.put(DBUtils.ColumnTable.music_artist,entity.getMusicArtist());
        values.put(DBUtils.ColumnTable.music_time,entity.getMusicTime());
        values.put(DBUtils.ColumnTable.path,entity.getPath());
        values.put(DBUtils.ColumnTable.display_name,entity.getDisplayName());
        values.put(DBUtils.ColumnTable.music_album_image,entity.getMusicAlbumImage());
        values.put(DBUtils.ColumnTable.music_id,entity.getId());
        long insert = database.insert(DBUtils.DB_NAME, null, values);
        ToolUtils.close(database);
        return insert>0?true:false;
    }

    /**
     * 查看当前的对象是否在数据库中存在
     * @return
     */
    public static boolean isExistence(MusicEntity entity){
        SQLiteDatabase database = dbUtils.getReadableDatabase();
        Cursor query = database.query(DBUtils.DB_NAME, null, DBUtils.ColumnTable.music_id + "=?", new String[]{entity.getId()}, null, null, null);
        if (query.moveToNext()){
            System.out.println("数据库中是否存在："+query.getString(2));
            ToolUtils.close(database);
            return true;
        }
       ToolUtils.close(database);
        return false;
    }

    /**
     * 查询播放了保存在数据库中的音乐
     * @param
     */
    public static ArrayList<MusicEntity> getHanshMap() {
        if (arrayList == null)
        {
            arrayList = new ArrayList<MusicEntity>();
        }else {
            arrayList.clear();
        }
        SQLiteDatabase database = dbUtils.getReadableDatabase();
        Cursor query = database.query(DBUtils.DB_NAME, null,null,null, null, null, null);
        while (query.moveToNext()){
            MusicEntity musicEntity = new MusicEntity();
            String musicId = query.getString(query.getColumnIndex(DBUtils.ColumnTable.music_id));
            System.out.println("当前的音乐是: "+musicId+"    "+query.getString(query.getColumnIndex(DBUtils.ColumnTable.music_name)));
            musicEntity.setId(musicId);
            musicEntity.setMusicRating(query.getString(query.getColumnIndex(DBUtils.ColumnTable.music_rating)));
            musicEntity.setMusicName(query.getString(query.getColumnIndex(DBUtils.ColumnTable.music_name)));
            musicEntity.setMusicPath(query.getString(query.getColumnIndex(DBUtils.ColumnTable.music_path)));
            musicEntity.setMusicArtist(query.getString(query.getColumnIndex(DBUtils.ColumnTable.music_artist)));
            musicEntity.setMusicAlbum(query.getString(query.getColumnIndex(DBUtils.ColumnTable.music_album)));
            musicEntity.setMusicTime(query.getLong(query.getColumnIndex(DBUtils.ColumnTable.music_time)));
            musicEntity.setPath(query.getString(query.getColumnIndex(DBUtils.ColumnTable.path)));
            musicEntity.setDisplayName(query.getString(query.getColumnIndex(DBUtils.ColumnTable.display_name)));
            musicEntity.setMusicAlbumImage(query.getString(query.getColumnIndex(DBUtils.ColumnTable.music_album_image)));
            arrayList.add(musicEntity);
        }
        return arrayList;
    }

    /**
     * 删除数据
     */
    public static boolean deleteMusic(MusicEntity entity){
        SQLiteDatabase database =  dbUtils.getWritableDatabase();
        int delete = database.delete(DBUtils.DB_NAME, DBUtils.ColumnTable.music_id + "=?", new String[]{entity.getId()});
        ToolUtils.close(database);
        return delete>0?true:false;
    }
}
