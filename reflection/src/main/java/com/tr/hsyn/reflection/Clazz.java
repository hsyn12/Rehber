package com.tr.hsyn.reflection;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Reflection tool
 */
public final class Clazz {
	
	@Nullable
	private static Class<?>[] getTypes(@Nullable final Object... parameters) {
		
		if (parameters != null)
			return Arrays.stream(parameters).filter(Objects::nonNull).map(Object::getClass).toArray(Class[]::new);
		
		return null;
	}
	
	/**
	 * Verilen {@linkplain Class} sınıfından nesne oluştur.
	 *
	 * @param clazz      Sınıf
	 * @param parameters Kurucu argümanları
	 * @param <T>        Sınıf türü
	 * @return Nesne
	 */
	@Nullable
	public static <T> T create(@NotNull final Class<T> clazz, @Nullable final Object... parameters) {
		
		try {
			//noinspection ConstantConditions
			return findConstructor(clazz, getTypes(parameters)).newInstance(parameters);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Sınıfın kurucusunu bul.
	 * Sadece public.
	 *
	 * @param clazz          Sınıf
	 * @param parameterTypes Kurucunun parametre türleri
	 * @param <T>            Sınıf türü
	 * @return Kurucu
	 */
	@Nullable
	public static <T> Constructor<T> findConstructor(@NotNull Class<T> clazz, @Nullable Class<?>... parameterTypes) {
		
		try {
			return clazz.getConstructor(parameterTypes);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Metot ara.
	 * Aranan metot private olabilir.
	 * Arama, sınıfın üst sınıflarında da yapılır.
	 * Static metotlar hariç.
	 *
	 * @param clazz          Sınıf
	 * @param methodName     Aranan metodun adı
	 * @param parameterTypes Aranan metodun parametre türleri
	 * @param <T>            Sınıf türü
	 * @return {@linkplain Method}
	 */
	@Nullable
	public static <T> Method findMethod(@NotNull Class<? super T> clazz, @NotNull String methodName, @Nullable Class<?>... parameterTypes) {
		
		do {
			
			try {return clazz.getDeclaredMethod(methodName, parameterTypes);}
			catch (NoSuchMethodException ignored) {}
			
			clazz = clazz.getSuperclass();
			
		} while (clazz != null && clazz != Object.class);
		
		return null;
	}
	
	public static <T> Method findMethod(@NotNull Class<? super T> clazz, @NotNull String methodName, @Nullable Object... args) {
		
		return findMethod(clazz, methodName, convertTypes(args));
	}
	
	/**
	 * Metot ara.
	 * Aranan metot private olabilir.
	 * Arama, sınıfın üst sınıflarında yapılmaz.
	 * Static metotlar aranmaz.
	 *
	 * @param clazz          Sınıf
	 * @param methodName     Aranan metodun adı
	 * @param parameterTypes Aranan metodun parametre türleri
	 * @param <T>            Sınıf türü
	 * @return {@linkplain Method}
	 */
	@Nullable
	public static <T> Method findMethodThis(@NotNull Class<T> clazz, @NotNull String methodName, @Nullable Class<?>... parameterTypes) {
		
		try {return clazz.getDeclaredMethod(methodName, parameterTypes);}
		catch (NoSuchMethodException ignored) {}
		
		return null;
	}
	
	/**
	 * Metot ara.
	 * Aranan metot private olabilir.
	 * Arama, sınıfın üst sınıflarında da yapılır.
	 * Static metotlar aranmaz.
	 *
	 * @param clazzs         Nesne
	 * @param methodName     Aranan metodun adı
	 * @param parameterTypes Aranan metodun parametre türleri
	 * @param <T>            Sınıf türü
	 * @return {@linkplain Method}
	 */
	@Nullable
	public static <T> Method findMethod(@NotNull Object clazzs, @NotNull String methodName, @Nullable Class<?>... parameterTypes) {
		
		var clazz = clazzs.getClass();
		
		do {
			
			try {return clazz.getDeclaredMethod(methodName, parameterTypes);}
			catch (NoSuchMethodException ignored) {}
			
			clazz = clazz.getSuperclass();
			
		} while (clazz != null && clazz != Object.class);
		
		return null;
	}
	
	/**
	 * Metot ara.
	 * Aranan metot private olabilir.
	 * Arama, sınıfın üst sınıflarında yapılmaz.
	 * Static metotlar aranmaz.
	 *
	 * @param clazzs         Nesne
	 * @param methodName     Aranan metodun adı
	 * @param parameterTypes Aranan metodun parametre türleri
	 * @param <T>            Sınıf türü
	 * @return {@linkplain Method}
	 */
	@Nullable
	public static <T> Method findMethodThis(@NotNull Object clazzs, @NotNull String methodName, @Nullable Class<?>... parameterTypes) {
		
		var clazz = clazzs.getClass();
		
		try {return clazz.getDeclaredMethod(methodName, parameterTypes);}
		catch (NoSuchMethodException ignored) {}
		
		return null;
	}
	
	/**
	 * Verilen sınıfın, verilen annotation sınıfı ile
	 * işaretlenmiş tüm metotlarını döndür.
	 * Private metotlar dahil.
	 * Sınıfın üst sınıfları da dahil.
	 * Static metotlar hariç.
	 *
	 * @param clazz           Sınıf
	 * @param annotationClazz Annotation sınıfı
	 * @param <A>             Annotation türü
	 * @param <T>             Sınıf türü
	 * @return Metot listesi
	 */
	@NotNull
	public static <A extends Annotation, T> List<Method> getMethodsAnnotated(@NotNull Class<? super T> clazz, @NotNull Class<A> annotationClazz) {
		
		List<Method> methods = new LinkedList<>();
		
		do {
			var _methods = clazz.getDeclaredMethods();
			methods.addAll(Arrays.stream(_methods).filter(m -> m.getAnnotation(annotationClazz) != null).collect(Collectors.toList()));
			clazz = clazz.getSuperclass();
		} while (clazz != null && clazz != Object.class);
		
		return methods;
	}
	
	/**
	 * Verilen sınıfın, verilen annotation sınıfı ile
	 * işaretlenmiş tüm metotlarını döndür.
	 * Private metotlar dahil.
	 * Sınıfın üst sınıfları dahil değil.
	 * Static metotlar dahil değil.
	 *
	 * @param clazz           Sınıf
	 * @param annotationClazz Annotation sınıfı
	 * @param <A>             Annotation türü
	 * @param <T>             Sınıf türü
	 * @return Metot listesi
	 */
	public static <A extends Annotation, T> List<Method> getMethodsAnnotatedThis(@NotNull Class<T> clazz, @NotNull Class<A> annotationClazz) {
		
		var _methods = clazz.getDeclaredMethods();
		
		return Arrays.stream(_methods).filter(m -> m.getAnnotation(annotationClazz) != null).collect(Collectors.toCollection(LinkedList::new));
	}
	
	/**
	 * Tüm metotları al.
	 * Private metotlar dahil.
	 * Sınıfın üst sınıfları da dahil.
	 * Static metotlar hariç.
	 *
	 * @param clazz Sınıf
	 * @param <T>   Sınıf türü
	 * @return Metot listesi
	 */
	@NotNull
	public static <T> List<Method> getMethods(@NotNull Class<? super T> clazz) {
		
		List<Method> methods = new LinkedList<>();
		
		do {
			methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
			clazz = clazz.getSuperclass();
		} while (clazz != null && clazz != Object.class);
		
		return methods;
	}
	
	/**
	 * Tüm metotları al.
	 * Private metotlar dahil.
	 * Sınıfın üst sınıfları dahil değil.
	 * Static metotlar dahil değil.
	 *
	 * @param clazz Sınıf
	 * @param <T>   Sınıf türü
	 * @return Metot listesi
	 */
	@NotNull
	public static <T> List<Method> getMethodsThis(@NotNull final Class<T> clazz) {
		
		return new LinkedList<>(Arrays.asList(clazz.getDeclaredMethods()));
	}
	
	public static void invoke(@NotNull Method method, @Nullable Object object, @Nullable Object... args) {
		
		try {method.invoke(object, args);}
		catch (Exception e) {e.printStackTrace();}
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T invoke(@Nullable Object object, @NotNull Method method, @Nullable Object... args) {
		
		try {return (T) method.invoke(object, args);}
		catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}
		
		return null;
	}
	
	@Nullable
	public static <T extends Annotation> T getAnnotation(@NotNull AnnotatedElement element, @NotNull Class<T> annotationClass) {
		
		return element.getAnnotation(annotationClass);
	}
	
	//@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> List<T> getAnnotations(AnnotatedElement element, @Nullable Class<T>... annotations) {
		
		if (annotations != null && annotations.length > 0)
			return Arrays.stream(annotations).filter(Objects::nonNull).map(a -> getAnnotation(element, a)).collect(Collectors.toList());
		
		return new ArrayList<>(Arrays.asList((T[]) element.getAnnotations()));
	}
	
	public static List<Field> getDeclaredFields(@NotNull Class<?> clazz, int modifier) {
		
		return Arrays.stream(clazz.getDeclaredFields()).filter(f -> (f.getModifiers() & modifier) == modifier).collect(Collectors.toList());
	}
	
	public static List<Field> getDeclaredFields(@NotNull Class<?> clazz, Class<?> type) {
		
		return Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.getType() == type).collect(Collectors.toList());
	}
	
	public static List<Field> getDeclaredFields(@NotNull Class<?> clazz) {
		
		return Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
	}
	
	@NotNull
	@Contract(pure = true)
	public static Class<?>[] convertTypes(Object... args) {
		
		if (args.length == 0) return new Class<?>[0];
		
		var types = new Class<?>[args.length];
		
		for (int i = 0; i < args.length; i++) types[i] = args[i].getClass();
		
		return types;
	}
	
	public static void main(String... args) {
		
		//int modifiers = Modifier.FINAL | Modifier.PUBLIC | Modifier.STATIC;
		
		var c = getDeclaredFields(Clazz.class);
		
		for (int i = 0; i < c.size(); i++) {
			
			var f = c.get(i);
			
			System.out.printf("%s - %s\n", f.getName(), f.getType().getCanonicalName());
		}
		
		
	}
	
	public static int getIntStatic(@NotNull Class<?> clazz, @NotNull String fieldName) {
		
		try {
			
			var field = clazz.getDeclaredField(fieldName);
			
			field.setAccessible(true);
			return field.getInt(null);
		}
		catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		return Integer.MAX_VALUE;
	}
	
	public static int getInt(@NotNull Object o, @NotNull String fieldName) {
		
		try {
			
			var field = o.getClass().getDeclaredField(fieldName);
			
			field.setAccessible(true);
			
			return field.getInt(o);
		}
		catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		return Integer.MAX_VALUE;
	}
	
}