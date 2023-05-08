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

package com.microfocus.example.service;

import com.microfocus.example.entity.CustomUserDetails;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.UserLockedOutException;
import com.microfocus.example.repository.UserRepositoryCustom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Implementation of basic User Details Service for spring security database authentication
 * @author Kevin A. Lee
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {



    @Autowired
    private UserRepositoryCustom userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        try {
            // Tenta encontrar o usuário pelo nome de usuário e, se não encontrado, tenta encontrar pelo e-mail
            user = userRepository.findUserByUsername(username)
                .orElseGet(() -> userRepository.findUserByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User with email: " + username + " not found.")));
        } catch (UserLockedOutException ignored) {
            // Tratamento de exceção para usuário bloqueado
        }
        // Retorna os detalhes do usuário
        return new CustomUserDetails(user);
    }
}
