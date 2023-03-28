package dev.wony.jpa1;

import dev.wony.jpa1.domain.Member;
import dev.wony.jpa1.domain.Team;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;

public class Jpa1Application {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa1"); // EntityManagerFactory는 애플리케이션 전체에서 하나만 생성해서 공유
        EntityManager em = emf.createEntityManager(); // EntityManager는 데이터 변경을 위한 핵심 객체
        EntityTransaction tx = em.getTransaction(); // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
        tx.begin();  // 트랜잭션 시작

        try {

            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);
            
            Member member2 = new Member();
            member2.setUsername("member1");
            em.persist(member2);

            em.flush();
            em.clear();

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member referenceMember = em.getReference(Member.class, 1L);// 조회 , 실제 객체를 조회하는 것이 아니라 프록시 객체를 조회한다. (프록시 객체는 실제 객체의 참조를 보관하고 있다.)
            Member findMember = em.find(Member.class, 1L);// 조회, 실제 객체를 조회한다. (프록시 객체가 아니다.)
            Member referenceMember2 = em.getReference(Member.class, 1L);// 조회 , 실제 객체를 조회하는 것이 아니라 프록시 객체를 조회한다. (프록시 객체는 실제 객체의 참조를 보관하고 있다.)
            System.out.println("referenceMember.getClass() = " + referenceMember.getClass()); //실제 객체를 사용할 때 초기화를 한다. (프록시 객체를 초기화 한다.)
            // 프록시 객체는 처음 사용할 때 한번만 초기화 한다. (프록시 객체를 초기화 한다.)
            // 실제 엔티티로 바뀌는 것이 아니라 프록시 객체가 실제 객체를 상속 받는다. == 비교 실패 (프록시 객체는 실제 객체의 참조를 보관하고 있다.), instanceOf로 비교

            System.out.println("findMember == referenceMember = " + (findMember == referenceMember)); // 같은 트랜잭션 안에서는 같은 객체를 반환한다. referenceMember는 프록시가 아니라 실제 객체를 반환한다.
            System.out.println("referenceMember == referenceMember2 = " + (referenceMember == referenceMember2)); // 같은 트랜잭션 안에서는 같은 객체를 반환한다. 같은 프록시를 반환한다.

            /**
             *  같은 트랜잭션 안에서는 같은 객체를 반환한다.(중요)
             *
             *  findMember == referenceMember = true
             *  Member member1 = em.getReference(Member.class, 1L);
             *  Member member2 = em.find(Member.class, 1L);
             *
             *  member1 == member2 = true (proxy 로 동일)
             *
             *  Member member1 = em.find(Member.class, 1L);
             *  Member member2 = em.getReference(Member.class, 1L);
             *
             *  member1 == member2 = true (Entity(객체) 로 동일)
             *
             *  먼저 받아온 객체 타입으로 동일하게 구성된다.
             *
             * **/

            PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();
            System.out.println("persistenceUnitUtil.isLoaded(referenceMember) = " + persistenceUnitUtil.isLoaded(referenceMember)); // 프록시 객체를 초기화 했는지 확인한다.
            System.out.println(referenceMember.getClass());
            Hibernate.initialize(referenceMember); // 프록시 객체를 초기화 한다.
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
