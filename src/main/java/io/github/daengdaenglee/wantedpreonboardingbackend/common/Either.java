package io.github.daengdaenglee.wantedpreonboardingbackend.common;

public sealed interface Either<TLeft, TRight> permits Either.Left, Either.Right {

    boolean isLeft();

    boolean isRight();

    TLeft left();

    TRight right();

    record Left<TLeft, TRight>(TLeft left) implements Either<TLeft, TRight> {

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public TLeft left() {
            return this.left;
        }

        @Override
        public TRight right() {
            throw new RuntimeException("this is left, not right.");
        }

    }

    record Right<TLeft, TRight>(TRight right) implements Either<TLeft, TRight> {

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public TLeft left() {
            throw new RuntimeException("this is right, not left.");
        }

        @Override
        public TRight right() {
            return this.right;
        }

    }

}
