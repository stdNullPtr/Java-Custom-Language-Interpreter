package com.stdnullptr.emailgenerator.interpreter;

import java.util.Map;

public class Context {
    private final Map<String, String> inputs;

    public Context(final Map<String, String> inputs) {
        this.inputs = inputs;
    }

    public String getValue(final String key) {
        return inputs.get(key);
    }
}