package ru.job4j.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.StoreService;
import ru.job4j.auth.domain.Person;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AuthApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class PersonControllerTest {

    @MockBean
    private StoreService storeService;

    @Autowired
    private MockMvc mockMvc;

    List<Person> persons;

    @BeforeEach
    public void makePersons() {
        persons = new ArrayList<>() {{
            add(Person.of(1, "person1", "password1"));
            add(Person.of(2, "person2", "password2"));
            add(Person.of(3, "person3", "password3"));
        }};
    }

    @Test
    public void whenFindAll() throws Exception {
        when(storeService.findAll()).thenReturn(persons);

        mockMvc.perform(get("/person/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].login", is("person3")))
                .andExpect(jsonPath("$[2].password", is("password3")));
    }

    @Test
    public void whenFindByIdIsFound() throws Exception {
        int personId = 1;
        int index = 0;
        when(storeService.findById(personId))
                .thenReturn(java.util.Optional.ofNullable(persons.get(index)));

        mockMvc.perform(get("/person/{id}", personId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(personId)))
                .andExpect(jsonPath("$.login", is("person1")))
                .andExpect(jsonPath("$.password", is("password1")));
    }

    @Test
    public void whenFindByIdIsNotFound() throws Exception {
        int personId = 100;
        mockMvc.perform(get("/person/{id}", personId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(0)))
                .andExpect(jsonPath("$.login", nullValue()))
                .andExpect(jsonPath("$.password", nullValue()));
    }

    @Test
    public void whenCreateIsOk() throws Exception {
        Person newPerson = Person.of(4, "personNew", "passwordTheStrongest");
        when(storeService.save(ArgumentMatchers.any(Person.class))).thenReturn(newPerson);

        mockMvc.perform(post("/person/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"" + newPerson.getLogin() + "\","
                                + "\"password\":\""+ newPerson.getPassword() + "\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(newPerson.getId())))
                .andExpect(jsonPath("$.login", is(newPerson.getLogin())))
                .andExpect(jsonPath("$.password", is(newPerson.getPassword())));

        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(storeService).save(argument.capture());
        assertEquals(newPerson.getLogin(), argument.getValue().getLogin());
        assertEquals(newPerson.getPassword(), argument.getValue().getPassword());
    }

    @Test
    public void whenUpdateIsOk() throws Exception {
        int id = 0;
        String changedLogin = "laLogin";
        String changedPassword = "lePassword";
        when(storeService.save(ArgumentMatchers.any(Person.class))).thenReturn(persons.get(id));

        mockMvc.perform(put("/person/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"" + persons.get(id).getId() + "\","
                                + "\"login\":\"" + changedLogin +"\","
                                + "\"password\":\"" + changedPassword + "\"}"))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(storeService).save(argument.capture());

        assertEquals(persons.get(id).getId(), argument.getValue().getId());
        assertEquals(changedLogin, argument.getValue().getLogin());
        assertEquals(changedPassword, argument.getValue().getPassword());
    }


    @Test
    public void whenDeleteIsOk() throws Exception {
        int id = 1;

        mockMvc.perform(delete("/person/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(storeService).delete(argument.capture());

        assertEquals(id, argument.getValue().getId());
    }
}