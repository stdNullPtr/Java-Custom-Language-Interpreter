package com.stdnullptr.emailgenerator.interpreter;

import com.stdnullptr.emailgenerator.exception.InterpreterException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class LiteralExpression implements Expression {
    private final String inputKey;

    @Override
    public String interpret(final Context ctx) {
        final var value = ctx.getValue(inputKey);
        if (value == null) {
            throw new InterpreterException("Input value is null for input key: " + inputKey);
        }
        return value;
    }
}
