package com.gmy.blog.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/28.
 */
public class NetworkUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    private static final String IP = "192.168.0.121";
    private static final String MAINURL = "http://" + IP + "/blog/home";

    public static <T> String post(String url, Map<String, T> map) throws IOException {
        String json = put(map);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(MAINURL + url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Log.d("PARAM","   url:   "+MAINURL + url+"\n"+"   return:   "+response.body().string());
        return response.body().string();
    }

    /**
     * 将Map转化为Json
     *
     * @param map
     * @return String
     */
    public static <T> String put(Map<String, T> map) {
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }
}
