package com.stdnullptr.emailgenerator.controller;

import com.stdnullptr.emailgenerator.exception.GlobalExceptionHandler;
import com.stdnullptr.emailgenerator.exception.InterpreterException;
import com.stdnullptr.emailgenerator.exception.InvalidArgumentException;
import com.stdnullptr.emailgenerator.service.EmailGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.nio.file.Files;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {EmailController.class, EmailGeneratorService.class, GlobalExceptionHandler.class})
@ExtendWith(SpringExtension.class)
class EmailControllerIT {

    private MockMvc controllerMockMvc;

    @Autowired
    private EmailController emailController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        controllerMockMvc = MockMvcBuilders.standaloneSetup(emailController).setControllerAdvice(globalExceptionHandler).build();
    }

    @Test
    void generateEmails_missingQueryParams_shouldReturnBadRequest() throws Exception {
        final var requestBuilder = MockMvcRequestBuilders
                .get("/app/v1/email/generate");

        final var actualPerformResult = controllerMockMvc.perform(requestBuilder);

        actualPerformResult.andExpect(status().is(BAD_REQUEST.value()))
                .andExpect(result -> assertInstanceOf(HandlerMethodValidationException.class, result.getResolvedException()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid request argument(s): {queryParams=[Query parameters cannot be empty]}"));
    }

    /**
     * The purpose of this integration test is to test every {@link InvalidArgumentException} handled in {@link GlobalExceptionHandler}
     */
    @ParameterizedTest
    @MethodSource("com.stdnullptr.emailgenerator.util.TestData#invalidArgumentQueryParamsProvider")
    void generateEmails_InvalidArgumentException_shouldReturnBadRequest(final Map<String, MultiValueMap<String, String>> testParams) throws Exception {
        final var queryParams = testParams.values().stream().findFirst().orElseThrow();
        final var expectedErrorMessage = testParams.keySet().stream().findFirst().get();

        final var requestBuilder = MockMvcRequestBuilders
                .get("/app/v1/email/generate")
                .queryParams(queryParams);

        final var actualPerformResult = controllerMockMvc.perform(requestBuilder);

        actualPerformResult.andExpect(status().is(BAD_REQUEST.value()))
                .andExpect(result -> assertInstanceOf(InvalidArgumentException.class, result.getResolvedException()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    /**
     * The purpose of this integration test is to test every {@link InterpreterException} handled in {@link GlobalExceptionHandler}
     */
    @ParameterizedTest
    @MethodSource("com.stdnullptr.emailgenerator.util.TestData#interpreterErrorQueryParamsProvider")
    void generateEmails_interpreterException_shouldReturnBadRequest(final Map<String, MultiValueMap<String, String>> testParams) throws Exception {
        final var queryParams = testParams.values().stream().findFirst().orElseThrow();
        final var expectedErrorMessage = testParams.keySet().stream().findFirst().get();

        final var requestBuilder = MockMvcRequestBuilders
                .get("/app/v1/email/generate")
                .queryParams(queryParams);

        final var actualPerformResult = controllerMockMvc.perform(requestBuilder);

        actualPerformResult.andExpect(status().is(BAD_REQUEST.value()))
                .andExpect(result -> assertInstanceOf(InterpreterException.class, result.getResolvedException()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    void generateEmails_success_shouldReturnOk() throws Exception {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("str1", "Ivan");
        queryParams.add("str1", "Nikola");
        queryParams.add("str1", "Rado");
        queryParams.add("str2", "gmail");
        queryParams.add("str2", "yahoo");
        queryParams.add("str3", "com");
        queryParams.add("str3", "bg");
        queryParams.add("expression",
                "first(str1,2) ; raw(.) ; lit(str1) ; raw(@) ; first(str2, 4) ; raw(.) ; lit(str3)");

        final var requestBuilder = MockMvcRequestBuilders
                .get("/app/v1/email/generate")
                .queryParams(queryParams);

        final var actualPerformResult = controllerMockMvc.perform(requestBuilder);

        final var expectedResponse = Files.readString(ResourceUtils.getFile("classpath:response/testGenerateEmails_success_shouldReturnOk.json").toPath());

        actualPerformResult.andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse));
    }
}
