package presentation;

import java.util.Comparator;

/**
 * @author James Kelley
 * Genome Navigator and Genome Navigator Data Processor code
 * - likely used by both
 */
public class IntComparator implements Comparator<Object> {
    public int compare(Object o1, Object o2) {
        Integer int1 = Integer.valueOf((String) o1);
        Integer int2 = Integer.valueOf((String) o2);
        return int1.compareTo(int2);
    }

    public boolean equals(Object o2) {
        return this.equals(o2);
    }
}
