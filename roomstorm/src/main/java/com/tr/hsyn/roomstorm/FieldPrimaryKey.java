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


import com.tr.hsyn.roomstormanno.PrimaryKey;

import java.lang.reflect.Field;


/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 31.01.2015.
 */
public class FieldPrimaryKey {

    private static final Class<PrimaryKey> PRIMARY_KEY_CLASS = PrimaryKey.class;

    private FieldPrimaryKey() {}

    public static boolean isPrimaryKey(Field field) {

        return field.isAnnotationPresent(PRIMARY_KEY_CLASS);
    }
}
