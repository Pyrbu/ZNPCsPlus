package lol.pyr.znpcsplus.util.list;

import java.util.Iterator;
import java.util.ListIterator;

public class ArrayIterator<T> implements Iterator<T>, ListIterator<T> {
    private final T[] array;
    private int index = 0;

    public ArrayIterator(T[] array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return array.length > index;
    }

    @Override
    public T next() {
        return array[index++];
    }

    private boolean inBounds(int index) {
        return index >= 0 && index < array.length;
    }

    @Override
    public boolean hasPrevious() {
        return inBounds(index - 1);
    }

    @Override
    public T previous() {
        return array[--index];
    }

    @Override
    public int nextIndex() {
        return index + 1;
    }

    @Override
    public int previousIndex() {
        return index - 1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(T t) {
        throw new UnsupportedOperationException();
    }
}
