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
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// Item Table 이 없다. 구현 클래스마다 테이블을 만든다. (추천) - 장점은 서브 타입을 명확하게 구분해서 처리할 때 효과적이다. - 단점은 여러 자식 테이블을 함께 조회할 때 성능이 느리다. (UNION SQL을 사용해서 조회) - 조회 성능이 느리다. (UNION SQL을 사용해서 조회) - 조회할 때 자식 테이블을 통합해서 서브 쿼리를 날린다. (UNION SQL을 사용해서 조회), 페이징이 불가능하다. (UNION SQL을 사용해서 조회) - 테이블이 정규화되지 않는다. (중복이 많아진다.) - 상황에 따라 적절하게 사용하자.
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Item {
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
