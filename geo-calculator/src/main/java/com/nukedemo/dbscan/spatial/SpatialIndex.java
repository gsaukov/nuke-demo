package com.nukedemo.dbscan.spatial;

import com.esri.core.geometry.Point;

import java.util.Set;

/**
 * Interface of spatial index for searching objects by means of spatial properties. There may be
 * different implementations of this interface providing different underlying data structures for
 * efficient data access, e.g. Quad-tree (see {@link QuadTreeIndex}) or R-Tree (not implemented
 * yet).
 *
 * @param <T> Result types depend on the implementation.
 */
public interface SpatialIndex<T> {
    /**
     * Gets nearest object stored in the index, which may be nevertheless multiple results if they
     * have the same distance.
     *
     * @param c Point of reference for nearest search.
     * @return Result set of nearest object(s), may be multiple objects if they have the same
     *         distance.
     */
    Set<T> nearest(Point c);

    /**
     * Gets objects stored in the index that are within a certain radius or overlap a certain
     * radius.
     *
     * @param c Center point for radius search.
     * @param r Radius in meters.
     * @return Result set of object(s) that are within a the given radius or overlap the radius.
     */
    Set<T> radius(Point c, double r);

    /**
     * Gets <i>k</i> nearest objects stored in the index.
     *
     * @param c Point of reference for nearest search.
     * @param k Number of objects to be searched.
     * @return Result set of nearest objects (exactly k objects).
     */
    Set<T> knearest(Point c, int k);
}
