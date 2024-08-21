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

import com.microfocus.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Interface for User Repository
 * @author Kevin A. Lee
 */
@Repository
public interface UserRepositoryBasic extends JpaRepository<User, UUID> {

    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query(value="UPDATE User u " + 
        "SET u.firstName = :firstName, u.lastName = :lastName, u.email = :email, u.phone = :phone, u.address = :address, u.city = :city, u.state = :state, u.zip = :zip, u.country = :country, u.mfa = :mfa " +
        "WHERE u.id = :userId")
    int updateProfile(@Param("userId") UUID userId, @Param("firstName") String firstname, @Param("lastName") String lastName,
        @Param("email") String email, @Param("phone") String phone, @Param("address") String address, @Param("city") String city,
        @Param("state") String state, @Param("zip") String zip, @Param("country") String country, @Param("mfa") Boolean mfa
    );
}
