package dev.wony.jpa1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Member extends BaseEntity{ // 상속을 받는다. , 상속을 받으면 부모의 필드도 컬럼으로 인식한다. extends 는 @Entity 나 @MappedSuperclass 만 가능하다.
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    @Embedded // 값 타입을 직접 사용한다. (기본적으로 값 타입은 임베디드 타입으로 사용한다.)
    private Period workPeriod;
    @Embedded
    private Address homeAddress;

    // 값타입 Collections은 '영속석 전이(CASCADE) + 고아 객체 제거' 기능을 필수로 가진다. cascade = CascadeType.ALL, orphanRemoval = true
    // 값 타입 컬렉션은 @ElementCollection, @CollectionTable
    // 기본적으로 지연 로딩(LAZY) 전략을 사용한다.
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "FAVORITYE_FOOD", joinColumns = @JoinColumn(name = "member_id"))// 값 타입 컬렉션
    @Column(name = "food_name") // 예외적으로 값 타입 컬렉션의 컬럼명을 지정할 수 있다., 값이 1개 (String) 이기 때문에 컬럼명을 지정해준다. (food_name) 없으면 favoriteFoods 로 필드명 생성된다.
    private Set<String> favoriteFoods = new HashSet<>(); // 값 타입 컬렉션

    /**
     * 실무에서는 상황에 따라 값 타입을 사용할지, 엔티티를 사용할지 결정해야 한다. 값 타입 권장 하지 않는다.
     * 정말 단순한 값이라면 값 타입을 사용하고, 그렇지 않으면 엔티티를 사용한다.
     */
    // 값타입 Collections은 '영속석 전이(CASCADE) + 고아 객체 제거' 기능을 필수로 가진다. cascade = CascadeType.ALL, orphanRemoval = true
    // 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본 키를 구성해야 한다.
    // 기본적으로 지연 로딩(LAZY) 전략을 사용한다.
    /*@ElementCollection
    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "member_id")) // 값 타입 컬렉션
    // 값 타입 컬렉션 제약사항
    // 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 관련된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
    private List<Address> addressHistory = new ArrayList<>(); // 값 타입 컬렉션*/

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id") // 값 타입일 경우에 예외
    private List<AddressEntity> addressHistory = new ArrayList<>(); // 값 타입 Entity

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id")
    private Locker locker;
    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩, 프록시 객체 조회, 실무에서는 가급적 지연로딩만을 사용한다.
    @JoinColumn(name = "team_id")
    private Team team;
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>(); // 초기화를 해주는 것이 좋다. (null이 아닌 빈 컬렉션을 사용하자) - NPE를 방지할 수 있다. 관례상 초기화를 해주는 것이 좋다.
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

}
