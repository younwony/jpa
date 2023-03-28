package dev.wony.jpa1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Setter
@Getter
@Entity
public class Locker extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToOne(mappedBy = "locker", fetch = FetchType.LAZY)
    private Member member;
}
