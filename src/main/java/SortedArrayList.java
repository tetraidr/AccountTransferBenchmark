import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sbt-khruzin-mm on 14.06.2017.
 */
class SortedArrayList<T> extends ArrayList<T> {
    public int PercentileRank;
    boolean NeedRebalance;
    @SuppressWarnings("unchecked")
    void insertSorted(T value) {
        add(value);
        Comparable<T> cmp = (Comparable<T>) value;
        for (int i = size()-1; i > 0 && cmp.compareTo(get(i-1)) < 0; i--)
            Collections.swap(this, i, i-1);
    }
    T lower(){
        return this.get(0);
    }
    T higher(){
        return this.get(this.size()-1);
    }
}
