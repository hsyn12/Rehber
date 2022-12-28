package com.tr.hsyn.telefonrehberi.main.code.contact.cast;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public interface ContactData {

    @NotNull
    static ContactData newData(String value, int type) {

        class Dat implements ContactData {

            private final String value;
            private final int    type;

            Dat(String value, int type) {

                this.value = value;
                this.type  = type;
            }

            @Override
            public String getValue() {

                return value;
            }

            @Override
            public int getType() {

                return type;
            }

            @Override
            public boolean equals(Object obj) {

                return obj instanceof ContactData && type == ((ContactData) obj).getType() && value.equals(((ContactData) obj).getValue());
            }

            @Override
            public int hashCode() {

                return Objects.hash(type, value);
            }

            @NonNull
            @Override
            public String toString() {

                return "Data{" +
                       "value='" + value + '\'' +
                       ", type=" + type +
                       '}';
            }
        }

        return new Dat(value, type);
    }

    String getValue();

    int getType();
}
