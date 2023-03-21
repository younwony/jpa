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
            Member member = new Member();
            member.setName("member1");
            System.out.println("===============");
            em.persist(member);
            // Identity 전략을 사용하면 persist()를 호출한 직후에 식별자가 즉시 할당된다. - 해당 전략만이 persist()를 호출할 때 바로 INSERT SQL을 데이터베이스에 전송, 쓰기 지연 SQL 저장소를 사용하지 않는다.
            // SEQUENCE 전략을 사용하면 persist()를 호출한 직후에 식별자가 즉시 할당되지 않는다. - 해당 전략은 persist()를 호출할 때 INSERT SQL을 데이터베이스에 전송하지 않고, 식별자를 조회하는 SELECT SQL을 데이터베이스에 전송한다.
            // TABLE 전략을 사용하면 persist()를 호출한 직후에 식별자가 즉시 할당되지 않는다.
            System.out.println("member.getId() = " + member.getId());
            System.out.println("===============");
            tx.commit(); // 트랜잭션 커밋
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
