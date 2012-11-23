/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;


public class PolyRenderer {

    // Buffers for OpenGL-formatted model data
    private final FloatBuffer mVertexCoords;
    private final FloatBuffer mVertexNormals;
    private final FloatBuffer mVertexTexCoords;

    // Defines the material to apply to the rendered shape
    private Material mMaterial;

    // Defines the shape to be rendered
    private PolyShape mPolyShape;

    public PolyRenderer(PolyShape polyShape, Material material) {
        mMaterial = material;
        mPolyShape = polyShape;
        // Init OpenGL buffers
        float vertexCoords[] = new float[mPolyShape.numCoords()];
        float vertexNormals[] = new float[mPolyShape.numCoords()];
        float vertexTexCoords[] = new float[mPolyShape.numTexCoords()];
        int i = 0;
        // Write OpenGL vertex coordinate, texture, and normal buffers
        for (Face f : mPolyShape) {
            Vect3 norm = f.getNormal();
            for (int j = 0; j < PolyShape.VERTICIES_PER_FACE; j++) {
                int offset = (i + j) * PolyShape.COORDS_PER_VERTEX;
                // Vertex coordinate
                f.vertex[j].pos.asFloats(vertexCoords, offset);
                // Texture coordinate
                f.texCoord[j].asFloats(vertexTexCoords, (i + j) * PolyShape.TEX_COORDS_PER_VERTEX);
                // Use a combination of the face normal and vertex normal
                Vect3 avgNorm = norm.getScaled(0.3f).getSum(f.vertex[j].normal.getScaled(0.7f));
                avgNorm.asFloats(vertexNormals, offset);
            }
            i += PolyShape.VERTICIES_PER_FACE;
        }
        mVertexCoords = createFloatBuffer(vertexCoords);
        mVertexNormals = createFloatBuffer(vertexNormals);
        mVertexTexCoords = createFloatBuffer(vertexTexCoords);
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
        // Translates 10 units into the screen.
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
        // Enable textures
        gl.glEnable(GL10.GL_TEXTURE_2D);

        // Enable the OpenGL buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        // Apply material
        mMaterial.apply(gl);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glTexCoordPointer(PolyShape.TEX_COORDS_PER_VERTEX, GL10.GL_FLOAT, 0, mVertexTexCoords);

        // Set OpenGL buffers
        gl.glVertexPointer(PolyShape.COORDS_PER_VERTEX, GL10.GL_FLOAT, 0, mVertexCoords);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mVertexNormals);

        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, mPolyShape.numVerticies());

        // Disable the OpenGL buffers
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        // Restore state
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_NORMALIZE);
    }
}
