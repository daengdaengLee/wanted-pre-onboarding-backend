package io.github.daengdaenglee.wantedpreonboardingbackend.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EitherTest {

    @Test
    @DisplayName("Left 객체의 isLeft() getter 는 true 를 반환한다.")
    void leftIsLeft1() {
        var either = new Either.Left<String, String>("left");

        assertThat(either.isLeft()).isTrue();
    }

    @Test
    @DisplayName("Left 객체의 left() getter 는 Left 생성자에 주입한 값을 그대로 반환한다.")
    void leftIsLeft2() {
        var inputLeft = "이건 left";

        var either = new Either.Left<String, String>(inputLeft);

        assertThat(either.left()).isEqualTo(inputLeft);
    }

    @Test
    @DisplayName("Left 객체의 isRight() getter 는 false 를 반환한다.")
    void leftIsNotRight1() {
        var either = new Either.Left<String, String>("left");

        assertThat(either.isRight()).isFalse();
    }

    @Test
    @DisplayName("Left 객체의 right() getter 는 에러를 던진다.")
    void leftIsNotRight2() {
        var inputLeft = "이건 left";

        var either = new Either.Left<String, String>(inputLeft);

        assertThatThrownBy(either::right).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Right 객체의 isRight() getter 는 true 를 반환한다.")
    void rightIsRight1() {
        var either = new Either.Right<String, String>("right");

        assertThat(either.isRight()).isTrue();
    }

    @Test
    @DisplayName("Right 객체의 right() getter 는 Right 생성자에 주입한 값을 그대로 반환한다.")
    void rightIsRight2() {
        var inputRight = "이건 right";

        var either = new Either.Right<String, String>(inputRight);

        assertThat(either.right()).isEqualTo(inputRight);
    }

    @Test
    @DisplayName("Right 객체의 isLeft() getter 는 false 를 반환한다.")
    void rightIsNotLeft1() {
        var either = new Either.Right<String, String>("right");

        assertThat(either.isLeft()).isFalse();
    }

    @Test
    @DisplayName("Right 객체의 left() getter 는 에러를 던진다.")
    void rightIsNotLeft2() {
        var inputRight = "이건 right";

        var either = new Either.Right<String, String>(inputRight);

        assertThatThrownBy(either::left).isInstanceOf(RuntimeException.class);
    }

}