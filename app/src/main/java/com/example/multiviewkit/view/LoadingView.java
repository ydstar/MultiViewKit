package com.example.multiviewkit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.multiviewkit.R;

/**
 * Author: 信仰年轻
 * Date: 2021-04-01 11:38
 * Email: hydznsq@163.com
 * Des: 加载页
 */
public class LoadingView extends LinearLayout {


    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_state_loading, this, true);

        this.setOrientation(LinearLayout.VERTICAL);
    }


}
