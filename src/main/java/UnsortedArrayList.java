import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sbt-khruzin-mm on 15.06.2017.
 */
public class UnsortedArrayList<V> extends HashMap<Long,V> {
    private long NextIndex = 0;
    public V put(V value){
        return this.put(NextIndex++,value);
    }
    public void putAll(List<V> list){
        Iterator<V> entries = list.iterator();
        V cur;
        while (entries.hasNext()){
            cur = entries.next();
            this.put(cur);
        }
    }
    public SortedArrayList<V> getMaxValues(int number){
        SortedArrayList<V> MaxValues = new SortedArrayList<>();
        Iterator<Entry<Long,V>> entries = this.entrySet().iterator();
        V cur;
        while (entries.hasNext()){
            cur = entries.next().getValue();
            Comparable<V> cmp = (Comparable<V>) cur;
            if (cmp.compareTo(MaxValues.lower())>0){
                MaxValues.remove(0);
                MaxValues.add(cur);
            }
        }
        return MaxValues;
    }
}
