package com.tr.hsyn.telefonrehberi.dev.registery;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.telefonrehberi.main.dev.Over;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * File Register.<br>
 * Tüm kayıtlar uygulamaya özel dizinde gerçekleşir.
 * Bu dizin için herhangi bir izin gerekmez, ancak
 * uygulama kaldırıldığında tüm kayıtlar silinir.
 */
public class FileRegister implements Register {
	
	/**
	 * Default directory name in the system files directory
	 */
	private static final String DEFAULT_ROOM = "badbox";
	/**
	 * Current room
	 */
	private final        File   mainDirectory;
	
	
	public FileRegister() {
		
		this(DEFAULT_ROOM);
	}
	
	public FileRegister(@NonNull String room) {
		
		Context context = Over.App.getContext();
		mainDirectory = new File(context.getFilesDir(), room.trim().isEmpty() ? DEFAULT_ROOM : room);
		
		if (!mainDirectory.exists()) //noinspection ResultOfMethodCallIgnored
			mainDirectory.mkdirs();
	}
	
	@Override
	public <T> void write(@NonNull String key, @NonNull T object) {
		
		try (FileWriter writer = new FileWriter(getFile(key))) {
			
			Register.GSON.toJson(object, writer);
			
			writer.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public <T> void write(@NonNull String key, @NonNull List<T> objectList) {
		
		try (FileWriter writer = new FileWriter(getFile(key))) {
			
			Register.GSON.toJson(objectList, writer);
			
			writer.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Nullable
	@Override
	public <T> T read(@NonNull String key, @NonNull Class<T> clazz) {
		
		try (FileReader reader = new FileReader(getFile(key))) {
			
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
		
		try (FileReader reader = new FileReader(getFile(key))) {
			
			return Register.GSON.fromJson(reader, Register.getTypeOfList(clazz));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public boolean exist(@NotNull String key) {
		
		return getKeys().contains(key);
	}
	
	@NonNull
	@Override
	public List<String> getKeys() {
		
		String[] list = mainDirectory.list();
		
		if (list != null) return Arrays.asList(list);
		
		return new ArrayList<>(0);
	}
	
	@Override
	public boolean delete(@NotNull String key) {
		
		return getFile(key).delete();
	}
	
	@NonNull
	private File getFile(@NonNull String key) {
		
		return new File(mainDirectory, key);
	}
}
