package preceptor.hwa;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class WordList implements Set<String> {

    private final Map<StringWrapper, String> wordsList = new HashMap<StringWrapper, String>(250000);

    public WordList() {
    }

    @Override
    public int size() {
        return wordsList.size();
    }

    @Override
    public boolean isEmpty() {
        return wordsList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof StringWrapper) {
            return wordsList.containsKey(o);
        } else {
            return wordsList.containsKey(new StringWrapper((String) o));
        }
    }

    public boolean contains(StringWrapper o) {
        return wordsList.containsKey(o);
    }

    public String getAnagram(String s) {
        return getAnagram(new StringWrapper(s));
    }

    public String getAnagram(StringWrapper sw) {
        return wordsList.get(sw);
    }

    @Override
    public Iterator<String> iterator() {
        return wordsList.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return wordsList.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return wordsList.values().toArray(a);
    }

    @Override
    public boolean add(String s) {
        return wordsList.put(new StringWrapper(s), s) == null;
    }

    @Override
    public boolean remove(Object o) {
        return wordsList.remove(new StringWrapper((String) o)) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        wordsList.clear();
    }
}
