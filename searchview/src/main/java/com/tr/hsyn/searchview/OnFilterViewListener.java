package com.tr.hsyn.searchview;



import com.tr.hsyn.searchview.model.Filter;

import java.util.List;


public interface OnFilterViewListener{
   
   void onFilterAdded(Filter filter);
   
   void onFilterRemoved(Filter filter);
   
   void onFilterChanged(List<Filter> list);
}
