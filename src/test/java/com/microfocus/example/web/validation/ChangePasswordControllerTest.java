package com.microfocus.example.web.validation;

import com.microfocus.example.BaseIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sun.misc.PostVMInitHook.run;

@AutoConfigureMockMvc
public class ChangePasswordControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void emptyTest() {
        run();
    }
    /*
    @Test
    public void submitSavePasswordValid() throws Exception {
        mvc
                .perform(
                        post("/user/savePassword")
                                .param("username", "test")
                                .param("password", "xJ3!dij50")
                                .param("confirmPassword", "xJ3!dij50")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "password", "confirmPassword"))
                .andExpect(status().isOk());
    }

    @Test
    public void submitSavePasswordNotValid() throws Exception {
        mvc
                .perform(
                        post("/user/savePassword")
                                .param("username", "test")
                                .param("password", "xJ3!dij50")
                                .param("confirmPassword", "xJ3!dij50")
                )
                .andExpect(model().hasErrors())
                .andExpect(redirectedUrl("user/change-password"))
                .andExpect(status().is3xxRedirection());

    }*/

}
