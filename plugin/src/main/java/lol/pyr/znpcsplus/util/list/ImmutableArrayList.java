package lol.pyr.znpcsplus.util.list;

import java.util.*;

public class ImmutableArrayList<T> implements List<T>, RandomAccess {
    private final T[] elements;

    public ImmutableArrayList(T[] array) {
        this.elements = array;
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public boolean isEmpty() {
        return elements.length != 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(elements);
    }

    @Override
    public Object[] toArray() {
        return elements;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T1> T1[] toArray(T1[] a) {
        return (T1[]) elements;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) if (!contains(obj)) return false;
        return true;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new ImmutableArrayList<>(Arrays.copyOfRange(elements, fromIndex, toIndex));
    }

    @Override
    public T get(int index) {
        return elements[index];
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < elements.length; i++) if (Objects.equals(elements[i], o)) return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = 0; i < elements.length; i++) {
            int index = elements.length - (i + 1);
            if (Objects.equals(elements[index], o)) return index;
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ArrayIterator<>(elements);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ArrayIterator<>(elements);
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }
}