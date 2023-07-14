package com.tr.hsyn.telefonrehberi.main.cast;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * İzin sorgulama işlemleri.<br>
 * Sorgu için şahsın {@link Context} nesnesi olması yeterli.
 * {@link #isPermissionPermanentlyDenied(String)} için ise {@link Activity} olması gerek.
 */
public interface PermissionHolder extends ActivityCompat.OnRequestPermissionsResultCallback {
	
	String[] CALL_LOG_PERMISSIONS = new String[]{
			
			Manifest.permission.READ_CALL_LOG,
			Manifest.permission.WRITE_CALL_LOG,
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.CALL_PHONE
	};
	String[] CONTACT_PERMISSIONS  = new String[]{
			
			Manifest.permission.READ_CONTACTS,
			Manifest.permission.WRITE_CONTACTS,
			Manifest.permission.GET_ACCOUNTS
	};
	
	/**
	 * İzinlerin sonuçları için çağrılır.
	 *
	 * @param requestCode requestCode
	 * @param result      [Permission : true/false] şeklinde izin sonuçlarını taşır
	 */
	void onPermissionsResult(int requestCode, Map<String, Boolean> result);
	
	default boolean isPermissionPermanentlyDenied(String permission) {
		
		return !((Activity) this).shouldShowRequestPermissionRationale(permission);
	}
	
	/**
	 * @return Rehber için okuma-yazma ve hesaplara erişim izni varsa <code>true</code>
	 */
	default boolean hasContactPermissions() {
		
		return hasPermissions(CONTACT_PERMISSIONS);
	}
	
	default boolean hasPermissions(@NonNull final String... permissions) {
		
		return Arrays.stream(permissions).allMatch(this::hasPermission);
	}
	
	default boolean hasPermission(@NonNull final String permission) {
		
		return ContextCompat.checkSelfPermission((Context) this, permission) == PackageManager.PERMISSION_GRANTED;
	}
	
	/**
	 * @return Arama kayıtları için okuma-yazma, telefon durumunu okuma ve arama yapma izni varsa <code>true</code>
	 */
	default boolean hasCallLogPermissions() {
		
		return hasPermissions(CALL_LOG_PERMISSIONS);
	}
	
	/**
	 * İzin talep edilmesini sağlar.
	 * <p>
	 * Bu izinlerin sonuçları, {@link #onPermissionsResult(int, Map)} metoduna düşer.
	 *
	 * @param requestCode İzin kodu
	 * @param permissions Talep edilecek izinler
	 */
	default void askPermissions(int requestCode, String... permissions) {
		
		((Activity) this).requestPermissions(permissions, requestCode);
	}
	
	default void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		
		onPermissionsResult(requestCode, createPermissionsResult(permissions, grantResults));
	}
	
	@NonNull
	default Map<String, Boolean> createPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
		
		Map<String, Boolean> result = new HashMap<>();
		
		for (int i = 0; i < grantResults.length; i++) {
			
			String  key   = permissions[0];
			boolean value = grantResults[0] == PackageManager.PERMISSION_GRANTED;
			
			result.put(key, value);
		}
		
		return result;
	}
}
