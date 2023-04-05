package dev.wony.jpa1;

import dev.wony.jpa1.domain.Member;
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

            for (int i = 0; i < 100; i++) {
                Team team = new Team();
                team.setName("team" + i);

                em.persist(team);

                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                member.changeTeam(team);

                em.persist(member);
            }

//            String qlString = "select m from Member m join m.team t order by m.age desc"; // 내부 조인
//            String qlString = "select m from Member m left join m.team t order by m.age desc"; // 외부 조인
//            String qlString = "select m from Member m , Team  t where m.username = t.name order by m.age desc"; // 세타 조인
//            String qlString = "select m from Member m left join m.team t on t.name = 'A'"; // 조인 대상 필터링 (on 절)
//            String qlString = "select m from Member m left join m.team t on m.username = t.name "; // 조인 대상 필터링 (on 절) - 연관관계 없는 필드로 조인
//            String qlString = "select m from Member m left join m.team t where m.username = t.name "; // 조인 대상 필터링 (where 절) - 연관관계 없는 필드로 조인
            String qlString = "select m from Member m left join Team t on m.username = t.name "; // 조인 대상 필터링 (on 절) - 연관관계 없는 필드로 조인
            // on 과  where 의 차이는 on 은 조인 대상을 필터링 하는 것으로 join 되기 이전에 필터링이 되어 성능상 이점을 가질 수 있다., where 은 조인 이후에 필터링 하는 것이다. where 읽기 쉽고 on 은 복잡하다.

            em.createQuery(qlString, Member.class)
                    .setFirstResult(1) // 시작점,  0부터 시작, 0이면 첫번째,
                    .setMaxResults(10) // 최대 몇개까지 조회할 것인지
                    .getResultList()
                    .forEach(Jpa1Application::printMember);

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
