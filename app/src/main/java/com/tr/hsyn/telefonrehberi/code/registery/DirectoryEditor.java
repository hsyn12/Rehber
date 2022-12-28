package com.tr.hsyn.telefonrehberi.code.registery;


import android.net.Uri;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;

import com.tr.hsyn.launcher.OpenDocumentTreeLauncher;


/**
 * Dizin editörü.
 */
public interface DirectoryEditor extends Directory {

    static void requestDirectoryTreeAccess(@NonNull ComponentActivity activity, ActivityResultCallback<Uri> callback) {

        //noinspection UseOfConcreteClass
        OpenDocumentTreeLauncher launcher = new OpenDocumentTreeLauncher(activity);

        launcher.launch(callback);
    }

    @NonNull
    static DirectoryEditor createInstance() {

        return new MainDirectory();
    }

    void saveDirectoryTree(@NonNull Uri uri);

    void deleteDirectoryTree();


}
