/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import java.lang.Math;


public class Vector {

    public float x;
    public float y;
    public float z;

    public Vector() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Vector(float x, float y, float z) {
        set(x, y, z);
    }

    public Vector(float[] values, int offset) {
        this(values[offset], values[offset + 1], values[offset + 2]);
    }

    public Vector(float[] values) {
        this(values, 0);
    }

    // Set values
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Write to float
    public void asFloats(float[] out, int offset) {
        out[offset] = x;
        out[offset + 1] = y;
        out[offset + 2] = z;
    }

    // Clone
    // Don't implement Cloneable so we don't have to deal with an exception that
    // won't be raised anyway.
    public Vector clone() {
        return new Vector(x, y, z);
    }

    // Normalize
    public void normalize() {
        float len = (float)Math.sqrt(x * x + y * y + z * z);
        if (len != 0.0f) {
            set(x / len, y / len, z / len);
        }
    }

    // Clone and normalize clone
    public Vector getNormalized() {
        Vector norm = clone();
        norm.normalize();
        return norm;
    }

    // Cross product
    public Vector getCrossProduct(Vector b) {
        return new Vector(y * b.z - z * b.y,
                          z * b.x - x * b.z,
                          x * b.y - y * b.x);
    }

    // Offset
    public Vector getOffsetFrom(Vector b) {
        return new Vector(x - b.x, y - b.y, z - b.z);
    }
}
