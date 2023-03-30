package dev.wony.jpa1.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Setter
@Getter
@Embeddable
@NoArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
