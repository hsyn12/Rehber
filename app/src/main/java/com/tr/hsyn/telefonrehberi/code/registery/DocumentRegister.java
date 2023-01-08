package com.tr.hsyn.telefonrehberi.code.registery;


import android.content.ContentResolver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.tr.hsyn.telefonrehberi.main.dev.Over;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Document Register.<br>
 * Tüm kayıt işlemleri android dosya sisteminde kullanıcının seçtiği herhangi bir
 * dizin içinde gerçekleşir. Sınıfın bazı kurucuları bu dizin seçiminin
 * yapılmış olduğunu varsayar.
 *
 * <ul>
 *    <li>{@link #DocumentRegister()}</li>
 *    <li>{@link #DocumentRegister(String)}</li>
 *    <li>{@link #DocumentRegister(ContentResolver)}</li>
 *    <li>{@link #DocumentRegister(ContentResolver, String)}</li>
 * </ul>
 */
public class DocumentRegister implements Register {
	
	/**
	 * Default directory name in the system files directory
	 */
	private static final String          DEFAULT_ROOM = "docbox";
	/**
	 * Kayıtların içinde olacağı dizin.
	 */
	private final        DocumentFile    directory;
	private final        ContentResolver resolver;
	
	public DocumentRegister() {
		
		this(DEFAULT_ROOM);
	}
	
	public DocumentRegister(@NonNull String directoryName) {
		
		this(Over.App.getContext().getContentResolver(), directoryName);
		
		/*this.resolver = Over.App.getContext().getContentResolver();
		var doc = DirectoryEditor.createInstance();
		
		var main = DocumentFile.fromTreeUri(Over.App.getContext(), Objects.requireNonNull(doc.getDirectoryTree()));
		
		if (main != null) {
			
			var leaf = main.findFile(directoryName);
			
			if (leaf == null) directory = main.createDirectory(directoryName);
			else directory = leaf;
		}
		else throw new IllegalArgumentException("Ana dizin bulunamadı. Bu kurucu ana dizinin daha önce kullanıcıdan alınmış olduğunu varsayar");
	*/
	}
	
	public DocumentRegister(@NonNull ContentResolver resolver) {
		
		this(resolver, DEFAULT_ROOM);
	}
	
	public DocumentRegister(@NonNull ContentResolver resolver, @NonNull String directoryName) {
		
		this(resolver, Objects.requireNonNull(DocumentFile.fromTreeUri(Over.App.getContext(), Objects.requireNonNull(DirectoryEditor.createInstance().getDirectoryTree()))), directoryName);
	}
	
	public DocumentRegister(@NonNull ContentResolver resolver, @NonNull DocumentFile directory) {
		
		this(resolver, directory, DEFAULT_ROOM);
	}
	
	/**
	 * @param resolver      resolver
	 * @param mainDirectory bu dizin kullanıcıdan alınmalı ve var olmalı
	 * @param directoryName ana dizinin altındaki işlem yapılacak dizinin adı. Bu dizin olmak zorunda değil, varsa kullanıır yoksa oluşturulur
	 */
	public DocumentRegister(@NonNull ContentResolver resolver, @NonNull DocumentFile mainDirectory, @NonNull String directoryName) {
		
		this.resolver = resolver;
		
		if (!mainDirectory.canRead()) {
			
			throw new IllegalArgumentException(String.format("Ana dizine erişim yok [%s]", mainDirectory));
		}
		
		var dir = mainDirectory.findFile(directoryName);
		
		if (dir == null) dir = mainDirectory.createDirectory(directoryName);
		
		this.directory = dir;
		
		
		if (dir == null) {
			
			throw new IllegalArgumentException(String.format("Ana dizin altında kullanılacak dizine erişim sağlanamadı [%s/%s]", mainDirectory, directoryName));
		}
	}
	
	@Override
	public <T> void write(@NonNull String key, @NonNull T object) {
		
		var file = getFile(key);
		
		try {
			if (file == null) throw new AssertionError();
			try (var writer = new FileWriter(File.getFileDescriptorW(resolver, file.getUri()))) {
				
				Register.GSON.toJson(object, writer);
				
				writer.flush();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public <T> void write(@NonNull String key, @NonNull List<T> objectList) {
		
		var file = getFile(key);
		
		try {
			
			if (file == null) throw new AssertionError();
			try (var writer = new FileWriter(File.getFileDescriptorW(resolver, file.getUri()))) {
				
				Register.GSON.toJson(objectList, writer);
				
				writer.flush();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Nullable
	@Override
	public <T> T read(@NonNull String key, @NonNull Class<T> clazz) {
		
		var file = getFile(key);
		
		try (var reader = new FileReader(File.getFileDescriptor(resolver, file.getUri()))) {
			
			return Register.GSON.fromJson(reader, clazz);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Nullable
	@Override
	public <T> List<T> readList(@NonNull String key, @NonNull Class<T> clazz) {
		
		var file = getFile(key);
		
		try (var reader = new FileReader(File.getFileDescriptor(resolver, file.getUri()))) {
			
			return Register.GSON.fromJson(reader, Register.getTypeOfList(clazz));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public boolean exist(@NonNull String key) {
		
		return directory.findFile(key) != null;
	}
	
	@NonNull
	@Override
	public List<String> getKeys() {
		
		var files = directory.listFiles();
		
		//noinspection ConstantConditions
		if (files == null) return new ArrayList<>(0);
		
		return Arrays.stream(files).map(DocumentFile::getName).collect(Collectors.toList());
	}
	
	@Override
	public boolean delete(@NonNull String key) {
		
		var file = getFile(key);
		
		if (file != null) return file.delete();
		
		return false;
	}
	
	
	/**
	 * İstenen dosyayı döndürür.
	 * Eğer dosya yoksa oluşturur.
	 *
	 * @param key dosya adı
	 * @return DocumentFile
	 */
	private DocumentFile getFile(@NonNull String key) {
		
		var file = directory.findFile(key);
		
		if (file != null) return file;
		
		return directory.createFile("text/*", key);
	}
	
}
