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

import com.microfocus.example.entity.Authority;
import com.microfocus.example.entity.AuthorityType;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.UserLockedOutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of Custom Role Repository
 * @author Kevin A. Lee
 */
@Repository
@Transactional
public class RoleRepositoryImpl implements RoleRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(RoleRepositoryImpl.class);

    private final RoleRepositoryBasic roleRepositoryBasic;

    @PersistenceContext
    EntityManager entityManager;

    public RoleRepositoryImpl(RoleRepositoryBasic roleRepositoryBasic) {
        this.roleRepositoryBasic = roleRepositoryBasic;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Authority> findByName(String roleName) {
        Query q = entityManager.createQuery(
		        "SELECT a FROM Authority a WHERE a.name = ?1", Authority.class);
        q.setParameter(1, AuthorityType.valueOf(roleName));
        Authority result = (Authority) q.getSingleResult();
        Optional<Authority> optionalAuthority = Optional.empty();
        if (q.getMaxResults() > 0) {
            optionalAuthority = Optional.of(result);
        }
        return optionalAuthority;
    }
}
