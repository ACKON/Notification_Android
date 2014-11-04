package com.ackon.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;

import com.olivestory.ackon.Ackon;
import com.olivestory.ackon.AckonDataManager;
import com.olivestory.ackon.AckonInfo;
import com.olivestory.ackon.AckonListener;
import com.olivestory.ackon.AckonService;
import com.olivestory.ackon.AckonUpdateListener;

/**
 * AckonListener 인터페이스를 구현한 메니저 클래스
 * 
 * @author android
 * 
 */
public class AckonManager implements AckonListener {

	private static final int NOTIFY_ID = 1000;

	public static final String KEY_NOTI = "noti";
	public static final String KEY_LINK = "link";

	private final NotificationManager notificationManager;
	private final Context context;

	/**
	 * 생성자
	 * 
	 * @param context
	 */
	public AckonManager(final Context context) {
		this.context = context;
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		// 리스너 등록
		AckonDataManager.setAckonListener(this);

		// CMS 데이터 갱신 주기 설정, 120초 마다 데이터를 자동 갱신한다, 단위는 ms
		AckonDataManager.setUpdateTime(120 * 1000);

		// CMS 데이터 갱신
		AckonDataManager.update(context, new AckonUpdateListener() {

			@Override
			public void onUpdateResult(boolean result) {
				// 데이터 갱신 완료 후 서비스 시작
				if (result)
					AckonService.startService(context);
			}
		});
	}

	/**
	 * Ackon이 발견되면 onAckonEnter 메소드가 호출됩니다.노티피케이션 알림을 띄우는 것이 목적일 경우 onAckonEnter를 사용하는 것이 가장 간단한 구현 방법입니다.
	 */
	@Override
	public void onAckonEnter(Ackon ackon) {
		// 진입할 때 노티를 띄운다.
		if (ackon != null) {

			// Ackon과 연결된 CMS 데이터 가져오기
			AckonInfo info = ackon.getAckonInfo();
			if (info != null) {

				// KEY_NOTI, KEY_LINK 데이터가 있는 Ackon만 노티피케이션을 띄웁니다.
				notification(info.getValue(KEY_NOTI), info.getValue(KEY_LINK));
			}
		}
	}

	/**
	 * onAckonUpdate가 더이상 호출되지 않는 경우 onAckonExit가 호출됩니다.
	 */
	@Override
	public void onAckonExit(Ackon arg0) {
	}

	/**
	 * onAckonEnter, onAckonUpdate, onAckonExit 호출 할 때 같이 호출됩니다.
	 * <p>
	 * 호출순서<br>
	 * onAckonEnter > onAckonResult<br>
	 * onAckonUpdate > onAckonResult<br>
	 * onAckonExit > onAckonResult<br>
	 */
	@Override
	public void onAckonResult(int arg0, Ackon arg1) {
	}

	/**
	 * onAckonEnter 호출 후 onAckonUpdate가 호출됩니다.
	 */
	@Override
	public void onAckonUpdate(Ackon arg0) {
	}

	/**
	 * 노티 메시지 출력과 액티비티 띄우기
	 * 
	 * @param message
	 * @param link
	 */
	private void notification(String message, String link) {
		// message와 link가 null이 아닐 경우 동작.
		if (message != null && message.length() > 0 && link != null && link.length() > 0
				&& Patterns.WEB_URL.matcher(link).matches()) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFY_ID, intent,
					PendingIntent.FLAG_ONE_SHOT);

			Notification.Builder builder = new Notification.Builder(context);
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setTicker(message);
			builder.setContentTitle(message);
			builder.setContentText(link);

			// 진동 패턴
			builder.setVibrate(new long[] { 500, 400, 250, 400 });

			builder.setContentIntent(pendingIntent);

			notificationManager.notify(NOTIFY_ID, builder.build());

			MainActivity.showNotification(context, message, link);
		}
	}

}
