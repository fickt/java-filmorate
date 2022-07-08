package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@ComponentScan
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    private Gson json = new Gson();


    @Test
    void shouldReplaceNameWithLoginIfNameIsMissing() throws Exception { //if run separately, test passes... strange
        User user = new User();                                         //TODO to figure out why
        user.setId(0);
        user.setName("");
        user.setEmail("login@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2001, 1, 12));

        String userAsJson = json.toJson(user);

        this.mvc.perform(post("/users") //newuser
                .contentType(MediaType.APPLICATION_JSON)
                .content(userAsJson));

        this.mvc.perform(get("/users")) //allusers
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("login")));
    }

    @Test
    void shouldReturnResponseStatus400CreatingUserWithNoEmail() throws Exception {
        User user = new User();
        user.setName("Name");
        user.setEmail("");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2001, 1, 12));

        int responseStatus = this.mvc.perform(post("/users") //newuser
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(user))).andReturn().getResponse().getStatus();

        assertEquals(400, responseStatus);
    }

    @Test
    void shouldReturnResponseStatus400CreatingUserWithIncorrectEmailWithoutAdSymbol() throws Exception {
        User user = new User();
        user.setName("Name");
        user.setEmail("login.mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2001, 1, 12));

        int responseStatus = this.mvc.perform(post("/users") //newuser
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(user))).andReturn().getResponse().getStatus();

        assertEquals(400, responseStatus);
    }

    @Test
    void shouldReturnResponseStatus400CreatingUserWithEmailWithGap() throws Exception {
        User user = new User();
        user.setId(0);
        user.setName("Name");
        user.setEmail("login@mail.ru");
        user.setLogin("lo gin");
        user.setBirthday(LocalDate.of(2001, 1, 12));

        int responseStatus = this.mvc.perform(post("/users") //newuser
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(user))).andReturn().getResponse().getStatus();

        assertEquals(400, responseStatus);
    }


    @Test
    void shouldReturnResponseStatus400CreatingUserWithInvalidBirthday() throws Exception {
        User user = new User();
        user.setId(0);
        user.setName("name");
        user.setEmail("login@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2100, 1, 12));

        String userAsJson = json.toJson(user);

        this.mvc.perform(post("/users") //newuser
                .contentType(MediaType.APPLICATION_JSON)
                .content(userAsJson)).andExpect(status().isBadRequest());

    }

    @Test
    void shouldReturnResponseStatus200CreatingUserWithValidValues() throws Exception {
        User user = new User();
        user.setId(0);
        user.setName("name");
        user.setEmail("login@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2001, 1, 12));

        String userAsJson = json.toJson(user);

        this.mvc.perform(post("/users") //newuser
                .contentType(MediaType.APPLICATION_JSON)
                .content(userAsJson)).andExpect(status().isOk());
    }
}
