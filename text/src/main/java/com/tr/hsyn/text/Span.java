package com.tr.hsyn.text;

public class Span{
   
   private final SpanBuilder builder;
   
   public Span(SpanBuilder builder){
      
      this.builder = builder;
   }
   
   Object buildSpan(){
      
      return builder.build();
   }
}
