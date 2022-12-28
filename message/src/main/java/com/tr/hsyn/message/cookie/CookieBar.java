package com.tr.hsyn.message.cookie;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import com.tr.hsyn.message.R;


/**
 * CookieBar is a lightweight library for showing a brief message at the top or bottom of the
 * screen.
 * <p>
 * CookieBar
 * .build(MainActivity.this)
 * .setTitle("TITLE")
 * .setMessage("MESSAGE")
 * .setAction("ACTION", new OnActionClickListener() {})
 * .show();
 */
@SuppressWarnings("MethodReturnOfConcreteClass")
public class CookieBar {
   
   private             Cookie   cookieView;
   private final       Activity context;
   public static final int      TOP    = Gravity.TOP;
   public static final int      BOTTOM = Gravity.BOTTOM;
   
   
   private CookieBar(Activity context, Params params) {
      
      this.context = context;
      
      if (params == null) {
         // since params is null, this CookieBar object can only be used to dismiss
         // existing cookies
         dismiss();
         return;
      }
      
      cookieView = new Cookie(context);
      cookieView.setParams(params);
   }
   
   private void show() {
      
      if (cookieView != null) {
         
         final ViewGroup decorView = (ViewGroup) context.getWindow().getDecorView();
         final ViewGroup content   = decorView.findViewById(android.R.id.content);
         
         if (cookieView.getParent() == null) {
            
            ViewGroup parent = cookieView.getLayoutGravity() == Gravity.BOTTOM ? content : decorView;
            addCookie(parent, cookieView);
         }
      }
   }
   
   private void dismiss() {
      
      final ViewGroup decorView = (ViewGroup) context.getWindow().getDecorView();
      final ViewGroup content   = decorView.findViewById(android.R.id.content);
      
      removeFromParent(decorView);
      removeFromParent(content);
   }
   
   private void removeFromParent(ViewGroup parent) {
      
      int childCount = parent.getChildCount();
      
      for (int i = 0; i < childCount; i++) {
         
         View child = parent.getChildAt(i);
         
         if (child instanceof Cookie) {
            
            ((Cookie) child).dismiss();
            return;
         }
      }
   }
   
   private void addCookie(final ViewGroup parent, final Cookie cookie) {
      
      if (cookie.getParent() != null) {
         return;
      }
      
      // if exists, remove existing cookie
      int childCount = parent.getChildCount();

      for (int i = 0; i < childCount; i++) {
         
         View child = parent.getChildAt(i);
         
         if (child instanceof Cookie) {
            
            Cookie                         currentCookie   = (Cookie) child;
            final CookieBarDismissListener dismissListener = currentCookie.getDismissListenr();
            
            ((Cookie) child).dismiss(dismissType -> {
               
               if (dismissListener != null) {
                  dismissListener.onDismiss(CookieBarDismissListener.DismissType.REPLACE_DISMISS);
               }
            });
         }
      }
      
      parent.addView(cookie);
   }
   
   public View getView() {
      
      return cookieView;
   }
   
   public static Builder build(Activity activity) {
      
      return new Builder(activity);
   }
   
   public static Builder builder(Activity activity) {
      
      return new Builder(activity);
   }
   
   public static void dismiss(Activity activity) {
      
      new CookieBar(activity, null);
   }
   
   public interface CustomViewInitializer {
      
      void initView(View view);
   }
   
   @SuppressWarnings({"MethodReturnOfConcreteClass", "PublicMethodNotExposedInInterface"})
   public static class Builder {
      
      private final Params   params = new Params();
      private final Activity context;
      
      /**
       * Create a builder for an cookie.
       */
      Builder(Activity activity) {
         
         this.context = activity;
      }
      
      public Builder delay(long delay) {
         
         params.delay = delay;
         return this;
      }
      
      public Builder icon(@DrawableRes int iconResId) {
         
         params.iconResId = iconResId;
         return this;
      }
      
      public Builder title(CharSequence title) {
         
         params.title = title;
         return this;
      }
      
      public Builder title(@StringRes int resId) {
         
         params.title = context.getString(resId);
         return this;
      }
      
      public Builder message(CharSequence message) {
         
         params.message = message;
         return this;
      }
      
      public Builder message(@StringRes int resId) {
         
         params.message = context.getString(resId);
         return this;
      }
      
      public Builder duration(long duration) {
         
         params.duration = duration;
         return this;
      }
      
