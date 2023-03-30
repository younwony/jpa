package dev.wony.jpa1.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

//@Setter // 불변 객체로 만들기 위해 setter를 제거 -> 생성자를 통해 값을 변경하도록 함
@Getter
@Access(AccessType.FIELD)
@Embeddable // 값 타입을 정의하는 곳에 사용, 임베디드 타입은 자바 기본 타입이나 임베디드 타입만 사용 가능, 임베디드 타입은 값 타입을 포함한 모든 값들을 복사해서 사용
@NoArgsConstructor
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public boolean isWork() {
        return startDate.isBefore(LocalDateTime.now()) && endDate.isAfter(LocalDateTime.now());
    }
}
