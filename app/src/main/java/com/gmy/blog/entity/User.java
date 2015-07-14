package com.gmy.blog.entity;

public class User {
	private int id;
	private String username;
	private String sex;
	private String photo;
	private String pwd; // null
	private String email;
	private String location;
	private String intro;
	private String hobby;
	private int collect_num; // null
	private String collect_id; // null
	private String birthday;
	private String regist_date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public int getCollect_num() {
		return collect_num;
	}
	public void setCollect_num(int collect_num) {
		this.collect_num = collect_num;
	}
	public String getCollect_id() {
		return collect_id;
	}
	public void setCollect_id(String collect_id) {
		this.collect_id = collect_id;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getRegist_date() {
		return regist_date;
	}
	public void setRegist_date(String regist_date) {
		this.regist_date = regist_date;
	}
	
}
