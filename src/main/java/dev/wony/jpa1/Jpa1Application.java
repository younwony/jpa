package dev.wony.jpa1;

import dev.wony.jpa1.domain.Address;
import dev.wony.jpa1.domain.AddressEntity;
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

            Member member = em.find(Member.class, member1.getId());
            System.out.println("member = " + member);

            // 값 타입 수정
//            member.getHomeAddress().setCity("newCity"); // 값타입은 immutable 객체로 설계해야 한다. 따라서 값타입의 특정 필드 값을 변경하려면, setter를 사용하면 안된다. 대신 값타입 전체를 교체해야 한다.
            Address newAddress = new Address("newCity", member.getHomeAddress().getStreet(), member.getHomeAddress().getZipcode()); // 값타입 생성 후 교체
            member.setHomeAddress(newAddress);

            // 값 타입 컬렉션 수정,
            member.getFavoriteFoods().remove("치킨"); // 값타입 컬렉션은 값타입을 추가, 삭제할 때, 값타입을 참조하는 엔티티를 통해서만 수정할 수 있다.
            member.getFavoriteFoods().add("한식");

            // 값 타입 컬렉션 수정2
            // 값 타입 컬렉션 제약사항
            // 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 관련된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
            member.getAddressHistory().remove(new AddressEntity("old1", "street", "10000")); // 값 타입을 수정, 삭제를 하기 위해서는 equals, hashCode를 재정의 해야 한다. (값타입은 식별자 개념이 없기 때문에) 기본적으로 equals or hashcode 로 찾는다.
            member.getAddressHistory().add(new AddressEntity("newCity1", "street", "10000"));



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
