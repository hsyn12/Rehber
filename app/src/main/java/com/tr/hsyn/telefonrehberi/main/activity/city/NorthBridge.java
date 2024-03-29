package com.tr.hsyn.telefonrehberi.main.activity.city;


import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.message.CMessage;
import com.tr.hsyn.permissions.Permissions;
import com.tr.hsyn.telefonrehberi.main.cast.PermissionHolder;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.xlog.xlog;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public abstract class NorthBridge extends MainActivity implements PermissionHolder {
	
	/**
	 * Rehber okuma izni.<br>
	 * Bu izin, kullanıcının rehberinde kayıtlı olan kişilere erişim sağlar.
	 */
	protected final String  CONTACTS_R  = Manifest.permission.READ_CONTACTS;
	/**
	 * Arama kayıtları okuma izni
	 */
	protected final String  CALLS_R     = Manifest.permission.READ_CALL_LOG;
	/**
	 * Arama yapma izni
	 */
	protected final String  CALL_PHONE  = Manifest.permission.CALL_PHONE;
	/**
	 * Rehber izinleri için çağrı kodu
	 */
	private final   int     RC_CONTACTS = 11;
	/**
	 * Arama kaydı izinleri için çağrı kodu
	 */
	private final   int     RC_CALLS    = 12;
	/**
	 * {@code false} değeri taşır. Rehber uygulamasının sistemdeki ayarlar sayfasına gidildiyse {@code true} değeri atanır.
	 * Bu değişken {@link #onResume()} metodunda kontrol edilerek gereken işler yapılır ve tekrar {@code false} değerine atanır.
	 */
	protected       boolean gotoAppSettings;
	
	/**
	 * Rehber izinleri kullanıcı tarafından onaylandığında çağrılır.
	 */
	protected abstract void onGrantContactsPermissions();
	
	/**
	 * Arama kaydı izinleri kullanıcı tarafından onaylandığında çağrılır.
	 */
	protected abstract void onGrantCallsPermissions();
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		if (gotoAppSettings) {
			
			gotoAppSettings = false;
			onPageChange(currentPage);
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		boolean isGrant = Arrays.stream(grantResults).allMatch(r -> r == PackageManager.PERMISSION_GRANTED);
		
		if (requestCode == RC_CONTACTS) {
			
			if (isGrant) {
				
				//- İzinler onaylandıysa iyi
				onGrantContactsPermissions();
			}
			else {
				
				onDeniedContactsPermissions(createPermissionsResult(permissions, grantResults));
			}
		}
		else if (requestCode == RC_CALLS) {
			
			if (isGrant) {
				
				onGrantCallsPermissions();
			}
			else {
				
				onDeniedCallsPermissions(createPermissionsResult(permissions, grantResults));
			}
		}
		else {
			
			onPermissionResult(requestCode, permissions, grantResults);
		}
	}
	
	/**
	 * Talep edilen izinlerin sonuçları.
	 * Bu metot sonuçlar üzerinde hiçbir işlem yapmaz.
	 * İşlem yapma sorumluluğu izni talep eden yukarıdaki görevlilerindir.
	 *
	 * @param requestCode İzin kodu
	 * @param result      İzin sonuçları
	 */
	@Override
	public void onPermissionsResult(int requestCode, Map<String, Boolean> result) {}
	
	/**
	 * Rehber izinlerini kullanıcıdan ister.<br>
	 * Bu izinler şunlardır ; {@link #CONTACTS_R}, {@link #CONTACTS_W}, {@link #ACCOUNTS}
	 */
	protected void askContactPermissions() {
		
		requestPermissions(PermissionHolder.REQUIRED_PERMISSIONS, RC_CONTACTS);
	}
	
	/**
	 * Arama izinlerini kullanıcıdan ister.
	 */
	protected void askCallLogPermissions() {
		
		requestPermissions(PermissionHolder.REQUIRED_PERMISSIONS, RC_CALLS);
	}
	
	/**
	 * Rehber izinleri kullanıcıya sorulduğunda, sorulan izinlerden herhangi biri reddedilirse çağrılır.
	 *
	 * @param result İzinlerin sonuçları. <code>true</code> olanlar onaylananlar, diğerleri reddedilenler.
	 */
	protected void onDeniedContactsPermissions(@NonNull Map<String, Boolean> result) {
		
		Boolean readPermission = result.get(CONTACTS_R);
		
		if (readPermission != null) {
			
			if (readPermission) {
				
				xlog.i("Rehber okuma izni var");
			}
			
			boolean permenentlyDenied = isPermanentlyDenied(CONTACTS_R);
			
			if (permenentlyDenied) {
				
				xlog.w("Rehber okuma izni otomatik olarak redediliyor");
				
				CMessage.builder()
						.message(Spanner.of("Rehber okuma izni otomatik olarak ").append("reddediliyor. ", Spans.bold()))
						.actionMessage("Düzelt")
						.action(() -> {
							Permissions.openAppSettings(this, 27);
							gotoAppSettings = true;
						})
						.duration(TimeUnit.SECONDS.toMillis(10))
						.backgroundColor(Colors.getPrimaryColor())
						.relationDegree(1)
						.build()
						.showOn(this);
			}
		}
		
		xlog.w("Rehber izni reddedildi : %s", result);
	}
	
	/**
	 * Arama kaydı izinleri kullanıcıya sorulduğunda, sorulan izinlerden herhangi biri reddedilirse çağrılır.
	 *
	 * @param result İzinlerin sonuçları. <code>true</code> olanlar onaylananlar, diğerleri reddedilenler.
	 */
	protected void onDeniedCallsPermissions(@NonNull Map<String, Boolean> result) {
		
		Boolean readPermission = result.get(CALLS_R);
		
		if (readPermission != null) {
			
			if (readPermission) {
				
				xlog.i("Arama kayıtları okuma izni var");
			}
			
			boolean permenentlyDenied = isPermanentlyDenied(CALLS_R);
			
			if (permenentlyDenied) {
				
				xlog.w("Arama Kayıtları okuma izni otomatik olarak redediliyor");
				
				CMessage.builder()
						.message(Spanner.of("Arama Kaydı okuma izni otomatik olarak ").append("reddediliyor. ", Spans.bold()))
						.actionMessage("Düzelt")
						.action(() -> {
							Permissions.openAppSettings(this, 27);
							gotoAppSettings = true;
						})
						.duration(TimeUnit.SECONDS.toMillis(10))
						.backgroundColor(Colors.getPrimaryColor())
						.relationDegree(1)
						.build()
						.showOn(this);
			}
		}
		
		xlog.w("Arama Kayıtları izni reddedildi : %s", result);
	}
	
	/**
	 * @param permission Sorgulanacak izin
	 * @return İzin sistem tarafından kullanıcıya sorulmadan otomatik olarak reddediliyorsa {@code true}.
	 */
	public boolean isPermanentlyDenied(String permission) {
		
		return !shouldShowRequestPermissionRationale(permission);
	}
}
