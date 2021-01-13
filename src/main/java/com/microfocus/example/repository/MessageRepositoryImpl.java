/*
        Insecure Web App (IWA)

        Copyright (C) 2020 Micro Focus or one of its affiliates

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.microfocus.example.repository;

import com.microfocus.example.entity.Message;
import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.User;
import com.microfocus.example.payload.request.MessageRequest;
import com.microfocus.example.web.form.MessageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of Custom Message Repository
 * @author Kevin A. Lee
 */
@Transactional
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(MessageRepositoryImpl.class);

    private final MessageRepositoryBasic messageRepositoryBasic;

    @PersistenceContext
    EntityManager entityManager;

    public MessageRepositoryImpl(MessageRepositoryBasic messageRepositoryBasic) {
        this.messageRepositoryBasic = messageRepositoryBasic;
    }

    @SuppressWarnings("unchecked")
    public List<Message> findByUserId(UUID userId) {
        Query query = entityManager.createQuery(
                "SELECT m FROM Message m WHERE m.user.id = ?1",
                Message.class);
        query.setParameter(1, userId);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public long countByUserId(UUID userId) {
        Query query = entityManager.createQuery(
                "SELECT count(m) FROM Message m WHERE m.user.id = ?1",
                Long.class);
        query.setParameter(1, userId);
        return (long)(query.getSingleResult());
    }

    @SuppressWarnings("unchecked")
    public long countUnreadByUserId(UUID userId) {
        Query query = entityManager.createQuery(
                "SELECT count(m) FROM Message m WHERE m.user.id = ?1 AND m.read = false",
                Long.class);
        query.setParameter(1, userId);
        return (long)(query.getSingleResult());
    }

    @SuppressWarnings("unchecked")
    public void markMessageAsReadById(UUID messageId) {
        Query query = entityManager.createQuery(
                "UPDATE Message m SET m.read = true WHERE m.id = ?1");
        query.setParameter(1, messageId);
        query.executeUpdate();
    }

    public Message save(MessageForm message) {
        Message m =  new Message();
        m.setUser(message.getUser());
        m.setText(message.getText());
        return messageRepositoryBasic.save(m);
    }

}
