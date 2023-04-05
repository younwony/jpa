package dev.wony.jpa1;

import dev.wony.jpa1.domain.Member;
import dev.wony.jpa1.domain.MemberDTO;

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
            
            // 프로젝션
            String sql1 = "select m from Member m"; // 엔티티 프로젝션, 엔티티를 조회하는 것과 같다., 영속성 컨텍스트에서 관리
            String sql2 = "select m.team from Member m"; // 엔티티 프로젝션, 엔티티를 조회하는 것과 같다., 영속성 컨텍스트에서 관리
            String sql2_1 = "select t from Member m join m.team t"; // 2번과 같은 쿼리 (조인), 좀더 명시적으로 표현 하는게 유지보수성에 좋다.
            String sql3 = "select o.address from Order o"; // 임베디드 프로젝션, 임베디드 타입을 조회하는 것과 같다.
            String sql4 = "select m.username, m.age from Member m"; // 스칼라 프로젝션
            String sql4_1 = "select new dev.wony.jpa1.domain.MemberDTO(m.username, m.age) from Member m"; // 스칼라 프로젝션 + DTO 조회 (DTO를 생성하는 것과 같다.) , 패키지명 포함

            TypedQuery<Member> selectMFromMemberM = em.createQuery("select m from Member m", Member.class); // TypedQuery는 반환 타입이 명확할 때 사용
            List<Member> resultList = selectMFromMemberM.getResultList();

            // 스칼라 타입
            Query query = em.createQuery(sql4);
            TypedQuery<Object[]> query1 = em.createQuery(sql4, Object[].class); // 스칼라 타입은 Object[]로 조회 한다. (타입이 명확하지 않기 때문에)
            List<Object[]> resultList1 = query.getResultList(); // 스칼라 타입은 Object[]로 조회 한다. (타입이 명확하지 않기 때문에)
            for (Object[] objects : resultList1) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
            }
            // 스칼라 타입을 DTO로 조회
            TypedQuery<MemberDTO> query2 = em.createQuery(sql4_1, MemberDTO.class);
            List<MemberDTO> resultList2 = query2.getResultList();
            for (MemberDTO memberDTO : resultList2) {
                System.out.println("memberDTO = " + memberDTO.getUsername());
                System.out.println("memberDTO = " + memberDTO.getAge());
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
        System.out.println("member = " + member.getUsername());
    }

    public static void printMemberAndTeam(Member member) {
        System.out.println("member = " + member);
        System.out.println("member.team = " + member.getTeam());
    }
}
