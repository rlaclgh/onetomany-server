package com.rlaclgh.onetomany.chat_room;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rlaclgh.onetomany.auth.AuthService;
import com.rlaclgh.onetomany.config.CustomUserDetails;
import com.rlaclgh.onetomany.config.CustomUserDetailsService;
import com.rlaclgh.onetomany.config.RestDocsConfig;
import com.rlaclgh.onetomany.dto.ChatRoomDto;
import com.rlaclgh.onetomany.dto.CreateChatRoomDto;
import com.rlaclgh.onetomany.dto.SignInDto;
import com.rlaclgh.onetomany.dto.SignUpDto;
import com.rlaclgh.onetomany.dto.UpdateChatRoomDto;
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
class ChatRoomControllerTest {


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  protected RestDocumentationResultHandler restDocs;


  @Autowired
  private CustomUserDetailsService customUserDetailsService;


  @Autowired
  private ChatRoomService chatRoomService;


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
  @DisplayName("chat room을 생성할 수 있다.")
  @Transactional
  public void createChatRoomTest() throws Exception {

    // Given
    String email = "rlaclgh11@gmail.com";
    String nickname = "rlaclgh";
    String password = "password11";
    String rePassword = "password11";

    authService.signUp(new SignUpDto(email, nickname, password, rePassword));
    String token = authService.signIn(new SignInDto(email, password));

    // When
    String name = "chatroom1";
    String imageUrl = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String description = "chatroom1 description";

    CreateChatRoomDto createChatRoomDto = new CreateChatRoomDto(
        name, imageUrl, description
    );

    mockMvc.perform(post("/chat_room")
            .content(objectMapper.writeValueAsString(createChatRoomDto))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
        )
        .andExpect(jsonPath("$.name").value(name))
        .andExpect(jsonPath("$.imageUrl").value(imageUrl))
        .andExpect(jsonPath("$.description").value(description))
        .andExpect(status().isCreated())

        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("id").description("채팅방 ID"),
                fieldWithPath("name").description("채팅방 이름"),
                fieldWithPath("imageUrl").description("채팅방 이미지"),
                fieldWithPath("description").description("채팅방 설명")
            ), requestHeaders(
                headerWithName("Authorization").description("The authorization header")
                    .attributes(
                        key("required").value("true")
                    )
            )
        ));

  }


  @Test
  @DisplayName("생성된 채팅방을 구독할 수 있다.")
  @Transactional
  public void subscribeChatRoomTest() throws Exception {

    // Given
    String email1 = "rlaclgh11@gmail.com";
    String nickname1 = "rlaclgh";
    String password1 = "password11";
    String rePassword1 = "password11";
    authService.signUp(new SignUpDto(email1, nickname1, password1, rePassword1));

    String email2 = "rlaclgh22@gmail.com";
    String nickname2 = "rlaclgh";
    String password2 = "password11";
    String rePassword2 = "password11";
    authService.signUp(new SignUpDto(email2, nickname2, password2, rePassword2));

    //// user1 이 채팅방 생성
    String name = "chatroom1";
    String imageUrl = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String description = "chatroom1 description";

    CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email1);

    ChatRoomDto chatRoomDto = chatRoomService.createChatRoom(userDetails.getMember(),
        new CreateChatRoomDto(
            name, imageUrl, description
        ));

    //// user2 로그인
    String token = authService.signIn(new SignInDto(email2, password2));

    mockMvc.perform(post("/chat_room/{chatRoomId}/subscribe",
            chatRoomDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
        )
        .andExpect(jsonPath("$.isHost").value(false))
        .andExpect(jsonPath("$.chatRoom.name").value(chatRoomDto.getName()))
        .andExpect(jsonPath("$.chatRoom.imageUrl").value(chatRoomDto.getImageUrl()))
        .andExpect(
            jsonPath("$.chatRoom.description").value(chatRoomDto.getDescription()))
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("chatRoomId").description("채팅방 ID")
            ),
            responseFields(
                fieldWithPath("id").description("채팅방 ID"),
                fieldWithPath("isHost").description("채팅방 host 여부"),
                fieldWithPath("unReadCount").description("안읽은 메시지 개수"),
                fieldWithPath("lastChat").description("최근 채팅"),
                fieldWithPath("chatRoom.id").description("채팅방 ID"),
                fieldWithPath("chatRoom.name").description("채팅방 이름"),
                fieldWithPath("chatRoom.imageUrl").description("채팅방 이미지"),
                fieldWithPath("chatRoom.description").description("채팅방 설명")
            ), requestHeaders(
                headerWithName("Authorization").description("The authorization header")
                    .attributes(
                        key("required").value("true")
                    )
            )
        ));
  }


  @Test
  @DisplayName("본인의 chat_room을 수정할 수 있다.")
  @Transactional
  public void canUpdateTest() throws Exception {
    // Given
    String email = "rlaclgh000011123@gmail.com";
    String password = "password11";
    String rePassword = "password11";
    String nickname = "rlaclgh";

    authService.signUp(new SignUpDto(email, nickname, password, rePassword));
    String token = authService.signIn(new SignInDto(email, password));

    String name = "chatroom1";
    String imageUrl = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String description = "chatroom1 description";

    CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

    ChatRoomDto chatRoomDto = chatRoomService.createChatRoom(userDetails.getMember(),
        new CreateChatRoomDto(
            name, imageUrl, description
        ));

    // When

    String nameUpdated = "chatroom_updated";
    String imageUrlUpdated = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String descriptionUpdated = "chatroom1 description_updated";

    UpdateChatRoomDto updateChatRoomDto = new UpdateChatRoomDto(
        nameUpdated, imageUrlUpdated, descriptionUpdated
    );

    mockMvc.perform(patch("/chat_room/{chatRoomId}", chatRoomDto.getId())
            .content(objectMapper.writeValueAsString(updateChatRoomDto))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(nameUpdated))
        .andExpect(jsonPath("$.imageUrl").value(imageUrlUpdated))
        .andExpect(jsonPath("$.description").value(descriptionUpdated))
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("chatRoomId").description("채팅방 ID")
            ),
            responseFields(
                fieldWithPath("id").description("채팅방 ID"),
                fieldWithPath("name").description("채팅방 이름"),
                fieldWithPath("imageUrl").description("채팅방 이미지"),
                fieldWithPath("description").description("채팅방 설명")
            ), requestHeaders(
                headerWithName("Authorization").description("The authorization header")
                    .attributes(
                        key("required").value("true")
                    )
            )
        ));
  }

  @Test
  @DisplayName("채팅방 리스트를 불러올 수 있다.")
  @Transactional
  public void getChatRoomsTest() throws Exception {

    // Given

    String email = "rlaclgh000011123@gmail.com";
    String password = "password11";
    String rePassword = "password11";
    String nickname = "rlaclgh";

    authService.signUp(new SignUpDto(email, nickname, password, rePassword));
    CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

    String name1 = "chatroom1";
    String imageUrl1 = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String description1 = "chatroom1 description";

    String name2 = "chatroom2";
    String imageUrl2 = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String description2 = "chatroom2 description";

    chatRoomService.createChatRoom(userDetails.getMember(),
        new CreateChatRoomDto(name1, imageUrl1, description1));

    chatRoomService.createChatRoom(userDetails.getMember(),
        new CreateChatRoomDto(name2, imageUrl2, description2));

    mockMvc.perform(get("/chat_room"))
        .andExpectAll(status().isOk())
        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("[].id").description("채널 ID"),
                fieldWithPath("[].isHost").description("채팅방 호스트여부"),
                fieldWithPath("[].unReadCount").description("안읽은 메시지 개수"),
                fieldWithPath("[].lastChat").description("최근 채팅"),
                fieldWithPath("[].chatRoom.id").description("채팅방 ID"),
                fieldWithPath("[].chatRoom.name").description("채팅방 이름"),
                fieldWithPath("[].chatRoom.imageUrl").description("채팅방 이미지"),
                fieldWithPath("[].chatRoom.description").description("채팅방 설명")
            )
        ));


  }


}