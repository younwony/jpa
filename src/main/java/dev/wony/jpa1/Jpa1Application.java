package dev.wony.jpa1;

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
                if (i == 5) {
                    member.setUsername(null);
                }
                member.setAge(i);
                member.setMemberType(MemberType.ADMIN);
                member.changeTeam(team);

                em.persist(member);
            }

            // 기본 Case 식
            String query = "" +
                    "select " +
                    "   case when m.age <= 10 then '학생요금' " +
                    "        when m.age >= 60 then '경로요금' " +
                    "        else '일반요금' " +
                    "   end " +
                    "from Member m";

            em.createQuery(query, String.class)
                    .getResultList()
                    .forEach(System.out::println);

            // 단순 Case 식
            query = "" +
                    "select " +
                    "   case m.memberType " +
                    "       when 'ADMIN' then '관리자' " +
                    "       when 'USER' then '사용자' " +
                    "       else '기타' " +
                    "   end " +
                    "from Member m";

            em.createQuery(query, String.class)
                    .getResultList()
                    .forEach(System.out::println);

            // Coalesce 식, 하나씩 조회해서 null이 아닌 값을 반환, ifnull과 같음
            query = "" +
                    "select " +
                    "   coalesce(m.username, '이름 없는 회원') " +
                    "from Member m";

            em.createQuery(query, String.class)
                    .getResultList()
                    .forEach(System.out::println);

            // Nullif 식, 두 값이 같으면 null을 반환 아니면 첫번째 값 반환 (두 값이 같으면 null을 반환)
            query = "" +
                    "select " +
                    "   nullif(m.username, '관리자') " +
                    "from Member m";

            em.createQuery(query, String.class)
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
