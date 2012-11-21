/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;


public class Vertex {

    public Vect3 pos;
    public Vect3 normal;

    public Vertex(Vect3 pos, Vect3 normal) {
        this.pos = pos;
        this.normal = normal;
    }

    public Vertex(Vect3 pos) {
        this(pos, null);
    }
}
