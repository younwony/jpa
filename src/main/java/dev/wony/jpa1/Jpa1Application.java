package dev.wony.jpa1;

import dev.wony.jpa1.domain.Item;
import dev.wony.jpa1.domain.Member;
import dev.wony.jpa1.domain.MemberType;
import dev.wony.jpa1.domain.Team;

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

            for (int i = 0; i < 10; i++) {
                Team team = new Team();
                team.setName("team" + i);

                em.persist(team);

                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                member.setMemberType(MemberType.ADMIN);
                member.changeTeam(team);

                em.persist(member);
            }

            // Enum Type 조회
            String jpql = "select m from Member m join fetch m.team " +
                    "where m.memberType = dev.wony.jpa1.domain.MemberType.ADMIN"; // Enum 타입은 패키지명을 포함해야 한다.

            em.createQuery(jpql, Member.class)
                    .getResultList()
                    .forEach(Jpa1Application::printMemberAndTeam);

            String jpql_1 = "select m from Member m join fetch m.team " +
                    "where m.memberType = :memberType";

            em.createQuery(jpql_1, Member.class)
                    .setParameter("memberType", MemberType.ADMIN)
                    .getResultList()
                    .forEach(Jpa1Application::printMemberAndTeam);

            // Item Book Enum Type 상속 조회
            String jpql_2 = "select i from Item i where type(i) in (Book, Movie)";

            em.createQuery(jpql_2, Item.class)
                    .getResultList()
                    .forEach(System.out::println);



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
        System.out.print("name = " + member.getUsername());
        System.out.println(", age = " + member.getAge());
    }

    public static void printMemberAndTeam(Member member) {
        System.out.println("member = " + member);
        System.out.println("member.team = " + member.getTeam());
    }
}
