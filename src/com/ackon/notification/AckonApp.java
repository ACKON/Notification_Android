package com.ackon.notification;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;

/**
 * 어플리케이션 클래스 <br>
 * AndroidManifest.xml의 동록해야 합니다.
 * 
 * @author android
 * 
 */
public class AckonApp extends Application {

	private AckonManager ackonManager;

	/**
	 * 앱이 실행 될 때 가장 먼저 호출 된다.
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		// 블루투스 강제로 켜기
		if (BluetoothAdapter.getDefaultAdapter().isEnabled() == false) {
			BluetoothAdapter.getDefaultAdapter().enable();
		}

		// 앱이 실행 되면 매니저를 동작 시킨다.
		ackonManager = new AckonManager(getApplicationContext());

	}

	public AckonManager getAckonManager() {
		return ackonManager;
	}

}
