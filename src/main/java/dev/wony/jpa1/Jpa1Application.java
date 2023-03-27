package dev.wony.jpa1;

import dev.wony.jpa1.domain.Member;
import dev.wony.jpa1.domain.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Jpa1Application {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa1"); // EntityManagerFactory는 애플리케이션 전체에서 하나만 생성해서 공유
        EntityManager em = emf.createEntityManager(); // EntityManager는 데이터 변경을 위한 핵심 객체
        EntityTransaction tx = em.getTransaction(); // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
        tx.begin();  // 트랜잭션 시작

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setCity("서울");
            member.setStreet("강가");
            member.setZipcode("12345");
            member.setCreatedDate("2021-01-01");
            member.setCreatedBy("wony");
            member.setLastModifiedDate("2021-01-01");
            member.setLastModifiedBy("wony");

            em.persist(member);

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
