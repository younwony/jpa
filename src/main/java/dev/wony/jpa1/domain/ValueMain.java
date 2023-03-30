package dev.wony.jpa1.domain;

public class ValueMain {
    public static void main(String[] args) {
        Integer a = 10;
        Integer b = a;

        a = 20;
        System.out.println("b = " + b);
        System.out.println("a = " + a);

    }
}
