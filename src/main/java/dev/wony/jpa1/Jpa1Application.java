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

            // JPQL 기본 함수

            // CONCAT
            String query = "select concat('a', 'b') from Member m"; // ab, 'a' || 'b' 도 가능
            em.createQuery(query, String.class)
                    .getResultList()
                    .forEach(System.out::println);

            // SUBSTRING
            query = "select substring(m.username, 2, 3) from Member m"; // 2번째 인덱스부터 3개의 문자열
            em.createQuery(query, String.class)
                    .getResultList()
                    .forEach(System.out::println);

            // TRIM
            /**
             * 이러한 함수의 주요 차이점은 공백 문자가 제거되는 문자열 부분입니다.
             * 'TRIM'은 문자열의 시작과 끝 모두에서 공백 문자를 제거하고,
             * 'LTRIM'은 문자열의 시작 부분에서만 공백 문자를 제거하며,
             * 'RTRIM'은 문자열의 끝 부분에서만 공백 문자를 제거합니다.
             *
             * 또한 TRIM을 사용하면 문자열에서 제거할 문자를 지정할 수 있는 반면 LTRIM 및 RTRIM은 공백 문자만 제거합니다.
             */
            query = "select trim(m.username) from Member m"; // 공백 제거, ltrim, rtrim 도 가능
            em.createQuery(query, String.class)
                    .getResultList()
                    .forEach(System.out::println);

            // LOWER, UPPER
            query = "select lower(m.username) from Member m"; // 소문자, 대문자
            em.createQuery(query, String.class)
                    .getResultList()
                    .forEach(System.out::println);

            // LENGTH
            query = "select length(m.username) from Member m"; // 문자열 길이
            em.createQuery(query, Integer.class)
                    .getResultList()
                    .forEach(System.out::println);

            // LOCATE
            query = "select locate('de', m.username) from Member m"; // 문자열 위치
            em.createQuery(query, Integer.class)
                    .getResultList()
                    .forEach(System.out::println);

            // ABS, SQRT, MOD, SIZE, INDEX
            query = "select abs(m.age) from Member m"; // 절대값
            em.createQuery(query, Integer.class)
                    .getResultList()
                    .forEach(System.out::println);

            query = "select sqrt(m.age) from Member m"; // 제곱근
            em.createQuery(query, Double.class)
                    .getResultList()
                    .forEach(System.out::println);

            query = "select mod(m.age, 2) from Member m"; // 나머지
            em.createQuery(query, Integer.class)
                    .getResultList()
                    .forEach(System.out::println);

            // SIZE, INDEX
            // SIZE: 컬렉션의 크기, INDEX: 컬렉션의 인덱스
            query = "select size(t.members) from Team t"; // 컬렉션의 크기
            em.createQuery(query, Integer.class)
                    .getResultList()
                    .forEach(System.out::println);
            
            // 왠만하면 Order Column을 사용하지 않는 것이 좋다. 사용 미권유
//            query = "select index(t.members) from Team t"; // 컬렉션의 인덱스, 컬렉션의 인덱스는 0부터 시작, Order Column이 있으면 Order Column의 값이 인덱스가 된다.
//            em.createQuery(query, Integer.class)
//                    .getResultList()
//                    .forEach(System.out::println);

            // 사용자 정의 함수
            // Group Concat
//            query = "select function('group_concat', m.username) from Member m"; // 문자열을 하나로 합침
            query = "select group_concat(m.username) from Member m"; // 문자열을 하나로 합침
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
