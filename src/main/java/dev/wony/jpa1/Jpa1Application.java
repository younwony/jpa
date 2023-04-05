package dev.wony.jpa1;

import dev.wony.jpa1.domain.Member;
import dev.wony.jpa1.domain.Order;
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
                member.changeTeam(team);

                em.persist(member);
            }

            // Sub query
            // 한계 : 서브쿼리는 JPQL에서 FROM 절에서 사용할 수 없다. -> 조인으로 해결
            // JPA는 WHERE, HAVING 절에서만 서브쿼리 사용 가능


            String qlString = "" +
                    "   select m " +
                    "   from Member m " +
                    "   where m.age > (select avg(m2.age) from Member m2) " +
                    "   order by m.age desc " +
                    " ";
            em.createQuery(qlString, Member.class)
                    .getResultList()
                    .forEach(System.out::println);

            // Sub query exists, 조건을 만족하는 데이터가 있으면 참, team0에 소속된 회원들
            String qlString2 = "" +
                    "   select m " +
                    "   from Member m " +
                    "   where exists (select t from m.team t where t.name = 'team0') " +
                    "   order by m.age desc " +
                    " ";
            em.createQuery(qlString2, Member.class)
                    .getResultList()
                    .forEach(System.out::println);

            // Sub query ALL, 모두 만족하면 참, 전체 상품 각각의 재고보다 주문량이 많은 주문들
            String qlString3 = "" +
                    "   select o " +
                    "   from Order o " +
                    "   where o.orderAmount > ALL (select p.stockAmount from Product p) " +
                    " ";

            em.createQuery(qlString3, Order.class)
                    .getResultList()
                    .forEach(System.out::println);

            // Sub query ANY, SOME, 조건을 하나라도 만족하면 참, 전체 상품 중 재고가 10개 이상인 상품이 하나라도 있는 주문들 -> min
            String qlString4 = "" +
                    "   select o " +
                    "   from Order o " +
                    "   where o.orderAmount > ANY (select p.stockAmount from Product p where p.stockAmount >= 10) " +
                    " ";

            /**             *
             * "any"와 "min"은 특정한 경우에 비슷한 결과를 얻기 위해 사용될 수 있지만 장단점이 다릅니다.
             *
             *
             * "any" 사용의 장점:
             *
             *
             * "Any"는 더 유연하며 값을 최소값이나 최대값뿐만 아니라 컬렉션의 모든 값과 비교하는 데 사용할 수 있습니다.
             * "Any"는 최소값을 계산할 필요가 없기 때문에 일반적으로 "min"보다 빠릅니다.
             *
             * "any" 사용의 단점:
             *
             *
             * "Any"는 컬렉션의 요소가 조건을 충족하는 경우 true를 반환할 수 있으며 이는 의도한 바가 아닐 수 있습니다.
             * 컬렉션에 null 값이 포함된 경우 "Any"는 예기치 않은 결과를 초래할 수 있습니다. -> is not null 사용하여 필터링하면 된다.
             *
             * "최소" 사용의 장점:
             *
             *
             * "Min"은 보다 구체적이며 컬렉션의 정확한 최소값을 반환하며 경우에 따라 유용할 수 있습니다.
             * "최소"는 바람직할 수 있는 null 값을 필터링하는 데 사용할 수 있습니다.
             *
             * "최소" 사용의 단점:
             *
             *
             * "Min"은 컬렉션의 최소값을 계산해야 하므로 "any"보다 느립니다.
             * "Min"은 값을 컬렉션의 최소값과 비교하는 데만 사용할 수 있으며 일부 또는 모든 값을 비교할 수는 없습니다.
             *
             * 요약하면, 값을 컬렉션의 일부 또는 모든 값과 비교하려면 "any"를 사용해야 합니다. 컬렉션의 최소값을 찾거나 null 값을 필터링하려면 "min"을 사용해야 합니다.
             *
             */

            em.createQuery(qlString4, Order.class)
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
