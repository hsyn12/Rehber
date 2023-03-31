package com.tr.hsyn.text;

import android.text.style.LeadingMarginSpan;


public class LeadingMarginSpanBuilder implements SpanBuilder{
   
   private final int     everyOrFirst;
   private final Integer rest;
   
   public LeadingMarginSpanBuilder(int everyOrFirst, Integer rest){
      
      this.everyOrFirst = everyOrFirst;
      this.rest         = rest;
   }
   
   @Override
   public Object build(){
   
      if(rest != null){
         return new LeadingMarginSpan.Standard(everyOrFirst, rest);
      }
      else{
         return new LeadingMarginSpan.Standard(everyOrFirst);
      }
   }
   
}
