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


import androidx.annotation.NonNull;

import com.tr.hsyn.roomstorm.util.MapUtils;

import java.util.Map;


/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 25.01.2015.
 */
public class InstanceCreatorsMap {

    private final Map<Class<?>, InstanceCreator<?>> mCreators;
    private final InstanceCreator<?> mDefault;

    InstanceCreatorsMap() {
        mCreators = MapUtils.create();
        mDefault  = new ReflectionInstanceCreator<>();
    }

    <T, IC extends InstanceCreator<T>> void register(@NonNull Class<T> clazz, @NonNull IC ic) {
        mCreators.put(clazz, ic);
    }

    public void unregister(@NonNull Class<?> clazz) {
        mCreators.remove(clazz);
    }

    @SuppressWarnings("unchecked")
    <T> InstanceCreator<T> get(Class<T> clazz) {
        
        final InstanceCreator<T> ic = (InstanceCreator<T>) mCreators.get(clazz);
        if (ic == null) {
            
            return (InstanceCreator<T>) mDefault;
        }
        return ic;
    }

    Map<Class<?>, InstanceCreator<?>> getCreators() {
        return mCreators;
    }
}
