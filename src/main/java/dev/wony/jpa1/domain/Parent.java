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
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Child> children = new ArrayList<>();

    public void addChild(Child child) {
        children.add(child);
        child.setParent(this);
    }

}
