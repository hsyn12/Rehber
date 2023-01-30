package com.tr.hsyn.telefonrehberi.code;


import static android.content.Context.TELEPHONY_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.message.Show;
import com.tr.hsyn.telefonrehberi.BuildConfig;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;


public enum Phone {
	;
	
	private static boolean gotoAppSettings = false;
	
	public static boolean isGotoAppSettings() {
		
		return gotoAppSettings;
	}
	
	public static void resetGotoAppSettings() {
		
		Phone.gotoAppSettings = false;
	}
	
	public static void openAppSettings(Context context) {
		
		var i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
		                   Uri.parse("package:" + BuildConfig.APPLICATION_ID));
		
		gotoAppSettings = true;
		context.startActivity(i);
	}
	
	public static boolean isEnableComponent(@NonNull Context context, Class<?> component) {
		
		PackageManager pm = context.getPackageManager();
		
		int state = pm.getComponentEnabledSetting(new ComponentName(context, component));
		
		return state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT || state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
	}
   
   /*@Nullable
   public static Drawable getIconForAccount(Context context, Account account){
      
      AuthenticatorDescription[] descriptions = AccountManager.get(context).getAuthenticatorTypes();
      PackageManager             pm           = context.getPackageManager();
      
      for(AuthenticatorDescription description : descriptions){
         
         if(description.type.equals(account.type)){
            
            return pm.getDrawable(description.packageName, description.smallIconId, null);
         }
      }
      return null;
   }*/
	
	public static boolean isAirplaneModeOn(@NonNull Context context) {
		
		return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
	}
	
	@SuppressLint("MissingPermission")
	public static boolean isOnline(@NonNull Context context) {
		
		ConnectivityManager c = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (c == null) return false;
		
		if (Permissions.hasPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
			
			Network network = c.getActiveNetwork();
			
			if (network == null) return false;
			
			NetworkCapabilities actNw = c.getNetworkCapabilities(network);
			
			return actNw != null &&
			       (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
			        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
			        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
			        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
		}
		
		return false;
	}
	
	@SuppressLint("MissingPermission")
	public static boolean isWifiConnected(@NonNull Context context) {
		
		if (Permissions.hasPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
			
			ConnectivityManager c = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (c == null) return false;
			
			Network network = c.getActiveNetwork();
			
			if (network == null) return false;
			
			NetworkCapabilities actNw = c.getNetworkCapabilities(network);
			
			return actNw != null &&
			       (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
		}
		
		return false;
	}
	
	public static boolean isScreenOn(Context context) {
		
		if (context == null) {
			return false;
		}
		
		DisplayManager displayManager = (DisplayManager) context.getApplicationContext().getSystemService(Context.DISPLAY_SERVICE);
		
		if (displayManager == null) {
			
			xlog.w("DisplayManager null");
			return false;
		}
		
		for (Display display : displayManager.getDisplays()) {
			
			if (display.getState() != Display.STATE_OFF) {
				
				return true;
			}
		}
		
		return false;
	}
	
	@SuppressWarnings({"deprecation", "RedundantSuppression"})
	public static boolean isServiceRunning(Context context, Class<?> clazz) {
		
		if (context == null) return false;
		
		final String serviceName = clazz.getName();
		
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		
		if (manager == null) return false;
		
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			
			if (serviceName.equals(service.service.getClassName())) {
				
				return true;
			}
		}
		
		return false;
	}
	
	public static void openNotificationAccessSetting(@NonNull Context context) {
		
		Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		context.startActivity(intent);
		
	}
   
   /*public static boolean isValidGoogleEmail(String email) {
      
      return isValidEmailAddress(email) && email.endsWith("gmail.com");
   }*/
   
  /* public static boolean isValidEmailAddress(String email){
      
      if(email == null) return false;
      
      boolean result = true;
      
      try{
         
         InternetAddress emailAddr = new InternetAddress(email);
         emailAddr.validate();
      }
      catch(AddressException ex){
         result = false;
      }
      
      return result;
   }*/
	
	private static boolean isTelephonyEnabled(@NonNull Context context) {
		
		TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
		
		if (mTelephonyManager == null) return false;
		
		return mTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
		
	}
	
	private static boolean isCallReady(Context context) {
		
		if (isAirplaneModeOn(context)) {
			
			//Toast.makeText(context, context.getString(R.string.msg_airplane_mode_on), Toast.LENGTH_SHORT).show();
			return false;
		}
		
		//Toast.makeText(context, context.getString(R.string.msg_simcard_not_ready), Toast.LENGTH_SHORT).show();
		return isTelephonyEnabled(context);
	}
	
	public static void makeCall(@NonNull final Context context, @NonNull final String number) {
		
		if (!isCallReady(context)) {
			return;
		}
		
		if (Permissions.hasPermission(context, Manifest.permission.CALL_PHONE)) {
			
			try {
				
				context.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + Uri.encode(number))));
				Show.tost(context, context.getString(R.string.calling, number));
			}
			catch (ActivityNotFoundException e) {
				
				Show.tost(context, context.getString(R.string.no_call_app));
			}
		}
		else {
			
			Show.tost(context, context.getString(R.string.no_permissions));
		}
	}
	
	@NonNull
	public static File getPakDir(@NonNull Context context) {
		
		File dir = new File(context.getDataDir(), "pak");
		
		if (!dir.exists()) //noinspection ResultOfMethodCallIgnored
			dir.mkdir();
		
		return dir;
	}
	
	@SuppressWarnings("MagicNumber")
	public static String getAndroidCodeName() {
		
		String codeName = null;
		
		switch (android.os.Build.VERSION.SDK_INT) {
			
			case 21:
			case 22:
				codeName = "Lollipop";
				break;
			
			case 23:
				
				codeName = "Marshmallow";
				break;
			
			case 24:
			case 25:
				codeName = "Nougat";
				break;
			
			case 26:
			case 27:
				codeName = "Oreo";
				break;
			
			case 28:
				codeName = "Pie";
				break;
			
			case 29:
				codeName = "Android Q";
				break;
			
			case 30:
				codeName = "Android 11";
				break;
			
			case 31:
				codeName = "Android 12";
				break;
		}
		
		return codeName;
	}
	
	public static void setEnableComponent(@NonNull Context context, Class<?> clazz, boolean enable) {
		
		ComponentName  componentName  = new ComponentName(context.getApplicationContext(), clazz);
		PackageManager packageManager = context.getPackageManager();
		
		packageManager.setComponentEnabledSetting(
				componentName,
				enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}
	
	@NonNull
	public static File getPrefsDir(@NonNull Context context) {
		
		return new File(context.getDataDir(), "shared_prefs");
	}
	
	@NonNull
	public static File getDatabaseDir(@NonNull Context context) {
		
		return new File(context.getDataDir(), "databases");
		
	}
	
	@Nullable
	public static File getDatabaseFile(@NonNull Context context, String dbName) {
		
		File dataDir = new File(context.getDataDir(), "databases");
		;
		
		File[] files = dataDir.listFiles();
		
		xlog.w(Arrays.toString(files));
		
		if (files != null) {
			
			for (File file : files) {
				
				if (file.getName().equals(dbName)) return file;
			}
		}
		
		return null;
	}
	
	public static @NotNull Point getDisplaySize() {
		
		var metrics = Resources.getSystem().getDisplayMetrics();
		
		return new Point((int) pxToDp(metrics.widthPixels), (int) pxToDp(metrics.heightPixels));
	}
	
	public static float pxToDp(float px) {
		
		Resources r = Resources.getSystem();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, r.getDisplayMetrics());
	}
}
