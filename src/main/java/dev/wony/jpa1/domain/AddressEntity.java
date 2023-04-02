package dev.wony.jpa1.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "address")
@NoArgsConstructor
// 실무에서 사용하는 방법
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Address address;

    public AddressEntity(String old1, String street, String number) {
        this.address = new Address(old1, street, number);
    }
}
