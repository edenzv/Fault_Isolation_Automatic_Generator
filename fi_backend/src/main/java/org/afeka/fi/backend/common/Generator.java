package org.afeka.fi.backend.common;


import java.util.Random;
import java.util.UUID;

public class Generator {

    public static Integer number(){
       return new Random().nextInt(Integer.MAX_VALUE);
    }

    public static String id(){
        return UUID.randomUUID().toString();
    }
}
