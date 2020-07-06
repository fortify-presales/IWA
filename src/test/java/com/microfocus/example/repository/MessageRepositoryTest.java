package com.microfocus.example.repository;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.Authority;
import com.microfocus.example.entity.Message;
import com.microfocus.example.entity.User;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageRepositoryTest extends BaseIntegrationTest {

    @Autowired
    MessageRepository messageRepository;

    @Test
    public void a_messageRepository_existsById() {
        if (!messageRepository.existsById(99999)) fail("Message 1 does not exist");
    }

    @Test
    public void b_messageRepository_findMessageById() {
        Optional<Message> m = messageRepository.findById(DataSeeder.TEST_MESSAGE1_ID);
        if (m.isPresent())
            assertThat(m.get().getText()).isEqualTo(DataSeeder.TEST_MESSAGE1_TEXT);
        else
            fail("Test Message 1 not found");
    }

    @Test
    public void c_messageRepository_findMessageByUserId() {
        List<Message> messages = messageRepository.findByUserId(DataSeeder.TEST_MESSAGE1_USERID);
        assertThat(messages.size()).isEqualTo(1);
    }

}
