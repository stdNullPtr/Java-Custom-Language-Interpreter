package com.stdnullptr.emailgenerator.interpreter;

import com.stdnullptr.emailgenerator.exception.InterpreterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionFactoryTest {

    @Test
    void createExpression_ShouldCreateFirstCharsExpression() {
        final var expressionStr = "first(testKey, 5)";
        final var expression = ExpressionFactory.createExpression(expressionStr);

        assertInstanceOf(FirstCharsExpression.class, expression);
    }

    @Test
    void createExpression_ShouldCreateLastCharsExpression() {
        final var expressionStr = "last(testKey, 3)";
        final var expression = ExpressionFactory.createExpression(expressionStr);

        assertInstanceOf(LastCharsExpression.class, expression);
    }

    @Test
    void createExpression_ShouldCreateLiteralExpression() {
        final var expressionStr = "lit(testKey)";
        final var expression = ExpressionFactory.createExpression(expressionStr);

        assertInstanceOf(LiteralExpression.class, expression);
    }

    @Test
    void createExpression_ShouldCreateRawExpression() {
        final var expressionStr = "raw(constant value)";
        final var expression = ExpressionFactory.createExpression(expressionStr);

        assertInstanceOf(RawExpression.class, expression);
    }

    @Test
    void createExpression_ShouldCreateSubstringExpression() {
        final var expressionStr = "substr(testKey, 2, 5)";
        final var expression = ExpressionFactory.createExpression(expressionStr);

        assertInstanceOf(SubstringExpression.class, expression);
    }

    @Test
    void createExpression_ShouldCreateEqualityExpression() {
        final var expressionStr = "eq(lit(val1), lit(val2), raw(result))";
        final var expression = ExpressionFactory.createExpression(expressionStr);

        assertInstanceOf(EqualityExpression.class, expression);
    }

    @Test
    void createExpression_ShouldCreateLengthComparisonExpression() {
        final var expressionStr = "longer(lit(long), lit(short), raw(result))";
        final var expression = ExpressionFactory.createExpression(expressionStr);

        assertInstanceOf(LengthComparisonExpression.class, expression);
    }

    @Test
    void createExpression_ShouldThrowExceptionForInvalidFormat() {
        final var expressionStr = "invalidExpression";

        final var exception = assertThrows(InterpreterException.class, () -> ExpressionFactory.createExpression(expressionStr));

        assertEquals("Expression must start with an operation name and follow with parameters in parentheses.", exception.getMessage());
    }

    @Test
    void createExpression_ShouldThrowExceptionForUnknownOperation() {
        final var expressionStr = "unknownOp(testKey, 5)";

        final var exception = assertThrows(InterpreterException.class, () -> ExpressionFactory.createExpression(expressionStr));

        assertEquals("Unknown operation: unknownOp", exception.getMessage());
    }

    @Test
    void createExpression_ShouldThrowExceptionForInvalidNumberParameter() {
        final var expressionStr = "first(testKey, invalidNumber)";

        final var exception = assertThrows(InterpreterException.class, () -> ExpressionFactory.createExpression(expressionStr));

        assertTrue(exception.getMessage().contains("Invalid number parameter"));
    }

    @Test
    void createExpression_ShouldThrowExceptionForEmptyParameters() {
        final var expressionStr = "first()";

        final var exception = assertThrows(InterpreterException.class, () -> ExpressionFactory.createExpression(expressionStr));

        assertEquals("Expression has no parameters", exception.getMessage());
    }
}
