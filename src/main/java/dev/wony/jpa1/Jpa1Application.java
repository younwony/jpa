package dev.wony.jpa1;

import dev.wony.jpa1.domain.Member;

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

            for (int i = 0; i < 20; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(10);
                em.persist(member);
            }

            /**
             * 벌크 연산 - 쿼리 한번으로 여러 테이블을 수정할 수 있다.
             *
             *  - 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리를 날린다.
         *          (벌크 연산을 먼저 실행하고 영속성 컨텍스트를 초기화 하면 영속성 컨텍스트에는 벌크 연산을 실행하기 전의 데이터가 남아있기 때문에 문제가 발생할 수 있다.)
             *      1. 벌크 연산을 먼저 실행하고 영속성 컨텍스트를 사용 한다. - 이 방법을 사용하지 않는다. (영속성 컨텍스트에 아무 것도 두지않고 벌크연산 먼저 실행)
             *      2. 영속성 컨텍스트를 초기화 하지 않고 벌크 연산을 먼저 실행하고 영속성 컨텍스트를 초기화 한다. - 이 방법을 사용한다. (영속성 컨텍스트에 뭐가있던 상관없이 벌크연산 먼저 실행 후 영속성 컨텍스트 초기화)
             *          - em.clear() 를 호출하면 영속성 컨텍스트를 초기화 한다.
             *              -> JPA Data 사용법
             *              -> @Modifying(clearAutomatically = true) 를 사용하면 자동으로 영속성 컨텍스트를 초기화 한다.
             *              -> @Query(value = "update Member m set m.age = 20") 를 사용하면 자동으로 영속성 컨텍스트를 초기화 한다.
             *  - executeUpdate() 를 호출하면 영속성 컨텍스트를 초기화 한다.
             *  - executeUpdate() 의 결과는 영향받은 엔티티의 개수이다.
             *  - Update, Delete 쿼리를 사용할 수 있다.
             *  - Insert 쿼리를 사용할 수 없다.(하이버 네이트는 insert into select 쿼리를 사용할 수 있다.)
             */

            // flush 자동 호출, commit, query 실행 시에 flush 자동 호출
            int count = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("count = " + count);

            // Member 조회 해보면 age 가 변경 되지 않았다. (벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리를 날린다.)
            printMember(em.find(Member.class, 1L));

            /**
             * 벌크 연산을 먼저 실행하고 영속성 컨텍스트를 초기화 한다.
            */
            em.clear();

            // Member 조회 해보면 age 가 변경 되었다.
            printMember(em.find(Member.class, 1L));



            // Lazy Loading 보다 Fetch Join 이 우선
            em.flush();
            em.clear();


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
