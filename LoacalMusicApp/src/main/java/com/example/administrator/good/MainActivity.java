package com.example.administrator.good;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.good.Service.MusicService;
import com.example.administrator.good.Service.SaveMusic;
import com.example.administrator.good.aidl.IMyAidlInterface;
import com.example.administrator.good.entity.MusicEntity;
import com.example.administrator.good.faceinter.OnMusicItemClickListener;
import com.example.administrator.good.utils.Constom;
import com.example.administrator.good.utils.MusicUtils;
import com.example.administrator.good.utils.StringUtils;
import com.example.administrator.good.view.BaseFragment;
import com.example.administrator.good.view.BaseView;
import com.example.administrator.good.view.CenView;
import com.example.administrator.good.view.MusicListView;
import com.example.administrator.good.view.MusicView;
import com.example.administrator.good.view.PersonView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMusicItemClickListener {
    private ArrayList<MusicEntity> musicList = new ArrayList<>();
    private RadioGroup rgp;
    private RadioButton btnPer;
    private RadioButton btnCen;
    private RadioButton btnMusic;
    private RadioButton btnList;
    private FrameLayout addViewGroup;
    private  MyServiceConn myServiceConn;
    private  Intent intent;
    private  IMyAidlInterface iMyAidlInterface;
    private  int nextPosition = 0;
    private  ImageView pause1;
    private  AppCompatSeekBar bar;
    private  TextView singNick;
    private  TextView musicNick;
    private  TextView currentTime;
    private  TextView allTime;
    private boolean isPause = false;
    private boolean isBoo = false;
    private MusicEntity entity;
    private boolean isFistPause = false;
    private SharedPreferences sp;
    private LinearLayout layout;
    private Bundle bundle;
    private FrameLayout addDl;
    private FrameLayout addTl;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constom.currentMessage) {
                if ( (bundle = (Bundle) msg.obj)!=null) {
                    setCurrentProcess((Bundle) msg.obj);
                }
            }else if (msg.what == Constom.nextMusic) {
                if(nextPosition !=musicList.size()-1){
                    nextPosition++;
                    nextMusic();
                }else {
                    nextPosition =0;
                    nextMusic();
                }
            }
        }
    };

    public Handler getHandler(){
        return handler;
    }
    public void receive(int position,MusicEntity entitys){
        for(int i =0;i<musicList.size();i++) {
            if (entitys.getId().equals(musicList.get(i).getId())) {
                nextPosition = i;
                nextMusic();
                break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("page", MODE_PRIVATE);
        musicList = new MusicUtils().loadSongs();
        bindService();
        initView();
        SaveMusic saveMusic = SaveMusic.newInstanceSaveMusic();
        if ((entity = saveMusic.getEntity())!=null) {
            String musicName = entity.getMusicName();
            String singName = StringUtils.splitString(entity.getMusicArtist());
            nextPosition = saveMusic.getPo();
            isPause = saveMusic.getSave();
            singNick.setText(singName.length() > 10 ? singName.substring(0, 10) : singName);
            musicNick.setText(musicName.length() > 12 ? musicName.substring(0, 10) : musicName);
            if (isPause) {
                //开始进入时候设置的暂停图片
                pause1.setImageResource(R.drawable.pause_selector);
            }else {
                pause1.setImageResource(R.drawable.start_selector);
            }
        }else {
            //代表用户第一次进入程序
            IsShowData();
            //将状态设置为暂停
            if (!isBoo) {
                pause1.setImageResource(R.drawable.pause_selector);
                isBoo=true;
            }
        }
    }

    private void setCurrentProcess(Bundle bundle){
        currentTime.setText(StringUtils.duration(((int) bundle.get("currentPosition"))));
        allTime.setText(StringUtils.duration(((int) bundle.get("currentDuration"))));
        bar.setMax((int) bundle.get("currentDuration"));
        bar.setProgress((int) bundle.get("currentPosition"));
    }
    private void bindService(){
        myServiceConn = new MyServiceConn();
        intent = new Intent(this, MusicService.class);
        intent.putExtra("list", musicList);
        intent.putExtra("messenger", new Messenger(handler));
        startService(intent);
        bindService(intent, myServiceConn, 0);
    }
    private void IsShowData(){
        entity  =  musicList.get(nextPosition);
        String musicName = entity.getMusicName();
        String singName = StringUtils.splitString(entity.getMusicArtist());
        singNick.setText(singName.length() > 10 ? singName.substring(0, 10) : singName);
        musicNick.setText(musicName.length() > 12 ? musicName.substring(0, 10) : musicName);
    }

    /**
     * 页面切换
     * @param baseFragment
     */
    private void replacePage(BaseFragment baseFragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.center_view,baseFragment).commit();
    }
    private void initView() {
        setContentView(R.layout.activity_main);
        rgp = (RadioGroup) findViewById(R.id.main_rg);
        btnPer = (RadioButton) findViewById(R.id.main_rbt_per);
        btnCen = (RadioButton) findViewById(R.id.main_rbt_zhuanji);
        btnMusic = (RadioButton) findViewById(R.id.main_rbt_music);
        btnList = (RadioButton) findViewById(R.id.main_rbt_list);
        addViewGroup = (FrameLayout) findViewById(R.id.center_view);
        pause1 = (ImageView) findViewById(R.id.img_pause);
        bar = (AppCompatSeekBar) findViewById(R.id.see_bar);
        singNick = (TextView) findViewById(R.id.sing_nick);
        musicNick = (TextView) findViewById(R.id.music_nick);
        currentTime = (TextView) findViewById(R.id.seet_tv_currentTime);
        allTime = (TextView) findViewById(R.id.see_tv_allTime);
        layout = (LinearLayout) findViewById(R.id.main_in);
        addDl = (FrameLayout) findViewById(R.id.add_dl_fl);
        addTl = (FrameLayout) findViewById(R.id.add_tl_fl);

        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.main_rbt_per:
                        replacePage(new PersonView(musicList));
                        sp.edit().putInt("page",0).commit();
                        break;
                    case R.id.main_rbt_zhuanji:
                        replacePage(new CenView());
                        sp.edit().putInt("page",1).commit();
                        break;
                    case R.id.main_rbt_music:
                        replacePage(new MusicView(musicList));
                        //设置资源适配器
                        sp.edit().putInt("page",2).commit();
                        break;
                    case R.id.main_rbt_list:
                        replacePage(new MusicListView());
                        sp.edit().putInt("page",3).commit();
                        break;
                }
            }
        });
        //控制进入的页面数量
        if(sp.getInt("page",0) == 0){
            rgp.check(R.id.main_rbt_per);
        }else if(sp.getInt("page",0) == 1){
            rgp.check(R.id.main_rbt_zhuanji);
        }
        else if(sp.getInt("page",0) == 2){
            rgp.check(R.id.main_rbt_music);
        }
        else if(sp.getInt("page",0) == 3){
            rgp.check(R.id.main_rbt_list);
        }

        //上一曲
        ((ImageView) findViewById(R.id.img_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicList.size() > 1 && nextPosition >= 1) {
                    nextPosition--;
                    IsShowData();
                    try {
                        if (isBoo) {
                            isFistPause = true;
                            pause1.setImageResource(R.drawable.start_selector);
                        } else {
                            if (!isPause) {
                                pause1.setImageResource(R.drawable.start_selector);
                            }
                        }
                        iMyAidlInterface.up(nextPosition);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //下一曲
        ((ImageView) findViewById(R.id.img_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicList.size() >= 2 && nextPosition < musicList.size() - 1){
                    nextPosition++;
                    nextMusic();
                }
            }
        });

        //暂停
        pause1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上一次退出去以后，再次进入的时候查看当前歌曲是否是正在播放，还是正在暂停
                try {
                    //如果是第一次进入程序
                    if (isBoo) {
                        iMyAidlInterface.pausePlay(0);
                        if (!isFistPause) {
                            pause1.setImageResource(R.drawable.start_selector);
                            isFistPause = true;
                        } else {
                            pause1.setImageResource(R.drawable.pause_selector);
                            isFistPause = false;
                        }
                    } else {
                        //不是第一次进入
                        isPause = !isPause;
                        if (isPause) {
                            //传入一个默认值0
                            iMyAidlInterface.pausePlay(0);
                            pause1.setImageResource(R.drawable.pause_selector);
                        } else {
                            iMyAidlInterface.pausePlay(0);
                            pause1.setImageResource(R.drawable.start_selector);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //SeekTo进度条
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    //设置拖拽的进度
                    currentTime.setText(StringUtils.duration(seekBar.getProgress()));
                    iMyAidlInterface.setCurrentProcess(seekBar.getProgress());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //meun菜单
        ((ImageView) findViewById(R.id.img_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout.getVisibility()==View.GONE){
                    if(bundle != null){
                        setCurrentProcess(bundle);
                        layout.setVisibility(View.VISIBLE);
                    }
                }else{
                    layout.setVisibility(View.GONE);
                }
            }
        });
    }

    //下一曲
    private void nextMusic(){
            IsShowData();
            try {
                if (isBoo) {
                    isFistPause = true;
                    pause1.setImageResource(R.drawable.start_selector);
                }else {
                    if(isPause) {
                        pause1.setImageResource(R.drawable.start_selector);
                    }
                }
                iMyAidlInterface.next(nextPosition);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(myServiceConn);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            {
                isBoo = false;
                //保存当前的歌曲
                SaveMusic saveMusic = SaveMusic.newInstanceSaveMusic();
                saveMusic.setPo(nextPosition);
                saveMusic.setIsSave(isPause);
                saveMusic.setEntity(entity);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(int index, View view, Object object,int pl) {
        if (pl ==Constom.objectPlay) {
            nextPosition = index;
            nextMusic();
        }else if (pl == Constom.objectPause) {
            MusicEntity entitys = (MusicEntity) object;
            for(int i =0;i<musicList.size();i++) {
                if (entitys.getMusicPath().equals(musicList.get(i).getMusicPath())) {
                    nextPosition = i;
                    nextMusic();
                    break;
                }
            }
        }
    }

    private class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            iMyAidlInterface = null;
        }
    }
}
