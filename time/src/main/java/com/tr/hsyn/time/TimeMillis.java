package com.tr.hsyn.time;


import java.util.concurrent.TimeUnit;


/**
 * Zaman birimlerinin milisaniye değerlerini tutar.
 * Bu değerler sabittir ve en kaba şekilde değerlendirilir.<br><br>
 * <p>
 * Mesela 1 gün daima 24 saattir.<br>
 * Mesela 1 hafta daima 7 gündür.<br>
 * Mesela 1 ay daima 30 gündür.<br>
 * Mesela 1 yıl daima 365 gündür.<br><br>
 *
 * <p>
 * Bu sebeple, bu değerler üzerinden ince hesap yapmak doğru değildir.
 * </p>
 */
public interface TimeMillis {

    long SECOND = 1000L;
    long MINUTE = TimeUnit.MINUTES.toMillis(1); //my favor//
    long HOUR   = TimeUnit.HOURS.toMillis(1);
    long DAY    = TimeUnit.DAYS.toMillis(1);
    long MONTH  = 30L * DAY;
    long YEAR   = 365L * DAY;
}
