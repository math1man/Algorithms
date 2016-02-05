package preceptor.hwa;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class StringWrapper {

    private final String string;
    private final char[] sorted;

    public StringWrapper(String string) {
        this.string = string;
        this.sorted = string.toCharArray();
        Arrays.sort(sorted);
    }

    public String getString() {
        return string;
    }

    public char[] getSorted() {
        return sorted;
    }

    public int size() {
        return sorted.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringWrapper that = (StringWrapper) o;

        return Arrays.equals(sorted, that.sorted);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(sorted);
    }

    @Override
    public String toString() {
        return string;
    }
}
