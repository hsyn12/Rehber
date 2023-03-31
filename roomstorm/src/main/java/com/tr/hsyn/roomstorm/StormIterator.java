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

import com.tr.hsyn.roomstorm.pool.ObjectPool;


/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 21.02.2015.
 */
public interface StormIterator<T> {

    /**
     * @see android.database.Cursor#getCount()
     * @return total number of rows
     */
    int getCount();

    /**
     * If <code>Cursor.moveToPosition(int)</code> returns false then method returns <code>null</code>
     * @see android.database.Cursor#moveToPosition(int)
     * @return item on the current <code>position</code>
     */
    @Nullable
    T get(int position);

    /**
     * It's crucial to call this method as instance of {@link StormIterator}
     * holds a {@link android.database.Cursor}
     * @see android.database.Cursor#close()
     */
    void close();

    /**
     * Sets caching object pool for this iterator.
     * @see ObjectPool
     * @param objectPool to cache results in
     */
    void setObjectPool(ObjectPool<T> objectPool);

    /**
     * @return previously set {@link ObjectPool} or null if there was no previous mapping
     */
    @Nullable ObjectPool<T> getObjectPool();
}
