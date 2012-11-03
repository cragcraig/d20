/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;


public class IcosahedronShape extends PolyShape {

    private final static float vertexCoords[] = { -0.5f,  0.5f, 0.0f,   // top left
                                                  -0.5f, -0.5f, 0.0f,   // bottom left
                                                   0.5f, -0.5f, 0.0f,   // bottom right
                                                   0.5f,  0.5f, 0.0f }; // top right

    private final static short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    public float[] getVertexCoords() {
        return vertexCoords;
    }

    public short[] getDrawOrder() {
        return drawOrder;
    }
}
