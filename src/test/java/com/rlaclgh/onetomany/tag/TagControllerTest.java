package com.rlaclgh.onetomany.tag;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rlaclgh.onetomany.auth.AuthService;
import com.rlaclgh.onetomany.config.CustomUserDetailsService;
import com.rlaclgh.onetomany.config.RestDocsConfig;
import com.rlaclgh.onetomany.dto.CreateTagDto;
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
class TagControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  protected RestDocumentationResultHandler restDocs;


  @Autowired
  private CustomUserDetailsService customUserDetailsService;


  @Autowired
  private AuthService authService;


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
  @DisplayName("tag를 생성할 수 있다.")
  @Transactional
  public void createTagTest() throws Exception {

    // Given
    String email = "rlaclgh11@gmail.com";
    String nickname = "rlaclgh";
    String password = "password11";
    String rePassword = "password11";

    authService.signUp(new SignUpDto(email, nickname, password, rePassword));
    String token = authService.signIn(new SignInDto(email, password));

    String name = "tag1";

    CreateTagDto createTagDto = new CreateTagDto(name);

    mockMvc.perform(post("/tag")
            .content(objectMapper.writeValueAsString(createTagDto))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
        )
        .andExpect(jsonPath("$.name").value(name))
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("id").description("태그 ID"),
                fieldWithPath("name").description("태그 명")
            ), requestHeaders(
                headerWithName("Authorization").description("The authorization header")
                    .attributes(
                        key("required").value("true")
                    )
            )
        ));


  }


}