package com.microfocus.example.repository;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.Message;
import com.microfocus.example.entity.User;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageRepositoryTest extends BaseIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Test
    public void a_messageRepository_existsById() {
        if (!messageRepository.existsById(DataSeeder.TEST_MESSAGE1_ID)) fail("Message 1 does not exist");
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

    @Test
    public void d_messageRepository_persist() {
        Message m = DataSeeder.generateMessage();
        Optional<User> u = userRepository.findById(DataSeeder.TEST_MESSAGE2_USERID);
        if (u.isPresent()) {
            m.setUser(u.get());
        } else {
            fail("Unable to retrieve user using id: " + DataSeeder.TEST_MESSAGE2_USERID);
        }
        m = messageRepository.saveAndFlush(m);
        Optional<Message> message = messageRepository.findById(m.getId());
        if (message.isPresent()) {
            assertThat(message.get().getText().equals(DataSeeder.TEST_MESSAGE2_TEXT));
        } else {
            fail("Test Message 2 not found");
        }
    }

    @Test
    public void e_messageRepository_update() {
        List<Message> messages = messageRepository.findByUserId(DataSeeder.TEST_MESSAGE2_USERID);
        if (messages.size() > 0) {
            Message m = messages.get(0);
            m.setText(DataSeeder.TEST_MESSAGE2_TEXT + " updated");
            m.setRead(true);
            messageRepository.saveAndFlush(m);
            Optional<Message> optionalMessage = messageRepository.findById(m.getId());
            if (optionalMessage.isPresent()) {
                Message m2 = optionalMessage.get();
                assertThat(m2.getText()).isEqualTo(DataSeeder.TEST_MESSAGE2_TEXT + " updated");
                assertThat(m2.getRead()).isEqualTo(true);
            } else
                fail("Test Message 2 not found");
        } else
            fail("Test Message 2 not found");
    }

}
