package dev.wony.jpa1;

import javax.persistence.Entity;
import javax.persistence.Id;

// @Table(name = "MEMBER") // 엔티티와 매핑할 테이블 지정 - 기본값은 엔티티 이름을 사용 (단어의 첫 글자는 대문자로) - Member -> MEMBER, 테이블 이름이 다를 경우 사용
//@Entity(name = "Member") // 엔티티 클래스 이름 지정 - 기본값은 클래스 이름을 사용 (단어의 첫 글자는 소문자로) - Member -> member, 다른 패키지에 같은 이름의 클래스가 있을 경우 사용 - dev.wony.jpa1.Member -> Member
@Entity // JPA가 관리하는 엔티티 클래스
public class Member {

    @Id
    private Long id;
    private String name;

    // JPA 스펙상 엔티티 클래스에는 기본 생성자가 필수
    public Member() {
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
