package dev.wony.jpa1.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Entity
// N:M일 경우 연결 테이블로 승격하여 사용 권장
public class MemberProduct extends BaseEntity{

    @Id
    @GeneratedValue
    // 연결 ID (member_id, product_id 를 PK로 사용할 수 있지만, 별도의 PK를 따서 하는게 일관성 + 유연성에 좋다)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    private LocalDateTime orderDateTime;
}
