package com.stdnullptr.emailgenerator.interpreter;

import com.stdnullptr.emailgenerator.exception.InterpreterException;
import com.stdnullptr.emailgenerator.util.Util;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LiteralExpressionTest {

    @Test
    void interpret_ShouldReturnInputValueWhenInputIsValid() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "expected value");
        final var context = Util.createContext(inputs);
        final var expression = new LiteralExpression("testKey");

        final var result = expression.interpret(context);

        assertEquals("expected value", result);
    }

    @Test
    void interpret_ShouldThrowExceptionWhenInputIsNull() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", null);
        final var context = Util.createContext(inputs);
        final var expression = new LiteralExpression("testKey");

        final var thrown = assertThrows(InterpreterException.class, () -> expression.interpret(context));

        assertEquals("Input value is null for input key: testKey", thrown.getMessage());
    }

    @Test
    void interpret_ShouldThrowExceptionWhenInputKeyIsMissing() {
        final Map<String, String> inputs = new HashMap<>();
        final var context = Util.createContext(inputs);
        final var expression = new LiteralExpression("missingKey");

        final var thrown = assertThrows(InterpreterException.class, () -> expression.interpret(context));

        assertEquals("Input value is null for input key: missingKey", thrown.getMessage());
    }
}
