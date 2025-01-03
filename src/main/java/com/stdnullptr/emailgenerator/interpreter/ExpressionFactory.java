package com.stdnullptr.emailgenerator.interpreter;

import com.stdnullptr.emailgenerator.exception.InterpreterException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
class ExpressionFactory {

    static Expression createExpression(final String expression) {
        log.debug("Creating expression from expression string: {}", expression);

        final var firstParen = expression.indexOf('(');
        if (firstParen == -1 || !expression.endsWith(")")) {
            log.error("Invalid expression format: {}", expression);
            throw new InterpreterException("Expression must start with an operation name and follow with parameters in parentheses.");
        }

        final var operationName = expression.substring(0, firstParen).trim();
        if (!Operations.isOperation(operationName)) {
            log.error("Unknown operation: {}", operationName);
            throw new InterpreterException("Unknown operation: " + operationName);
        }

        final var parameterString = expression.substring(firstParen + 1, expression.length() - 1).trim();
        final var parameters = parseParameters(parameterString);
        if (parameters.isEmpty()) {
            throw new InterpreterException("Expression has no parameters");
        }

        final var operation = Operations.valueOf(operationName.toUpperCase());

        try {
            return switch (operation) {
                case FIRST -> new FirstCharsExpression(parameters.get(0), Integer.parseInt(parameters.get(1)));
                case LAST -> new LastCharsExpression(parameters.get(0), Integer.parseInt(parameters.get(1)));
                case LIT -> new LiteralExpression(parameters.get(0));
                case RAW -> new RawExpression(parameters.get(0));
                case SUBSTR ->
                        new SubstringExpression(parameters.get(0), Integer.parseInt(parameters.get(1)), Integer.parseInt(parameters.get(2)));
                case EQ -> new EqualityExpression(
                        createExpression(parameters.get(0)),
                        createExpression(parameters.get(1)),
                        createExpression(parameters.get(2)));
                case LONGER -> new LengthComparisonExpression(
                        createExpression(parameters.get(0)),
                        createExpression(parameters.get(1)),
                        createExpression(parameters.get(2))
                );
            };
        } catch (final NumberFormatException e) {
            log.error("Invalid number parameter: {}", parameters);
            throw new InterpreterException("Invalid number parameter: " + e.getMessage());
        } catch (final InterpreterException e) {
            log.error("Interpreter failed to evaluate parameters: {}", parameters);
            throw new InterpreterException(e.getMessage());
        }
    }

    private static List<String> parseParameters(final String params) {
        log.debug("Parsing parameters from parameter string: {}", params);

        final List<String> resultParameters = new ArrayList<>();
        var balance = 0;
        final var currentParam = new StringBuilder();

        for (final var c : params.toCharArray()) {
            switch (c) {
                case '(':
                    balance++;
                    currentParam.append(c);
                    break;
                case ')':
                    balance--;
                    currentParam.append(c);
                    break;
                case ',':
                    if (balance == 0) {
                        resultParameters.add(currentParam.toString().trim());
                        currentParam.setLength(0);
                    } else {
                        currentParam.append(c);
                    }
                    break;
                default:
                    currentParam.append(c);
            }
        }

        if (!currentParam.isEmpty()) {
            resultParameters.add(currentParam.toString().trim());
        }

        return resultParameters;
    }
}
