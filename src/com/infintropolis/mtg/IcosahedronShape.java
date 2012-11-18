/* Copyright (C) 2012 Craig Harrison */

package com.infintropolis.mtg;

import java.lang.Math;


public class IcosahedronShape extends PolyShape {

    private final static float PHI = (float)(1 + Math.sqrt(5)) / 2.0f;

    private final static Vertex vertex[] = {
        new Vertex(new Vector( 1.0f,  0.0f,   PHI)),
        new Vertex(new Vector(-1.0f,  0.0f,   PHI)),
        new Vertex(new Vector(-1.0f,  0.0f,  -PHI)),
        new Vertex(new Vector( 1.0f,  0.0f,  -PHI)),

        new Vertex(new Vector( 0.0f,  -PHI, -1.0f)),
        new Vertex(new Vector( 0.0f,  -PHI,  1.0f)),
        new Vertex(new Vector( 0.0f,   PHI,  1.0f)),
        new Vertex(new Vector( 0.0f,   PHI, -1.0f)),

        new Vertex(new Vector(  PHI,  1.0f,  0.0f)),
        new Vertex(new Vector(  PHI, -1.0f,  0.0f)),
        new Vertex(new Vector( -PHI, -1.0f,  0.0f)),
        new Vertex(new Vector( -PHI,  1.0f,  0.0f)) };

    private final static short drawOrder[] = {  0,  6,  1,
                                                0,  1,  5,
                                                0,  5,  9,
                                                0,  9,  8,
                                                0,  8,  6,
                                                1, 10,  5,
                                                1, 11, 10,
                                                1,  6, 11,
                                                2, 11,  7,
                                                2,  7,  3,
                                                2, 10, 11,
                                                2,  3,  4,
                                                2,  4, 10,
                                                3,  7,  8,
                                                3,  8,  9,
                                                3,  9,  4,
                                                4,  9,  5,
                                                4,  5, 10,
                                                6,  8,  7,
                                                6,  7, 11 };

    public IcosahedronShape() {
        // Create faces
        for (int i = 0; i < drawOrder.length; i += PolyShape.VERTICIES_PER_FACE) {
            addFace(new Face(vertex[drawOrder[i]],
                             vertex[drawOrder[i + 1]],
                             vertex[drawOrder[i + 2]]));
        }
        // Generate vertex normals
        computeVertexNormals();
        // Generate texture coordinates
        computeTextureCoords();
    }

    private final void computeVertexNormals() {
        Vector sum = new Vector();
        for (int v = 0; v < vertex.length; v++) {
            sum.set(0.0f, 0.0f, 0.0f);
            int count = 0;
            // Vertex normal is the average of all surrounding face normals
            for (int i = 0; i < numFaces(); i++) {
                Vector norm = getFace(i).getNormal();
                for (int j = 0; j < PolyShape.VERTICIES_PER_FACE; j++) {
                    if (drawOrder[i * PolyShape.VERTICIES_PER_FACE + j] == v) {
                        sum.x += norm.x;
                        sum.y += norm.y;
                        sum.z += norm.z;
                        count++;
                        break;
                    }
                }
            }
            vertex[v].normal = new Vector(sum.x / (float)count,
                                          sum.y / (float)count,
                                          sum.z / (float)count);
        }
    }

    private final void computeTextureCoords() {
        for (int i = 0; i < numFaces(); i++) {
            Face face = getFace(i);
            face.texCoord[0] = new Vector(0.5f, 1.0f, 0.0f);
            face.texCoord[1] = new Vector(0.0f, 0.0f, 0.0f);
            face.texCoord[2] = new Vector(1.0f, 0.0f, 0.0f);
        }
    }
}
