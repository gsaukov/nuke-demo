package com.nukedemo.dbscan.util;

import java.io.Serializable;

/**
 * Generic 2-tuple (tuple).
 *
 * @param <X> Type of first element.
 * @param <Y> Type of second element.
 */
public class Tuple<X, Y> implements Serializable {
    private static final long serialVersionUID = 1L;
    private X one = null;
    private Y two = null;

    /**
     * Creates a {@link Tuple} object.
     *
     * @param one First element.
     * @param two Second element.
     */
    public Tuple(X one, Y two) {
        this.one = one;
        this.two = two;
    }

    /**
     * Gets first element.
     *
     * @return First element, may be null if set to null previously.
     */
    public X one() {
        return one;
    }

    /**
     * Gets second element.
     *
     * @return Second element, may be null if set to null previously.
     */
    public Y two() {
        return two;
    }

    /**
     * Sets first element.
     *
     * @param one First element.
     */
    public void one(X one) {
        this.one = one;
    }

    /**
     * Sets second element.
     *
     * @param two Second element.
     */
    public void two(Y two) {
        this.two = two;
    }
}
