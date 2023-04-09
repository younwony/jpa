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

            String query = "" +
                    "select m.username " + // 상태 필드, 단순 값 , 경로탐색의 끝 (탐색 불가)
                    "   ,m.team.name " + // 단일 값 연간 경로 (탐색가능, 묵시적 내부 조인 발생) -> 묵시적 내부 조인이 발생하지 않도록 한다. 튜닝하기 어렵다.
                    " from Member m " +
                    " join m.team t " + // 단일 값 연관 필드 (탐색가능, 묵시적 내부 조인 발생)
                    " join m.orders o " + // 컬렉션 값 연관 필드 (탐색 불가능, 묵시적 내부 조인 발생)
                    "where t.name = 'member0'"
                    ;

            em.createQuery(query, Object[].class)
                    .getResultList()
                    .forEach(System.out::println);

            query = "select t.members.size from Team t"; // size만 탐색 가능
            em.createQuery(query, Object.class)
                                .getResultList()
                                .forEach(System.out::println);

            query = "select t.members from Team t";
            em.createQuery(query, Object.class)
                    .getResultList()
                    .forEach(System.out::println);

            // 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능
            query = "select t.name from Member m join m.team t";
            em.createQuery(query, Object.class)
                    .getResultList()
                    .forEach(System.out::println);

            /**
             *  실무 조언
             *
             *  1. 가급적 묵시적 조인보다는 명시적 조인을 사용하자. (명시적 조인 반드시 사용)
             *  2. 조인은 SQL 튜닝에 중요 포인트 인데 묵시적 조인을 사용하면 튜닝하기 어렵다.
             *  3. 묵시적 조인은 조인 상황을 한눈에 파악하기 어렵다.
             */

            query = "select o.member.team from Order o";
            em.createQuery(query, Object.class)
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
