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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rlaclgh.onetomany.auth.AuthService;
import com.rlaclgh.onetomany.config.CustomUserDetails;
import com.rlaclgh.onetomany.config.RestDocsConfig;
import com.rlaclgh.onetomany.constant.Seed;
import com.rlaclgh.onetomany.dto.ChannelDto;
import com.rlaclgh.onetomany.dto.CreateChatRoomDto;
import com.rlaclgh.onetomany.dto.SignInDto;
import com.rlaclgh.onetomany.dto.UpdateChatRoomDto;
import com.rlaclgh.onetomany.entity.Member;
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
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.security.config.BeanIds;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class})
@Import(RestDocsConfig.class)
class ChatRoomControllerTest {


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  protected RestDocumentationResultHandler restDocs;


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


  public static RequestPostProcessor testUser() {
    CustomUserDetails customUserDetails = new CustomUserDetails(
        new Member(1L, "rlaclgh11@gmail.com", "password11")
    );
    return user(customUserDetails);
  }

  public static RequestPostProcessor testUser2() {
    CustomUserDetails customUserDetails = new CustomUserDetails(
        new Member(2L, "rlaclgh22@gmail.com", "password11")
    );
    return user(customUserDetails);
  }


  public ChannelDto createChatRoom() {

    String name = "chatroom1";
    String imageUrl = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String description = "chatroom1 description";

    ChannelDto channelDto = chatRoomService.createChatRoom(Seed.user1,
        new CreateChatRoomDto(name, imageUrl, description));

    return channelDto;
  }


  private ResponseFieldsSnippet channelDto() {
    return responseFields(
        fieldWithPath("id").description("채널 ID"),
        fieldWithPath("isHost").description("채팅방 호스트여부"),
        fieldWithPath("chatRoom.id").description("채팅방 ID"),
        fieldWithPath("chatRoom.name").description("채팅방 이름"),
        fieldWithPath("chatRoom.imageUrl").description("채팅방 이미지"),
        fieldWithPath("chatRoom.description").description("채팅방 설명")
    );
  }


  public String signIn(Member member) {

    return authService.signIn(new SignInDto(member.getEmail(), member.getPassword()));
  }

