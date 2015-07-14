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
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.gmy.blog.entity.Attachment;
import com.gmy.blog.entity.BlogBean;
import com.gmy.blog.entity.Topic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BlogNetUtil {
	/**
	 * 获取热门微博
	 * 
	 * @param url
	 * @param page
	 * @return
	 */
	public static List<BlogBean> getHotBlogs(String url, String page) {
		final HttpClient httpClient = new DefaultHttpClient();
		List<BlogBean> blogBeans;
		Gson gson = new Gson();
		try {
			HttpPost post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("page", page));// 密码
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String json = EntityUtils.toString(response.getEntity());
				blogBeans = gson.fromJson(json,
						new TypeToken<List<BlogBean>>() {
						}.getType());
				return blogBeans;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取博客的json数据
	 * 
	 * @param url
	 * @return
	 */
	public static List<BlogBean> getBlogs(String url, String username,
			String page) {
		HttpClient httpClient = new DefaultHttpClient();
		List<BlogBean> blogBeans;
		Gson gson = new Gson();
		try {
			HttpPost post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			params.add(new BasicNameValuePair("page", page));// 密码
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String json = EntityUtils.toString(response.getEntity());
				blogBeans = gson.fromJson(json,
						new TypeToken<List<BlogBean>>() {
						}.getType());
				List<BlogBean> reblogBeans = new ArrayList<BlogBean>();
				for (int i = blogBeans.size() - 1; i >= 0; i--) {
					reblogBeans.add(blogBeans.get(i));
				}
				return reblogBeans;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 通过id 获取某条微博详细信息
	 *
	 * @param url
	 * @param id
	 *            blogid
	 * @return
	 */
	public static BlogBean getBlogByID(String url, String id) {
		HttpClient httpClient = new DefaultHttpClient();
		BlogBean blogBeans = new BlogBean();
		Gson gson = new Gson();
		try {
			HttpPost post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("blog_id", id));// 用户名
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String json = EntityUtils.toString(response.getEntity());
				blogBeans = gson.fromJson(json, new TypeToken<BlogBean>() {
				}.getType());
				return blogBeans;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 发送博客
	 * 
	 * @param model
	 * @param location
	 * 
	 * @param url
	 * @return
	 */
	public static String sendBlogs(String username, String content,
			String model, String location, List<Attachment> files) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpPost post = new HttpPost(Config.URL_SENDBLOG);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("user_name",
					new StringBody(username, Charset.forName("utf-8")));
			reqEntity.addPart("content",
					new StringBody(content, Charset.forName("utf-8")));
			reqEntity.addPart("comfrom",
					new StringBody(model, Charset.forName("utf-8")));
			reqEntity.addPart("location",
					new StringBody(location, Charset.forName("utf-8")));
			for (final Attachment att : files) {
				if (att != null && !att.getFile_path().equals("")) {
					ContentBody cb = new FileBody(new File(att.getFile_path()));
					reqEntity.addPart(att.getFiletype(), cb);
				}
			}
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
	 * 赞微博
	 * 
	 * @param url
	 * @return
	 */
	public static String supportBlogs(int id) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(Config.URL_SUPPORTBLOG);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("blog_id", id + ""));// id
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
	 * 转发微博
	 * 
	 * @param url
	 * @return
	 */
	public static String forwardBlogs(String username, int id) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(Config.URL_FORWARDBLOG);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			params.add(new BasicNameValuePair("blog_id", id + ""));// id
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
	 * 收藏微博
	 * 
	 * @param url
	 * @return
	 */
	public static String collectionBlogs(String username, int id) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(Config.URL_COLLECTBLOG);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			params.add(new BasicNameValuePair("blog_id", id + ""));// id
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
	 * 取消收藏微博
	 * 
	 * @param url
	 * @return
	 */
	public static String delCollectionBlogs(String username, int id) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(Config.URL_DELCOLLECTBLOG);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			params.add(new BasicNameValuePair("blog_id", id + ""));// id
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
	 * 发送博客评论
	 * 
	 * @param url
	 * @return
	 */
	public static String commendBlogs(String username, int id, String content) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(Config.URL_COMMENT);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name", username));// 用户名
			params.add(new BasicNameValuePair("blog_id", id + ""));// 密码
			params.add(new BasicNameValuePair("content", content));// 密码
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
	 * 获取热门话题
	 * 
	 * @param url
	 * @param page
	 * @return
	 */
	public static List<Topic> getTopics(String url) {
		final HttpClient httpClient = new DefaultHttpClient();
		List<Topic> topics;
		Gson gson = new Gson();
		try {
			HttpPost post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String json = EntityUtils.toString(response.getEntity());
				topics = gson.fromJson(json, new TypeToken<List<Topic>>() {
				}.getType());
				return topics;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
