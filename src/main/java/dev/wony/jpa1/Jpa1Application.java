package dev.wony.jpa1;

import dev.wony.jpa1.domain.Book;
import dev.wony.jpa1.domain.Item;
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

            // Type 다형성 쿼리
            String query = "select i from Item i where type(i) in (Book, Movie)";

            em.createQuery(query, Item.class)
                    .getResultList()
                    .forEach(System.out::println);

            // TREAT 다형성 쿼리
            String query2 = "select i from Item i where treat(i as Book).author = '김영한'";
            em.createQuery(query2, Item.class)
                    .getResultList()
                    .forEach(System.out::println);

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
