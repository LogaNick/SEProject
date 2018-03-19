package ca.dal.cs.athletemonitor.athletemonitor.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class joins tokens with a given delimiter.
 * The chosen API level does not have Java 8 classes, so this
 * class should mimic the functionality of the Java 8 class of the same name.
 */
public class StringJoiner {
    private String delimiter;
    private List<String> tokens = new ArrayList<>();

    /**
     * Construct a StringJoiner with a given delimiter.
     * @param delimiter the String to use to separate tokens
     */
    public StringJoiner(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Add a token to this StringJoiner
     * @param token the token to be added
     * @return this StringJoiner, for method chaining
     */
    public StringJoiner add(String token) {
        tokens.add(token);
        return this;
    }

    /**
     * Construct a String representation of this StringJoiner
     * @return a flattened version of this StringJoiner, where
     * each token is separated by the delimiter
     */
    public String toString() {
        Iterator<String> it = tokens.iterator();
        StringBuilder sb = new StringBuilder();

        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext())
                sb.append(delimiter);
        }

        return sb.toString();
    }
}
