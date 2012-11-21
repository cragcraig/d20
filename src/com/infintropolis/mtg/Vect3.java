/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import java.lang.Math;


public class Vect3 implements Vect {

    public float x;
    public float y;
    public float z;

    public Vect3() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Vect3(Vect2 vect2, float z) {
        this(vect2.x, vect2.y, z);
    }

    public Vect3(float x, float y, float z) {
        set(x, y, z);
    }

    public Vect3(float[] values, int offset) {
        this(values[offset], values[offset + 1], values[offset + 2]);
    }

    public Vect3(float[] values) {
        this(values, 0);
    }

    // Set values
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Write to float
    @Override
    public void asFloats(float[] out, int offset) {
        out[offset] = x;
        out[offset + 1] = y;
        out[offset + 2] = z;
    }

    // Returns new float array
    @Override
    public float[] getFloats() {
        float[] ret = new float[3];
        asFloats(ret, 0);
        return ret;
    }

    // Clone
    // Don't implement Cloneable so we don't have to deal with an exception that
    // won't be raised anyway.
    public Vect3 clone() {
        return new Vect3(x, y, z);
    }

    // Normalize
    public void normalize() {
        float len = (float)Math.sqrt(x * x + y * y + z * z);
        if (len != 0.0f) {
            set(x / len, y / len, z / len);
        }
    }

    // Clone and normalize clone
    public Vect3 getNormalized() {
        Vect3 norm = clone();
        norm.normalize();
        return norm;
    }

    // Cross product
    public Vect3 getCrossProduct(Vect3 b) {
        return new Vect3(y * b.z - z * b.y,
                         z * b.x - x * b.z,
                         x * b.y - y * b.x);
    }

    // Sum
    public Vect3 getSum(Vect3 b) {
        return new Vect3(x + b.x, y + b.y, z + b.z);
    }

    // Offset
    public Vect3 getOffsetFrom(Vect3 b) {
        return new Vect3(x - b.x, y - b.y, z - b.z);
    }

    // Average
    public Vect3 getScaled(float factor) {
        return new Vect3(x * factor, y * factor, z * factor);
    }
}
