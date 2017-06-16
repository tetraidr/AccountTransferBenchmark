import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by sbt-khruzin-mm on 15.06.2017.
 */
public class UnsortedArrayList<T> implements List<T> {
    HashMap<Integer,T> Array;

    @NotNull
    @Override
    public Object[] toArray() {
        return Array.values().toArray();
    }

    @Override
    public void clear() {
        Array.clear();
    }

    @Override
    public T get(int i) {
        return Array.get(i);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> collection) {
        return false;
    }

    @Override
    public boolean addAll(int i, @NotNull Collection<? extends T> collection) {
        return false;
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return Array.entrySet().iterator();
    }
}
