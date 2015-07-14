package com.gmy.blog.services;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.gmy.blog.db.DBManager;
import com.gmy.blog.db.SQLiteTemplate;
import com.gmy.blog.db.SQLiteTemplate.RowMapper;
import com.gmy.blog.entity.Blog;
import com.gmy.blog.entity.BlogBean;
import com.gmy.blog.entity.Comment;

/**
 * 处理question表的services
 * 
 * @author GMY
 *
 */
public class BlogListServices {
	private static DBManager manager = null;
	private static BlogListServices services = null;
	private String table_blog = "blog";
	private String table_comment = "comment";

	public BlogListServices(Context context) {
		super();
		manager = DBManager.getInstance(context, "database");
	}

	public static BlogListServices getInstance(Context context) {
		if (services == null) {
			services = new BlogListServices(context);
		}
		return services;
	}

	/**
	 * 保存问题数据
	 * 
	 * @param lstQuestion
	 * @return
	 */
	public boolean savaData(List<BlogBean> lstQuestion) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = null;
		Blog blog = null;
		List<Comment> comments = null;
		if (lstQuestion == null || lstQuestion.size() == 0) {
			return false;
		} else {

			for (int i = 0; i < lstQuestion.size(); i++) {

				// 根据数据存不存在判断是否插入
				if (!st.isExistsById(table_blog,
						String.valueOf(lstQuestion.get(i).getBlog().getId()))) {
					contentValues = new ContentValues();
					blog = lstQuestion.get(i).getBlog();
					contentValues.put("id", blog.getId());
					contentValues.put("content", blog.getContent());
					contentValues.put("picture", blog.getPicture());
					contentValues.put("music", blog.getMusic());
					contentValues.put("movie", blog.getMovie());
					contentValues.put("user_name", blog.getUser_name());
					contentValues.put("blog_date", blog.getBlog_date());
					contentValues.put("support", blog.getSupport());
					contentValues.put("forward_id", blog.getForward_id());
					contentValues.put("comefrom", blog.getComefrom());
					st.insert(table_blog, contentValues);
					contentValues = null;
					blog = null;
				}
				// db.execSQL("CREATE TABLE [comment] ([id] INTEGER NOT NULL
				// PRIMARY KEY AUTOINCREMENT, [content] NVARCHAR,
				// [user_name] NVARCHAR, [blog_id] INTEGER, [support] INTEGER,
				// [comment_date] NVARCHAR);");
				comments = lstQuestion.get(i).getList();
				for (Comment comment : comments) {
					// 根据数据存不存在判断是否插入
					if (!st.isExistsById(table_comment,
							String.valueOf(comment.getId()))) {
						contentValues = new ContentValues();

						contentValues.put("id", comment.getId());
						contentValues.put("content", comment.getContent());
						contentValues.put("user_name", comment.getUser_name());
						contentValues.put("blog_id", comment.getBlog_id());
						contentValues.put("support", comment.getSupport());
						contentValues.put("comment_date",
								comment.getComment_date());
						st.insert(table_comment, contentValues);
						contentValues = null;
						comment = null;
					}
				}

			}
			return true;
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param type
	 * 
	 * @param pageNum
	 *            第几页
	 * @param pageSize
	 *            要查询记录的条数
	 * @return
	 */
	public List<BlogBean> getList(int pageNum, int pageSize) {

		int fromIndex = (pageNum - 1) * pageSize;
		final SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<BlogBean> list = st.queryForList(new RowMapper<BlogBean>() {

			@Override
			public BlogBean mapRow(Cursor cursor, int index) {
				BlogBean question = new BlogBean();
				Blog blog = new Blog();

				blog.setId(Integer.parseInt(cursor.getString(cursor
						.getColumnIndex("id"))));
				blog.setContent(cursor.getString(cursor
						.getColumnIndex("content")));
				blog.setPicture(cursor.getString(cursor
						.getColumnIndex("picture")));
				blog.setMusic(cursor.getString(cursor.getColumnIndex("music")));
				blog.setMovie(cursor.getString(cursor.getColumnIndex("movie")));
				blog.setUser_name(cursor.getString(cursor
						.getColumnIndex("user_name")));
				blog.setBlog_date(cursor.getString(cursor
						.getColumnIndex("blog_date")));
				blog.setSupport(Integer.parseInt(cursor.getString(cursor
						.getColumnIndex("support"))));
				blog.setForward_id(Integer.parseInt(cursor.getString(cursor
						.getColumnIndex("forward_id"))));
				blog.setComefrom(cursor.getString(cursor
						.getColumnIndex("comefrom")));
				List<Comment> comments = st.queryForList(
						new RowMapper<Comment>() {

							@Override
							public Comment mapRow(Cursor cursor, int index) {
								Comment comment = new Comment();
								comment.setId(Integer.parseInt(cursor
										.getString(cursor.getColumnIndex("id"))));
								comment.setContent(cursor.getString(cursor
										.getColumnIndex("content")));
								comment.setUser_name(cursor.getString(cursor
										.getColumnIndex("user_name")));
								comment.setSupport(Integer.parseInt(cursor
										.getString(cursor
												.getColumnIndex("support"))));
								comment.setComment_date(cursor.getString(cursor
										.getColumnIndex("comment_date")));
								return comment;
							}

						}, "select * from comment where id = ? order by id",
						new String[] { String.valueOf(blog.getId()) });
				question.setBlog(blog);
				question.setList(comments);
				return question;
			}
		}, "select * from blog  order by id desc limit ? , ? ", new String[] {
				"" + fromIndex, "" + pageSize });
		return list;
	}

}
