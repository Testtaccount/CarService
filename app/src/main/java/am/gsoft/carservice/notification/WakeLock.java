package am.gsoft.carservice.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

public class WakeLock {

	private static PowerManager.WakeLock wakeLock;

	@SuppressLint("InvalidWakeLockTag")
	public static void acquire(Context context) {
		if(wakeLock != null && wakeLock.isHeld()) return;

		wakeLock = ((PowerManager)context.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tag");
		wakeLock.acquire();
	}

	public static void release() {
		if(wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
		}
		wakeLock = null;
	}
}