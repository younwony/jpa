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

                em.persist(team);
                Team team2 = new Team();
                team2.setName("teamB");

                em.persist(team2);

                Member member = new Member();
                member.setUsername("member1");
                member.changeTeam(team);

                em.persist(member);
                Member member1 = new Member();
                member1.setUsername("member2");
                member1.changeTeam(team);

                em.persist(member1);

                Member member2 = new Member();
                member2.setUsername("member3");
                member2.changeTeam(team2);

                em.persist(member2);

                Member member3 = new Member();
                member3.setUsername("member4");
                member3.changeTeam(team2);

                em.persist(member3);
            // Lazy Loading 보다 Fetch Join 이 우선
            em.flush();
            em.clear();

            // N : 1 관계
            String query = "select m from Member m join fetch m.team";
            List<Member> resultList1 = em.createQuery(query, Member.class)
                    .getResultList();
            resultList1
                    .forEach(Jpa1Application::printMemberAndTeam);


            // 1 : N 관계, 데이터가 더 많아짐
            String query2 = "select t from Team t join fetch t.members";
            List<Team> resultList = em.createQuery(query2, Team.class)
                    .getResultList();
            resultList
                    .forEach(team1 -> {
                        System.out.println("team1 = " + team1);
                        team1.getMembers().forEach(Jpa1Application::printMember);
                    });

            // 1 : N 관계, 데이터가 더 많아짐, DISTINCT 추가, 중복 제거(SQL 만) + (JPA 에서는 엔티티 중복 제거 도 포함, 따라서 실제 SQL 에서 Query 하는 결과 값가 다르다.)
            String query3 = "select distinct t from Team t join fetch t.members";
            List<Team> resultList3 = em.createQuery(query3, Team.class)
                    .getResultList();
            resultList3
                    .forEach(team1 -> {
                        System.out.println("team1 = " + team1);
                        team1.getMembers().forEach(Jpa1Application::printMember);
                    });

            // 일반 조인 -> Member 만 조회 (Team 은 조회 X) , 일반 조인은 연관된 엔티티를 함께 조회할 수 없다.
            String query4 = "select m from Member m join m.team t ";
            List<Member> resultList4 = em.createQuery(query4, Member.class)
                    .getResultList();

            // Fetch Join
            String query5 = "select m from Member m join fetch m.team";
            List<Member> resultList5 = em.createQuery(query5, Member.class)
                    .getResultList();


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
