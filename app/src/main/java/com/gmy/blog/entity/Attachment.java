package com.gmy.blog.entity;

import com.gmy.blog.utils.Config;

/*
 * <Auto Created by fd.CreateTable> 不得擅自修改！
 */
/**
 * 上传附件信息表
 */
public class Attachment {

	private String id; // 附件序号
	private String comm_date; // 提交日期
	private String comm_user;// 上传人姓名
	private String file_path; // 文件存放路径
	private String filename; // 自定义文件名
	private String filetype;// 图片为img,音频为 audio
	private String filesize; // 文件大小
	private String url;
	private int flag = Config.FLAG_SERVER; // 标示，从哪获取资源,默认为0

	public String getUrl() {
		return url;
	}

	public String getComm_user() {
		return comm_user;
	}

	public void setComm_user(String comm_user) {
		this.comm_user = comm_user;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getComm_date() {
		return comm_date;
	}

	public void setComm_date(String comm_date) {
		this.comm_date = comm_date;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
