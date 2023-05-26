package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class ConfigurationSingletonTest {

    // 문제점 -> 원래 스프링 컨테이너는 싱글톤 패턴을 보장하므로, 밑에 memberRepository 객체에 대한 생성이 여러번 일어나도
    // 모두 같은 인스턴스를 공유하고 있음이 보장되므로 해당 변수들(memberRepository, 1, 2)은 모두 같은 인스턴스 값을 참조 하고 있어야 함으로
    // 주소값이 서로 동일해야 하는데. 현재 서로 다른 참조값을 저장하고 있다. 이 말은 서로 다른 객체를 생성했다는 것인데
    // 둘 중의 하나다, 첫번째 코드 내에서 서로 다른 인스턴스를 생성하는 코드를 작성 했던가. 두번째 스프링 컨테이너가 제대로 적용되지 않았다는 것이다
    // 문제 해결 여부 : no
    @Test
    void configurationTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        System.out.println("memberService -> memberRepository1 = " + memberRepository1);
        System.out.println("orderService -> memberRepository2 = " + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);

        // 해당 부분에서 에러가 나는 원인은, 서로 다른 값을 참조 하고 있기 때문임
//        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
//        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }

    @Test
    void configurationDeep() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        AppConfig bean = ac.getBean(AppConfig.class);

        System.out.println("bean = " + bean.getClass());
    }
}
