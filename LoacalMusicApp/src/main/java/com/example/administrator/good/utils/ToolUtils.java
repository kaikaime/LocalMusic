package com.example.administrator.good.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.administrator.good.customview.SweepCustomView;
import com.example.administrator.good.database.DBDao;
import com.example.administrator.good.entity.MusicEntity;

import java.util.ArrayList;

/**
 * Created by lvkaixue on 2016/9/24.
 */
public class ToolUtils {
    public static Context getContext(){
        return BaseApplication.getContext();
    }
    public static void mRefrenUI(Runnable tast){
        BaseApplication.getHandler().post(tast);
    }
    public static void mToastShow(String str){
        Toast.makeText(BaseApplication.getContext(), str, Toast.LENGTH_SHORT).show();
    }

    public static  void close(SQLiteDatabase database){
        if(database!=null){
            database.close();
        }
    }

    public static boolean[] getPar(ArrayList<?> mList){
        boolean  parId[] = new boolean[mList.size()];
        for(int i =0;i<mList.size();i++){
            parId[i]=false;
        }
        return parId;
    }
    public static void deleteIndexArray(boolean parId[], int index, ArrayList<MusicEntity> mList){
        parId[index]=false;
        mList.remove(index);
    }
    public static void deleteStringArray(boolean parId[], int index, ArrayList<String> mList){
        parId[index]=false;
        mList.remove(index);
    }
    public static void closeSweep(final boolean[] parId,SweepCustomView sweepCustomView, final int position){
        if(parId[position]){
            sweepCustomView.open();
        }else {
            sweepCustomView.closeAll();
        }
        //删除
        sweepCustomView.addOpenListener(new SweepCustomView.OnOpenListener() {
            @Override
            public void isOpen(boolean flag, SweepCustomView sweepCustomView) {
                parId[position] = flag;
            }
        });
    }

    //删除歌曲
   public static boolean deleteMusic(final boolean bo){
       ToolUtils.mRefrenUI(new Runnable() {
           @Override
           public void run() {
               if (bo) {
                   ToolUtils.mToastShow("删除成功!");
               } else {
                   ToolUtils.mToastShow("删除失败!");
               }
           }
       });
       if(bo){
            return true;
       }else {
            return false;
       }
   }
}
