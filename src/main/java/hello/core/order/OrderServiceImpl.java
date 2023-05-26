package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

// 롬복 적용하기 @RequiredArgsConstructor을 통해서 생성자 주입 부분을 자동으로 생성해줌
// 생성자 부분을 생략해도 되기 때문에 코드를 간결하게 유지 할 수 있음
@Component
public class OrderServiceImpl implements OrderService{
    // 상수 선언할때 사용하는 키워드 final
    // final -> 생성자에서만 값을 세팅할 수 있음(상수의 의미 파악하기)
    //       -> 선언과 초기화를 동시에 작성해야 하므로 선언만 되어있는 경우 컴파일 오류를 발생하기 때문에
    //          누락을 막을 수 있음.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // 생성자가 하나이기 때문에 @Autowired 생략 가능함
    // 생성자에 롬복 라이브 적용하기 -> 기본 코드를 최적화하기 위하여
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }


    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
