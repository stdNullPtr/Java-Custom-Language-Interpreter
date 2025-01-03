package com.stdnullptr.emailgenerator.interpreter;

import com.stdnullptr.emailgenerator.exception.InterpreterException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class LastCharsExpression implements Expression {
    private final String inputKey;
    private final int numCharacters;

    @Override
    public String interpret(final Context ctx) {
        final var input = ctx.getValue(inputKey);

        if (input == null) {
            throw new InterpreterException("Input value is null for input key: " + inputKey);
        }

        if (numCharacters < 0) {
            throw new InterpreterException("Number of characters cannot be negative: " + numCharacters);
        }

        final var inputLength = input.length();
        final var start = Math.max(0, inputLength - numCharacters);

        return input.substring(start);
    }
}