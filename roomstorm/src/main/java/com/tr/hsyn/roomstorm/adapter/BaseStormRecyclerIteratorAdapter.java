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

package com.tr.hsyn.roomstorm.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.roomstorm.StormIterator;
import com.tr.hsyn.roomstorm.pool.RecyclerObjectPool;



/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 22.02.2015.
 */
public abstract class BaseStormRecyclerIteratorAdapter<T, VH extends RecyclerView.ViewHolder>
        extends BaseStormRecyclerAdapter<VH> implements StormIteratorAdapter<T> {

    private final RecyclerObjectPool<T> mPool;
    private       StormIterator<T>      mIterator;

    public BaseStormRecyclerIteratorAdapter(Context context, T[] poolArray) {
        super(context);
        this.mPool = new RecyclerObjectPool<>(poolArray);
    }

    @Override
    public int getItemCount() {
        return mIterator != null ? mIterator.getCount() : 0;
    }

    @Override
    public void setIterator(StormIterator<T> iterator) {
        this.setIterator(iterator, true);
    }

    @Override
    public void setIterator(StormIterator<T> iterator, boolean shouldNotify) {
        this.setIterator(iterator, shouldNotify, true);
    }

    @Override
    public void setIterator(StormIterator<T> iterator, boolean shouldNotify, boolean closePrevious) {
        if (mIterator != null) {
            if (closePrevious) {
                mIterator.close();
            }
            mIterator.setObjectPool(null);
        }

        mIterator = iterator;
        mIterator.setObjectPool(mPool);
        if (shouldNotify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public StormIterator<T> getIterator() {
        return mIterator;
    }

    @Override
    public T getItem(int position) {
        return mIterator.get(position);
    }

    @Override
    public int getCount() {
        return getItemCount();
    }
}
