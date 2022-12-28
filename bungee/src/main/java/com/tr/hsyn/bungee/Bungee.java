package com.tr.hsyn.bungee;

import android.app.Activity;
import android.content.Context;


public class Bungee {
	
	private Bungee() {}
	
	public static void slideLeft(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.slide_left_enter, com.tr.hsyn.resanim.R.anim.slide_left_exit);
	}
	
	public static void slideRight(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.slide_in_left, com.tr.hsyn.resanim.R.anim.slide_out_right);
	}
	
	public static void slideDown(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.slide_down_enter, com.tr.hsyn.resanim.R.anim.slide_down_exit);
	}
	
	public static void slideUp(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.slide_up_enter, com.tr.hsyn.resanim.R.anim.slide_up_exit);
	}
	
	public static void zoom(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.zoom_enter, com.tr.hsyn.resanim.R.anim.zoom_exit);
	}
	
	public static void zoomFast(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.zoom_enter_fast, com.tr.hsyn.resanim.R.anim.zoom_exit_fast);
	}
	
	public static void fade(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.fade_enter, com.tr.hsyn.resanim.R.anim.fade_exit);
	}
	
	public static void fadeFast(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.fade_enter_fast, com.tr.hsyn.resanim.R.anim.fade_exit_fast);
	}
	
	public static void windmill(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.windmill_enter, com.tr.hsyn.resanim.R.anim.windmill_exit);
	}
	
	public static void spin(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.spin_enter, com.tr.hsyn.resanim.R.anim.spin_exit);
	}
	
	public static void diagonal(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.diagonal_right_enter, com.tr.hsyn.resanim.R.anim.diagonal_right_exit);
	}
	
	public static void split(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.split_enter, com.tr.hsyn.resanim.R.anim.split_exit);
	}
	
	public static void shrink(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.shrink_enter, com.tr.hsyn.resanim.R.anim.shrink_exit);
	}
	
	public static void cardIn(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.card_in_enter, com.tr.hsyn.resanim.R.anim.card_in_exit);
	}
	
	public static void cardOut(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.card_out_enter, com.tr.hsyn.resanim.R.anim.card_out_exit);
	}
	
	public static void inAndOut(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.in_out_enter, com.tr.hsyn.resanim.R.anim.in_out_exit);
	}
	
	public static void swipeLeft(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.swipe_left_enter, com.tr.hsyn.resanim.R.anim.swipe_left_exit);
	}
	
	public static void swipeRight(Context context) {
		
		((Activity) context).overridePendingTransition(com.tr.hsyn.resanim.R.anim.swipe_right_enter, com.tr.hsyn.resanim.R.anim.swipe_right_exit);
	}
}
