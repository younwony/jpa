package dev.wony.jpa1.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Getter
@Entity
public class Delivery extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "street", nullable = false)
    private String street;
    @Column(name = "zipcode", nullable = false)
    private String zipcode;
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @OneToOne(mappedBy = "delivery")
    private Order order;

}
