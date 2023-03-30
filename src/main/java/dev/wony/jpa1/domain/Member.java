package dev.wony.jpa1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
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
import java.util.List;

@Getter
@Setter
@Entity
public class Member extends BaseEntity{ // 상속을 받는다. , 상속을 받으면 부모의 필드도 컬럼으로 인식한다. extends 는 @Entity 나 @MappedSuperclass 만 가능하다.
    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    @Embedded // 값 타입을 직접 사용한다. (기본적으로 값 타입은 임베디드 타입으로 사용한다.)
    private Period workPeriod;
    @Embedded
    private Address homeAddress;
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
