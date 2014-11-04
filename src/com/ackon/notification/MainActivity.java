package com.ackon.notification;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

public class MainActivity extends ActionBarActivity {

	/**
	 * 노티 화면 띄우기
	 * 
	 * @param context
	 * @param message
	 * @param link
	 */
	public static final void showNotification(Context context, String message, String link) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

		intent.putExtra(AckonManager.KEY_NOTI, message);
		intent.putExtra(AckonManager.KEY_LINK, link);

		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();

		// 꺼진 화면 화면 켜기
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.activity_main);

		final String link = getIntent().getStringExtra(AckonManager.KEY_LINK);
		final String message = getIntent().getStringExtra(AckonManager.KEY_NOTI);

		// 노티 알림 메시지가 있을 경우 다이얼로그 출력
		if (link != null && message != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_name);
			builder.setMessage(message);

			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// 캔슬할 경우 액티비티 종료
					finish();
				}
			});

			// 확인할 경우 웹 사이트로 이동
			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
					startActivity(intent);
				}
			});

			builder.show();

			return;
		}

	}
}
