/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;

public class Material {

    private int mTextureHandle;
    private Vect4 mColor;
    private float mShininess;
    private Vect4 mAmbient;
    private Vect4 mDiffuse;
    private Vect4 mSpecular;

    public Material(GL10 gl, int textureHandle, Vect4 color, float shininess,
                    Vect4 ambient, Vect4 diffuse, Vect4 specular) {
        mTextureHandle = textureHandle;
        mColor = color;
        mShininess = shininess;
        mAmbient = ambient;
        mDiffuse = diffuse;
        mSpecular = specular;
    }

    public void apply(GL10 gl) {
        gl.glColor4f(mColor.x, mColor.y, mColor.z, mColor.a);
        applyMaterial(gl);
        applyTexture(gl);
    }

    private void applyTexture(GL10 gl) {
        // Set the active texture unit to texture unit 0.
        gl.glActiveTexture(GL10.GL_TEXTURE0);
        gl.glClientActiveTexture(GL10.GL_TEXTURE0);
        // Bind the texture to this unit.
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureHandle);
    }

    private void applyMaterial(GL10 gl) {
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mDiffuse.getFloats(), 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mAmbient.getFloats(), 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, mSpecular.getFloats(), 0);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, mShininess);
    }

    public static int loadTexture(GL10 gl, final Context activityContext, final int resourceId) {
        final int[] textureHandle = new int[1];

        gl.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource (dimensions must be a power of 2)
            final Bitmap bitmap = BitmapFactory.decodeResource(
                activityContext.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                               GL10.GL_TEXTURE_MIN_FILTER,
                               GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                               GL10.GL_TEXTURE_MAG_FILTER,
                               GL10.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }
        return textureHandle[0];
    }
}
