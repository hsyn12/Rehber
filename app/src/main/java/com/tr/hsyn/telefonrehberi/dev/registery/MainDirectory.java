package com.tr.hsyn.telefonrehberi.dev.registery;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.paperdb.Book;
import io.paperdb.Paper;


/**
 * Uygulamanın ana dizin kaydı.
 */
public class MainDirectory implements DirectoryEditor {

    private final String key                   = "main_directory";
    private final Book   directoryRegisterData = Paper.book("directory_data");

    @Nullable
    @Override
    public Uri getDirectoryTree() {

        var rootDirectory = directoryRegisterData.read(key, "");

        assert rootDirectory != null;

        if (!rootDirectory.isEmpty())
            return Uri.parse(rootDirectory);

        return null;
    }

    @Override
    public void saveDirectoryTree(@NonNull Uri uri) {

        directoryRegisterData.write(key, uri.toString());
    }

    @Override
    public void deleteDirectoryTree() {

        directoryRegisterData.delete(key);
    }
}
