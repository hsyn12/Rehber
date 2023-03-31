package com.tr.hsyn.text;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;


public class ClickSpanBuilder implements SpanBuilder {
   
   private final View.OnClickListener listener;
   private       int                  textColor = Color.WHITE;
   
   ClickSpanBuilder(View.OnClickListener listener, int textColor) {
      
      this.listener = listener;
      this.textColor = textColor;
   }
   ClickSpanBuilder(View.OnClickListener listener) {
      
      this.listener = listener;
   }
   
   @Override
   public Object build() {
      
      return new EasyClickableSpan(listener, textColor);
   }
   
   
   private static class EasyClickableSpan extends ClickableSpan {
      
      private View.OnClickListener listener;
      private int textColor = Color.WHITE;
      
      EasyClickableSpan(View.OnClickListener listener, int textColor) {
         
         this.listener = listener;
         this.textColor = textColor;
      }
      
      public void setListener(View.OnClickListener listener) {
         
         this.listener = listener;
      }
      
      @Override
      public void updateDrawState(@NonNull TextPaint ds) {
         
         super.updateDrawState(ds);
         ds.setColor(textColor);
      }
      
      @Override
      public void onClick(@NonNull View widget) {
         
         if (listener != null) listener.onClick(widget);
      }
      
   }
   
}
