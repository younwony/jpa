package dev.wony.jpa1.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

//@Setter // 불변 객체로 만들기 위해 setter를 제거 -> 생성자를 통해 값을 변경하도록 함
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Column(length = 10)
    private String city;
    @Column(length = 20)
    private String street;
    @Column(length = 5)
    private String zipcode;

    // 공통 메서드 선언해서 사용
    public String fullAddress() {
        return getCity() + " " + getStreet() + " " + getZipcode();
    }
    
    public boolean isValidate() {
        return getCity() != null && getStreet() != null && getZipcode() != null;
    }

    // 동등성 비교, 값 타입은 식별자 개념이 없기 때문에 동등성 비교를 해야함, 식별자가 없는 엔티티는 동일성 비교를 해야함 (ex. Member)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
