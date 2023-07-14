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


/**
 * Provides methods to check the permissions.
 */
public interface Permissions {
	
	/**
	 * Checks if the permission is granted.
	 *
	 * @param context    context
	 * @param permission permission to check
	 * @return {@code true} if the permission is granted
	 */
	static boolean isGrant(Context context, String permission) {
		
		if (context == null || permission == null) return false;
		
		return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
	}
	
	/**
	 * Checks if the permissions are granted.
	 *
	 * @param context     context
	 * @param permissions permissions to check
	 * @return {@code true} if the all permissions are granted. {@code false} otherwise.
	 */
	static boolean isGrant(Context context, String... permissions) {
		
		if (context == null) return false;
		
		return Stream.of(permissions).allMatch(perm -> isGrant(context, perm));
	}
	
	/**
	 * Returns the granted permissions from the given permissions array.
	 *
	 * @param context     context
	 * @param permissions permissions to check
	 * @return the granted permissions
	 */
	@NonNull
	static List<String> getGrantedPermissions(Context context, String... permissions) {
		
		if (context == null) return new ArrayList<>(0);
		
		return Stream.of(permissions).filter(perm -> isGrant(context, perm)).collect(Collectors.toList());
	}
	
	/**
	 * Returns the permanently denied permissions from the given permissions array.
	 *
	 * @param fragment    the fragment
	 * @param permissions permissions to check
	 * @return the permanently denied permissions
	 */
	static List<String> getPermanentlyDeniedPermissions(Fragment fragment, String... permissions) {
		
		if (fragment == null) return new ArrayList<>(0);
		
		return Stream.of(permissions).filter(perm -> isPermanentlyDenied(fragment, perm)).collect(Collectors.toList());
	}
	
	/**
	 * Checks if the permission is permanently denied.
	 *
	 * @param fragment   the fragment
	 * @param permission the permission to check
	 * @return {@code true} if the permission is permanently denied
	 */
	static boolean isPermanentlyDenied(@NonNull Fragment fragment, @NonNull String permission) {
		
		return !fragment.shouldShowRequestPermissionRationale(permission);
	}
	
	/**
	 * Returns the permanently denied permissions from the given permissions array.
	 *
	 * @param activity    the activity
	 * @param permissions the permissions to check
	 * @return the permanently denied permissions
	 */
	static List<String> getPermanentlyDeniedPermissions(Activity activity, String... permissions) {
		
		if (activity == null) return new ArrayList<>(0);
		
		return Stream.of(permissions).filter(perm -> isPermanentlyDenied(activity, perm)).collect(Collectors.toList());
	}
	
	/**
	 * Checks if the permission is permanently denied.
	 *
	 * @param activity   the activity
	 * @param permission the permission to check
	 * @return {@code true} if the permission is permanently denied
	 */
	static boolean isPermanentlyDenied(Activity activity, String permission) {
		
		return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
	}
	
	/**
	 * Opens the app settings.
	 *
	 * @param activity    the activity
	 * @param requestCode the request code to use for opening the app settings
	 */
	static void openAppSettings(Activity activity, int requestCode) {
		
		if (activity == null) return;
		
		Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
		
		activity.startActivityForResult(i, requestCode);
	}
}
