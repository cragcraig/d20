/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;


public abstract class PolyShape implements Iterable<Face> {

    // Constants
    static final int COORDS_PER_VERTEX = 3;
    static final int TEX_COORDS_PER_VERTEX = 2;
    static final int VERTICIES_PER_FACE = 3;
    static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // List of faces composing the shape
    private ArrayList<Face> mFaces = new ArrayList<Face>();

    public PolyShape() {
    }

    // Iterate through the shape's faces
    @Override
    public Iterator<Face> iterator() {
        return mFaces.iterator();
    }

    // Add a new Face
    public void addFace(Face f) {
        mFaces.add(f);
    }

    // Get a Face
    public Face getFace(int n) {
        return mFaces.get(n);
    }

    // Get the number of faces
    public int numFaces() {
        return mFaces.size();
    }

    // Get the number of verticies
    public int numVerticies() {
        return numFaces() * VERTICIES_PER_FACE;
    }

    // Get the number of texture coordinates
    public int numTexCoords() {
        return numVerticies() * TEX_COORDS_PER_VERTEX;
    }

    // Get the number of Coordinates
    public int numCoords() {
        return numVerticies() * COORDS_PER_VERTEX;
    }
}
