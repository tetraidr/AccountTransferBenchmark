/**
 * Created by sbt-khruzin-mm on 01.09.2017.
 */
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.*;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JCache<K,V> implements Cache{

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public Map getAll(Set keys) {
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public void loadAll(Set keys, boolean replaceExistingValues, CompletionListener completionListener) {

    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public Object getAndPut(Object key, Object value) {
        return null;
    }

    @Override
    public void putAll(Map map) {

    }

    @Override
    public boolean putIfAbsent(Object key, Object value) {
        return false;
    }

    @Override
    public boolean remove(Object key) {
        return false;
    }

    @Override
    public boolean remove(Object key, Object oldValue) {
        return false;
    }

    @Override
    public Object getAndRemove(Object key) {
        return null;
    }

    @Override
    public boolean replace(Object key, Object oldValue, Object newValue) {
        return false;
    }

    @Override
    public boolean replace(Object key, Object value) {
        return false;
    }

    @Override
    public Object getAndReplace(Object key, Object value) {
        return null;
    }

    @Override
    public void removeAll(Set keys) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public CacheManager getCacheManager() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void registerCacheEntryListener(CacheEntryListenerConfiguration cacheEntryListenerConfiguration) {

    }

    @Override
    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration cacheEntryListenerConfiguration) {

    }

    @Override
    public Iterator<Entry> iterator() {
        return null;
    }

    @Override
    public Object unwrap(Class clazz) {
        return null;
    }

    @Override
    public Map invokeAll(Set keys, EntryProcessor entryProcessor, Object... arguments) {
        return null;
    }

    @Override
    public Object invoke(Object key, EntryProcessor entryProcessor, Object... arguments) throws EntryProcessorException {
        return null;
    }

    @Override
    public javax.cache.configuration.Configuration getConfiguration(Class clazz) {
        return null;
    }
}
