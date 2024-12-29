package com.stdnullptr.emailgenerator.interpreter;

import com.stdnullptr.emailgenerator.util.Util;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RawExpressionTest {

    @Test
    void interpret_ShouldReturnRawValue() {
        final var context = Util.createContext(new HashMap<>());
        final var rawValue = "This is a raw value";
        final var expression = new RawExpression(rawValue);

        final var result = expression.interpret(context);

        assertEquals(rawValue, result);
    }

    @Test
    void interpret_ShouldReturnEmptyStringWhenValueIsEmpty() {
        final var context = Util.createContext(new HashMap<>());
        final var emptyValue = "";
        final var expression = new RawExpression(emptyValue);

        final var result = expression.interpret(context);

        assertEquals(emptyValue, result);
    }

    @Test
    void interpret_ShouldReturnProvidedNumericValue() {
        final var context = Util.createContext(new HashMap<>());
        final var numericValue = "12345";
        final var expression = new RawExpression(numericValue);

        final var result = expression.interpret(context);

        assertEquals(numericValue, result);
    }

    @Test
    void interpret_ShouldReturnSpecialCharacters() {
        final var context = Util.createContext(new HashMap<>());
        final var specialChars = "@#$%^&*";
        final var expression = new RawExpression(specialChars);

        final var result = expression.interpret(context);

        assertEquals(specialChars, result);
    }
}
