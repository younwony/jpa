package dev.wony.jpa1;

import dev.wony.jpa1.domain.Address;
import dev.wony.jpa1.domain.AddressEntity;
import dev.wony.jpa1.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class Jpa1Application {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa1"); // EntityManagerFactory는 애플리케이션 전체에서 하나만 생성해서 공유
        EntityManager em = emf.createEntityManager(); // EntityManager는 데이터 변경을 위한 핵심 객체
        EntityTransaction tx = em.getTransaction(); // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
        tx.begin();  // 트랜잭션 시작

        try {
            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setHomeAddress(new Address("city", "street", "10000"));

            member1.getFavoriteFoods().add("치킨");
            member1.getFavoriteFoods().add("피자");
            member1.getFavoriteFoods().add("족발");

            member1.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
            member1.getAddressHistory().add(new AddressEntity("old2", "street2", "10001"));
            // 값타입 Collections은 '영속석 전이(CASCADE) + 고아 객체 제거' 기능을 필수로 가진다. cascade = CascadeType.ALL, orphanRemoval = true

            em.persist(member1);

            em.flush();
            em.clear();

            // 1. JPQL 사용
            List<Member> selectMFromMemberM = em.createQuery("select m from Member m WHERE m.username like '%member%' ", Member.class)
                    .getResultList();

            // 2. JPQL Criteria 사용 - (실무에서 잘 사용하지 않음 - 비 권장) - 코드가 복잡하고, 유지보수가 어려움 - 실무에서는 QueryDSL을 사용
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);

            CriteriaQuery<Member> cq = query.select(m)
                    .where(cb.like(m.get("username"), "%member%"));
            String username = "member1";
            
            // 동적 쿼리 사용
            if (username != null) {
                cq.where(cb.equal(m.get("username"), username));
            }

            List<Member> resultList = em.createQuery(cq).getResultList();

            // 3. QueryDSL 사용 - 실무에서 가장 많이 사용 + 권장, ( jpa1 에서는 다루지 않음 )
            // QueryDSL은 JPQL을 생성해주는 빌더 역할을 한다.
            // 코드가 간결하고 유지보수가 쉬움
            // 동적 쿼리 작성이 쉬움
            // 타입 안정성이 보장, IDE 지원
            // 컴파일 시점에 문법 오류를 찾을 수 있음
            // 오타 등의 실수를 줄일 수 있음
            // 런타임에 발생할 수 있는 오류를 컴파일 시점에 찾을 수 있음
            // 단. Setting 하기가 어려움

            // 4. 네이티브 SQL 사용
            // SQL을 직접 사용하는 방법
            // 데이터베이스에 의존적인 코드가 될 수 있음
            // JPA 표준이 아니므로 하이버네이트만 지원
            // JPQL을 사용하는 것을 권장
            List<Member> nativeMembers = em.createNativeQuery("select MEMBER_ID, CITY, STREET, ZIPCODE, USERNAME from MEMBER", Member.class)
                            .getResultList();

            // 5. JDBC API 직접 사용
            // JPA를 사용하지 않고 JDBC API를 직접 사용하는 방법
            // JPA를 사용하지 않으므로 영속성 컨텍스트를 사용할 수 없음
            // JPA를 사용하지 않으므로 트랜잭션을 지원하지 않음
            // JPA를 사용하지 않으므로 데이터베이스 방언을 사용할 수 없음
            // JPA를 사용하지 않으므로 데이터베이스 벤더에 종속적인 코드를 작성해야 함
            // JPA를 사용하지 않으므로 JPA가 제공하는 기능을 사용할 수 없음


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
