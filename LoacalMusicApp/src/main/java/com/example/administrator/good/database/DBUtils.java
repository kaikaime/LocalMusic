package com.example.administrator.good.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by lvkaixue on 2016/9/24.
 */
public class DBUtils extends SQLiteOpenHelper {
    private final static int version = 1;
    public final  static String DB_NAME = "music_table";
    public DBUtils(Context context) {
        super(context, DB_NAME+".db", null, version);
    }

    public static class ColumnTable implements BaseColumns {
        public final static String id=ColumnTable._ID;
        public final static String music_rating ="musicRating";
        public final static String music_name="music_name";
        public final static String music_id = "music_id";
        public final static String music_path ="music_path";
        public final static String music_album="music_album";
        public final static String music_artist="music_artist";
        public final static String music_time="music_time";
        public final static String path="path";
        public final static String display_name="display_name";
        public final static String music_album_image="musicAlbumImage";

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+DB_NAME+"" +
                "("+ColumnTable._ID+" integer primary key autoincrement," +
                ""+ColumnTable.music_id+" text," +
                ""+ColumnTable.music_rating+" text," +
                ""+ColumnTable.music_name+" text," +
                ""+ColumnTable.music_path+" text," +
                ""+ColumnTable.music_artist+" text," +
                ""+ColumnTable.music_album+" text," +
                ""+ColumnTable.music_time+" text," +
                ""+ColumnTable.path+" text," +
                ""+ColumnTable.display_name+" text," +
                ""+ColumnTable.music_album_image+" text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table exites if "+DB_NAME);
    }
}
