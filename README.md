# multistateview
----
### 一、简介
多状态视图,支持加载中,加载成功,加载失败,空数据等页面,支持自定义页面

动图演示如下：

<img src="https://github.com/ydstar/multistateview/blob/master/preview/show.gif" alt="动图演示效果" width="250px">

### 二、引入
Step 1. Add it in your root build.gradle at the end of repositories：

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.ydstar:multistateview:1.0.0'
	}
