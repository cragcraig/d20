/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;


public class Vect4 implements Vect {

    public float x;
    public float y;
    public float z;
    public float a;

    public Vect4() {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Vect4(Vect3 vect3, float a) {
        this(vect3.x, vect3.y, vect3.z, a);
    }

    public Vect4(float x, float y, float z, float a) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
    }

    public Vect4(float[] values) {
        this(values, 0);
    }

    public Vect4(float[] values, int offset) {
        this(values[offset], values[offset + 1], values[offset + 2], values[offset + 3]);
    }

    // Factory color methods
    public static Vect4 fromGreyValue(float grey) {
        return new Vect4(grey, grey, grey, 1.0f);
    }

    public static Vect4 fromRGB(int r, int g, int b) {
        return fromRGBA(r, g, b, 1.0f);
    }

    public static Vect4 fromRGBA(int r, int g, int b, float alpha) {
        return new Vect4((float)r / 255.0f, (float)g / 255.0f, (float)b / 255.0f, alpha);
    }

    // Write to float
    @Override
    public void asFloats(float[] out, int offset) {
        out[offset] = x;
        out[offset + 1] = y;
        out[offset + 2] = z;
        out[offset + 3] = a;
    }

    // Returns new float array
    @Override
    public float[] getFloats() {
        float[] ret = new float[4];
        asFloats(ret, 0);
        return ret;
    }
}
