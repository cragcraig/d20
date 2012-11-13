/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;


public class Vertex {

    public Vector pos;
    public Vector normal;

    public Vertex(Vector pos, Vector normal) {
        this.pos = pos;
        this.normal = normal;
    }

    public Vertex(Vector pos) {
        this(pos, null);
    }
}
