package com.tr.hsyn.permissions;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public interface Permissions {
	
	
	/**
	 * İznin verilip verilmediğini kontrol et.
	 *
	 * @return İzinler verilmiş ise {@code true}
	 */
	static boolean isGrant(Context context, String permission) {
		
		if (context == null || permission == null) return false;
		
		return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
	}
	
	static boolean isGrant(Context context, String... permissions) {
		
		if (context == null) return false;
		
		return Stream.of(permissions).allMatch(perm -> isGrant(context, perm));
	}
	
	@NonNull
	static List<String> getGrantedPermissions(Context context, String... permissions) {
		
		if (context == null) return new ArrayList<>(0);
		
		return Stream.of(permissions).filter(perm -> isGrant(context, perm)).collect(Collectors.toList());
	}
	
	static List<String> getPermanentlyDeniedPermissions(Fragment fragment, String... permissions) {
		
		if (fragment == null) return new ArrayList<>(0);
		
		return Stream.of(permissions).filter(perm -> isPermanentlyDenied(fragment, perm)).collect(Collectors.toList());
	}
	
	static boolean isPermanentlyDenied(@NonNull Fragment fragment, @NonNull String permission) {
		
		return !fragment.shouldShowRequestPermissionRationale(permission);
	}
	
	static List<String> getPermanentlyDeniedPermissions(Activity activity, String... permissions) {
		
		if (activity == null) return new ArrayList<>(0);
		
		return Stream.of(permissions).filter(perm -> isPermanentlyDenied(activity, perm)).collect(Collectors.toList());
	}
	
	static boolean isPermanentlyDenied(Activity activity, String permission) {
		
		return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
	}
	
	static void openAppSettings(Activity activity, int requstCode) {
		
		if (activity == null) return;
		
		Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
		
		activity.startActivityForResult(i, requstCode);
	}
}
