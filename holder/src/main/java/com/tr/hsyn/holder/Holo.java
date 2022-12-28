package com.tr.hsyn.holder;

class Holo<T> implements Holder<T> {

    private T value;

    public Holo() {}

    public Holo(T value) {this.value = value;}

    @Override
    public T get() {return value;}

    @Override
    public void set(T t) {value = t;}
}
