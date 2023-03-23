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
            team.setName("TeamA");
//            team.setMembers(List.of(member)); // 읽기 전용이므로 수정 불가
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.changeTeam(team); // 연관관계 편의 메소드 사용 (양방향 연관관계일 때)
            em.persist(member);


            // 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정하자.
//            team.getMembers().add(member); // 넣어주지 않으면 member에 team이 null로 들어간다.

            Member findMember = em.find(Member.class, member.getId());// 영속성 컨텍스트에 캐시되어있기 때문에 select 쿼리가 나가지 않는다.
            List<Member> members = findMember.getTeam().getMembers(); // 영속성 컨텍스트에 캐시되어있기 때문에 select 쿼리가 나가지 않는다., 따라서 양쪽 Entity에 값을 넣어줘야 한다.
            // 즉 양방향 연관관계는 항상 양쪽에 값을 넣어줘야 한다.

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
