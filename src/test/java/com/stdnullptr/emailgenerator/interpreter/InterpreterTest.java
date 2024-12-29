package com.stdnullptr.emailgenerator.interpreter;

import com.stdnullptr.emailgenerator.exception.InterpreterException;
import com.stdnullptr.emailgenerator.util.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InterpreterTest {

    @ParameterizedTest
    @MethodSource("com.stdnullptr.emailgenerator.util.TestData#validInputInterpreterProvider")
    void evaluate_WithValidExpression_ShouldReturnExpectedResult(final Map<String, Map<String, String>> testParams) {
        final var inputs = testParams.values().stream().findFirst().orElseThrow();
        final var expression = Optional.ofNullable(inputs.remove("expression")).orElseThrow();
        final var expectedResult = testParams.keySet().stream().findFirst().orElseThrow();

        final var context = Util.createContext(inputs);

        final var result = Interpreter.evaluate(expression, context);

        assertEquals(expectedResult, result);
    }

    @Test
    void evaluate_WithInvalidExpression_ShouldThrow() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("str1", "test");
        final var context = Util.createContext(inputs);

        final var expression = "first(str1,1);invalid(str1, 2)";

        final var thrown = assertThrows(InterpreterException.class, () ->
                Interpreter.evaluate(expression, context));

        assertEquals("Unknown expression: invalid(str1, 2)", thrown.getMessage(), "Expected specific message on failure");
    }

    @Test
    void evaluate_WithInvalidInput_ShouldThrow() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("wrong1", "test");
        final var context = Util.createContext(inputs);

        final var expression = "first(str1,1)";

        final var thrown = assertThrows(InterpreterException.class, () ->
                Interpreter.evaluate(expression, context));

        assertEquals("Input value is null for input key: str1", thrown.getMessage(), "Expected specific message on failure");
    }

    @Test
    void evaluate_WithInvalidExpressionParam_ShouldThrow() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("str1", "Ivan");
        final var context = Util.createContext(inputs);

        final var expression = "first(str1,str1)";

        final var thrown = assertThrows(InterpreterException.class, () ->
                Interpreter.evaluate(expression, context));

        assertEquals("Invalid number parameter: For input string: \"str1\"", thrown.getMessage(), "Expected specific message on failure");
    }

    @Test
    void evaluate_WithMalformedExpressionParam_ShouldThrow() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("str1", "Ivan");
        final var context = Util.createContext(inputs);

        final var expression = "first(str1,1;raw(test)";

        final var thrown = assertThrows(InterpreterException.class, () ->
                Interpreter.evaluate(expression, context));

        assertEquals("Expression must start with an operation name and follow with parameters in parentheses.", thrown.getMessage(), "Expected specific message on failure");
    }

    @Test
    void evaluate_WithMissingExpressionParam_ShouldThrow() {
        final Map<String, String> inputs = new HashMap<>();
        inputs.put("str1", "Ivan");
        final var context = Util.createContext(inputs);

        final var thrownNull = assertThrows(InterpreterException.class, () ->
                Interpreter.evaluate(null, context));

        final var thrownEmpty = assertThrows(InterpreterException.class, () ->
                Interpreter.evaluate("", context));

        final var thrownBlank = assertThrows(InterpreterException.class, () ->
                Interpreter.evaluate(" ", context));

        assertEquals("Expression is empty", thrownNull.getMessage(), "Expected specific message on failure");
        assertEquals("Expression is empty", thrownEmpty.getMessage(), "Expected specific message on failure");
        assertEquals("Expression is empty", thrownBlank.getMessage(), "Expected specific message on failure");
    }

    @Test
    void evaluate_WithMissingInputParam_ShouldThrow() {
        final Map<String, String> inputs = new HashMap<>();
        final var context = Util.createContext(inputs);

        final var expression = "first(str1,1);raw(test)";

        final var thrown = assertThrows(InterpreterException.class, () ->
                Interpreter.evaluate(expression, context));

        assertEquals("Input value is null for input key: str1", thrown.getMessage(), "Expected specific message on failure");
    }


}
