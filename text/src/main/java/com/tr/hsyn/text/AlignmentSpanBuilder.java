package com.tr.hsyn.text;

import android.text.Layout;
import android.text.style.AlignmentSpan;


public class AlignmentSpanBuilder implements SpanBuilder{
   
   private final Layout.Alignment alignment;
   
   public AlignmentSpanBuilder(Layout.Alignment alignment){
      
      this.alignment = alignment;
   }
   
   @Override
   public Object build(){
      
      return new AlignmentSpan.Standard(alignment);
   }
   
}
