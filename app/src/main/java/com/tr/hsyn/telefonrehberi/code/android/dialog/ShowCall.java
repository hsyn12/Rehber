package com.tr.hsyn.telefonrehberi.code.android.dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.files.Files;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.call.act.Calls;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.CallKey;
import com.tr.hsyn.textdrawable.TextDrawable;
import com.tr.hsyn.time.Time;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


public class ShowCall {
	
	private final AlertDialog dialog;
	
	@SuppressLint("InflateParams")
	public ShowCall(@NotNull Activity activity, @NotNull Call call) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true);
		
		
		View view = activity.getLayoutInflater().inflate(R.layout.call, null, false);
		
		setCallInfo(view, call);
		builder.setView(view);
		
		dialog                                              = builder.create();
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBounce;
	}
	
	private void setCallInfo(@NotNull View view, @NotNull Call call) {
		
		TextView  name            = view.findViewById(R.id.name);
		TextView  number          = view.findViewById(R.id.number);
		TextView  callDuration    = view.findViewById(R.id.callDuration);
		TextView  ringingDuration = view.findViewById(R.id.ringingDuration);
		TextView  date            = view.findViewById(R.id.date);
		ImageView type            = view.findViewById(R.id.type);
		ImageView image           = view.findViewById(R.id.image);
		
		var root = view.findViewById(R.id.call_item);
		root.setBackgroundResource(Colors.getRipple());
		
		
		String _name   = call.getName();
		String _number = PhoneNumbers.beautifyNumber(call.getNumber());
		
		_name = _name == null || _name.trim().isEmpty() ? _number : _name;
		
		name.setText(_name);
		number.setText(_number);
		
		Drawable _type = ContextCompat.getDrawable(view.getContext(), getTypeIcon(call.getType()));
		
		type.setImageDrawable(_type);
		callDuration.setText(Files.formatSeconds(call.getDuration()));
		ringingDuration.setText(Files.formatMilliSeconds(call.getLong(CallKey.RINGING_DURATION, 0L)));
		date.setText(Time.toString(call.getTime(), "d MMMM yyyy HH:mm"));
		
		String letter = getLeter(_name);
		int    color  = Colors.getRandomColor();
		
		Drawable _image = TextDrawable.builder()
				.beginConfig()
				.useFont(ResourcesCompat.getFont(view.getContext(), com.tr.hsyn.resfont.R.font.z))
				.endConfig()
				.buildRound(letter, color);
		
		image.setImageDrawable(_image);
	}
	
	private void onCancel() {
		
		dialog.dismiss();
	}
	
	public void show() {
		
		dialog.show();
	}
	
	private int getTypeIcon(int type) {
		
		return Calls.getCallTypeIcon(type);
	}
	
	@NonNull
	private String getLeter(String str) {
		
		var l = Stringx.getFirstChar(str);
		
		if (l.isEmpty()) return "?";
		
		if (Character.isAlphabetic(l.charAt(0))) return l.toUpperCase(Locale.ROOT);
		
		return "?";
	}
	
}
