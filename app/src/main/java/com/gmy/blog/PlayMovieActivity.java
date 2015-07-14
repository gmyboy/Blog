package com.gmy.blog;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

public class PlayMovieActivity extends BaseActivity implements OnClickListener,
		Callback, OnBufferingUpdateListener, OnCompletionListener,
		OnPreparedListener, OnVideoSizeChangedListener {
	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;

	private static final String MEDIA = "media";
	private static final int LOCAL_AUDIO = 1;
	private static final int STREAM_AUDIO = 2;
	private static final int RESOURCES_AUDIO = 3;
	private static final int LOCAL_VIDEO = 4;
	private static final int STREAM_VIDEO = 5;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;

	private Button btn_play;
	private RelativeLayout video_view_bottom;
	private FrameLayout lay_main;
	private SeekBar skbProgress;
	private ProgressBar progressBar;
	private String url = "";
	private int t = 0;
	private Timer timer;
	private int recLen = 5;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 2) {
				video_view_bottom.setVisibility(View.GONE);
			}
		};
	};

	@Override
	public void setLayout() {
		// vitamio
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_movieplayer);
	}

	@Override
	public void initView() {
		mPreview = (SurfaceView) findViewById(R.id.tzt_surfceview);
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888);
		url = getIntent().getStringExtra("url");
		skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
		progressBar = (ProgressBar) this.findViewById(R.id.video_progress);
		video_view_bottom = (RelativeLayout) this
				.findViewById(R.id.video_view_bottom);
		lay_main = (FrameLayout) this.findViewById(R.id.lay_main);
		btn_play = (Button) this.findViewById(R.id.btnPlay);
		btn_play.setOnClickListener(this);
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lay_main.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				recLen = 5;
				video_view_bottom.setVisibility(View.VISIBLE);
				setTimmer();
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPlay:
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				btn_play.setBackgroundResource(R.drawable.videoplayer_icon_play_bg);
				t += 1;
			} else {
				recLen = 5;
				if (t == 0) {
					Log.i("gmyboy", url);
					playVideo(STREAM_VIDEO);
				} else {
					mMediaPlayer.start();
				}
				btn_play.setBackgroundResource(R.drawable.videoplayer_icon_stop_bg);
				setTimmer();
			}

			break;

		default:
			break;
		}
	}

	private void setTimmer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				recLen--;
				if (recLen < 0) {
					timer.cancel();
					handler.sendEmptyMessage(2);
					recLen = 5;
				}
			}
		}, 1000, 1000);
	}

	private void playVideo(Integer Media) {
		doCleanUp();
		try {

			switch (Media) {
			case LOCAL_VIDEO:
				/*
				 * TODO: Set the path variable to a local media file path.
				 */
				// path =
				// Environment.getExternalStorageDirectory().getAbsolutePath()
				// + "/1.mp4";
				// if (path == "") {
				// // Tell the user to provide a media file URL.
				// Toast.makeText(
				// MediaPlayerDemo_Video.this,
				// "Please edit MediaPlayerDemo_Video Activity, "
				// + "and set the path variable to your media file path."
				// + " Your media file must be stored on sdcard.",
				// Toast.LENGTH_LONG).show();
				// return;
				// }
				break;
			case STREAM_VIDEO:
				/*
				 * TODO: Set path variable to progressive streamable mp4 or 3gpp
				 * format URL. Http protocol should be used. Mediaplayer can
				 * only play "progressive streamable contents" which basically
				 * means: 1. the movie atom has to precede all the media data
				 * atoms. 2. The clip has to be reasonably interleaved.
				 */
				if (url.equals("")) {
					showToast("连接失效");
					return;
				}

				break;

			}

			// Create a new media player and set the listeners
			mMediaPlayer = new MediaPlayer(this);
			mMediaPlayer.setDataSource(url);
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepare();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			setVolumeControlStream(AudioManager.STREAM_MUSIC);

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
		}
	}

	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			this.progress = (int) (progress * mMediaPlayer.getDuration() / seekBar
					.getMax());
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			mMediaPlayer.seekTo(progress);

		}

	}

	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	/**
	 * 清除缓存
	 */
	private void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
		holder.setFixedSize(mVideoWidth, mVideoHeight);
		mMediaPlayer.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated called");
		playVideo(STREAM_VIDEO);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "surfaceChanged called");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surfaceDestroyed called");

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		Log.d(TAG, "onBufferingUpdate percent:" + percent);

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d(TAG, "onCompletion called");

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaPlayer();
		doCleanUp();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		doCleanUp();
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged called");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height
					+ ")");
			return;
		}
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}

	}

}