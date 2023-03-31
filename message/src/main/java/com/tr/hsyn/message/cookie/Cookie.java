package com.tr.hsyn.message.cookie;


import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tr.hsyn.message.R;


final class Cookie extends LinearLayout implements View.OnTouchListener {
	
	private Animation                slideOutAnimation;
	private ViewGroup                layoutCookie;
	private TextView                 titleTextView;
	private TextView                 messageTextView;
	private ImageView                iconImageView;
	private TextView                 actionButton;
	private long                     duration      = 2000L;
	private int                      layoutGravity = Gravity.BOTTOM;
	private float                    initialDragX;
	private float                    dismissOffsetThreshold;
	private float                    viewWidth;
	private boolean                  swipedOut;
	private int                      animationInTop;
	private int                      animationInBottom;
	private int                      animationOutTop;
	private int                      animationOutBottom;
	private boolean                  isAutoDismissEnabled;
	private boolean                  isSwipeable;
	private CookieBarDismissListener dismissListener;
	private boolean                  actionClickDismiss;
	private boolean                  timeOutDismiss;
	private Activity                 context;
	private ViewPropertyAnimator     hrAnimator;
	View hr_duration, hr_duration_top;
	
	public Cookie(@NonNull final Context context) {
		
		this(context, null);
	}
	
	public Cookie(@NonNull final Context context, @Nullable final AttributeSet attrs) {
		
		this(context, attrs, 0);
	}
	
	public Cookie(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
		
		super(context, attrs, defStyleAttr);
		this.context = (Activity) context;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
		viewWidth              = getWidth();
		dismissOffsetThreshold = viewWidth / 2;
		
		if (layoutGravity == Gravity.TOP) {
			super.onLayout(changed, l, 0, r, layoutCookie.getMeasuredHeight());
		}
		else {
			super.onLayout(changed, l, t, r, b);
		}
	}
	
	private void initViews(@LayoutRes int rootView, CookieBar.CustomViewInitializer viewInitializer) {
		
		if (rootView != 0) {
			
			inflate(getContext(), rootView, this);
			
			if (viewInitializer != null) {
				
				viewInitializer.initView(getChildAt(0));
			}
		}
		else {
			
			inflate(getContext(), R.layout.layout_cookie, this);
		}
		
		if (getChildAt(0).getLayoutParams() instanceof LayoutParams) {
			
			LayoutParams lp = (LayoutParams) (getChildAt(0).getLayoutParams());
			lp.gravity = Gravity.BOTTOM;
		}
		
		layoutCookie    = findViewById(R.id.cookie);
		titleTextView   = findViewById(R.id.tv_title);
		messageTextView = findViewById(R.id.tv_message);
		iconImageView   = findViewById(R.id.iv_icon);
		actionButton    = findViewById(R.id.btn_action);
		hr_duration     = findViewById(R.id.hr_duration);
		hr_duration_top = findViewById(R.id.hr_duration_top);
		
		if (rootView == 0) {
			
			validateLayoutIntegrity();
			initDefaultStyle(getContext());
		}
		
		layoutCookie.setOnTouchListener(this);
		
		
	}
	
	private void validateLayoutIntegrity() {
		
		if (layoutCookie == null || titleTextView == null || messageTextView == null ||
		    iconImageView == null || actionButton == null) {
			
			throw new RuntimeException("Your custom cookie view is missing one of the default required views");
		}
	}
	
	
	/**
	 * Init the default text color or background color. You can change the default style by set the
	 * Theme's attributes.
	 * <p>
	 * <pre>
	 *  <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
	 *          <item name="cookieTitleColor">@color/default_title_color</item>
	 *          <item name="cookieMessageColor">@color/default_message_color</item>
	 *          <item name="cookieActionColor">@color/default_action_color</item>
	 *          <item name="cookieBackgroundColor">@color/default_bg_color</item>
	 *  </style>
	 * </pre>
	 */
	private void initDefaultStyle(Context context) {
		
		int titleColor   = ThemeResolver.getColor(context, R.attr.cookieTitleColor, Color.WHITE);
		int messageColor = ThemeResolver.getColor(context, R.attr.cookieMessageColor, Color.WHITE);
		int actionColor  = ThemeResolver.getColor(context, R.attr.cookieActionColor, Color.WHITE);
		int backgroundColor = ThemeResolver.getColor(context, R.attr.cookieBackgroundColor,
		                                             ContextCompat.getColor(context, R.color.default_bg_color));
		
		titleTextView.setTextColor(titleColor);
		messageTextView.setTextColor(messageColor);
		actionButton.setTextColor(actionColor);
		layoutCookie.setBackgroundColor(backgroundColor);
	}
	
