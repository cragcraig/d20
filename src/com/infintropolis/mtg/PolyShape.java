/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


public abstract class PolyShape {

    static final int COORDS_PER_VERTEX = 3;
    static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public final FloatBuffer vertexCoordsBuffer;
    public final ShortBuffer drawOrderBuffer;

    public PolyShape() {
        vertexCoordsBuffer = createFloatBuffer(getVertexCoords());
        drawOrderBuffer = createShortBuffer(getDrawOrder());
    }

    public abstract float[] getVertexCoords();
    public abstract short[] getDrawOrder();

    public final int numVerticies() {
        return getDrawOrder().length;
    }

    private static final FloatBuffer createFloatBuffer(float[] floats) {
        // initialize vertex byte buffer for shape coordinates
        // (# of values * 4 bytes per float)
        ByteBuffer bb = ByteBuffer.allocateDirect(floats.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(floats);
        vertexBuffer.position(0);
        return vertexBuffer;
    }

    private static final ShortBuffer createShortBuffer(short[] shorts) {
        // initialize byte buffer for the draw list
        // (# of values * 2 bytes per short)
        ByteBuffer dlb = ByteBuffer.allocateDirect(shorts.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        ShortBuffer drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(shorts);
        drawListBuffer.position(0);
        return drawListBuffer;
    }
}
