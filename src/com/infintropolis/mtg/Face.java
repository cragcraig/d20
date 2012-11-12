/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;


public class Face {

    public Vector vertex[] = new Vector[3];

    public Face(Vector v1, Vector v2, Vector v3) {
        vertex[0] = v1;
        vertex[1] = v2;
        vertex[2] = v3;
    }

    public Face(Vector[] verticies, int offset) {
        this(verticies[offset], verticies[offset + 1], verticies[offset + 2]);
    }

    public Face(Vector[] verticies) {
        this(verticies, 0);
    }

    public Vector getNormal() {
        // Uses p1 as a new origin for p0, p2.
        Vector a = vertex[2].getOffsetFrom(vertex[1]);
        Vector b = vertex[0].getOffsetFrom(vertex[1]);
        // Compute the cross product a X b to get the face normal.
        Vector norm = a.getCrossProduct(b);
        // Return a normalized vector.
        norm.normalize();
        return norm;
    }
}
