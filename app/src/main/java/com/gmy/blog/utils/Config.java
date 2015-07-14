package com.gmy.blog.utils;

import android.os.Environment;
import android.text.format.DateFormat;

public class Config {
	/** 别人的主页 */
	public static final int ME_OTHER = 1;
	/** 自己的主页 */
	public static final int ME_MAIN = 0;
	/** 应用的总路径 */
	public static final String APP_PATH = Environment
			.getExternalStorageDirectory().toString() + "/Blog";
	/** 数据库地址 */
	public static final String DB_HOME = APP_PATH + "/database/";
	/** 系统时间 */
	public static final String DATETAKEN = DateFormat.format("yyyyMMddkkmmss",
			System.currentTimeMillis()).toString();
	/** 拍照图片名称 */
	public static final String CAMERA_IMG_FILENAME = "IMG_" + DATETAKEN
			+ ".jpg";
	/** 拍照相关配置 */
	public static final float CAMERA_BEEP_VOLUME = 0.10f;
	/** 震动持续时间 */
	public static final long CAMERA_VIBRATE_DURATION = 200L;
	/** 拍完照骗本地保存路径 */
	public static final String CAMREA_IMG_PATH = APP_PATH + "/img/";
	/** 用户配置 */
	public static final String USER_SHAREDPREFERENCES = "share_user";
	/** 用户收藏微博总数 */
	public static final String USER_COLLECT = "collect";
	/** 用户关注的总数 */
	public static final String USER_ATTENTION = "attention";
	/** 用户粉丝总数 */
	public static final String USER_FANS = "share_user";
	/** 用户简介 */
	public static final String USER_INTRO = "introduce";
	/** 用户名 */
	public static final String USER_NAME = "username";
	/** 用户名 */
	public static final String USER_ME_TOKEN = "metoken";
	/** 正常 */
	public static final int NORMAL = 0;
	/** 下拉 */
	public static final int PULL_DOWN = 1;
	/** 上拉 */
	public static final int PULL_UP = 2;
	/** 没有更多数据了 */
	public static final int NO_MORE = 3;
	/** 没有网 */
	public static final int NO_NET = 4;
	/** 转发 */
	public static final int ACTION_BLOG_TWEET = 5;
	/** 评论 */
	public static final int ACTION_BLOG_COMMENT = 6;
	/** 收藏 */
	public static final int ACTION_BLOG_COLLECT = 7;
	/** 从网络加载标示 */
	public static final int FLAG_SERVER = 0;
	/** 从本地加载标示 */
	public static final int FLAG_FILE = 3;
	/** 图片文件标识 */
	public static final String FILE_IMG_TYPE = "img";
	/** 音频文件标识 */
	public static final String FILE_AUDIO_TYPE = "audio";
	/** 视频文件标识 */
	public static final String FILE_VEDIO_TYPE = "video";
	/** 返回响应设置 */
	public final static int DEFAULT_ERROR = -1;
	/** 分享到微信 */
	public static final int SHARE_WEIXIN = 1;
	/** 分享到新浪微博 */
	public static final int SHARE_SINA = 2;
	/** 分享到qq */
	public static final int SHARE_QQ = 3;
	/** 分享到qq空间 */
	public static final int SHARE_QZONE = 4;
	/** 微信 */
	public static final String PACKAGE_WEIXIN = "com.tencent.mm";
	/** 新浪微博 */
	public static final String PACKAGE_SINA = "com.sina.weibo";
	/** qq */
	public static final String PACKAGE_QQ = "tencent.mobileqq";
	/** qq空间 */
	public static final String PACKAGE_QZONE = "com.qzone";
	// http://192.168.191.6:8080/PocketBlog/regist.android_userdo?user_name=gmy&password=123123&email=sdsadasd
	public static String IP = "http://192.168.0.121:8088/Blog/";
	// public static String IP = "http://192.168.191.1:8080/Blog/";
	public static String URL_LOGIN = IP + "login.android_userdo";// 用户登录
	public static String URL_CHECKLOGIN = IP + "checkUsername.android_userdo";// 检查用户名是否存在
	public static String URL_REGIST = IP + "regist.android_userdo";// 注册
	public static String URL_GETUSERINFO = IP + "getUserBean.android_beando";// 获取用户详细信息
	public static String URL_PERFECTUSERINFO = IP
			+ "perfectUser.android_userdo";// 完善用户信息
	public static String URL_CHANGEPWD = IP + "changePwd.android_userdo";// 改密码
	public static String URL_ATTENTION = IP + "attention.android_userdo";// 关注某人
	public static String URL_DELATTENTION = IP
			+ "deleteAttention.android_userdo";// 取消关注某人
	public static String URL_SENDBLOG = IP + "sendBlog.android_blogdo";// 发送微博
	public static String URL_FORWARDBLOG = IP + "forwardBlog.android_blogdo";// 转发微博
	public static String URL_COMMENT = IP + "comment.android_blogdo";// 评论微博
	public static String URL_SUPPORTBLOG = IP + "supportBlog.android_blogdo";// 赞微博
	public static String URL_SUPPORTCOMMENT = IP
			+ "supportComment.android_blogdo";// 赞评论
	public static String URL_COLLECTBLOG = IP + "collect.android_userdo";// 收藏微博
	public static String URL_DELCOLLECTBLOG = IP
			+ "deleteCollect.android_userdo";// 取消收藏微博
	public static String URL_GETCOLLECTNUM = IP
			+ "getCollectNum.android_beando";// 获取收藏微博总数
	public static String URL_MEBLOG = IP + "getBlogBean.android_beando";// 我的微博
	public static String URL_GETFRIENDBLOG = IP + "showBlogBean.android_beando"; // 获取好友微博
	public static String URL_GETHOTBLOG = IP + "showHotBlogBean.android_beando"; // 获取热门微博
	public static String URL_GETATTENTIONUSER = IP
			+ "getAttentionUserBean.android_beando"; // 获取关注的好友列表
	public static String URL_GETFANUSER = IP + "getFansUserBean.android_beando"; // 获粉丝列表
	public static String URL_GETCOLLECTBLOG = IP
			+ "showCollectBlogBean.android_beando"; // 获取收藏微博列表
	public static String URL_SEARCHUSERBEAN = IP
			+ "searchUserBean.android_beando"; // 检索用户列表列表
	public static String URL_CHANGEAVATOR = IP + "servlet/ChangeAvator"; // 更改用户头像
	public static String URL_GETBLOGBYID = IP
			+ "getBlogBeanById.android_beando";// 通过id获取blog 所有信息
	public static String URL_GETTOPIC = IP + "getTopics.android_beando";// 通过id获取blog
																		// 所有信息
}
