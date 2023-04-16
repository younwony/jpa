package dev.wony.jpa1;

import dev.wony.jpa1.domain.Book;
import dev.wony.jpa1.domain.Member;
import dev.wony.jpa1.domain.Movie;

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

            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("김영한");

            em.persist(book);

            Movie movie = new Movie();
            movie.setName("JPA1");
            movie.setDirector("김영한1");

            em.persist(movie);

            // Entity
            String qlString = "select count(m) from Member m"; // Entity 직접 사용, count(m) = count(m.id) 로 entity 로 사용해도 id 로 사용 된다.
            String qlString2 = "select count(m.id) from Member m"; // Entity ID 사용
            Long count = em.createQuery(qlString, Long.class).getSingleResult();

            // Entity 를 파라미터로 넘겨도  위와 같이 기본키를 사용하는걸로 동일하다 member = member.id
            String qlString3 = "select m from Member m where m = :member"; // Entity 직접 사용
            String qlString4 = "select m from Member m where m.id = :memberId"; // Entity ID 사용

            // Entity 외래키 사용해도 위와 동일 하다.
            String qlString5 = "select m from Member m where m.team = :team"; // Entity 직접 사용
            String qlString6 = "select m from Member m where m.team.id = :teamId"; // Entity ID 사용

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
