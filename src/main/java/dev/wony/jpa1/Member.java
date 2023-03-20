package dev.wony.jpa1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/*@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "NAME_AGE_UNIQUE",
                columnNames = {"NAME", "AGE"}
        )
}) // 복합 제약조건*/
// @Table(name = "MEMBER") // 엔티티와 매핑할 테이블 지정 - 기본값은 엔티티 이름을 사용 (단어의 첫 글자는 대문자로) - Member -> MEMBER, 테이블 이름이 다를 경우 사용
//@Entity(name = "Member") // 엔티티 클래스 이름 지정 - 기본값은 클래스 이름을 사용 (단어의 첫 글자는 소문자로) - Member -> member, 다른 패키지에 같은 이름의 클래스가 있을 경우 사용 - dev.wony.jpa1.Member -> Member
@Entity // JPA가 관리하는 엔티티 클래스
public class Member {

    @Id
    private Long id;

    // 매핑할 테이블의 컬럼 지정 - 기본값은 필드 이름을 사용 (단어의 첫 글자는 대문자로) - name -> NAME 다른 이름을 사용하고 싶을 경우 사용
    // nullable = false -> null 허용하지 않음
    // length = 10 -> 길이 제한
    // unique = true -> 중복 허용하지 않음, UK 이름이 자동으로 생성되어 잘 사용하지 않는다 (UK 이름을 직접 지정하고 싶을 경우 @Table(uniqueConstraints)을 사용)
    // insertable = false -> insert SQL에 포함하지 않음
    // updatable = false -> update SQL에 포함하지 않음
    // columnDefinition = "varchar(100) default 'EMPTY'" -> DDL 생성 시 사용할 컬럼 정보를 직접 지정
    @Column(name = "name", nullable = false, length = 10, unique = true, insertable = false, updatable = false, columnDefinition = "varchar(100) default 'EMPTY'")
    private String name;

    // precision -> 전체 자릿수
    // scale -> 소수점 자릿수
    // @Column(precision = 10, scale = 3) -> 10자리 중 소수점 3자리까지 표현, ex) 1234567.123
    @Column(precision = 10, scale = 3)
    private BigDecimal age;

    // EnumType.ORDINAL -> 숫자로 저장
    // EnumType.STRING -> 문자로 저장 (기본값) - 문자로 저장하면 나중에 Enum의 순서가 바뀌어도 문제가 없다. (숫자로 저장하면 순서가 바뀌면 데이터가 꼬일 수 있다.)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    // 날짜와 시간 정보를 모두 저장 (기본값) - @Temporal을 사용하지 않으면 날짜와 시간 정보를 모두 저장 (기본값)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    // 자바 8부터 지원하는 날짜와 시간 API 사용 가능 (JPA 2.1부터 지원) - @Temporal 사용하지 않아도 자동으로 매핑
    private LocalDate lastModifiedDate;
    // BLOB, CLOB 타입 매핑 - BLOB -> byte[], CLOB -> String 타입 매핑 - @Lob을 사용하지 않으면 CLOB 타입은 String, BLOB 타입은 byte[]로 매핑 (기본값)
    @Lob
    private String description;

    // transient -> JPA가 관리하지 않는 필드, DB에 매핑되지 않는다. -> DB에 저장되지 않는다.
    @Transient
    private int temp;


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
