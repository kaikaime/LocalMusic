package com.example.administrator.good;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.example.administrator.good.database.DBDao;
import com.example.administrator.good.entity.MusicEntity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void testQuery(){
        ArrayList<MusicEntity> entityHashMap =  DBDao.getHanshMap();
        System.out.println("音乐数量: "+entityHashMap.size());
    }
}