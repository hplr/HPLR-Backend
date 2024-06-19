package org.hplr.infrastructure.controller;

import org.hplr.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.infrastructure.dbadapter.repositories.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRESTControllerIT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    PlayerRepository playerRepository;

    @Test
    public void get_nonexistent_user_and_get_404() throws Exception {
        playerRepository.save(new PlayerEntity(UUID.randomUUID(),"lll","lll",10L));
        mvc.perform(MockMvcRequestBuilders
                        .get("/game/debug"))
                .andExpect(status().isOk());


    }
}
