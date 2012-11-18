/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;


public class Vector4 {

    private float[] floats = new float[4];

    public Vector4(float f0, float f1, float f2, float f3) {
        floats[0] = f0;
        floats[1] = f1;
        floats[2] = f2;
        floats[3] = f3;
    }

    public Vector4(float[] floats) {
        this(floats[0], floats[1], floats[2], floats[3]);
    }

    public final float[] getFloats() {
        return floats;
    }

    public float getFloat(int n) {
        return floats[n];
    }
}
