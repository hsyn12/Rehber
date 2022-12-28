package com.tr.hsyn.calldata;


import java.io.Serializable;


/**
 * interface for android system call
 */
public interface SystemCall extends CallContact, CallTime, CallDuration, CallType, Serializable {

    /**
     * @return {@code true} If this call is randomly generated.
     */
    boolean isRandom();

    /**
     * {@code 0}  hiçbir bilgi olmadığını,<br>
     * {@code -1} takip edilmediğini,<br>
     * {@code 1} takip edildiğini gösterir.
     *
     * @return takip türü
     */
    int getTrackType();

    /**
     * @return Aramanın takip bilgisi ve rastgele olup olmadığını bildiren bir string
     */
    String getExtra();

    void setExtra(String extra);


}