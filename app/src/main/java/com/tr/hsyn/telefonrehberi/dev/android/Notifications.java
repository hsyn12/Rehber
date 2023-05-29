package com.tr.hsyn.telefonrehberi.dev.android;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.tr.hsyn.telefonrehberi.BuildConfig;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.dev.WordGenerator;

import java.util.Random;


/**
 * Helper class for generating notifications.
 */
public class Notifications {
	
	/**
	 * Context
	 */
	private final Context             context;
	/**
	 * Notification manager service
	 */
	private final NotificationManager notificationManager;
	/**
	 * Word generator to create fake notification for test.
	 */
	private final WordGenerator       generator = new WordGenerator();
	/**
	 * Random number generator.
	 */
	private final Random              random    = new Random();
	/**
	 * Words
	 */
	private       String[]            words;
	
	/**
	 * Constructor for {@link Notifications} class.
	 *
	 * @param context {@link Context}
	 */
	public Notifications(@NonNull Context context) {
		
		this.context             = context;
		this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		this.words               = context.getResources().getStringArray(R.array.words);
	}
	
	/**
	 * Returns a notification.
	 *
	 * @param title     title
	 * @param message   message
	 * @param channelId channel id
	 * @return notification
	 */
	public Notification createNotification(String title, String message, String channelId) {
		
		return new NotificationCompat.Builder(context, channelId)
				.setContentTitle(title)
				.setContentText(message)
				.setSmallIcon(R.mipmap.call)
				.setDefaults(Notification.DEFAULT_ALL)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				//.setContentTitle(ticker)
				//.setTicker(ticker)
				.setSubText("tr.xyz")
				.build();
	}
	
	/**
	 * Returns the channel name.
	 *
	 * @param channelId channel id of the notification
	 * @return channel name
	 */
	@Nullable
	public CharSequence getChannelName(CharSequence channelId) {
		
		if (channelId != null) {
			
			var name = notificationManager.getNotificationChannel(channelId.toString());
			
			if (name != null) return name.getName();
		}
		
		return null;
	}
	
	/**
	 * Creates a test notification.
	 *
	 * @return notification
	 */
	public Notification createTestNotification() {
		
		Notification notification;
		
		var channel = createRandomChannel(true);
		
		notification = createSilentNotification("Test", getRandomWord(), channel.getId());
		
		return notification;
	}
	
	/**
	 * Creates a silent notification. No sound, no vibration, no light and minimum importance.
	 *
	 * @param title     title
	 * @param message   message
	 * @param channelId channel id
	 * @return notification
	 */
	public Notification createSilentNotification(String title, String message, String channelId) {
		
		return new NotificationCompat.Builder(context, channelId)
				.setContentTitle(title)
				.setContentText(message)
				.setSmallIcon(R.mipmap.call)
				.setDefaults(0)
				.setPriority(NotificationCompat.PRIORITY_MIN)
				.setVisibility(NotificationCompat.VISIBILITY_SECRET)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				//.setContentTitle(ticker)
				//.setTicker(ticker)
				.setSubText("tr.xyz")
				.build();
	}
	
	/**
	 * Creates a random channel.
	 *
	 * @param isSilent silent
	 * @return channel
	 */
	public NotificationChannel createRandomChannel(boolean isSilent) {
		
		String channelName = generator.generate();
		String channelId   = generator.generate();
		
		return isSilent ?
				createNewSilentChannel(channelName, channelId) :
				createNewChannel(channelName, channelId);
	}
	
	/**
	 * Creates a new notification channel.
	 *
	 * @param channelName channel name
	 * @param channelId   channel id
	 * @return notification
	 */
	public NotificationChannel createNewSilentChannel(String channelName, String channelId) {
		
		NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
		
		chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
		chan.enableVibration(false);
		chan.setSound(null, null);
		chan.enableLights(false);
		notificationManager.createNotificationChannel(chan);
		
		
		return chan;
	}
	
	/**
	 * Creates a new notification channel.
	 *
	 * @param channelName channel name
	 * @param channelId   channel id
	 * @return notification
	 */
	public NotificationChannel createNewChannel(String channelName, String channelId) {
		
		NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
		
		chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
		chan.enableLights(true);
		chan.enableVibration(true);
		chan.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.detuned_tone), new AudioAttributes.Builder()
				.setUsage(AudioAttributes.USAGE_NOTIFICATION)
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.build());
		
		notificationManager.createNotificationChannel(chan);
		
		return chan;
	}
	
	/**
	 * Returns a random word.
	 *
	 * @return random word
	 */
	private String getRandomWord() {
		
		if (words == null) words = context.getResources().getStringArray(R.array.words);
		
		return words[random.nextInt(words.length)];
	}
	
	/**
	 * Returns true if the channel is enabled.
	 *
	 * @param channel channel
	 * @return is channel enabled
	 */
	public boolean isChannelEnable(@NonNull NotificationChannel channel) {
		
		return channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
	}
}
