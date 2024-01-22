package com.nukedemo.geocalculator.dbscan.util;

import java.io.Serializable;

/**
 * Generic 3-tuple (triple).
 *
 * @param <X> Type of first element.
 * @param <Y> Type of second element.
 * @param <Z> Type of third element.
 */
public class Triple<X, Y, Z> extends Tuple<X, Y> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Z three = null;

    /**
     * Creates a {@link Triple} object.
     *
     * @param one First element.
     * @param two Second element.
     * @param three Third element.
     */
    public Triple(X one, Y two, Z three) {
        super(one, two);
        this.three = three;
    }

    /**
     * Gets third element.
     *
     * @return Third element, may be null if set to null previously.
     */
    public Z three() {
        return three;
    }

    /**
     * Sets third element.
     *
     * @param three Third element.
     */
    public void three(Z three) {
        this.three = three;
    }
}
