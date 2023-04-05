package dev.wony.jpa1;

import dev.wony.jpa1.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class Jpa1Application {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa1"); // EntityManagerFactory는 애플리케이션 전체에서 하나만 생성해서 공유
        EntityManager em = emf.createEntityManager(); // EntityManager는 데이터 변경을 위한 핵심 객체
        EntityTransaction tx = em.getTransaction(); // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
        tx.begin();  // 트랜잭션 시작

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> selectMFromMemberM = em.createQuery("select m from Member m WHERE m.username = :username", Member.class) // TypedQuery는 반환 타입이 명확할 때 사용
                    .setParameter("username", "member1"); // 파라미터 바인딩 , 이름 기준 바인딩 :username, 위치 기준 바인딩 ?1 (1부터 시작) , 이름과 위치 모두 사용 가능, 이름이 우선순위가 높음, 이름이 없으면 위치 기준 바인딩, 위치 기준 사용 X 하도록

            Member singleResult = selectMFromMemberM.getSingleResult(); // 결과가 하나일 때, 결과가 없으면 NoResultException 발생, 결과가 둘 이상이면 NonUniqueResultException 발생, 결과가 없으면 null 반환
            System.out.println("singleResult = " + singleResult.getUsername());
            List<Member> resultList = selectMFromMemberM.getResultList(); // 결과가 여러 개일 때, 결과가 없으면 빈 리스트 반환
            Query query = em.createQuery("select m.username, m.age from Member m"); // Query는 반환 타입이 명확하지 않을 때 사용




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
