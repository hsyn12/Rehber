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


import androidx.annotation.Nullable;

import com.tr.hsyn.roomstormanno.Column;

import java.lang.reflect.Field;


/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) 01.02.15.
 */
public class ColumnNameParser {

    private static final Class<Column> COLUMN_CLASS = Column.class;

    public @Nullable
    String get(Field field) {

        if (!field.isAnnotationPresent(COLUMN_CLASS)) {
            return null;
        }

        final Column column = field.getAnnotation(COLUMN_CLASS);

        final String columnName = column.value();

        if (Column.NULL.equals(columnName)) {
            return field.getName();
        }

        return columnName;
    }
}
