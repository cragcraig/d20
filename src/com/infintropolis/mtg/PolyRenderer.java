/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;


public class PolyRenderer {

    // Set color with red, green, blue and alpha
    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

    private PolyShape mPolyShape;

    public PolyRenderer(PolyShape polyShape) {
        mPolyShape = polyShape;
    }

    public void draw(GL10 gl, float mAngleX, float mAngleY) {
        gl.glLoadIdentity();
        // Translates 4 units into the screen.
        gl.glTranslatef(0, 0, -10);
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

        setMaterial(gl);

        gl.glColor4f(0.2f, 0.709803922f, 0.898039216f, 0.7f);

        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(PolyShape.COORDS_PER_VERTEX, GL10.GL_FLOAT, 0,
                           mPolyShape.vertexCoordsBuffer);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mPolyShape.vertexNormalsBuffer);

        gl.glDrawElements(GL10.GL_TRIANGLES, mPolyShape.numCoordinates(),
                          GL10.GL_UNSIGNED_SHORT, mPolyShape.drawOrderBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        // Restore state.
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
