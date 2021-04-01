package com.multiview.kit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: 信仰年轻
 * Date: 2020-04-17 11:38
 * Email: hydznsq@163.com
 * Des: 页面的多视图组件,加载中-成功-失败-空页面
 */
public class MultiViewKit extends FrameLayout {

    public static final int VIEW_STATE_UNKNOWN = -1;

    public static final int VIEW_STATE_CONTENT = 0;

    public static final int VIEW_STATE_ERROR = 1;

    public static final int VIEW_STATE_EMPTY = 2;

    public static final int VIEW_STATE_LOADING = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VIEW_STATE_UNKNOWN, VIEW_STATE_CONTENT, VIEW_STATE_ERROR, VIEW_STATE_EMPTY, VIEW_STATE_LOADING})
    public @interface ViewState {
    }

    private LayoutInflater mInflater;

    private View mContentView;

    private View mLoadingView;

    private View mErrorView;

    private View mEmptyView;

    private boolean mAnimateViewChanges = false;

    @Nullable
    private StateListener mListener;

    @ViewState
    private int mViewState = VIEW_STATE_UNKNOWN;

    public MultiViewKit(Context context) {
        this(context, null);
    }

    public MultiViewKit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiViewKit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mInflater = LayoutInflater.from(getContext());
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiViewKit);

        //loading
        int loadingViewResId = a.getResourceId(R.styleable.MultiViewKit_loadingView, -1);
        if (loadingViewResId > -1) {
            mLoadingView = mInflater.inflate(loadingViewResId, this, false);
            addView(mLoadingView, mLoadingView.getLayoutParams());
        }

        //空页面
        int emptyViewResId = a.getResourceId(R.styleable.MultiViewKit_emptyView, -1);
        if (emptyViewResId > -1) {
            mEmptyView = mInflater.inflate(emptyViewResId, this, false);
            addView(mEmptyView, mEmptyView.getLayoutParams());
        }

        //错误页面
        int errorViewResId = a.getResourceId(R.styleable.MultiViewKit_errorView, -1);
        if (errorViewResId > -1) {
            mErrorView = mInflater.inflate(errorViewResId, this, false);
            addView(mErrorView, mErrorView.getLayoutParams());
        }

        //当前的视图
        int viewState = a.getInt(R.styleable.MultiViewKit_viewState, VIEW_STATE_CONTENT);
        //动画
        mAnimateViewChanges = a.getBoolean(R.styleable.MultiViewKit_animateViewChanges, false);

        switch (viewState) {
            case VIEW_STATE_CONTENT:
                mViewState = VIEW_STATE_CONTENT;
                break;

            case VIEW_STATE_ERROR:
                mViewState = VIEW_STATE_ERROR;
                break;

            case VIEW_STATE_EMPTY:
                mViewState = VIEW_STATE_EMPTY;
                break;

            case VIEW_STATE_LOADING:
                mViewState = VIEW_STATE_LOADING;
                break;

            case VIEW_STATE_UNKNOWN:
            default:
                mViewState = VIEW_STATE_UNKNOWN;
                break;
        }

        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null){
            throw new IllegalArgumentException("Content view is not defined");
        }
        setView(VIEW_STATE_UNKNOWN);
    }

    @Override
    public void addView(View child) {
        if (isValidContentView(child)){
            mContentView = child;
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (isValidContentView(child)){
            mContentView = child;
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)){
            mContentView = child;
        }
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)){
            mContentView = child;
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isValidContentView(child)){
            mContentView = child;
        }
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)){
            mContentView = child;
        }
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (isValidContentView(child)){
            mContentView = child;
        }
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    @Nullable
    public View getView(@ViewState int state) {
        switch (state) {
            case VIEW_STATE_LOADING:
                return mLoadingView;

            case VIEW_STATE_CONTENT:
                return mContentView;

            case VIEW_STATE_EMPTY:
                return mEmptyView;

            case VIEW_STATE_ERROR:
                return mErrorView;

            default:
                return null;
        }
    }

    @ViewState
    public int getViewState() {
        return mViewState;
    }


    /**
     * 设置当前要显示的页面
     * @param state 状态
     */
    public void setViewState(@ViewState int state) {
        if (state != mViewState) {
            int previous = mViewState;
            mViewState = state;
            setView(previous);
            if (mListener != null){
                mListener.onStateChanged(mViewState);
            }
        }
    }

    private void setView(@ViewState int previousState) {
        switch (mViewState) {
            case VIEW_STATE_LOADING:
                if (mLoadingView == null) {
                    throw new NullPointerException("Loading View");
                }

                if (mContentView != null)
                    mContentView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);

                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState));
                } else {
                    mLoadingView.setVisibility(View.VISIBLE);
                }
                break;

            case VIEW_STATE_EMPTY:
                if (mEmptyView == null) {
                    throw new NullPointerException("Empty View");
                }

                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                if (mContentView != null)
                    mContentView.setVisibility(View.GONE);

                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState));
                } else {
                    mEmptyView.setVisibility(View.VISIBLE);
                }
                break;

            case VIEW_STATE_ERROR:
                if (mErrorView == null) {
                    throw new NullPointerException("Error View");
                }

                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mContentView != null)
                    mContentView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);

                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState));
                } else {
                    mErrorView.setVisibility(View.VISIBLE);
                }
                break;

            case VIEW_STATE_CONTENT:
            default:
                if (mContentView == null) {
                    throw new NullPointerException("Content View");
                }
                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);

                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState));
                } else {
                    mContentView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }
        return view != mLoadingView && view != mErrorView && view != mEmptyView;
    }


    /**
     * 根据状态设置页面
     * @param view 页面
     * @param state 状态
     */
    public void setViewForState(View view, @ViewState int state) {
        setViewForState(view, state, false);
    }

    /**
     * 根据状态设置页面
     * @param layoutRes 布局资源ID
     * @param state 状态
     */
    public void setViewForState(@LayoutRes int layoutRes, @ViewState int state) {
        setViewForState(layoutRes, state, false);
    }

    /**
     * 根据状态设置页面
     * @param layoutRes 布局资源ID
     * @param state 状态
     * @param switchToState 是否切换到要显示的页面视图
     */
    public void setViewForState(@LayoutRes int layoutRes, @ViewState int state, boolean switchToState) {
        if (mInflater == null){
            mInflater = LayoutInflater.from(getContext());
        }
        View view = mInflater.inflate(layoutRes, this, false);
        setViewForState(view, state, switchToState);
    }

    /**
     * 根据状态设置页面
     * @param view 页面
     * @param state 状态
     * @param switchToState 是否切换到要显示的页面视图
     */
    public void setViewForState(View view, @ViewState int state, boolean switchToState) {
        switch (state) {
            case VIEW_STATE_LOADING:
                if (mLoadingView != null){
                    removeView(mLoadingView);
                }
                mLoadingView = view;
                addView(mLoadingView);
                break;

            case VIEW_STATE_EMPTY:
                if (mEmptyView != null){
                    removeView(mEmptyView);
                }
                mEmptyView = view;
                addView(mEmptyView);
                break;

            case VIEW_STATE_ERROR:
                if (mErrorView != null){
                    removeView(mErrorView);
                }
                mErrorView = view;
                addView(mErrorView);
                break;

            case VIEW_STATE_CONTENT:
                if (mContentView != null){
                    removeView(mContentView);
                }
                mContentView = view;
                addView(mContentView);
                break;
        }

        setView(VIEW_STATE_UNKNOWN);
        if (switchToState){
            setViewState(state);
        }
    }


    /**
     * 设置是否展示页面切换的动画
     * @param animate
     */
    public void setAnimateLayoutChanges(boolean animate) {
        mAnimateViewChanges = animate;
    }

    /**
     * 状态改变的监听
     * @param listener 回调接口
     */
    public void setStateListener(StateListener listener) {
        mListener = listener;
    }

    private void animateLayoutChange(@Nullable final View previousView) {
        if (previousView == null) {
            getView(mViewState).setVisibility(View.VISIBLE);
            return;
        }
        previousView.setVisibility(View.VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofFloat(previousView, "alpha", 1.0f, 0.0f).setDuration(250L);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                previousView.setVisibility(View.GONE);
                getView(mViewState).setVisibility(View.VISIBLE);
                ObjectAnimator.ofFloat(getView(mViewState), "alpha", 0.0f, 1.0f).setDuration(250L).start();
            }
        });
        anim.start();
    }

    public interface StateListener {
        void onStateChanged(@ViewState int viewState);
    }
}
