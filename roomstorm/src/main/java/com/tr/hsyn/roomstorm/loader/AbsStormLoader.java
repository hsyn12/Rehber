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

package com.tr.hsyn.roomstorm.loader;

import android.content.Context;
import android.net.Uri;

import com.tr.hsyn.roomstorm.DatabaseManager;


public abstract class AbsStormLoader<T> extends AbsLoader<T> {

    private final DatabaseManager mManager;

    public AbsStormLoader(Context context, DatabaseManager manager) {
        super(context);
        this.mManager = manager;
    }

    protected DatabaseManager getManager() {
        return mManager;
    }

    /**
     * If you are using tables from database you could call
     * {@link DatabaseManager#getNotificationUri(Class)}.
     *
     * Else you should construct your own - and don't forget
     * to send a notification when an insert/update/delete operation was executed
     *
     * @see StormUriCreator
     *
     * @return {@link Uri}
     */
    @Override
    protected abstract Uri getNotificationUri();
}
