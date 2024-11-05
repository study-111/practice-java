package study111.commerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import study111.commerce.config.SecurityConfiguration;
import study111.commerce.service.UserJoinCommand;
import study111.commerce.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfiguration.class)
@WebMvcTest
class UserControllerTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;

    @Test
    void join_case01() throws Exception {
        // given
        UserJoinCommand command = new UserJoinCommand();
        command.setUsername("tester");
        command.setPassword("pass");
        String content = objectMapper.writeValueAsString(command);

        when(userService.join(any(UserJoinCommand.class))).thenReturn(1L);

        // when
        var result = mockMvc.perform(
            post("/users").with(anonymous())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        );

        // then
        result.andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data").value(1L))
        ;
    }

    @Test
    void join_case02() throws Exception {
        // given
        UserJoinCommand command = new UserJoinCommand();
        command.setUsername("2");
        command.setPassword("");
        String content = objectMapper.writeValueAsString(command);

//        when(userService.join(any(UserJoinCommand.class))).thenReturn(1L);

        // when
        var result = mockMvc.perform(
            post("/users").with(anonymous())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        );

        // then
        result.andDo(print())
            .andExpect(status().isBadRequest())
//            .andExpect(jsonPath("$.data").exists())
//            .andExpect(jsonPath("$.data").value(1L))
        ;
    }
}