package com.tr.hsyn.telefonrehberi.app;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

public interface Res {

    interface Calls {

        /**
         * Tüm arama kayıtları
         */
        int FILTER_ALL           = 0;
        /**
         * Gelen arama kayıtları
         */
        int FILTER_INCOMING      = 1;
        /**
         * Giden arama kayıtları
         */
        int FILTER_OUTGOING      = 2;
        /**
         * Cevapsız arama kayıtları
         */
        int FILTER_MISSED        = 3;
        /**
         * Reddedilen arama kayıtları
         */
        int FILTER_REJECTED      = 4;
        /**
         * İsimsiz arama kayıtları
         */
        int FILTER_NO_NAMED      = 5;
        /**
         * Rastgele üretilen arama kayıtları
         */
        int FILTER_RANDOM        = 6;
        /**
         * En çok arayanlar
         */
        int FILTER_MOST_INCOMING = 7;
        /**
         * En çok arananlar
         */
        int FILTER_MOST_OUTGOING = 8;
        /**
         * En çok cevapsız çağrı bırakanlar
         */
        int FILTER_MOST_MISSED   = 9;
        /**
         * En çok reddedilenler
         */
        int FILTER_MOST_REJECTED = 10;
        /**
         * En çok konuşanlar
         */
        int FILTER_MOST_SPEAKING = 11;
        /**
         * En çok konuştukların
         */
        int FILTER_MOST_TALKING  = 12;


        /**
         * Arama kayıtlarında kullanılan filtreleme seçeneklerinin isim karşılığını döndürür.
         *
         * @param context context
         * @param filter  Filtreleme türü
         * @return Filtre ismi
         */
        static String getCallFilterName(@NotNull Context context, int filter) {

            //var filters = context.getResources().getStringArray(R.array.call_filter_items);

            //return filters[filter];
            
            return null;
        }
    }


}
