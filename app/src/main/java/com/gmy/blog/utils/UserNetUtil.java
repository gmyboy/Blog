package com.gmy.blog.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.gmy.blog.entity.UserBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

public class UserNetUtil {
	OkHttpClient client = new OkHttpClient();
	/**
	 * 获取热门用户列表
	 * 
	 * @param url
	 * @param username
	 * @return
	 */
	public static List<UserBean> getsearchUserBean(String url, String username) {
		final HttpClient httpClient = new DefaultHttpClient();
		List<UserBean> userBeans;
		Gson gson = new Gson();
		try {
			HttpPost post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("keyword", username));// 用户名
			params.add(new BasicNameValuePair("page", "1"));// 用户名
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String json = EntityUtils.toString(response.getEntity());
				userBeans = gson.fromJson(json, new TypeToken<List<UserBean>>() {
				}.getType());
				return userBeans;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取收藏微博总数
	 * 
	 * @param url
	 * @param username
	 * @return
	 */
	public static String getCollectNum(String url, String username) {
		final HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String msg = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(msg);
				String data = jsonObject.getString("num");
				return data;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取关注好友列表
	 * 
	 * @param url
	 * @param username
	 * @return
	 */
	public static List<UserBean> getAttentionUser(String url, String username) {
		final HttpClient httpClient = new DefaultHttpClient();
		List<UserBean> userBeans;
		Gson gson = new Gson();
		try {
			HttpPost post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String json = EntityUtils.toString(response.getEntity());
				userBeans = gson.fromJson(json, new TypeToken<List<UserBean>>() {
				}.getType());
				return userBeans;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 修改密码
	 */
	public static String changePassword(String username, String pwd, String newPwd) {
		final HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(Config.URL_CHANGEPWD);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名

			params.add(new BasicNameValuePair("pwd", pwd));
			params.add(new BasicNameValuePair("newPwd", newPwd));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String msg = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(msg);
				String data = jsonObject.getString("n");
				return data;
			} else {
				return "2";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}

	/**
	 * 
	 * @param username
	 * @param content
	 * @param path
	 * @return
	 */
	public static String changeAvator(String username, String avator) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(Config.URL_CHANGEAVATOR);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("user_name", new StringBody(username, Charset.forName("utf-8")));
			reqEntity.addPart("pic", new FileBody(new File(avator)));
			post.setEntity(reqEntity);
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String msg = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(msg);
				String data = jsonObject.getString("n");
				return data;
			} else {
				return "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}
	}

	/**
	 * 完善用户信息
	 * 
	 * @param username
	 * @param sex
	 * @param location
	 * @param intro
	 * @param hobby
	 * @param birthday
	 * @return
	 */
	public static String perfectUserInfo(String username, String sex, String location,
			String intro, String hobby, String birthday) {
		final HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(Config.URL_PERFECTUSERINFO);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			params.add(new BasicNameValuePair("sex", sex));
			params.add(new BasicNameValuePair("location", location));
			params.add(new BasicNameValuePair("intro", intro));
			params.add(new BasicNameValuePair("hobby", hobby));
			params.add(new BasicNameValuePair("birthday", birthday));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String msg = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(msg);
				String data = jsonObject.getString("n");
				return data;
			} else {
				return "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}
	}

	/**
	 * 获取用户详细信息
	 * 
	 * @param url
	 * @param username
	 * @param page
	 * @return
	 */
	public static UserBean getUserInfo(String url, String username) {
		final HttpClient httpClient = new DefaultHttpClient();
		UserBean userBeans;
		Gson gson = new Gson();
		try {
			HttpPost post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String json = EntityUtils.toString(response.getEntity());
				userBeans = gson.fromJson(json, UserBean.class);
				return userBeans;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 登录
	 * 
	 * @param str2
	 *            username
	 * @param str3
	 *            password
	 * @return
	 */
	public static String Login(String str2, String str3) {
		final HttpClient httpClient = new DefaultHttpClient();
		final String username = str2;
		final String password = str3;
		try {
			HttpPost post = new HttpPost(Config.URL_LOGIN);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			params.add(new BasicNameValuePair("pwd", password));// 密码
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String msg = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(msg);
				String data = jsonObject.getString("n");
				// 返回json message
				return data;
			} else {
				return "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}
	}

	/**
	 * 后台检查用户名是否存在
	 * 
	 * @param str2
	 *            username
	 * @param str3
	 *            password
	 * @return
	 */
	public static String checkName(String str2) {
		final HttpClient httpClient = new DefaultHttpClient();
		final String username = str2;
		try {
			HttpPost post = new HttpPost(Config.URL_CHECKLOGIN);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String msg = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(msg);
				String data = jsonObject.getString("n");
				// 返回json message
				return data;
			} else {
				return "2";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}

	/**
	 * 注册帐号
	 * 
	 * @param str2
	 *            用户名
	 * @param str3
	 *            密码
	 * @param str4
	 *            邮箱
	 * @return
	 */
	public static String register(String str2, String str3, String str4) {
		final String username = str2;
		final String password = str3;
		final String email = str4;
		final HttpClient httpClient = new DefaultHttpClient();
		try {

			HttpPost post = new HttpPost(Config.URL_REGIST);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));
			params.add(new BasicNameValuePair("pwd", password));
			params.add(new BasicNameValuePair("email", email));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String msg = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(msg);
				String message = jsonObject.getString("n");
				// 返回json message
				return message;
			} else {
				return "注册失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "注册失败";
		}
	}

	/**
	 * 关注某人
	 * 
	 * @param 我的用户名
	 * @param 我关注的人的姓名
	 */

	public static String attentionUser(String username, String attentionname) {
		final HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(Config.URL_ATTENTION);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			params.add(new BasicNameValuePair("attention_name", attentionname));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String msg = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(msg);
				String data = jsonObject.getString("n");
				return data;
			} else {
				return "2";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}

	/**
	 * 取消关注某人
	 * 
	 * @param 我的用户名
	 * @param 我关注的人的姓名
	 */

	public static String delattentionUser(String username, String attentionname) {
		final HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(Config.URL_DELATTENTION);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			params.add(new BasicNameValuePair("attention_name", attentionname));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String msg = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(msg);
				String data = jsonObject.getString("n");
				return data;
			} else {
				return "2";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}
}
