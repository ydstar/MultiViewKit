# MultiViewKit


## YdKit通用组件库
YdKit 是一组功能丰富的 Android 通用组件。

* [LogKit](https://github.com/ydstar/LogKit) — 轻量级的 Android 日志系统。
* [RestfulKit](https://github.com/ydstar/RestfulKit) — 简洁但不简单的 Android 网络组件库。
* [StorageKit](https://github.com/ydstar/StorageKit) — 高性能 Android 离线缓存框架。
* [ExecutorKit](https://github.com/ydstar/ExecutorKit) — 简洁易用的 Android 多线程操作框架。
* [CrashKit](https://github.com/ydstar/CrashKit) — 简洁易用的 Android Crash日志捕捉组件。
* [PermissionKit](https://github.com/ydstar/PermissionKit) — 简洁易用的 Android 权限请求组件。
* [NavigationBarKit](https://github.com/ydstar/NavigationBarKit) — 简洁易用的 Android 顶部导航条组件。
* [RefreshKit](https://github.com/ydstar/RefreshKit) — 简洁易用的 Android 下拉刷新和上拉加载组件。
* [AdapterKit](https://github.com/ydstar/AdapterKit) — 简洁易用的 Android 列表组件。
* [BannerKit](https://github.com/ydstar/BannerKit) — 简洁易用的 Android 无限轮播图组件。
* [MultiViewKit](https://github.com/ydstar/MultiViewKit) — 简洁易用的 Android 多视图组件。
* [TabBottomKit](https://github.com/ydstar/TabBottomKit) — 简洁易用的 Android 底部导航组件。

## 效果预览

<img src="https://github.com/ydstar/MultiViewKit/blob/master/preview/show.gif" alt="动图演示效果" width="250px">

页面的多视图组件,支持自定义页面

## 导入方式

仅支持`AndroidX`
```
dependencies {
     implementation 'com.android.ydkit:multiview-kit:1.0.0'
}
```

项目根目录build.gradle
```
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url = "https://dl.bintray.com/ydswifty/MultiViewKitRepo"
        }
    }
}
```

## 使用方法
#### 1.在布局中添加MultiViewKit
```java
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.multiview.kit.MultiViewKit
        android:id="@+id/multiViewKit"
        android:layout_width="match_parent"
        android:background="#eeeeee"
        android:layout_height="match_parent">

        <ListView
            android:id="@+id/list"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            tools:listitem="@android:layout/simple_list_item_1" />
    </com.multiview.kit.MultiViewKit>

</androidx.constraintlayout.widget.ConstraintLayout>

```

#### 2.创建加载页面-空页面-错误页面,

加载页面
```java
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
```

空页面
```java
public class EmptyView extends LinearLayout {

    public EmptyView(Context context) {
        this(context,null);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_state_empty, this, true);
        this.setOrientation(LinearLayout.VERTICAL);
    }
}
```
错误页面
```java
public class ErrorView extends LinearLayout {

    public ErrorView(Context context) {
        this(context,null);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_state_error, this, true);
    }
}

```

#### 3.把页面添加multiViewKit中,并设置其状态
```
//loading
LoadingView loadingView = new LoadingView(context);
multiViewKit.setViewForState(loadingView, VIEW_STATE_LOADING);

//空页面
EmptyView emptyView = new EmptyView(context);
multiViewKit.setViewForState(emptyView, MultiViewKit.VIEW_STATE_EMPTY);

//错误页面
mErrorView = new ErrorView(context);
multiViewKit.setViewForState(mErrorView, VIEW_STATE_ERROR);

//展示加载页
multiViewKit.setViewState(VIEW_STATE_LOADING);
```

#### 4.根据状态设置对应的页面
```
multiViewKit.setViewState(VIEW_STATE_CONTENT);
```




## License
```text
Copyright [2021] [ydStar]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
