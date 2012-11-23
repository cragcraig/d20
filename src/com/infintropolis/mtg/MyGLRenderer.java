/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    final private Context mActivityContext;
    private PolyRenderer mShape;

    // Declare as volatile because we are updating it from another thread
    public volatile float mAngleX;
    public volatile float mAngleY;

    public MyGLRenderer(final Context context) {
        mActivityContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Enable Smooth Shading, default not really needed.
        gl.glShadeModel(GL10.GL_SMOOTH);
        // Depth buffer setup.
        gl.glClearDepthf(1.0f);
        // Enables depth testing.
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // The type of depth testing to do.
        gl.glDepthFunc(GL10.GL_LEQUAL);
        // Really nice perspective calculations.
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        // Set up lighting
        drawLights(gl);

        // Create material
        Material mat = new Material(
                           gl,
                           Material.loadTexture(gl, mActivityContext, R.drawable.texture),
                           Vect4.fromGreyValue(1.0f),
                           5.0f,
                           Vect4.fromGreyValue(0.3f),
                           Vect4.fromGreyValue(0.7f),
                           Vect4.fromGreyValue(1.0f) );

        mShape = new PolyRenderer(new IcosahedronShape(), mat);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Draw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Set GL_MODELVIEW transformation mode
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        // Using GL_MODELVIEW, set the camera view
        GLU.gluLookAt(gl, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Draw
        mShape.draw(gl, mAngleX, mAngleY);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) width / height;
        // Sets the current view port to the new size.
        gl.glViewport(0, 0, width, height);// OpenGL docs.
        // Select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);// OpenGL docs.
        // Reset the projection matrix
        gl.glLoadIdentity();// OpenGL docs.
        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f, ratio, 0.1f, 100.0f);
        // Select the modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);// OpenGL docs.
        // Reset the modelview matrix
        gl.glLoadIdentity();// OpenGL docs.
    }

    // Setting the light
    private void drawLights(GL10 gl) {
        Vect3 light = new Vect3(-0.2f, 0.1f, 1.0f);
        light.normalize();
        float[] position = { light.x, light.y, light.z, 0.0f }; // directional
        float[] diffuse = { 1.0f, 1.0f, 1.0f, 1f };
        float[] specular = { 0.3f, 0.3f, 0.3f, 0.3f };
        float[] ambient = { .3f, .3f, .3f, .3f };

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, position, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specular, 0);
    }
}
