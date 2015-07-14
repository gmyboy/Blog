package com.gmy.blog.entity;

import java.util.List;

public class BlogBean {
	Blog blog;
	List<Comment> comments;

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public List<Comment> getList() {
		return comments;
	}

	public void setList(List<Comment> comments) {
		this.comments = comments;
	}

}
