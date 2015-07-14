package com.gmy.blog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * SQLite数据库的帮助类
 * 
 * 该类属于扩展类,主要承担数据库初始化和版本升级使用,其他核心全由核心父类完成
 * 
 * @author shimiso
 * 
 */
public class DataBaseHelper extends SDCardSQLiteOpenHelper {

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// blog
		db.execSQL("CREATE TABLE [blog] ([id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, [content] NVARCHAR, [picture] NVARCHAR, [music] NVARCHAR, [movie] NVARCHAR, [user_name] NVARCHAR, [blog_date] NVARCHAR, [support] INTEGER, [forward_id] INTEGER, [comefrom] INTEGER);");
		// comments
		db.execSQL("CREATE TABLE [comment]  ([id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [content] NVARCHAR, [user_name] NVARCHAR, [blog_id] INTEGER, [support] INTEGER, [comment_date] NVARCHAR);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
