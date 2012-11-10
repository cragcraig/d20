/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;


public class PolyRenderer {

    private final static String vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 u_MVPMatrix;" +

        "uniform mat4 u_MVMatrix;" +
        "uniform vec3 u_LightPos;" +
        "uniform vec4 a_Color;" +

        "attribute vec4 a_Position;" +
        "attribute vec3 a_Normal;" +

        "varying vec4 v_Color;" +

        "void main() {" +
        // the matrix must be included as a modifier of gl_Position
        "  vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);" +
        "  vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));" +
        //"  float distance = length(u_LightPos - modelViewVertex);" +
        "  vec3 lightVector = normalize(u_LightPos - modelViewVertex);" +
        "  float diffuse = max(dot(modelViewNormal, lightVector), 0.1);" +
        //"  diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));" +
        "  v_Color = a_Color * diffuse;" +
        "  gl_Position = u_MVPMatrix * a_Position;" +
        "}";

    private final static String fragmentShaderCode =
        "precision mediump float;" +
        "varying vec4 v_Color;" +

        "void main() {" +
        "  gl_FragColor = v_Color;" +
        "}";

    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private int mMVMatrixHandle;
    private int mNormalHandle;
    private int mLightPosHandle;

    // Set color with red, green, blue and alpha
    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 0.7f };

    private PolyShape mPolyShape;

    public PolyRenderer(PolyShape polyShape) {
        mPolyShape = polyShape;

        // Prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                                                   vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                                                     fragmentShaderCode);
        mProgram = MyGLRenderer.createAndLinkProgram(vertexShader, fragmentShader,
            new String[] {"a_Position", "a_Normal"});
    }

    public void draw(float[] mvpMatrix, float[] mvMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get shader handles
        mLightPosHandle = GLES20.glGetUniformLocation(mProgram, "u_LightPos");
        GLES20.glUniform3f(mLightPosHandle, 3.0f, 3.0f, 3.0f);

        // Prepare the triangle normal data.
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal"); 
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLES20.glVertexAttribPointer(mNormalHandle,
                                     PolyShape.COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT,
                                     false,
                                     PolyShape.VERTEX_STRIDE,
                                     mPolyShape.vertexNormalsBuffer);

        // Prepare the triangle coordinate data
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle,
                                     PolyShape.COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT,
                                     false,
                                     PolyShape.VERTEX_STRIDE,
                                     mPolyShape.vertexCoordsBuffer);

        // Set color for drawing the triangle
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "a_Color");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix"); 
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mvMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the PolyShape
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,
                              3*6,
                              //mPolyShape.numCoordinates(),
                              GLES20.GL_UNSIGNED_SHORT,
                              mPolyShape.drawOrderBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mNormalHandle);
    }
}
