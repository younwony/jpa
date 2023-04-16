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

            /**
             * 1. Fetch Join 대상에는 별칭을 주지 않는게 좋다.
             * fetch join 대상 entity 를 조회 후 조작 했을 때 문제가 발생할 수 있다.
             * fetch join 전부를 대상으로 처리를 기대했지만 fetch join 대상 entity 를 조작하면 fetch join 대상 entity 를 제외한 나머지 entity 는 영속성 컨텍스트에 존재하지 않기 때문에 문제가 발생할 수 있다.
             *
             * 2. 둘 이상의 컬렉션은 fetch join 할 수 없다.
             *
             * 3. 컬렉션을 fetch join 하면 페이징 API를 사용할 수 없다.
             * 1 : 1, N : 1 같은 단일 값 관계에서는 페이징 API를 사용할 수 있다.
             * 페이징  API를 사용하면 메모리에서 페이징을 하기 때문에 DB 에서 페이징을 하는 것보다 성능이 떨어진다.
             * 그래서 페이징을 하려면 DB 에서 페이징을 해야 한다.
             *
             * 4. 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선시함.
             * @OneToMany(fetch = FetchType.LAZY) 로 설정했지만 fetch join 을 사용하면 글로벌 로딩 전략을 무시하고 fetch join 을 사용한다.
             * 
             * 5. 최적화가 필요한곳은 대부분 fetch join 으로 해결할 수 있다.
             * 
             * 6. 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
             *
             * ** 7. 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하는 경우에는 페치 조인 대신 일반 조인을 사용하고,
             * 필요한 데이터만 조회해서 DTO로 반환하는 것이 효과적
             */

/*            String query = "select t from Team t join fetch t.members m where m.username = :username";
            Team findTeam = em.createQuery(query, Team.class)
                    .setParameter("username", "member1")
                    .getSingleResult();*/

            // paging
            String query = "select t from Team t";
            List<Team> resultList = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            System.out.println("resultList.size() = " + resultList.size());

            for (Team team1 : resultList) {
                System.out.println("team1 = " + team1);
                System.out.println("team1.members = " + team1.getMembers());
            }

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
