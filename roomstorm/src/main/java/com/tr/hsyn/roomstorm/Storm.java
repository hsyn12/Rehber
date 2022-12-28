/*
 * Copyright 2015 Dimitry Ivanov (mail@dimitryivanov.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tr.hsyn.roomstorm;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.roomstorm.op.Delete;
import com.tr.hsyn.roomstorm.op.DeleteImpl;
import com.tr.hsyn.roomstorm.op.Insert;
import com.tr.hsyn.roomstorm.op.InsertImpl;
import com.tr.hsyn.roomstorm.op.Select;
import com.tr.hsyn.roomstorm.op.SelectImpl;
import com.tr.hsyn.roomstorm.op.Update;
import com.tr.hsyn.roomstorm.op.UpdateImpl;
import com.tr.hsyn.roomstorm.sd.AbsSerializer;
import com.tr.hsyn.roomstorm.sd.BooleanSerializer;
import com.tr.hsyn.roomstorm.sd.ByteArraySerializer;
import com.tr.hsyn.roomstorm.sd.DoubleSerializer;
import com.tr.hsyn.roomstorm.sd.EnumSerializer;
import com.tr.hsyn.roomstorm.sd.FloatSerializer;
import com.tr.hsyn.roomstorm.sd.IntSerializer;
import com.tr.hsyn.roomstorm.sd.LongSerializer;
import com.tr.hsyn.roomstorm.sd.ShortSerializer;
import com.tr.hsyn.roomstorm.sd.StringSerializer;
import com.tr.hsyn.roomstorm.util.MapUtils;
import com.tr.hsyn.roomstorm.vp.ContentValuesSetterFactory;
import com.tr.hsyn.roomstorm.vp.CursorValueProviderFactory;
import com.tr.hsyn.roomstorm.vp.FieldValueGetterFactory;
import com.tr.hsyn.roomstorm.vp.FieldValueSetterFactory;

import java.util.Map;



/**
 * The main class of Storm library.
 *
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) 01.02.15.
 */
public class Storm {

    private static volatile Storm sInstance = null;

    public static Storm getInstance() {
    	
        Storm local = sInstance;
        
        if (local == null) {
        	
            synchronized (Storm.class) {
            	
                local = sInstance;
                
                if (local == null) {
                	
                    local = sInstance = new Storm();
                }
            }
        }
        
        return local;
    }

    private final InstanceCreatorsMap                  mInstanceCreators;
    private final Map<Class<?>, AbsSerializer<Object>> mSerializers;

    private Storm() {
        mInstanceCreators = new InstanceCreatorsMap();
        mSerializers      = MapUtils.create();
    }

    private Context mApplicationContext;

    // factories
    private FieldValueGetterFactory    mFieldValueGetterFactory;
    private FieldValueSetterFactory    mFieldValueSetterFactory;
    private CursorValueProviderFactory mCursorValueProviderFactory;
    private ContentValuesSetterFactory mContentValuesSetterFactory;

    private boolean mIsDebug;

    private boolean mIsInitCalled;

    /**
     * The main entry point for the Storm library.
     * Should be called only once - Application's onCreate() is a good start
     * This method won't open any SQLite database connections. See {@link DatabaseManager#open()}
     *
     * @param applicationContext {@link Context} of an {@link android.app.Application}
     * @param isDebug flag to indicate that current build is debug,
     *                may be helpful when debugging as Storm
     *                will throw {@link Exception} more aggressively
     * @throws AssertionError if this method was already called
     */
    public void init(Context applicationContext, boolean isDebug) {

        if (mIsInitCalled) {
            throw new AssertionError("init() has already been called");
        }

        mApplicationContext         = applicationContext;

        mFieldValueGetterFactory    = new FieldValueGetterFactory();
        mFieldValueSetterFactory    = new FieldValueSetterFactory();
        mCursorValueProviderFactory = new CursorValueProviderFactory();
        mContentValuesSetterFactory = new ContentValuesSetterFactory();

        mIsDebug = isDebug;
        mIsInitCalled = true;
    }

    /**
     * Registers {@link InstanceCreator} for the specified {@link Class}.
     * It may be helpful and performance wise if, for example, an {@link Object}
     * has no empty constructor or has very specific constructor,
     * or if construction of an Object via Reflection is not desired
     * (default {@link InstanceCreator} for
     * all objects is {@link ReflectionInstanceCreator})
     * @param clazz to be registered
     * @param ics instance of {@link InstanceCreator}
     * @param <T> Type of object that {@link InstanceCreator} will handle
     * @param <IC> {@link InstanceCreator} of type T
     */
    public <T, IC extends InstanceCreator<T>> void registerInstanceCreator(@NonNull Class<T> clazz, @NonNull IC ics) {
        mInstanceCreators.register(clazz, ics);
    }

