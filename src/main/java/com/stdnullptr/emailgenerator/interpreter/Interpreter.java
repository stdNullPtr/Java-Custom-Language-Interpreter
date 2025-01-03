package com.stdnullptr.emailgenerator.interpreter;

import com.stdnullptr.emailgenerator.exception.InterpreterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.stream.Stream;

@Slf4j
public final class Interpreter {

    private Interpreter() {
        // no sneaky reflections
        throw new IllegalStateException("What are you doing?");
    }

    public static String evaluate(final String fullExpressionString, final Context context) {
        if (!StringUtils.hasText(fullExpressionString)) {
            log.error("Expression is empty: {}", fullExpressionString);
            throw new InterpreterException("Expression is empty");
        }

        if (context == null) {
            throw new InterpreterException("Context is null");
        }

        log.debug("Evaluating expression: {} for context: {}", fullExpressionString, context);

        final var result = new StringBuilder();
        final var expressions = Stream.of(fullExpressionString.split(";")).map(String::trim).toArray(String[]::new);

        for (final var expression : expressions) {
            if (Operations.isOperation(expression)) {
                final var exp = ExpressionFactory.createExpression(expression);
                result.append(exp.interpret(context));
            } else {
                log.error("Unknown expression: {}, full expression string: {}", expression, fullExpressionString);
                throw new InterpreterException("Unknown expression: " + expression);
            }
        }

        return result.toString();
    }
}