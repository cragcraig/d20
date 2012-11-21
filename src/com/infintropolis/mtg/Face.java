/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;


public class Face {

    public Vertex vertex[] = new Vertex[3];
    public Vect2 texCoord[] = new Vect2[3];

    public Face(Vertex v1, Vertex v2, Vertex v3) {
        vertex[0] = v1;
        vertex[1] = v2;
        vertex[2] = v3;
    }

    public Face(Vertex[] verticies, int offset) {
        this(verticies[offset], verticies[offset + 1], verticies[offset + 2]);
    }

    public Face(Vertex[] verticies) {
        this(verticies, 0);
    }

    public Vect3 getNormal() {
        // Uses p1 as a new origin for p0, p2.
        Vect3 a = vertex[2].pos.getOffsetFrom(vertex[1].pos);
        Vect3 b = vertex[0].pos.getOffsetFrom(vertex[1].pos);
        // Compute the cross product a X b to get the face normal.
        Vect3 norm = a.getCrossProduct(b);
        // Return a normalized vector.
        norm.normalize();
        return norm;
    }
}
