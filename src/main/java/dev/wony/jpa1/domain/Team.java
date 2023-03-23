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
import javax.persistence.OneToMany;
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
    @OneToMany(mappedBy = "team") // 주인이 아닌 곳에 mappedBy를 적어준다. 주인이 아닌 곳은 FK를 가지고 있지 않다. 주인이 아닌쪽은 읽기만 가능하다.
    private List<Member> members;

    // 연관관계 편의 메소드 (양방향일 때) - 주인쪽에만 적어주면 된다. 주인쪽에만 적어주면 연관관계 편의 메소드를 사용할 수 있다. Team, Member 둘 중 하나만 적어주면 된다.
    public void addMember(Member member) {
        members.add(member);
        member.setTeam(this);
    }

    // toString(), lombok, JSON 생성 라이브러리 등 을 사용할 때는 주의해야 한다. 무한 루프에 빠질 수 있다.
}
