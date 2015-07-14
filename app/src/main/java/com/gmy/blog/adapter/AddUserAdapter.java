package com.gmy.blog.adapter;

import java.util.List;

import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmy.blog.R;
import com.gmy.blog.entity.User;
import com.gmy.blog.entity.UserBean;
import com.gmy.blog.utils.CommentUtil;
import com.gmy.blog.utils.UserNetUtil;

public class AddUserAdapter extends BaseComAdapter<UserBean> {

	private String username;
	private List<UserBean> attentionlist;
	private List<UserBean> fanslist;

	public AddUserAdapter(Context context, List<UserBean> list,
			List<UserBean> attentionlist, List<UserBean> fanslist,
			String username) {
		super(list, context);
		this.username = username;
		this.attentionlist = attentionlist;
		this.fanslist = fanslist;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyHodler hodler = new MyHodler();
		User user_item = null;
		int status = 0;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_attention, null);
			hodler.author = (TextView) convertView
					.findViewById(R.id.att_author);
			hodler.avater = (ImageView) convertView.findViewById(R.id.att_img);
			hodler.intro = (TextView) convertView.findViewById(R.id.att_intro);
			hodler.btn = (ImageView) convertView.findViewById(R.id.att_btn);
			convertView.setTag(hodler);
		}else {
			hodler = (MyHodler) convertView.getTag();
		}
		user_item = list.get(position).getUser();
		hodler.author.setText(user_item.getUsername());
		hodler.intro.setText(user_item.getIntro());
		for (UserBean bean : attentionlist) { // 遍历关注列表
			if (user_item.getUsername().equals(bean.getUser().getUsername())) {
				status = 1;// 我的关注
				break;
			}
		}
		for (UserBean bean : fanslist) {// 遍历粉丝列表
			if (user_item.getUsername().equals(bean.getUser().getUsername())) {
				if (status == 1) {
					status = 3;// 互相关注
				} else {
					status = 2;// 我的粉丝
				}
				break;
			}
		}
		final String attentionname = user_item.getUsername();
		switch (status) {
		case 1:// 我关注的用户
			hodler.btn.setImageResource(R.drawable.searchuser_card_top_bg);
			hodler.btn.setOnClickListener(new OnClickListener() {
				String back;

				@Override
				public void onClick(View v) {
					new Thread() {
						public void run() {
							back = UserNetUtil.delattentionUser(username,
									attentionname);
							Looper.prepare();
							if (back.equals("0")) {
								CommentUtil.showToast(context, "已取消关注");
							} else if (back.equals("1")) {
								CommentUtil.showToast(context, "未关注");
							} else {
								CommentUtil.showToast(context, "取消关注失败");
							}
							Looper.loop();
						};
					}.start();
				}
			});
			break;
		case 2:// 我的粉丝（关注我的）
			hodler.btn.setImageResource(R.drawable.card_icon_addattention);
			hodler.btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					new Thread() {
						public void run() {
							Looper.prepare();
							String back = UserNetUtil.attentionUser(username,
									attentionname);
							if (back.equals("0")) {
								CommentUtil.showToast(context, "关注成功");
							} else if (back.equals("1")) {
								CommentUtil.showToast(context, "已关注过");
							} else {
								CommentUtil.showToast(context, "关注失败");
							}
							Looper.loop();
						};
					}.start();
				}
			});
			break;
		case 3:// 互相关注
			hodler.btn.setImageResource(R.drawable.eachattention_card_top_bg);
			hodler.btn.setOnClickListener(new OnClickListener() {
				String back;

				@Override
				public void onClick(View v) {
					new Thread() {
						public void run() {
							back = UserNetUtil.delattentionUser(username,
									attentionname);
							Looper.prepare();
							if (back.equals("0")) {
								CommentUtil.showToast(context, "已取消关注");
							} else if (back.equals("1")) {
								CommentUtil.showToast(context, "未关注");
							} else {
								CommentUtil.showToast(context, "取消关注失败");
							}
							Looper.loop();
						};
					}.start();
				}
			});
			break;
		default:
			hodler.btn.setImageResource(R.drawable.card_icon_addattention);
			hodler.btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					new Thread() {
						public void run() {
							Looper.prepare();
							String back = UserNetUtil.attentionUser(username,
									attentionname);
							if (back.equals("0")) {
								CommentUtil.showToast(context, "关注成功");
							} else if (back.equals("1")) {
								CommentUtil.showToast(context, "已关注过");
							} else {
								CommentUtil.showToast(context, "关注失败");
							}
							Looper.loop();
						};
					}.start();
				}
			});
			break;
		}

		return convertView;
	}

	class MyHodler {
		ImageView avater;
		TextView author;
		TextView intro;
		ImageView btn;
	}
}
