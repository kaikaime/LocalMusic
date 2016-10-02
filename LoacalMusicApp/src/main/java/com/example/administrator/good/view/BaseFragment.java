package com.example.administrator.good.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.good.entity.MusicEntity;

import java.util.List;

/**
 * Created by lvkaixue on 2016/9/25.
 */
public abstract class BaseFragment extends Fragment {
    public Context context;
    public View baseView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseView = initView();
        return baseView;
    }

    public void initData() {}

    protected abstract View initView();

    public void notifyData() {}
}
