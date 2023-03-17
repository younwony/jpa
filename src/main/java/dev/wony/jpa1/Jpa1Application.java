package dev.wony.jpa1;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Jpa1Application {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa1");
    }

}
