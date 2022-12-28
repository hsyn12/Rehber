package com.tr.hsyn.bool;


import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class Bool implements Any<Boolean> {

    public static final     Bool    FALSE = new Bool();
    public static final     Bool    TRUE  = new Bool(true);
    public static final     Bool    NONE  = new Bool(null);
    @Nullable private final Boolean value;

    public Bool() {

        value = false;
    }

    public Bool(@Nullable Boolean value) {

        this.value = value;
    }

    /**
     * @return Saklanan değer {@code null} ise false, yada saklanan değer
     */
    public boolean bool() {

        return value != null && value;
    }

    @Nullable
    @Override
    public Boolean getObject() {

        return value;
    }

    @Override
    public boolean equals(Object o) {

        return o instanceof Bool && Objects.equals(value, ((Bool) o).value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value);
    }
}
