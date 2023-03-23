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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Member {

    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*@Column(name = "team_id")
    private Long teamId;*/
    @ManyToOne
    @JoinColumn(name = "team_id") // 연관관계 주인 지정 (FK가 있는 곳), 일반적으로 연관관계 주인은 외래키를 가지고 있는 곳이다. 일반적으로 team없이는 member가 존재할 수 없다.
    private Team team;
    private String username;
    private String city;
    private String street;
    private String zipcode;
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>(); // 초기화를 해주는 것이 좋다. (null이 아닌 빈 컬렉션을 사용하자) - NPE를 방지할 수 있다. 관례상 초기화를 해주는 것이 좋다.

    // 연관관계 편의 메소드, 양방향 연관관계일 때 사용한다. 양방향 연관관계일 때는 양쪽에 값을 세팅해줘야 한다.
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