  @Test
  @DisplayName("chat room을 생성할 수 있다.")
  public void createChatRoomTest() throws Exception {

    String token = signIn(Seed.user1);

    String name = "chatroom1";
    String imageUrl = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String description = "chatroom1 description";

    CreateChatRoomDto createChatRoomDto = new CreateChatRoomDto(
        name, imageUrl, description
    );

    mockMvc.perform(post("/chat_room")
//            .with(testUser())
                .content(objectMapper.writeValueAsString(createChatRoomDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        )
//        .andExpect(jsonPath("$.channel.isHost").value(true))
        .andExpect(jsonPath("$.chatRoom.name").value(name))
        .andExpect(jsonPath("$.chatRoom.imageUrl").value(imageUrl))
        .andExpect(jsonPath("$.chatRoom.description").value(description))
        .andExpect(status().isCreated())

        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("id").description("채널 ID"),
                fieldWithPath("isHost").description("채팅방 호스트여부"),
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
//        .andDo(restDocs.document(channelDto()));

  }


  @Test
  @DisplayName("생성된 채팅방을 구독할 수 있다.")
  public void subscribeChatRoomTest() throws Exception {

    //    user1 created ChatRoom
    String name = "chatroom1";
    String imageUrl = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String description = "chatroom1 description";

    ChannelDto channelDto = chatRoomService.createChatRoom(Seed.user1,
        new CreateChatRoomDto(name, imageUrl, description));

    String token = signIn(Seed.user2);

    mockMvc.perform(post("/chat_room/{chatRoomId}/subscribe",
                channelDto.getChatRoom().getId())
//            .with(testUser2())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        )
        .andExpect(jsonPath("$.chatRoom.name").value(channelDto.getChatRoom().getName()))
        .andExpect(jsonPath("$.chatRoom.imageUrl").value(channelDto.getChatRoom().getImageUrl()))
        .andExpect(
            jsonPath("$.chatRoom.description").value(channelDto.getChatRoom().getDescription()))
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("chatRoomId").description("채팅방 ID")
            ),
            responseFields(
                fieldWithPath("id").description("채널 ID"),
                fieldWithPath("isHost").description("채팅방 호스트여부"),
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


  //  @Test
  @DisplayName("없는 채팅방은 구독할 수 없다.")
  public void nonExistChatRoomTest() throws Exception {
    mockMvc.perform(post("/chat_room/{chatRoomId}/subscribe", 999999L)
            .with(testUser())
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
  }

  //  @Test
  @DisplayName("본인이 만든 chatroom은 구독할 수 없다.")
  public void subscribeMineTest() throws Exception {
    ChannelDto channelDto = createChatRoom();

    mockMvc.perform(post("/chat_room/{chatRoomId}/subscribe", channelDto.getChatRoom().getId())
            .with(testUser())
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest());
  }

  //  @Test
  @DisplayName("이미 구독한 chatroom은 다시 구독할 수 없다.")
  public void alreadySubscribedTest() throws Exception {
    ChannelDto channelDto = createChatRoom();

    mockMvc.perform(post("/chat_room/{chatRoomId}/subscribe", channelDto.getChatRoom().getId())
            .with(testUser2())
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());

    mockMvc.perform(post("/chat_room/{chatRoomId}/subscribe", channelDto.getChatRoom().getId())
            .with(testUser2())
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest());
  }

  //  @Test
  @DisplayName("본인의 chat_room만을 수정할 수 있다.")
  public void canUpdateMineTest() throws Exception {
    ChannelDto channelDto = createChatRoom();

    String name = "chatroom_updated";
    String imageUrl = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String description = "chatroom1 description_updated";

    UpdateChatRoomDto updateChatRoomDto = new UpdateChatRoomDto(
        name, imageUrl, description
    );

    mockMvc.perform(patch("/chat_room/{chatRoomId}", channelDto.getChatRoom().getId())
            .with(testUser2())
            .content(objectMapper.writeValueAsString(updateChatRoomDto))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("본인의 chat_room을 수정할 수 있다.")
  public void canUpdateTest() throws Exception {

    //    user1 created ChatRoom
    String name = "chatroom1";
    String imageUrl = "https://mymu.s3.ap-northeast-2.amazonaws.com/default-profile.png";
    String description = "chatroom1 description";

    ChannelDto channelDto = chatRoomService.createChatRoom(Seed.user1,
        new CreateChatRoomDto(name, imageUrl, description));

    String token = signIn(Seed.user1);

    String nameUpdated = "chatroom_updated";
    String imageUrlUpdated = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String descriptionUpdated = "chatroom1 description_updated";

    UpdateChatRoomDto updateChatRoomDto = new UpdateChatRoomDto(
        nameUpdated, imageUrlUpdated, descriptionUpdated
    );

    mockMvc.perform(patch("/chat_room/{chatRoomId}", channelDto.getChatRoom().getId())
            .content(objectMapper.writeValueAsString(updateChatRoomDto))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.chatRoom.name").value(nameUpdated))
        .andExpect(jsonPath("$.chatRoom.imageUrl").value(imageUrlUpdated))
        .andExpect(jsonPath("$.chatRoom.description").value(descriptionUpdated))
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("chatRoomId").description("채팅방 ID")
            ),
            responseFields(
                fieldWithPath("id").description("채널 ID"),
                fieldWithPath("isHost").description("채팅방 호스트여부"),
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
  @DisplayName("채팅방 리스트를 불러올 수 있다.")
  public void getChatRoomsTest() throws Exception {

    String name1 = "chatroom1";
    String imageUrl1 = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String description1 = "chatroom1 description";

    String name2 = "chatroom2";
    String imageUrl2 = "https://i.pinimg.com/564x/6a/95/83/6a958390de7924f68e1dfbd57d8c41d6.jpg";
    String description2 = "chatroom2 description";

    chatRoomService.createChatRoom(Seed.user1,
        new CreateChatRoomDto(name1, imageUrl1, description1));

    chatRoomService.createChatRoom(Seed.user1,
        new CreateChatRoomDto(name2, imageUrl2, description2));

    mockMvc.perform(get("/chat_room"))
        .andExpectAll(status().isOk())
        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("[].id").description("채널 ID"),
                fieldWithPath("[].isHost").description("채팅방 호스트여부"),
                fieldWithPath("[].chatRoom.id").description("채팅방 ID"),
                fieldWithPath("[].chatRoom.name").description("채팅방 이름"),
                fieldWithPath("[].chatRoom.imageUrl").description("채팅방 이미지"),
                fieldWithPath("[].chatRoom.description").description("채팅방 설명")
            )
        ));


  }


}