	private void setDefaultTextSize(TextView textView, @AttrRes int attr) {
		
		float size = ThemeResolver.getDimen(getContext(), attr, 0);
		if (size > 0) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		}
	}
	
	private CookieBar.Params params;
	
	@SuppressWarnings({"deprecation", "RedundantSuppression"})
	private void setHR(CookieBar.Params params) {
		
		float width;
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
			
			var display = context.getWindowManager().getCurrentWindowMetrics();
			
			var bounds = display.getBounds();
			width = bounds.width();
		}
		else {
			
			DisplayMetrics metrics = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			
			width = metrics.widthPixels;
		}
		
		
		final long delay = 555L;
		
		if (params.cookiePosition == Gravity.TOP) {
			
			hrAnimator = hr_duration.animate()
					.translationX(width)
					.setInterpolator(new DecelerateInterpolator())
					.setDuration(params.duration + delay);
		}
		else if (params.cookiePosition == Gravity.BOTTOM) {
			
			hr_duration.setVisibility(View.INVISIBLE);
			hr_duration_top.setVisibility(View.VISIBLE);
			
			hrAnimator = hr_duration_top.animate()
					.translationX(width)
					.setInterpolator(new DecelerateInterpolator())
					.setDuration(params.duration + delay);
		}
	}
	
	private void createInAnim() {
		
		int       animationResId   = layoutGravity == Gravity.BOTTOM ? animationInBottom : animationInTop;
		Animation slideInAnimation = AnimationUtils.loadAnimation(getContext(), animationResId);
		slideInAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
				setHR(params);
				hrAnimator.start();
				// no implementation
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				if (!isAutoDismissEnabled) {
					return;
				}
				
				postDelayed(new Runnable() {
					@Override
					public void run() {
						
						timeOutDismiss = true;
						dismiss();
					}
				}, duration);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// no implementation
			}
		});
		
		setAnimation(slideInAnimation);
	}
	
	private void createOutAnim() {
		
		int animationResId = layoutGravity == Gravity.BOTTOM ? animationOutBottom : animationOutTop;
		slideOutAnimation = AnimationUtils.loadAnimation(getContext(), animationResId);
	}
	
	private int getDismissType() {
		
		int dismissType = CookieBarDismissListener.DismissType.PROGRAMMATIC_DISMISS;
		if (actionClickDismiss) {
			dismissType = CookieBarDismissListener.DismissType.USER_ACTION_CLICK;
		}
		else if (timeOutDismiss) {
			dismissType = CookieBarDismissListener.DismissType.DURATION_COMPLETE;
		}
		return dismissType;
	}
	
	private void cookieListenerDismiss(@CookieBarDismissListener.DismissType int dismissType) {
		
		if (dismissListener != null) {
			dismissListener.onDismiss(dismissType);
		}
	}
	
	private void removeFromParent() {
		
		try {
			
			postDelayed(new Runnable() {
				@Override
				public void run() {
					
					ViewParent parent = getParent();
					if (parent != null) {
						Cookie.this.clearAnimation();
						((ViewGroup) parent).removeView(Cookie.this);
					}
				}
			}, 200);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	private Animator.AnimatorListener getDestroyListener() {
		
		return new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				// no implementation
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				
				dismiss();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// no implementation
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// no implementation
			}
		};
	}
	
	public int getLayoutGravity() {
		
		return layoutGravity;
	}
	
	public void setParams(final CookieBar.Params params) {
		
		this.params = params;
		initViews(params.customViewResource, params.viewInitializer);
		
		duration             = params.duration;
		layoutGravity        = params.cookiePosition;
		animationInTop       = params.animationInTop;
		animationInBottom    = params.animationInBottom;
		animationOutTop      = params.animationOutTop;
		animationOutBottom   = params.animationOutBottom;
		isSwipeable          = params.enableSwipeToDismiss;
		isAutoDismissEnabled = params.enableAutoDismiss;
		dismissListener      = params.dismissListener;
		final OnActionClickListener actionClickListener = params.onActionClickListener;
		
		if (params.iconResId != 0 && iconImageView != null) {
			
			iconImageView.setVisibility(VISIBLE);
			iconImageView.setBackgroundResource(params.iconResId);
			
			if (params.iconAnimator != null) {
				
				params.iconAnimator.setTarget(iconImageView);
				params.iconAnimator.start();
			}
		}
		
		if (titleTextView != null && !TextUtils.isEmpty(params.title)) {
			
			titleTextView.setVisibility(VISIBLE);
			titleTextView.setText(params.title);
			
			if (params.titleColor != 0) {
				
				titleTextView.setTextColor(ContextCompat.getColor(getContext(), params.titleColor));
			}
			
			setDefaultTextSize(titleTextView, R.attr.cookieTitleSize);
		}
		
		if (messageTextView != null && !TextUtils.isEmpty(params.message)) {
			
			messageTextView.setVisibility(VISIBLE);
			messageTextView.setText(params.message);
			
			if (params.messageColor != 0) {
				
				messageTextView.setTextColor(ContextCompat.getColor(getContext(), params.messageColor));
			}
			
			setDefaultTextSize(messageTextView, R.attr.cookieMessageSize);
		}
		
		if (actionButton != null && !TextUtils.isEmpty(params.action) && actionClickListener != null) {
			
			actionButton.setVisibility(VISIBLE);
			actionButton.setText(params.action);
			actionButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					
					actionClickListener.onClick();
					actionClickDismiss = true;
					dismiss();
				}
			});
			
			if (params.actionColor != 0) {
				
				actionButton.setTextColor(ContextCompat.getColor(getContext(), params.actionColor));
			}
			
			setDefaultTextSize(actionButton, R.attr.cookieActionSize);
		}
		
		if (params.backgroundColor != 0) {
			
			layoutCookie.setBackgroundColor(params.backgroundColor);
		}
      
      /*int defaultPadding = getContext().getResources().getDimensionPixelSize(R.dimen.default_padding);
      int padding        = ThemeResolver.getDimen(getContext(), R.attr.cookiePadding, defaultPadding);
      
      if (layoutGravity == Gravity.BOTTOM) {
         
         layoutCookie.setPadding(padding, padding, padding, padding);
      }*/
		
		createInAnim();
		createOutAnim();
	}
	
	public void dismiss() {
		
		try {
			
			dismiss(null);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	public CookieBarDismissListener getDismissListenr() {
		
		return dismissListener;
	}
	
	public void dismiss(final CookieBarDismissListener listener) {
		
		getHandler().removeCallbacksAndMessages(null);
		
		if (listener != null) {
			dismissListener = listener;
		}
		
		if (swipedOut) {
			
			removeFromParent();
			cookieListenerDismiss(CookieBarDismissListener.DismissType.USER_DISMISS);
			return;
		}
		
		slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(final Animation animation) {
				// no implementation
			}
			
			@Override
			public void onAnimationEnd(final Animation animation) {
				
				setVisibility(View.GONE);
				removeFromParent();
				cookieListenerDismiss(getDismissType());
			}
			
			@Override
			public void onAnimationRepeat(final Animation animation) {
				// no implementation
			}
		});
		
		startAnimation(slideOutAnimation);
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		
		if (!isSwipeable) {
			return true;
		}
		
		switch (motionEvent.getAction()) {
			
			case MotionEvent.ACTION_DOWN:
				
				initialDragX = motionEvent.getRawX();
				return true;
			
			case MotionEvent.ACTION_UP:
				
				if (!swipedOut) {
					
					view.animate()
							.x(0)
							.alpha(1)
							.setDuration(200)
							.start();
				}
				
				return true;
			
			case MotionEvent.ACTION_MOVE:
				
				if (swipedOut) return true;
				
				float offset = motionEvent.getRawX() - initialDragX;
				float alpha = 1 - Math.abs(offset / viewWidth);
				long duration = 0;
				
				if (Math.abs(offset) > dismissOffsetThreshold) {
					
					offset    = viewWidth * Math.signum(offset);
					alpha     = 0;
					duration  = 200;
					swipedOut = true;
				}
				
				view.animate()
						.setListener(swipedOut ? getDestroyListener() : null)
						.x(offset)
						.alpha(alpha)
						.setDuration(duration)
						.start();
				
				return true;
			
			default:
				return false;
		}
	}
}
