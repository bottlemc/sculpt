package com.github.glassmc.sculpt.framework;

public class Pair<T, U> {

    private final T key;
    private final U value;

    public Pair(T key, U value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public U getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            return ((Pair<?, ?>) o).key.equals(this.key) && ((Pair<?, ?>) o).value.equals(this.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.key.hashCode() + this.value.hashCode();
    }
}
