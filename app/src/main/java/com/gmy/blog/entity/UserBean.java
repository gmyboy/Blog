package com.gmy.blog.entity;

public class UserBean {
	private User user;
	private int fans;
	private int attention;
	private BlogBean blogbean; // null
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getFans() {
		return fans;
	}
	public void setFans(int fans) {
		this.fans = fans;
	}
	public int getAttention() {
		return attention;
	}
	public void setAttention(int attention) {
		this.attention = attention;
	}
	public BlogBean getBlogbean() {
		return blogbean;
	}
	public void setBlogbean(BlogBean blogbean) {
		this.blogbean = blogbean;
	}
	
}
