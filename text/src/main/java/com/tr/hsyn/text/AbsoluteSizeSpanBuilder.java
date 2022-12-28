package com.tr.hsyn.text;

import android.text.style.AbsoluteSizeSpan;


public class AbsoluteSizeSpanBuilder implements SpanBuilder{
   
   private final int     size;
   private final boolean dip;
   
   public AbsoluteSizeSpanBuilder(int size, boolean dip){
      
      this.size = size;
      this.dip  = dip;
   }
   
   @Override
   public Object build(){
      
      return new AbsoluteSizeSpan(size, dip);
   }
   
}
