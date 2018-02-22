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
	String delimiter;
	List<String> tokens = new ArrayList<>();

	public StringJoiner(String delimiter) {
		this.delimiter = delimiter;
	}

	public StringJoiner add(String token) {
		tokens.add(token);
		return this;
	}

	public String toString() {
		Iterator<String> it  = tokens.iterator();
		StringBuilder sb = new StringBuilder();

		while (it.hasNext()) {
			sb.append(it.next());
			if (it.hasNext())
				sb.append(delimiter);
		}

		return sb.toString();
	}
}
