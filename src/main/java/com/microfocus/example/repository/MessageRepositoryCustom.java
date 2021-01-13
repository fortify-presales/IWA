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
import com.microfocus.example.payload.request.MessageRequest;
import com.microfocus.example.web.form.MessageForm;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for Message Repository
 * @author Kevin A. Lee
 */
public interface MessageRepositoryCustom {

    public List<Message> findByUserId(UUID userId);

    public long countByUserId(UUID userId);

    public long countUnreadByUserId(UUID userId);

    public void markMessageAsReadById(UUID messageId);

    //public Message save(MessageRequest message);

    public Message save(MessageForm message);

}
