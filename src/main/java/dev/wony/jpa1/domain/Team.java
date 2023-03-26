package dev.wony.jpa1.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "team_id", nullable = false)
    private Long id;
    private String name;
    @OneToMany
    @JoinColumn(name = "team_id") // 사용하지 않으면 중간 테이블이 생성된다. (team_member) - 중간 테이블을 사용하지 않고 싶다면 @JoinColumn을 사용하자.
    private List<Member> members = new ArrayList<>();

}
