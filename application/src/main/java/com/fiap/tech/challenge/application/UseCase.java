package com.fiap.tech.challenge.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIN);
}