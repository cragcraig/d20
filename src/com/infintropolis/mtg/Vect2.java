/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;


public class Vect2 implements Vect {

    public float x;
    public float y;

    public Vect2() {
        this(0.0f, 0.0f);
    }

    public Vect2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vect2(float[] values) {
        this(values, 0);
    }

    public Vect2(float[] values, int offset) {
        this(values[offset], values[offset + 1]);
    }

    // Write to float
    @Override
    public void asFloats(float[] out, int offset) {
        out[offset] = x;
        out[offset + 1] = y;
    }

    // Returns new float array
    @Override
    public float[] getFloats() {
        float[] ret = new float[2];
        asFloats(ret, 0);
        return ret;
    }
}
