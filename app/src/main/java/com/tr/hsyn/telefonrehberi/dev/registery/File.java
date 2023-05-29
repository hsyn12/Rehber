package com.tr.hsyn.telefonrehberi.dev.registery;


import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;


public interface File {

    @Nullable
    static OutputStream createOutput(@NonNull ContentResolver resolver, @NonNull Uri documentUri) {

        try {
            return resolver.openOutputStream(documentUri);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    static InputStream createInput(@NonNull ContentResolver resolver, @NonNull Uri documentUri) {

        try {
            return resolver.openInputStream(documentUri);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    static FileDescriptor getFileDescriptor(@NonNull ContentResolver resolver, @NonNull Uri documentUri) {

        try {
            return resolver.openFileDescriptor(documentUri, "r").getFileDescriptor();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    static FileDescriptor getFileDescriptorW(@NonNull ContentResolver resolver, @NonNull Uri documentUri) {

        try {
            return resolver.openFileDescriptor(documentUri, "w").getFileDescriptor();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
