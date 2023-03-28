package dev.wony.jpa1;

import dev.wony.jpa1.domain.Child;
import dev.wony.jpa1.domain.Member;
import dev.wony.jpa1.domain.Parent;

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

            Parent parent = new Parent();
            parent.setName("parent1");


            Child child1 = new Child();
            child1.setName("child1");
            parent.addChild(child1);

            Child child2 = new Child();
            child2.setName("child2");
            parent.addChild(child2);

            em.persist(parent); // casecade = CascadeType.ALL, persist(parent)만 해도 child1, child2도 persist된다.

            em.flush();
            em.clear();

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void printMember(Member member) {
        System.out.println("member = " + member.getUsername());
    }

    public static void printMemberAndTeam(Member member) {
        System.out.println("member = " + member);
        System.out.println("member.team = " + member.getTeam());
    }
}
