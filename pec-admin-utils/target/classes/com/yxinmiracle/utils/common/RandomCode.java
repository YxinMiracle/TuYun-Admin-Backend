package com.yxinmiracle.utils.common;

import java.io.Serializable;
import java.util.Random;

public class RandomCode implements Serializable {

    public static String getRandomCode(){
        Random random = new Random();
        return random.nextInt(10000)+"";
    }

    public static void main(String[] args) {
        System.out.println(getRandomCode());
    }
}
