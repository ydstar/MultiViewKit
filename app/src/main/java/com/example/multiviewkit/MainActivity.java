package com.example.multiviewkit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multiviewkit.manager.MultiViewManager;
import com.multiview.kit.MultiViewKit;

import static com.multiview.kit.MultiViewKit.VIEW_STATE_LOADING;

/**
 * Author: 信仰年轻
 * Date: 2021-04-01 11:38
 * Email: hydznsq@163.com
 * Des:多视图组件Demo
 */
public class MainActivity extends AppCompatActivity implements MultiViewKit.StateListener {

    private MyHandler mHandler = new MyHandler();
    private MultiViewManager mManager = MultiViewManager.INSTANCE;
    private MultiViewKit mMultiViewKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMultiViewKit = findViewById(R.id.multiViewKit);

        initMultiViewKit();
        initListView();
        sendMsg();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof MultiViewKit) {
                mManager.setViewForState(MultiViewKit.VIEW_STATE_CONTENT);
            }
        }
    }

    private void initMultiViewKit() {
        mManager.initAllViews(mMultiViewKit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "重新加载数据", Toast.LENGTH_SHORT).show();
                sendMsg();
            }
        });

        mMultiViewKit.setStateListener(this);
    }

    private void initListView() {
        ListView list = findViewById(R.id.list);
        String[] data = new String[100];
        for (int i = 0; i < 100; i++) {
            data[i] = "风清扬" + i;
        }
        list.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, data));
    }

    private void sendMsg() {
        mManager.setViewForState(VIEW_STATE_LOADING);
        Message msg = mHandler.obtainMessage();
        msg.obj = mMultiViewKit;
        mHandler.sendMessageDelayed(msg, 1000);
    }

    @Override
    protected void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.error:
                mManager.setViewForState(MultiViewKit.VIEW_STATE_ERROR, new Throwable());
                return true;

            case R.id.empty:
                mManager.setViewForState(MultiViewKit.VIEW_STATE_EMPTY);
                return true;

            case R.id.content:
                mManager.setViewForState(MultiViewKit.VIEW_STATE_CONTENT);
                return true;

            case R.id.loading:
                mManager.setViewForState(MultiViewKit.VIEW_STATE_LOADING);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStateChanged(@MultiViewKit.ViewState int viewState) {
        Log.i("状态切换", "状态切换: " + viewState);
    }


}
