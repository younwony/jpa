package dev.wony.jpa1.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Getter
@Setter
@Entity
@DiscriminatorValue("A") // DTYPE에 저장될 값을 지정 (기본값은 클래스명)
public class Album extends Item{

    private String artist;
}
