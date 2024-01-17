package com.rlaclgh.onetomany;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rlaclgh.onetomany.config.RestDocsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfig.class)
class IndexControllerTest {

  private MockMvc mockMvc;

  @Autowired
  protected RestDocumentationResultHandler restDocs;


  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(MockMvcResultHandlers.print())
        .alwaysDo(restDocs)
        .build();
  }

  @Test
  public void test() throws Exception {
    this.mockMvc.perform(get("/?page=2&per_page=100").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            restDocs.document(
                queryParameters(
                    parameterWithName("page").description("The page to retrieve"),
                    parameterWithName("per_page").description("Entries per page")
                )
            )
        );
  }

  @Test
  public void test2() throws Exception {
    this.mockMvc.perform(get("/?page=2&per_page=100").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("users2", queryParameters(
            parameterWithName("page").description("The page to retrieve"),
            parameterWithName("per_page").description("Entries per page")
        )));
  }

}