package com.tr.hsyn.text;

import android.text.style.StyleSpan;


public class StyleSpanBuilder implements SpanBuilder{
   
   private final int style;
   
   public StyleSpanBuilder(int style){
      
      this.style = style;
   }
   
   @Override
   public Object build(){
      
      return new StyleSpan(style);
   }
   
}
