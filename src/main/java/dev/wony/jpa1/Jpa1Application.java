package dev.wony.jpa1;

import dev.wony.jpa1.domain.Member;
import dev.wony.jpa1.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Jpa1Application {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa1"); // EntityManagerFactory는 애플리케이션 전체에서 하나만 생성해서 공유
        EntityManager em = emf.createEntityManager(); // EntityManager는 데이터 변경을 위한 핵심 객체
        EntityTransaction tx = em.getTransaction(); // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
        tx.begin();  // 트랜잭션 시작

        try {

            Team team = new Team();
            team.setName("teamA");

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team);
            em.persist(member);

            em.persist(team);

            em.flush();
            em.clear();

            /*Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();
            System.out.println("findTeam = " + findTeam.getName());*/

            List<Member> selectMFromMemberM = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            // SQL : select m from Member m
            // JPQL : select m from Member m
            // 즉시 로딩(FetchType.EAGER)일 경우 Team 객체를 함께 조회한다. (N+1 문제 발생)

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
