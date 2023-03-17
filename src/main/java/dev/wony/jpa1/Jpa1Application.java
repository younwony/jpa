package dev.wony.jpa1;

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
            // 비영속 상태
            Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");

            System.out.println("=== BEFORE ===");
            em.persist(member); // 영속성 컨텍스트에 저장
            em.detach(member); // 영속성 컨텍스트에서 분리, DB에는 저장되지 않음, 영속성 컨텍스트에서만 관리
            System.out.println("=== AFTER ===");

            em.remove(member); // 영속성 컨텍스트에서 삭제, DB에서 삭제

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
