package com.tr.hsyn.telefonrehberi.dev.registery;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.tr.hsyn.telefonrehberi.main.dev.Over;


/**
 * Uygulamanın kullanacağı android dosya sistemindeki ana dizin.
 * Bu dizin kullanıcı tarafından seçilir.
 * Uygulamanın ana dizinini temsil eder.
 */
public interface Directory {
	
	/**
	 * @return Seçilen dizine ait sistem tarafından verilen ve dizini temsil eden {@code uri} nesnesi
	 */
	@Nullable
	Uri getDirectoryTree();
	
	/**
	 * Ana dizinin altında döküman arar.
	 *
	 * @param name aranan dökümanın ismi. dosya uzantısı olmasın
	 * @return DocumentFile, yoksa {@code null}
	 */
	@Nullable
	static DocumentFile findFile(@NonNull String name) {
		
		DocumentFile document = getRootDocument();
		
		return document != null ? document.findFile(name) : null;
	}
	
	/**
	 * @return kullanıcının seçtiği ana klasör. Seçim yapılmamışsa {@code null}
	 */
	@Nullable
	static DocumentFile getRootDocument() {
		
		Directory mainDirectory = DirectoryEditor.createInstance();
		
		Uri directoryTree = mainDirectory.getDirectoryTree();
		
		if (directoryTree == null) return null;
		
		return DocumentFile.fromTreeUri(Over.App.getContext(), directoryTree);
	}
	
	/**
	 * Seçili dizinin okunabilir olup olmadığını kontrol eder.
	 *
	 * @return dizin okunabilirse {@code true}, diğer tüm hallerde {@code false} (mesela dizin seçilmemişse)
	 */
	static boolean canRead() {
		
		DocumentFile document = getRootDocument();
		
		return document != null && document.canRead();
	}
	
	/**
	 * Seçili dizinin yazılabilir olup olmadığını kontrol eder.
	 *
	 * @return dizin yazılabilirse {@code true}, diğer tüm hallerde {@code false} (mesela dizin seçilmemişse)
	 */
	static boolean canWrite() {
		
		DocumentFile document = getRootDocument();
		
		return document != null && document.canWrite();
	}
	
}
