package dev.wony.jpa1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

@Setter
@Getter
@MappedSuperclass // 상속관계 매핑시 공통 매핑 정보를 모으는 역할 (ex. BaseEntity), 실제 테이블과 매핑되지 않음, 직접 사용하지 않기 때문에 abstract로 선언
public abstract class BaseEntity {

    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;

}
