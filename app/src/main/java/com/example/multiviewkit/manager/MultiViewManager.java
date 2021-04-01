package com.example.multiviewkit.manager;

import android.content.Context;
import android.view.View;
import com.example.multiviewkit.R;
import com.example.multiviewkit.view.EmptyView;
import com.example.multiviewkit.view.ErrorView;
import com.example.multiviewkit.view.LoadingView;
import com.multiview.kit.MultiViewKit;
import static com.multiview.kit.MultiViewKit.VIEW_STATE_ERROR;
import static com.multiview.kit.MultiViewKit.VIEW_STATE_LOADING;

/**
 * Author: 信仰年轻
 * Date: 2021-04-01 11:38
 * Email: hydznsq@163.com
 * Des:多页面管理者
 */
public class MultiViewManager {

    private ErrorView mErrorView;
    private MultiViewKit mMultiViewKit;

    private MultiViewManager() {
    }

    public static volatile MultiViewManager INSTANCE = Holder.INSTANCE;

    private static class Holder {
        public static volatile MultiViewManager INSTANCE = new MultiViewManager();
    }

    /**
     * 初始化所有的页面
     */
    public void initAllViews(MultiViewKit multiViewKit, View.OnClickListener listener) {
        this.mMultiViewKit = multiViewKit;
        Context context = multiViewKit.getContext();

        //显示页面切换的动画,这里不显示切换动画
        multiViewKit.setAnimateLayoutChanges(false);
        //loading
        LoadingView loadingView = new LoadingView(context);
        multiViewKit.setViewForState(loadingView, VIEW_STATE_LOADING);

        //空页面
        EmptyView emptyView = new EmptyView(context);
        multiViewKit.setViewForState(emptyView, MultiViewKit.VIEW_STATE_EMPTY);
        emptyView.findViewById(R.id.ll_empty).setOnClickListener(listener);

        //错误页面
        mErrorView = new ErrorView(context);
        multiViewKit.setViewForState(mErrorView, VIEW_STATE_ERROR);
        mErrorView.findViewById(R.id.ll_error).setOnClickListener(listener);

        //展示加载页
        multiViewKit.setViewState(VIEW_STATE_LOADING);
    }

    /**
     * 拿到数据后展示对应的页面
     *
     * @param state
     */
    public void setViewForState(@MultiViewKit.ViewState int state) {
        setViewForState(state, null);
    }

    /**
     * 拿到数据后展示对应的页面
     *
     * @param state
     * @param throwable
     */
    public void setViewForState(@MultiViewKit.ViewState int state, Throwable throwable) {
        mMultiViewKit.setViewState(state);
        if (state == VIEW_STATE_ERROR) {
            mErrorView.showError(throwable);
        }
    }


}
