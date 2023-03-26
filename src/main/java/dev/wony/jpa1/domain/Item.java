package dev.wony.jpa1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
// 상속관계 매핑 전략 - 조인 전략 (각각의 테이블로 나누어서 저장) - 가장 많이 사용하는 전략 (조인이 많이 일어나는 단점이 있음) - 장점은 테이블이 정규화된다. (중복이 제거된다.) - 단점은 조회할 때 조인이 많이 일어나서 성능이 느리다. (N+1 문제) - 조회할 때 조인을 많이 사용하므로 페이징이 불가능하다.
//@Inheritance(strategy = InheritanceType.JOINED)
// 단일 테이블 전략 (한 테이블에 다 넣는다.) - 장점은 조인이 필요 없으므로 일반적으로 조회 성능이 빠르다. - 단점은 자식 엔티티가 매핑한 컬럼은 모두 null을 허용해야 한다. (null을 허용해야 한다.) - 단일 테이블 전략은 @DiscriminatorColumn을 사용해야 한다. (@DiscriminatorColumn 없으면 자동생성, 구분 컬럼을 만들어준다.) - 구분 컬럼을 만들어준다. (DType)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn // DType을 만들어준다. (상속관계 매핑시 구분 컬럼을 만들어준다.), 시스템 운영상 있는 게 좋다.
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id", nullable = false)
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

}
