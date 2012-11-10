/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import java.lang.Math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


public abstract class PolyShape {

    static final int COORDS_PER_VERTEX = 3;
    static final int VERTICIES_PER_FACE = 3;
    static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public final FloatBuffer vertexCoordsBuffer;
    public final ShortBuffer drawOrderBuffer;
    public final FloatBuffer vertexNormalsBuffer;

    private float[] vertexNormals;
    private float[] faceNormals;

    public PolyShape() {
        vertexCoordsBuffer = createFloatBuffer(getVertexCoords());
        drawOrderBuffer = createShortBuffer(getDrawOrder());
        // Compute normals.
        faceNormals = new float[numFaces() * COORDS_PER_VERTEX];
        vertexNormals = new float[numUniqueVerticies() * COORDS_PER_VERTEX];
        computeVertexNormals();
        vertexNormalsBuffer = createFloatBuffer(vertexNormals);
        assert getVertexCoords().length == vertexNormals.length;
    }

    public abstract float[] getVertexCoords();
    public abstract short[] getDrawOrder();

    public final int numVerticies() {
        return getDrawOrder().length / COORDS_PER_VERTEX;
    }

    public final int numUniqueVerticies() {
        return getVertexCoords().length / COORDS_PER_VERTEX;
    }

    public final int numCoordinates() {
        return getDrawOrder().length;
    }

    public final int numFaces() {
        return getDrawOrder().length / VERTICIES_PER_FACE;
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

    private final void computeVertexNormals() {
        final float[] vertexCoords = getVertexCoords();
        final short[] drawOrder = getDrawOrder();
        float[] sum = new float[3];
        computeFaceNormals();
        for (int v = 0; v < numUniqueVerticies(); v++) {
            sum[0] = 0.0f;
            sum[1] = 0.0f;
            sum[2] = 0.0f;
            int count = 0;
            // Vertex normal is the average of all surrounding face normals.
            for (int i = 0; i < drawOrder.length; i += VERTICIES_PER_FACE) {
                for (int j = 0; j < VERTICIES_PER_FACE; j++) {
                    if (drawOrder[i + j] == v) {
                        sum[0] += faceNormals[i];
                        sum[1] += faceNormals[i + 1];
                        sum[2] += faceNormals[i + 2];
                        count++;
                    }
                }
            }
            vertexNormals[v * COORDS_PER_VERTEX] = sum[0] / (float)count;
            vertexNormals[v * COORDS_PER_VERTEX + 1] = sum[1] / (float)count;
            vertexNormals[v * COORDS_PER_VERTEX + 2] = sum[2] / (float)count;
        }
            
            
    }

    private final void computeFaceNormals() {
        final float[] vertexCoords = getVertexCoords();
        final short[] drawOrder = getDrawOrder();
        for (int i = 0; i < faceNormals.length; i += COORDS_PER_VERTEX) {
            computeFaceNormal(vertexCoords, drawOrder[i] * COORDS_PER_VERTEX,
                              vertexCoords, drawOrder[i + 1] * COORDS_PER_VERTEX,
                              vertexCoords, drawOrder[i + 2] * COORDS_PER_VERTEX,
                              faceNormals, i);
        }
    }

    private void vectorOffset(float[] in, int inOffset,
                              float[] offset, int offsetOffset,
                              float[] out, int outOffset) {
          out[outOffset] = in[inOffset] - offset[offsetOffset];
          out[outOffset + 1] = in[inOffset + 1] - offset[offsetOffset + 1];
          out[outOffset + 2] = in[inOffset + 2] - offset[offsetOffset + 2];
    }

    private void vectorComputeNormal(float[] a, int aOffset,
                                     float[] b, int bOffset,
                                     float[] out, int outOffset) {
        // Compute cross product a X b into out.
        out[outOffset] = a[aOffset + 1] * b[bOffset + 2] - a[aOffset + 2] * b[bOffset + 1];
        out[outOffset + 1] = a[aOffset + 2] * b[bOffset] - a[aOffset] * b[bOffset + 2];
        out[outOffset + 2] = a[aOffset] * b[bOffset + 1] - a[aOffset + 1] * b[bOffset];
    }

    private boolean vectorNormalize(float[] in, int inOffset,
                                    float[] out, int outOffset) {
        float len = (float)Math.sqrt(in[inOffset] * in[inOffset] +
                                     in[inOffset + 1] * in[inOffset + 1] +
                                     in[inOffset + 2] * in[inOffset + 2]);
        if (len != 0.0f) {
            out[outOffset] = in[inOffset] / len;
            out[outOffset + 1] = in[inOffset + 1] / len;
            out[outOffset + 2] = in[inOffset + 2] / len;
            return true;
        }
        return false;
    }

    private boolean computeFaceNormal(float[] p1, int p1Offset,
                                      float[] p2, int p2Offset,
                                      float[] p3, int p3Offset,
                                      float[] pOut, int pOutOffset) {
        // Uses p2 as a new origin for p1, p3.
        float[] a = new float[3];
        vectorOffset(p3, p3Offset, p2, p2Offset, a, 0);
        float[] b = new float[3];
        vectorOffset(p1, p1Offset, p2, p2Offset, b, 0);
        // Compute the cross product a X b to get the face normal.
        float[] pn = new float[3];
        vectorComputeNormal(a, 0, b, 0, pn, 0);
        // Return a normalized vector.
        return vectorNormalize(pn, 0, pOut, pOutOffset);
    }
}
