package com.gmy.blog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.gmy.blog.utils.Config;
import com.gmy.blog.utils.ImageUtil;
import com.gmy.blog.widget.CameraPreview;
import com.gmy.blog.widget.CameraPreview.OnCameraStatusListener;

/**
 * 自定义的拍照界面
 * 
 * @author GMY
 * 
 */
public class CameraActivity extends BaseActivity implements
		OnCameraStatusListener, OnClickListener {

	public static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

	CameraPreview mCameraPreview;// 拍照预览
	ImageView changeCamera, back, change_camera;
	ImageView take_pictureIv;

	private MediaPlayer mediaPlayer;
	private boolean playBeep;

	private boolean vibrate;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_camera);
	}

	@Override
	public void initView() {
		mCameraPreview = (CameraPreview) this
				.findViewById(R.id.camera_surfaceView);
		take_pictureIv = (ImageView) this.findViewById(R.id.take_pictureIv);
		back = (ImageView) this.findViewById(R.id.back);
		change_camera = (ImageView) this.findViewById(R.id.change_camera);
		take_pictureIv.setOnClickListener(this);
		back.setOnClickListener(this);
		change_camera.setOnClickListener(this);
		mCameraPreview.setOnCameraStatusListener(this);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	/**
	 * 存储图像并将信息添加入媒体数据库
	 */
	private Uri insertImage(ContentResolver cr, String name, long dateTaken,
			String directory, String filename, Bitmap source, byte[] jpegData) {
		OutputStream outputStream = null;
		String filePath = directory + filename;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 6;
			options.inDither = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inTempStorage = new byte[32 * 1024];
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			if (source == null && jpegData != null) {
				source = BitmapFactory.decodeByteArray(jpegData, 0,
						jpegData.length, options);
			}
			boolean isHeadCamera = false;
			if (mCameraPreview.cameraPosition == 0) {
				isHeadCamera = true;
			}
			Bitmap bMapRotate = ImageUtil.changeRoate(source, isHeadCamera);
			File dir = new File(directory);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(directory, filename);
			if (file.createNewFile()) {
				outputStream = new FileOutputStream(file);
				if (bMapRotate != null) {
					bMapRotate.compress(Bitmap.CompressFormat.JPEG, 90,
							outputStream);
					if (bMapRotate != null) {
						bMapRotate.recycle();
						bMapRotate = null;
					}
				}
				// else {
				// outputStream.write(jpegData);
				// }
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Throwable t) {
				}
			}
		}
		ContentValues values = new ContentValues(7);
		values.put(MediaStore.Images.Media.TITLE, name);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
		values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.Images.Media.DATA, filePath);
		return cr.insert(IMAGE_URI, values);
	}

	/**
	 * 保存拍照后图像
	 */
	private Uri saveCameraMix(Bitmap newBitmap) {
		Uri uri = null;
		// 系统时间
		long dateTaken = System.currentTimeMillis();
		// 图像名称
		String filename = "IMG_"
				+ DateFormat.format("yyyyMMddkkmmss", dateTaken).toString()
				+ ".jpg";
		// 存储图像（PATH目录）
		uri = insertImage(getContentResolver(), filename, dateTaken,
				Config.CAMREA_IMG_PATH, filename, newBitmap, null);
		return uri;
	}

	/**
	 * 初始化照相声音文件
	 */
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnCompletionListener(beepListener);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			setMediaResorce();
		}
	}

	private void setMediaResorce() {
		try {
			mediaPlayer.reset();
			Uri cameraUri = Uri
					.parse("file:///system/media/audio/ui/camera_click.ogg");
			mediaPlayer.setDataSource(CameraActivity.this, cameraUri);
			mediaPlayer.setVolume(Config.CAMERA_VIBRATE_DURATION,
					Config.CAMERA_BEEP_VOLUME);
			mediaPlayer.prepare();
		} catch (Exception e) {
			mediaPlayer = null;
		}
	}

	/**
	 * 播放声音
	 */
	private void playBeepSound() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		playVibrate();
	}

	private void playVibrate() {
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(Config.CAMERA_VIBRATE_DURATION);
		}
	}

	/**
	 * 监听声音播放完成
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onCameraStopped(byte[] data) {
		String picPath = null;
		try {
			// 创建图像
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inDither = false;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			newOpts.inTempStorage = new byte[100 * 1024];// 100k缓存
			newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
			// newOpts.inSampleSize=3;
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					newOpts);
			boolean isHeadCamera = false;
			if (mCameraPreview.cameraPosition == 0) {
				isHeadCamera = true;
			}
			Bitmap bitTemp = ImageUtil.changeRoate(bitmap, isHeadCamera);
			Uri uri = saveCameraMix(bitTemp);
			picPath = ImageUtil.getPicPathFromUri(uri, this);
			if (bitTemp != null)
				bitTemp.recycle();
		} catch (Exception e) {
		}
		Intent intent = getIntent();
		intent.putExtra("path", picPath);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	@Override
	public void onAutoFocus(boolean success) {
		playBeepSound();
	}

	@Override
	protected void onResume() {
		super.onResume();
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.change_camera:
			// 切换镜头
			mCameraPreview.changeCameraFacing();
			break;
		case R.id.take_pictureIv:
			mCameraPreview.takePicture();
			break;
		default:
			break;
		}
	}

}
