package com.stdnullptr.emailgenerator.interpreter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class RawExpression implements Expression {
    private final String value;

    @Override
    public String interpret(final Context ctx) {
        return value;
    }
}