      public Builder titleColor(@ColorRes int titleColor) {
         
         params.titleColor = titleColor;
         return this;
      }
      
      public Builder messageColor(@ColorRes int messageColor) {
         
         params.messageColor = messageColor;
         return this;
      }
      
      public Builder backgroundColor(@ColorRes int backgroundColor) {
         
         params.backgroundColor = backgroundColor;
         return this;
      }
      
      public Builder actionColor(@ColorRes int actionColor) {
         
         params.actionColor = actionColor;
         return this;
      }
      
      public Builder action(CharSequence action, OnActionClickListener onActionClickListener) {
         
         params.action                = action;
         params.onActionClickListener = onActionClickListener;
         return this;
      }
      
      public Builder iconAnimation(@AnimatorRes int iconAnimation) {
         
         params.iconAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(context, iconAnimation);
         return this;
      }
      
      public Builder action(@StringRes int resId, OnActionClickListener onActionClickListener) {
         
         params.action                = context.getString(resId);
         params.onActionClickListener = onActionClickListener;
         return this;
      }
      
      /**
       * Sets cookie position
       *
       * @param layoutGravity Cookie position, use either CookieBar.TOP or CookieBar.BOTTOM
       * @return builder
       * @deprecated As of CookieBar2 1.1.0, use
       * {@link #cookiePosition(int)} instead.
       */
      @Deprecated
      public Builder layoutGravity(int layoutGravity) {
         
         return cookiePosition(layoutGravity);
      }
      
      /**
       * Sets cookie position
       *
       * @param cookiePosition Cookie position, use either CookieBar.TOP or CookieBar.BOTTOM
       * @return builder
       */
      public Builder cookiePosition(int cookiePosition) {
         
         params.cookiePosition = cookiePosition;
         return this;
      }
      
      public Builder customView(@LayoutRes int customView) {
         
         params.customViewResource = customView;
         return this;
      }
      
      public Builder customViewInitializer(CustomViewInitializer viewInitializer) {
         
         params.viewInitializer = viewInitializer;
         return this;
      }
      
      public Builder animationIn(@AnimRes int topAnimation, @AnimRes int bottomAnimation) {
         
         params.animationInTop    = topAnimation;
         params.animationInBottom = bottomAnimation;
         return this;
      }
      
      public Builder animationOut(@AnimRes int topAnimation, @AnimRes int bottomAnimation) {
         
         params.animationOutTop    = topAnimation;
         params.animationOutBottom = bottomAnimation;
         return this;
      }
      
      public Builder enableAutoDismiss(boolean enableAutoDismiss) {
         
         params.enableAutoDismiss = enableAutoDismiss;
         return this;
      }
      
      public Builder swipeToDismiss(boolean enableSwipeToDismiss) {
         
         params.enableSwipeToDismiss = enableSwipeToDismiss;
         return this;
      }
      
      public Builder cookieListener(CookieBarDismissListener dismissListener) {
         
         params.dismissListener = dismissListener;
         return this;
      }
      
      public CookieBar create() {
         
         return new CookieBar(context, params);
      }
      
      public CookieBar show() {
         
         final CookieBar cookie = create();
         
         new Handler(Looper.getMainLooper()).postDelayed(cookie::show, params.delay);
         
         return cookie;
      }
      
      
   }
   
   final static class Params {
      
      public CharSequence             title;
      public CharSequence             message;
      public CharSequence             action;
      public boolean                  enableSwipeToDismiss = true;
      public boolean                  enableAutoDismiss    = true;
      public int                      iconResId;
      public int                      backgroundColor;
      public int                      titleColor;
      public int                      messageColor;
      public int                      actionColor;
      public long                     duration             = 2000L;
      public long                     delay                = 300L;
      public int                      cookiePosition       = Gravity.TOP;
      public int                      customViewResource;
      public int                      animationInTop       = R.anim.slide_in_from_top;
      public int                      animationInBottom    = R.anim.slide_in_from_bottom;
      public int                      animationOutTop      = R.anim.slide_out_to_top;
      public int                      animationOutBottom   = R.anim.slide_out_to_bottom;
      public CustomViewInitializer    viewInitializer;
      public OnActionClickListener    onActionClickListener;
      public AnimatorSet              iconAnimator;
      public CookieBarDismissListener dismissListener;
   }
}