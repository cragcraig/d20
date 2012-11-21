/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;


public interface Vect {

    // Write to float
    public void asFloats(float[] out, int offset);

    // Returns new float array
    public float[] getFloats();
}
