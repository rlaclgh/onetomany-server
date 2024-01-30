package com.rlaclgh.onetomany.auth;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rlaclgh.onetomany.config.RestDocsConfig;
import com.rlaclgh.onetomany.dto.SignInDto;
import com.rlaclgh.onetomany.dto.SignUpDto;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.config.BeanIds;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ExtendWith({RestDocumentationExtension.class})
@Import(RestDocsConfig.class)
@ActiveProfiles("test")
class AuthControllerTest {

  @Autowired
  private AuthService authService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  protected RestDocumentationResultHandler restDocs;


  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) throws ServletException {

    DelegatingFilterProxy delegateProxyFilter = new DelegatingFilterProxy();
    delegateProxyFilter.init(
        new MockFilterConfig(webApplicationContext.getServletContext(),
            BeanIds.SPRING_SECURITY_FILTER_CHAIN));

    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(MockMvcResultHandlers.print())
        .alwaysDo(restDocs)
        .addFilters(
            new CharacterEncodingFilter("UTF-8", true),
            delegateProxyFilter
        )
        .build();
  }


  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("로그인 할 수 있다.")
  @Transactional
  public void signInTest() throws Exception {

    String email = "rlaclgh12@gmail.com";
    String password = "password11";
    String rePassword = "password11";
    String nickname = "rlaclgh";

    authService.signUp(new SignUpDto(email, nickname, password, rePassword));

    SignInDto signInDto = new SignInDto(
        email,
        password
    );

    mockMvc.perform(post("/auth/sign-in")
            .content(objectMapper.writeValueAsString(signInDto))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isString())
        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("token").description("Token")
            )
        ));
  }


  @Test
  @DisplayName("회원가입 할 수 있다.")
  @Transactional
  public void signUpTest() throws Exception {
    String email = "rlaclgh000011123@gmail.com";
    String password = "password11";
    String rePassword = "password11";
    String nickname = "rlaclgh";
    SignUpDto signUpDto = new SignUpDto(email, nickname, password, rePassword);

    mockMvc.perform(
            post("/auth/sign-up")
                .content(objectMapper.writeValueAsString(signUpDto))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(restDocs.document());
  }
}