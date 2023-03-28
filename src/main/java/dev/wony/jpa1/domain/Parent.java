package dev.wony.jpa1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Parent {

    @Id
    @GeneratedValue
    @Column(name = "parent_id")
    private Long id;
    private String name;
    // cascade = CascadeType.ALL, 하위 엔티티를 함께 저장, persist(parent) 하면 child도 함께 저장,
    // 오로지 하나의 부모가 자식을 관리하게 될 때 주로 사용, 자식의 소유자가 하나일 때 사용하는게 좋다, 그렇지 않으면 부모와 자식의 라이프 사이클이 같을 때 사용

    // orphanRemoval 고아 객체 제거: 부모와 연관관계가 끊어진 객체는 제거한다. (연관관계가 끊어진 객체는 부모가 관리하지 않는다.), 참조하는 곳이 하나일 떄 사용, @OneToOne, @OneToMany 만 사용 가능
    // cascade + orphanRemoval 을 사용하면 부모를 제거할 때 자식도 함께 제거된다. (참조하는 곳이 하나일 때 사용), 부모와 자식의 생명주기를 동일시 할 수 있다.
    // + DDD에서 Aggregate Root개념을 구현할 떄 유용
    @OneToMany(mappedBy = "parent",cascade = CascadeType.PERSIST, orphanRemoval = true)
    /**
     *
     * childe.remove(0) 할 때 CasecadeType이 PERSIST, ALL인 경우에만 orphanRemoval이 동작한다 하지만 이것은 버그이다!
     * - parent 자체를 remove 할 때는 Casecade 없어도 정상 작동
     *
     * 답변
     *
     * JPA 스펙상 원칙적으로 CascadeType.PERSIST이 없어도 orphanRemoval만으로 삭제되어야 하는 것이 맞습니다.
     *
     * 하이버네이트 구현체에서는 해당 기능에 버그가 있고, 그래서 CascadeType.PERSIST(또는 ALL)이 함께 적용되어야 동작합니다.
     *
     * (찾아보니 이클립스 링크 같은 다른 구현체에서는 정상 동작한다는 이야기도 있네요.)
     *
     * 버그 리포트: https://hibernate.atlassian.net/browse/HHH-6709
     *
     * 그런데 이 부분이 실제 개발을 할 때는 크게 영향이 없는데, 그 이유는 orphanRemoval만 따로 적용하는 경우는 거의 없고, 보통 주인 엔티티가 하위 엔티티를 관리하는 경우에는 CascadeType.PERSIST + orphanRemoval을 함께 적용하기 때문입니다.
     *
     * 추가로 실무에서 데이터를 향후 복구나 이력 확인을 위해서, 직접적으로 삭제하는 경우도 드물기 때문에 orphanRemoval 옵션 자체를 사용하는 경우도 드뭅니다.
     *
     * 감사합니다.
     */
    private List<Child> children = new ArrayList<>();

    public void addChild(Child child) {
        children.add(child);
        child.setParent(this);
    }

}