    /**
     * Unregisters previously registered {@link InstanceCreator}
     * @see #registerInstanceCreator(Class, InstanceCreator)
     * @param clazz to be unregistered
     */
    public void unregisterInstanceCreator(@NonNull Class<?> clazz) {
        mInstanceCreators.unregister(clazz);
    }

    /**
     * Registers Type serializer (aka not supported SQLite types) {@link AbsSerializer}
     * As a matter of fact {@link AbsSerializer} has only one method that
     * indicates what SQLite type({@link FieldType}) this type will represent.
     * Methods <code>serialize</code> and <code>deserialize</code>
     * are not in the inheritance tree. This is done due to the autoboxing issue.
     *
     * If you wish to supply your own TypeSerializer you should definitely extend one of the following:
     * {@link ShortSerializer}
     * {@link IntSerializer}
     * {@link LongSerializer}
     * {@link FloatSerializer}
     * {@link DoubleSerializer}
     * {@link StringSerializer}
     * {@link ByteArraySerializer}
     * {@link BooleanSerializer}
     *
     * Additionally you could easily register Serialization
     * for an Enum {@link EnumSerializer}
     *
     * @param who Class to be registered
     * @param serializer {@link AbsSerializer}
     * @param <T> Type to be linked with
     */
    @SuppressWarnings("unchecked")
    public <T> void registerTypeSerializer(Class<T> who, AbsSerializer<T> serializer) {
       
        mSerializers.put(who, (AbsSerializer<Object>) serializer);
    }

    /**
     * Unregisters type serializer
     * @see #registerTypeSerializer(Class, AbsSerializer)
     * @param clazz to be unregistered
     */
    public void unregisterTypeSerializer(Class<?> clazz) {
        mSerializers.remove(clazz);
    }

    /**
     * Prepares new insert operation
     * @param manager {@link DatabaseManager} to operate on
     * @return new instance of {@link Insert}
     */
    public static Insert newInsert(DatabaseManager manager) {
        return new InsertImpl(manager);
    }

    /**
     * Prepares new delete operation
     * @param manager {@link DatabaseManager} to operate on
     * @return new instance of {@link Delete}
     */
    public static Delete newDelete(DatabaseManager manager) {
        return new DeleteImpl(manager);
    }

    /**
     * Prepares new update operation
     * @param manager {@link DatabaseManager} to operate on
     * @return new instance of {@link Update}
     */
    public static Update newUpdate(DatabaseManager manager) {
        return new UpdateImpl(manager);
    }

    /**
     * Prepares new select operation
     * @param manager {@link DatabaseManager} to operate on
     * @return new instance of {@link Select}
     */
    public static Select newSelect(DatabaseManager manager) {
        return new SelectImpl(manager);
    }


     // Although these methods are public they are not intended to be used directly

    public static boolean isDebug() {
        return Storm.getInstance().mIsDebug;
    }

    public static Context getApplicationContext() {
        return Storm.getInstance().mApplicationContext;
    }

    public static <T> InstanceCreator<T> getInstanceCreator(Class<T> clazz) {
        final Storm storm = Storm.getInstance();
        return storm.mInstanceCreators.get(clazz);
    }

    public @Nullable
    static AbsSerializer<Object> getTypeSerializer(Class<?> clazz) {
        return Storm.getInstance().mSerializers.get(clazz);
    }

    static Map<Class<?>, AbsSerializer<Object>> getSerializers() {
        return Storm.getInstance().mSerializers;
    }

    static Map<Class<?>, InstanceCreator<?>> getInstanceCreators() {
        return Storm.getInstance().mInstanceCreators.getCreators();
    }

    public static FieldValueGetterFactory getFieldValueGetterFactory() {
        return Storm.getInstance().mFieldValueGetterFactory;
    }

    public static FieldValueSetterFactory getFieldValueSetterFactory() {
        return Storm.getInstance().mFieldValueSetterFactory;
    }

    public static CursorValueProviderFactory getCursorValueProviderFactory() {
        return Storm.getInstance().mCursorValueProviderFactory;
    }

    public static ContentValuesSetterFactory getContentValuesProviderFactory() {
        return Storm.getInstance().mContentValuesSetterFactory;
    }
}
