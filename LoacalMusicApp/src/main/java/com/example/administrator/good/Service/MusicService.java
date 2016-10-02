package com.example.administrator.good.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.administrator.good.aidl.IMyAidlInterface;
import com.example.administrator.good.database.DBDao;
import com.example.administrator.good.entity.MusicEntity;
import com.example.administrator.good.single.MusicSingle;
import com.example.administrator.good.utils.Constom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/9/23.
 */
public class MusicService extends Service implements MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener{
    private ArrayList<MusicEntity> musicList;
    private MediaPlayer mediaPlayer;
    private Intent intent;
    private int positon = -1;
    private int currentTime = 0;
    private int currentProcess = 0;//当前的进度
    private boolean isPause = false;//暂停状态
    private Messenger mMessenger;
    private Timer mTimer;
    private Bundle bundle;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind() ;
    }

    public class MyBind extends IMyAidlInterface.Stub{
        @Override
        public void startPlay(int positison) {
            MusicService.this.startPlay();
        }

        @Override
        public void pausePlay(int positison) {
            MusicService.this.pausePlay();
        }

        @Override
        public void resumPlay(int positison) {
            MusicService.this.resumPlay();
        }

        @Override
        public void stopPlay(int positison) {
            MusicService.this.stopPlay();
        }

        @Override
        public void up(int position) {
            MusicService.this.up(position);
        }

        @Override
        public void next(int position) {
            MusicService.this.next(position);
        }

        @Override
        public void setCurrentProcess(int cupc) {
            MusicService.this.setCurrentProcess(cupc);
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        bundle = new Bundle();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            this.intent = intent;
            musicList = (ArrayList<MusicEntity>) intent.getSerializableExtra("list");
            mMessenger = (Messenger) intent.getExtras().get("messenger");
        return super.onStartCommand(intent, flags, startId);
    }
    public void startPlay() {
        if(positon==-1){
            return;
        }
        if (mTimer == null && mediaPlayer != null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
             //  sendCurrentProcess();
        }
    },0,1000);
    if(mediaPlayer!=null){
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicList.get(positon).path);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                        if(isPause){
                            isPause = false;
                            mediaPlayer.seekTo(currentProcess);
                            currentProcess = 0;
                        }
                        //将播放过的音乐保存到数据库中
                        if(!DBDao.isExistence(musicList.get(positon))){
                            DBDao.addMusic(musicList.get(positon));
                        }
                        //将当前播放的歌曲保存起来，显示在前台
                        sendCurrentProcess();//发送当前进度
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setCurrentProcess(int cupc){
        if(mediaPlayer!= null && mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(cupc);
        }
    }
    private void sendCurrentProcess(){
        int currentPosition = mediaPlayer.getCurrentPosition();//获取当前的进度
        int currentDuration = mediaPlayer.getDuration();//获取当前的时间
        bundle.clear();
        bundle.putInt("currentPosition", currentPosition);
        bundle.putInt("currentDuration", currentDuration);
        try {
            //将当前播放的歌曲保存起来，显示在前台
            Message message = Message.obtain();
            message.what = Constom.currentMessage;
            message.obj =bundle;
            mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void pausePlay() {
        if(mediaPlayer != null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPause = true;
            currentTime = mediaPlayer.getDuration();//获取当前的时间
            currentProcess = mediaPlayer.getCurrentPosition();//获取当前的进度
            System.out.println("当前的秒数: "+currentProcess+"  "+currentTime);
        }else {
            mediaPlayer.start();
        }
    }

    public void resumPlay() {
        if(mediaPlayer !=null && mediaPlayer.isPlaying()){
            startPlay();
        }
    }

    public void stopPlay() {
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
    }

    /**
     * 上一曲
     * @param position
     */
    public void up(int position) {
        this.positon = position;
        if(mediaPlayer !=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        startPlay();
    }

    /**
     * 下一曲
     * @param position
     */
    public void next(int position) {
        this.positon = position;
        if(mediaPlayer !=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        startPlay();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            //播放完毕
            Message message = Message.obtain();
            message.what= Constom.nextMusic;
            mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(getApplicationContext(),"资源有问题!",Toast.LENGTH_SHORT).show();
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("========onDestroy");
        if(mediaPlayer !=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
