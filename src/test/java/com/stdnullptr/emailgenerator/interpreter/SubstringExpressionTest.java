package com.stdnullptr.emailgenerator.interpreter;

import com.stdnullptr.emailgenerator.exception.InterpreterException;
import com.stdnullptr.emailgenerator.util.Util;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubstringExpressionTest {

    @Test
    void interpret_ShouldReturnSubstringWhenInputIsValid() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "hello world");
        final var context = Util.createContext(inputs);
        final var expression = new SubstringExpression("testKey", 0, 5);

        final var result = expression.interpret(context);

        assertEquals("hello", result);
    }

    @Test
    void interpret_ShouldReturnSubstringWhenEndIndexExceedsInputLength() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "hello");
        final var context = Util.createContext(inputs);
        final var expression = new SubstringExpression("testKey", 1, 10);

        final var result = expression.interpret(context);

        assertEquals("ello", result);
    }

    @Test
    void interpret_ShouldReturnEmptyStringWhenStartIndexEqualsEndIndex() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "hello world");
        final var context = Util.createContext(inputs);
        final var expression = new SubstringExpression("testKey", 5, 5);

        final var result = expression.interpret(context);

        assertEquals("", result);
    }

    @Test
    void interpret_ShouldThrowExceptionWhenInputIsNull() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", null);
        final var context = Util.createContext(inputs);
        final var expression = new SubstringExpression("testKey", 0, 5);

        final var thrown = assertThrows(InterpreterException.class, () -> expression.interpret(context));

        assertEquals("Input value is null for input key: testKey", thrown.getMessage());
    }

    @Test
    void interpret_ShouldThrowExceptionWhenStartIndexIsOutOfBounds() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "hello");
        final var context = Util.createContext(inputs);
        final var expression = new SubstringExpression("testKey", 10, 15);

        final var thrown = assertThrows(InterpreterException.class, () -> expression.interpret(context));

        assertEquals("Start index out of bounds for substring expression, index: 10", thrown.getMessage());
    }

    @Test
    void interpret_ShouldThrowExceptionWhenEndIndexIsLessThanStartIndex() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "hello");
        final var context = Util.createContext(inputs);
        final var expression = new SubstringExpression("testKey", 5, 3);

        final var thrown = assertThrows(InterpreterException.class, () -> expression.interpret(context));

        assertEquals("End index is less than start index: 3", thrown.getMessage());
    }
}
