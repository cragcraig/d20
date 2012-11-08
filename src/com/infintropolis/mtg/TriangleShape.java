/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import java.lang.Math;


public class TriangleShape extends PolyShape {

    private final static float vertexCoords[] = {
        0.0f,  0.622008459f, 0.0f,   // top
       -0.5f, -0.311004243f, 0.0f,   // bottom left
        0.5f, -0.311004243f, 0.0f    // bottom right
    };

    private final static short drawOrder[] = {  0,  1,  2 };

    public float[] getVertexCoords() {
        return vertexCoords;
    }

    public short[] getDrawOrder() {
        return drawOrder;
    }
}
