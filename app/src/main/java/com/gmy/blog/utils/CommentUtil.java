package com.gmy.blog.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

public class CommentUtil {
	/**
	 * 将语音文件传位字节数组
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] getAudioContent(String filePath) throws IOException {
		File file = new File(filePath);

		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			System.out.println("file too big...");
			return null;
		}
		FileInputStream fi = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length
				&& (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		fi.close();
		return buffer;
	}
	/**
	 * 判断网络是否可用
	 * 
	 * @param NetworkUtil
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * 判断连接是否是视频连接
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isMovie(String url) {
		if (url.contains("/videos/")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isPic(String url) {
		if (url.contains("/imgs/")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isAudio(String url) {
		if (url.contains("/audios/")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isGif(String url) {
		if (url.contains("/gifs/")) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 访问官网
	 */
	public static void goToWeb(Context context, String url) {
		Uri uriUrl = Uri.parse(url);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		context.startActivity(launchBrowser);
	}

	public static void showToast(Context context, String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();

	}
}
