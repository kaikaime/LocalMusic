package com.example.administrator.good.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/22.
 */
public class CenView extends BaseFragment{

    @Override
    public View initView() {
        TextView textView = new TextView(context);
        textView.setText(getClass().getSimpleName());
        return textView;
    }
}
