package com.tr.hsyn.text;

import android.text.style.QuoteSpan;


public class QuoteSpanBuilder implements SpanBuilder{
   
   private final Integer color;
   
   public QuoteSpanBuilder(Integer color){
   
      this.color = color;
   }
   
   @Override
   public Object build(){
   
      if(color == null){
   
         return new QuoteSpan();
      }
      
      return new QuoteSpan(color);
   }
   
}
