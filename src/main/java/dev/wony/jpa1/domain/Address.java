package dev.wony.jpa1.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

//@Setter // 불변 객체로 만들기 위해 setter를 제거 -> 생성자를 통해 값을 변경하도록 함
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
