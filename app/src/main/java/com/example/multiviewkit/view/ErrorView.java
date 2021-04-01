package com.example.multiviewkit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.example.multiviewkit.R;
import com.google.gson.JsonParseException;
import org.json.JSONException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;
import javax.net.ssl.SSLHandshakeException;
import retrofit2.HttpException;


/**
 * Author: 信仰年轻
 * Date: 2021-04-01 11:38
 * Email: hydznsq@163.com
 * Des: 错误页面
 */
public class ErrorView extends LinearLayout {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

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

    /**
     * 根据retrofit提供的状态码设置错误信息
     * @param throwable
     */
    public void showError(Throwable throwable) {
        TextView tvMsg = findViewById(R.id.tv_msg);
        if (throwable instanceof HttpException){             //HTTP错误
            HttpException httpException = (HttpException) throwable;
            switch(httpException.code()){
                case NOT_FOUND:
                    //接口不存在
                    tvMsg.setText("接口不存在");
                    break;
                case INTERNAL_SERVER_ERROR:
                    //参数异常或服务器异常
                    tvMsg.setText("参数异常或服务器异常");
                    break;
                case UNAUTHORIZED:
                    //登录失效，请重新登录
                    tvMsg.setText("登录失效，请重新登录");
                    break;
                case FORBIDDEN:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    //服务器异常
                    tvMsg.setText("服务器异常");
                    break;
            }
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException){
            //数据格式异常
            tvMsg.setText("数据格式异常");
        }else if (throwable instanceof UnknownHostException){
            //请检查网络连接
            tvMsg.setText("请检查网络连接");
        }else if(throwable instanceof ConnectException){
            tvMsg.setText("链接失败");
        }else if (throwable instanceof SSLHandshakeException){
            //SSL证书无效
            tvMsg.setText("SSL证书无效");
        }else {
            //未知错误
            tvMsg.setText("未知错误");
        }
    }
}
