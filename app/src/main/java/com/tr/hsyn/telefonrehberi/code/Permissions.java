package com.tr.hsyn.telefonrehberi.code;


import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.Arrays;


public class Permissions {

    public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {

        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasPermissions(@NonNull Context context, @NonNull String... permissions) {

        return Arrays.stream(permissions).allMatch(perm -> hasPermission(context, perm));
    }
}
