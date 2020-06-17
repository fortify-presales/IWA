package com.microfocus.example.web.validation;

import com.microfocus.example.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class ChangePasswordControllerTest extends BaseIntegrationTest {

    @SuppressWarnings("unused")
	@Autowired
    private MockMvc mvc;

    @Test
    public void emptyTest() {

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
