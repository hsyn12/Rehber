package com.tr.hsyn.telefonrehberi.main.activity.color.cast;

import android.app.Activity;


/**
 * <h1>IColorController></h1>
 * 
 * <p>
 *    Renk kontrolcüsü
 * 
 * @author hsyn 2019-12-09 14:46:37
 */
public interface IColorController {
   
   /**
    * Renk seçimi için dialog'u aç.
    * 
    * @param activity activity
    */
   void openChangeColorDialog(Activity activity);
}
