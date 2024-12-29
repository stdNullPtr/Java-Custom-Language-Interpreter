package com.stdnullptr.emailgenerator.interpreter;

import com.stdnullptr.emailgenerator.util.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class EqualityExpressionTest {

    private AutoCloseable mocksCloseable;

    @Mock
    private Expression left;

    @Mock
    private Expression right;

    @Mock
    private Expression action;

    @BeforeEach
    void setUp() {
        mocksCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocksCloseable.close();
    }

    @Test
    void interpret_ShouldReturnActionWhenValuesAreEqual() {
        final var context = Util.createContext(new HashMap<>());
        when(left.interpret(context)).thenReturn("equalValue");
        when(right.interpret(context)).thenReturn("equalValue");
        when(action.interpret(context)).thenReturn("action result");

        final var equalityExpression = new EqualityExpression(left, right, action);

        final var result = equalityExpression.interpret(context);

        assertEquals("action result", result);
    }

    @Test
    void interpret_ShouldReturnEmptyWhenValuesAreNotEqual() {
        final var context = Util.createContext(new HashMap<>());
        when(left.interpret(context)).thenReturn("leftValue");
        when(right.interpret(context)).thenReturn("rightValue");
        when(action.interpret(context)).thenReturn("action result");

        final var equalityExpression = new EqualityExpression(left, right, action);

        final var result = equalityExpression.interpret(context);

        assertEquals("", result);
    }

    @Test
    void interpret_ShouldReturnEmptyWhenLeftExpressionReturnsEmpty() {
        final var context = Util.createContext(new HashMap<>());
        when(left.interpret(context)).thenReturn("");
        when(right.interpret(context)).thenReturn("rightValue");
        when(action.interpret(context)).thenReturn("action result");

        final var equalityExpression = new EqualityExpression(left, right, action);

        final var result = equalityExpression.interpret(context);

        assertEquals("", result);
    }

    @Test
    void interpret_ShouldReturnEmptyWhenRightExpressionReturnsEmpty() {
        final var context = Util.createContext(new HashMap<>());
        when(left.interpret(context)).thenReturn("leftValue");
        when(right.interpret(context)).thenReturn("");
        when(action.interpret(context)).thenReturn("action result");

        final var equalityExpression = new EqualityExpression(left, right, action);

        final var result = equalityExpression.interpret(context);

        assertEquals("", result);
    }

    @Test
    void interpret_ShouldReturnActionWhenActionReturnsEmptyButValuesAreEqual() {
        final var context = Util.createContext(new HashMap<>());
        when(left.interpret(context)).thenReturn("equalValue");
        when(right.interpret(context)).thenReturn("equalValue");
        when(action.interpret(context)).thenReturn("");

        final var equalityExpression = new EqualityExpression(left, right, action);

        final var result = equalityExpression.interpret(context);

        assertEquals("", result);
    }
}
