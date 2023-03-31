package com.tr.hsyn.text;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class RoundedBackground extends ReplacementSpan implements SpanBuilder {
   
   private final       int   cornerRadius;
   private final       float padding;
   private final       int   paddingTop;
   private final       int   paddingBottom;
   private final       int   backgroundColor;
   private final       int   textColor;
   private final       RectF rect                     = new RectF();
   public static final float DEFAULT_PADDIND          = 28.F;
   public static final int   DEFAULT_CORNER_RADIUS    = 20;
   public static final int   DEFAULT_TEXT_COLOR       = Color.parseColor("#c0c0c0");
   public static final int   DEFAULT_BACKGROUND_COLOR = Color.WHITE;
   
   
/*   public RoundedBackgroundSpan() {
      
      this(DEFAULT_BACKGROUND_COLOR, DEFAULT_TEXT_COLOR, DEFAULT_PADDIND, DEFAULT_CORNER_RADIUS);
   }
   
   public RoundedBackgroundSpan(int backgroundColor) {
      
      this(backgroundColor, DEFAULT_TEXT_COLOR, DEFAULT_PADDIND, DEFAULT_CORNER_RADIUS);
   }
   
   public RoundedBackgroundSpan(int backgroundColor, float padding) {
      
      this(backgroundColor, DEFAULT_TEXT_COLOR, padding, DEFAULT_CORNER_RADIUS);
   }
   
   public RoundedBackgroundSpan(int backgroundColor, int textColor) {
      
      this(backgroundColor, textColor, DEFAULT_PADDIND, DEFAULT_CORNER_RADIUS);
   }
   
   public RoundedBackgroundSpan(int backgroundColor, int textColor, int padding) {
      
      this(backgroundColor, textColor, padding, DEFAULT_CORNER_RADIUS);
   }*/
   
   //@Builder
   public RoundedBackground(int backgroundColor, int textColor, float padding, int cornerRadius, int paddingTop, int paddingBottom) {
      
      super();
      this.backgroundColor = backgroundColor;
      this.textColor       = textColor;
      this.padding         = padding;
      this.cornerRadius    = cornerRadius;
      this.paddingTop      = paddingTop;
      this.paddingBottom   = paddingBottom;
   }
   
   @Override
   public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
      
      return Math.round(paint.measureText(text, start, end) + padding);
   }
   
   @Override
   public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
      
      float width = paint.measureText(text, start, end);
      rect.set(x, top + paddingTop, x + width + padding, bottom + paddingBottom);
      paint.setColor(backgroundColor);
      canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
      
      paint.setColor(textColor);
      int xPos = Math.round(x + (((int) padding) >> 1));
      canvas.drawText(text, start, end, xPos, y, paint);
      
   }
   
   @Override
   public Object build() {
      
      return this;
   }
   
   @SuppressWarnings("FieldMayBeFinal")
	public static class RoundedBackgroundBuilder {
      
      private int   cornerRadius    = DEFAULT_CORNER_RADIUS;
      private float padding         = DEFAULT_PADDIND;
      private int   backgroundColor = DEFAULT_BACKGROUND_COLOR;
      private int   textColor       = DEFAULT_TEXT_COLOR;
      private int   paddingTop      = 0;
      private int   paddingBottom   = 0;
      
   }
}
