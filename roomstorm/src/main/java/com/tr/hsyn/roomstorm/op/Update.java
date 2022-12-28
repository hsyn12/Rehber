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

package com.tr.hsyn.roomstorm.op;

import com.tr.hsyn.roomstorm.ContentValuesCreator;
import com.tr.hsyn.roomstorm.DatabaseManager;
import com.tr.hsyn.roomstorm.exc.StormException;
import com.tr.hsyn.roomstorm.query.Selection;

import java.util.List;


/**
 * The main interface for update SQLite operations
 *
 * Automatically executes in transaction when dealing with {@link List} of items
 * @see Transactional
 *
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 06.02.2015.
 */
public interface Update extends Transactional {

    /**
     * @see #update(Object, boolean)
     */
    int update(Object value) throws StormException;

    /**
     * @see #update(Object, boolean, boolean)
     */
    int update(Object value, boolean shouldNotify) throws StormException;

    /**
     * Updates to specified Object in a db
     * An attempt to get a Object's primary field and its\' value is made,
     * if it could not be done - throws exception, else method generates {@link Selection}
     * and calls {@link #update(Object, Selection, boolean, boolean)}
     *
     * @see #update(Object, Selection, boolean, boolean)
     *
     * @param value to be updated
     * @param shouldNotify whether {@link android.content.ContentResolver} should be
     *                     notified about data change in the underlying table
     *                     defaults to TRUE
     * @param setPrimaryKey whether or not {@link ContentValuesCreator} should
     *                      set primary key to the Object's {@link android.content.ContentValues}.
     *                      defaults to FALSE
     * @return number of rows updated
     * @throws StormException if there was an error during execution
     */
    int update(Object value, boolean shouldNotify, boolean setPrimaryKey) throws StormException;

    /**
     * @see #update(Object, Selection, boolean)
     */
    int update(Object value, Selection selection) throws StormException;

    /**
     * @see #update(Object, Selection, boolean, boolean)
     */
    int update(Object value, Selection selection, boolean shouldNotify) throws StormException;

    /**
     *
     * @param value to be updated
     * @param selection {@link Selection} to be passed to {@link DatabaseManager}
     * @param shouldNotify whether {@link android.content.ContentResolver} should be
     *                     notified about data change in the underlying table
     *                     defaults to TRUE
     * @param setPrimaryKey whether or not {@link ContentValuesCreator} should
     *                      set primary key to the Object's {@link android.content.ContentValues}.
     *                      defaults to FALSE
     * @return number of rows updated
     * @throws StormException if there was an error during execution
     */
    int update(Object value, Selection selection, boolean shouldNotify, boolean setPrimaryKey) throws StormException;

    /**
     * @see #update(List, boolean)
     */
    int update(List<?> values) throws StormException;

    /**
     * @see #update(List, boolean, boolean)
     */
    int update(List<?> values, boolean shouldNotify) throws StormException;

    /**
     * Creates {@link Selection} for every Object in a List based on Object's primary field
     * Executes in one SQLite transaction
     *
     * @param values to be updated
     * @param shouldNotify whether {@link android.content.ContentResolver} should be
     *                     notified about data change in the underlying table
     *                     defaults to TRUE
     * @param setPrimaryKey whether or not {@link ContentValuesCreator} should
     *                      set primary key to the Object's {@link android.content.ContentValues}.
     *                      defaults to FALSE
     * @return number of rows updated
     * @throws StormException if there was an error during execution
     */
    int update(List<?> values, boolean shouldNotify, boolean setPrimaryKey) throws StormException;

}
