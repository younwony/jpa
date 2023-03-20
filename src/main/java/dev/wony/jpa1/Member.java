package dev.wony.jpa1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq", initialValue = 1, allocationSize = 1)
public class Member {

    @Id // PK 지정, @GeneratedValue를 사용하지 않으면 기본값은 AUTO, 기본 키 생성을 데이터베이스에 위임 (MySQL의 경우 AUTO_INCREMENT)
    // 기본 키 생성 전략 지정 - 기본값은 AUTO, 기본 키 생성을 데이터베이스에 위임 (MySQL의 경우 AUTO_INCREMENT)
    // - IDENTITY -> 기본 키 생성을 데이터베이스에 위임 (MySQL의 경우 AUTO_INCREMENT)
    // - SEQUENCE -> 데이터베이스 시퀀스 오브젝트를 사용해서 기본 키를 할당
    // - TABLE -> 키 생성 전용 테이블을 하나 만들고 여기에 이름과 값을 저장해서 사용
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
    private Long id;
    @Column(name = "name", nullable = false, length = 10, unique = true, insertable = false, updatable = false, columnDefinition = "varchar(100) default 'EMPTY'")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
