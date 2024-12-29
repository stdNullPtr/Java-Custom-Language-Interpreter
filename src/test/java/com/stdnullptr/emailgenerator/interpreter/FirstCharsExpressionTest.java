package com.stdnullptr.emailgenerator.interpreter;

import com.stdnullptr.emailgenerator.exception.InterpreterException;
import com.stdnullptr.emailgenerator.util.Util;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FirstCharsExpressionTest {

    @Test
    void interpret_ShouldReturnSubstringWhenInputIsValid() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "hello world");
        final var context = Util.createContext(inputs);
        final var expression = new FirstCharsExpression("testKey", 5);

        final var result = expression.interpret(context);

        assertEquals("hello", result);
    }

    @Test
    void interpret_ShouldReturnFullStringWhenNumCharsEqualsStringLength() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "hello");
        final var context = Util.createContext(inputs);
        final var expression = new FirstCharsExpression("testKey", 5);

        final var result = expression.interpret(context);

        assertEquals("hello", result);
    }

    @Test
    void interpret_ShouldReturnEmptyWhenInputIsEmpty() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "");
        final var context = Util.createContext(inputs);
        final var expression = new FirstCharsExpression("testKey", 5);

        final var result = expression.interpret(context);

        assertEquals("", result);
    }

    @Test
    void interpret_ShouldReturnStringWhenNumCharsIsGreaterThanStringLength() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "hello");
        final var context = Util.createContext(inputs);
        final var expression = new FirstCharsExpression("testKey", 10);

        final var result = expression.interpret(context);

        assertEquals("hello", result);
    }

    @Test
    void interpret_ShouldThrowExceptionWhenInputIsNull() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", null);
        final var context = Util.createContext(inputs);
        final var expression = new FirstCharsExpression("testKey", 5);

        final var thrown = assertThrows(InterpreterException.class, () -> expression.interpret(context));

        assertEquals("Input value is null for input key: testKey", thrown.getMessage());
    }

    @Test
    void interpret_ShouldThrowExceptionWhenNumCharactersIsNegative() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "hello world");
        final var context = Util.createContext(inputs);
        final var expression = new FirstCharsExpression("testKey", -1);

        final var thrown = assertThrows(InterpreterException.class, () -> expression.interpret(context));

        assertEquals("Number of characters cannot be negative: -1", thrown.getMessage());
    }

    @Test
    void interpret_ShouldIgnoreIrrelevantContextEntries() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("testKey", "hello world");
        inputs.put("irrelevantKey", "irrelevantValue");
        final var context = Util.createContext(inputs);
        final var expression = new FirstCharsExpression("testKey", 5);

        final var result = expression.interpret(context);

        assertEquals("hello", result);
    }
}
