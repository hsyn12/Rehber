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
	 * Creates an object from the given {@linkplain Class} class.
	 *
	 * @param clazz      class to create an object from
	 * @param parameters parameters for constructor
	 * @param <T>        type of the object
	 * @return object created from the given {@linkplain Class}
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
	 * Finds the constructor of the given {@linkplain Class}.
	 *
	 * @param clazz          class
	 * @param parameterTypes parameter types of the constructor
	 * @param <T>            type of the object
	 * @return {@linkplain Constructor}
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
	 * Search method.
	 * The method can be private.
	 * The search is also done in the superclasses of the class.
	 * Except for static methods.
	 *
	 * @param clazz          class to search the method in
	 * @param methodName     name of the method
	 * @param parameterTypes types of the method
	 * @param <T>            type of the object
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
	
	/**
	 * Search method.
	 * The method can be private.
	 * The search is also done in the superclasses of the class.
	 * Except for static methods.
	 *
	 * @param clazz      class to search the method in
	 * @param methodName name of the method
	 * @param args       arguments of the method
	 * @param <T>        type of the object
	 * @return {@linkplain Method}
	 */
	public static <T> Method findMethod(@NotNull Class<? super T> clazz, @NotNull String methodName, @Nullable Object... args) {
		
		return findMethod(clazz, methodName, convertToTypes(args));
	}
	
	/**
	 * Search method.
	 * The method can be private.
	 * The search is not done in the superclasses of the class.
	 * Static methods are not included.
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
	 * Search method.
	 * The method can be private.
	 * The search is also done in the superclasses of the class.
	 * Except for static methods.
	 *
	 * @param object         object to search the method in
	 * @param methodName     name of the method
	 * @param parameterTypes types of the method parameters
	 * @return {@linkplain Method}
	 */
	@Nullable
	public static Method findMethod(@NotNull Object object, @NotNull String methodName, @Nullable Class<?>... parameterTypes) {
		
		Class<?> clazz = object.getClass();
		
		do {
			
			try {return clazz.getDeclaredMethod(methodName, parameterTypes);}
			catch (NoSuchMethodException ignored) {}
			
			clazz = clazz.getSuperclass();
			
		} while (clazz != null && clazz != Object.class);
		
		return null;
	}
	
	/**
	 * Search method.
	 * The method can be private.
	 * The search is not done in the superclasses of the class.
	 * Static methods are not included.
	 *
	 * @param object         object to search the method in
	 * @param methodName     Aranan metodun adı
	 * @param parameterTypes Aranan metodun parametre türleri
	 * @param <T>            Sınıf türü
	 * @return {@linkplain Method}
	 */
	@Nullable
	public static <T> Method findMethodThis(@NotNull Object object, @NotNull String methodName, @Nullable Class<?>... parameterTypes) {
		
		Class<?> clazz = object.getClass();
		
		try {return clazz.getDeclaredMethod(methodName, parameterTypes);}
		catch (NoSuchMethodException ignored) {}
		
		return null;
	}
	
	/**
	 * Returns all methods of the given class
	 * that are marked with the given annotation class.
	 * Including private methods.
	 * Including the upper classes.
	 * Not including static methods.
	 *
	 * @param clazz           class
	 * @param annotationClazz Annotation class
	 * @param <A>             Annotation type
	 * @param <T>             class type
	 * @return List of methods
	 */
	@NotNull
	public static <A extends Annotation, T> List<Method> getMethodsAnnotated(@NotNull Class<? super T> clazz, @NotNull Class<A> annotationClazz) {
		
		List<Method> methods = new LinkedList<>();
		
		do {
			Method[] _methods = clazz.getDeclaredMethods();
			methods.addAll(Arrays.stream(_methods).filter(m -> m.getAnnotation(annotationClazz) != null).collect(Collectors.toList()));
			clazz = clazz.getSuperclass();
		} while (clazz != null && clazz != Object.class);
		
		return methods;
	}
	
	/**
	 * Return all methods of the given class that are marked with the given annotation class.
	 * Including private methods.
	 * The superclasses of the class are not included.
	 * Static methods are not included.
	 *
	 * @param clazz           class
	 * @param annotationClazz Annotation class
	 * @param <A>             Annotation type
	 * @param <T>             class type
	 * @return List of methods
	 */
	public static <A extends Annotation, T> List<Method> getMethodsAnnotatedThis(@NotNull Class<T> clazz, @NotNull Class<A> annotationClazz) {
		
		Method[] _methods = clazz.getDeclaredMethods();
		
		return Arrays.stream(_methods).filter(m -> m.getAnnotation(annotationClazz) != null).collect(Collectors.toCollection(LinkedList::new));
	}
	
	/**
	 * Returns all methods.
	 * Including private methods.
	 * Including the super classes.
	 * Except for static methods.
	 *
	 * @param clazz class
	 * @param <T>   class type
	 * @return List of methods
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
	 * Get all methods. Including private methods.
	 * The superclasses of the class are not included.
	 * Static methods are not included.
	 *
	 * @param clazz class
	 * @param <T>   class type
	 * @return List of methods
	 */
	@NotNull
	public static <T> List<Method> getMethodsThis(@NotNull final Class<T> clazz) {
		
		return new LinkedList<>(Arrays.asList(clazz.getDeclaredMethods()));
	}
	
	/**
	 * Invokes the method.
	 *
	 * @param method method to invoke
	 * @param object object that has the method
	 * @param args   arguments of the method
	 */
	public static void invoke(@NotNull Method method, @Nullable Object object, @Nullable Object... args) {
		
		try {method.invoke(object, args);}
		catch (Exception e) {e.printStackTrace();}
	}
	
	/**
	 * Invokes the method.
	 *
	 * @param object object that has the method
	 * @param method method to invoke
	 * @param args   arguments of the method
	 * @param <T>    return type
	 * @return the method return value
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T invoke(@Nullable Object object, @NotNull Method method, @Nullable Object... args) {
		
		try {return (T) method.invoke(object, args);}
		catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}
		
		return null;
	}
	
	/**
	 * Returns the annotation.
	 *
	 * @param element         AnnotatedElement
	 * @param annotationClass Annotation class
	 * @param <T>             Annotation type
	 * @return Annotation
	 */
	@Nullable
	public static <T extends Annotation> T getAnnotation(@NotNull AnnotatedElement element, @NotNull Class<T> annotationClass) {
		
		return element.getAnnotation(annotationClass);
	}
	
	/**
	 * Returns the annotations.
	 *
	 * @param element     AnnotatedElement
	 * @param annotations Annotation class list
	 * @param <T>         Annotation type
	 * @return List of annotations
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> List<T> getAnnotations(AnnotatedElement element, @Nullable Class<T>... annotations) {
		
		if (annotations != null && annotations.length > 0)
			return Arrays.stream(annotations).filter(Objects::nonNull).map(a -> getAnnotation(element, a)).collect(Collectors.toList());
		
		return new ArrayList<>(Arrays.asList((T[]) element.getAnnotations()));
	}
	
	/**
	 * Get declared fields.
	 * Includes public, protected, default (package) access, and private fields,
	 * but excludes inherited fields.
	 * If the Class object represents a class or interface with no declared fields,
	 * then this method returns an empty list.
	 *
	 * @param clazz    class to get the fields
	 * @param modifier field modifier to filter
	 * @return List of fields
	 */
	public static List<Field> getDeclaredFields(@NotNull Class<?> clazz, int modifier) {
		
		return Arrays.stream(clazz.getDeclaredFields()).filter(f -> (f.getModifiers() & modifier) == modifier).collect(Collectors.toList());
	}
	
	/**
	 * Get declared fields.
	 * Includes public, protected, default (package) access, and private fields,
	 * but excludes inherited fields.
	 * If the Class object represents a class or interface with no declared fields,
	 * then this method returns an empty list.
	 *
	 * @param clazz class to get the fields
	 * @param type  field type to filter
	 * @return List of fields
	 */
	public static List<Field> getDeclaredFields(@NotNull Class<?> clazz, Class<?> type) {
		
		return Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.getType() == type).collect(Collectors.toList());
	}
	
	/**
	 * Get declared fields.
	 * Includes public, protected, default (package) access, and private fields,
	 * but excludes inherited fields.
	 * If the Class object represents a class or interface with no declared fields,
	 * then this method returns an empty list.
	 *
	 * @param clazz class to get the fields
	 * @return List of fields
	 */
	public static List<Field> getDeclaredFields(@NotNull Class<?> clazz) {
		
		return Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
	}
	
	/**
	 * Converts the given arguments to an array of types.
	 *
	 * @param args arguments
	 * @return array of types of the arguments, which are in the same order
	 */
	@NotNull
	@Contract(pure = true)
	public static Class<?>[] convertToTypes(Object... args) {
		
		if (args.length == 0) return new Class<?>[0];
		
		Class<?>[] types = new Class<?>[args.length];
		
		for (int i = 0; i < args.length; i++) types[i] = args[i].getClass();
		
		return types;
	}
	
	public static void main(String... args) {
		
		//int modifiers = Modifier.FINAL | Modifier.PUBLIC | Modifier.STATIC;
		
		List<Field> c = getDeclaredFields(Clazz.class);
		
		for (int i = 0; i < c.size(); i++) {
			
			Field f = c.get(i);
			
			System.out.printf("%s - %s\n", f.getName(), f.getType().getCanonicalName());
		}
		
		
	}
	
	/**
	 * Returns the value of the static field.
	 *
	 * @param clazz     class
	 * @param fieldName field name
	 * @return value of the field
	 */
	public static int getIntStatic(@NotNull Class<?> clazz, @NotNull String fieldName) {
		
		try {
			
			Field field = clazz.getDeclaredField(fieldName);
			
			field.setAccessible(true);
			return field.getInt(null);
		}
		catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Returns the value of the field.
	 *
	 * @param o         object to get the field
	 * @param fieldName field name
	 * @return value of the field
	 */
	public static int getInt(@NotNull Object o, @NotNull String fieldName) {
		
		try {
			
			Field field = o.getClass().getDeclaredField(fieldName);
			
			field.setAccessible(true);
			
			return field.getInt(o);
		}
		catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Returns the value of the field.
	 *
	 * @param o         object to get the field
	 * @param fieldName field name
	 * @param <T>       type of the field
	 * @return value of the field
	 */
	@SuppressWarnings("unchecked")
	public static <T> @Nullable T getFieldValue(@NotNull Object o, @NotNull String fieldName) {
		
		try {
			
			Field field = o.getClass().getDeclaredField(fieldName);
			
			field.setAccessible(true);
			
			return (T) field.get(o);
		}
		catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Returns the value of the static field.
	 *
	 * @param clazz     class to get the field
	 * @param fieldName field name
	 * @param <T>       type of the field
	 * @return value of the field
	 */
	@SuppressWarnings("unchecked")
	public static <T> @Nullable T getFieldValue(@NotNull Class<?> clazz, @NotNull String fieldName) {
		
		try {
			
			Field field = clazz.getDeclaredField(fieldName);
			
			field.setAccessible(true);
			
			return (T) field.get(null);
		}
		catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}