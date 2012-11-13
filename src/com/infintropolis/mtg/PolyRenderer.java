/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


public class PolyRenderer {

    // Buffers for OpenGL-formatted model data
    private final FloatBuffer mVertexCoords;
    private final FloatBuffer mVertexNormals;

    // Set color with red, green, blue and alpha
    private float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

    // Defines the shape to be rendered
    private PolyShape mPolyShape;

    public PolyRenderer(PolyShape polyShape) {
        mPolyShape = polyShape;
        float vertexCoords[] = new float[mPolyShape.numCoords()];
        float vertexNormals[] = new float[mPolyShape.numCoords()];
        int i = 0;
        // Write OpenGL coordinate and normal buffers
        for (Face f : mPolyShape) {
            Vector norm = f.getNormal();
            for (int j = 0; j < PolyShape.VERTICIES_PER_FACE; j++) {
                int offset = i + j * PolyShape.COORDS_PER_VERTEX;
                // Coordinate
                f.vertex[j].pos.asFloats(vertexCoords, offset);
                // Normal is an average of face normal and vertex normal
                Vector avgNorm = norm.getScaled(0.3f).getSum(f.vertex[j].normal.getScaled(0.7f));
                avgNorm.asFloats(vertexNormals, offset);
            }
            i += PolyShape.VERTICIES_PER_FACE * PolyShape.COORDS_PER_VERTEX;
        }
        mVertexCoords = createFloatBuffer(vertexCoords);
        mVertexNormals = createFloatBuffer(vertexNormals);
    }

    // Initialize a byte buffer for floats from an array
    private static final FloatBuffer createFloatBuffer(float[] floats) {
        // (# of values * 4 bytes per float)
        ByteBuffer bb = ByteBuffer.allocateDirect(floats.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuf = bb.asFloatBuffer();
        floatBuf.put(floats);
        floatBuf.position(0);
        return floatBuf;
    }

    public void draw(GL10 gl, float mAngleX, float mAngleY) {
        gl.glLoadIdentity();
        // Translates 4 units into the screen.
        gl.glTranslatef(0, 0, -10);
        // Rotates
        gl.glRotatef(mAngleX, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-mAngleY, 1.0f, 0.0f, 0.0f);

        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW);
        // Enable face culling.
        gl.glEnable(GL10.GL_CULL_FACE);
        // Re-normalize to fix scaling issues.
        gl.glEnable(GL10.GL_NORMALIZE);
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK);

        // Set material (TODO: don't hard code this)
        setMaterial(gl);
        gl.glColor4f(0.2f, 0.709803922f, 0.898039216f, 0.7f);

        // Enable the OpenGL vertex and normal buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(PolyShape.COORDS_PER_VERTEX, GL10.GL_FLOAT, 0, mVertexCoords);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mVertexNormals);

        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, mPolyShape.numVerticies());

        // Disable the OpenGL buffers
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        // Restore state
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_NORMALIZE);
    }

    private void setMaterial(GL10 gl) {
        float shininess = 5;
        float[] ambient = { 0, 0, .3f, 1 };
        float[] diffuse = { 0, 0, .7f, 1 };
        float[] specular = { 1, 1, 1, 1 };

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specular, 0);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, shininess);
    }  
}
