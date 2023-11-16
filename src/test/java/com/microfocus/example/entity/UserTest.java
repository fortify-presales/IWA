package com.microfocus.example.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

class UserTest {
    /**
     * Method under test: {@link User#passwordEncoder()}
     */
    @Test
    void testPasswordEncoder() {
        assertFalse((new User()).passwordEncoder().upgradeEncoding("secret"));
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link User#User()}
     *   <li>{@link User#setAddress(String)}
     *   <li>{@link User#setAuthorities(Set)}
     *   <li>{@link User#setCity(String)}
     *   <li>{@link User#setConfirmPassword(String)}
     *   <li>{@link User#setCountry(String)}
     *   <li>{@link User#setDateCreated(Date)}
     *   <li>{@link User#setEmail(String)}
     *   <li>{@link User#setEnabled(boolean)}
     *   <li>{@link User#setFirstName(String)}
     *   <li>{@link User#setId(UUID)}
     *   <li>{@link User#setLastName(String)}
     *   <li>{@link User#setMfa(boolean)}
     *   <li>{@link User#setPassword(String)}
     *   <li>{@link User#setPhone(String)}
     *   <li>{@link User#setState(String)}
     *   <li>{@link User#setUsername(String)}
     *   <li>{@link User#setVerifyCode(String)}
     *   <li>{@link User#setZip(String)}
     *   <li>{@link User#toString()}
     *   <li>{@link User#getAddress()}
     *   <li>{@link User#getAuthorities()}
     *   <li>{@link User#getCity()}
     *   <li>{@link User#getConfirmPassword()}
     *   <li>{@link User#getCountry()}
     *   <li>{@link User#getDateCreated()}
     *   <li>{@link User#getEmail()}
     *   <li>{@link User#getEnabled()}
     *   <li>{@link User#getFirstName()}
     *   <li>{@link User#getId()}
     *   <li>{@link User#getLastName()}
     *   <li>{@link User#getMfa()}
     *   <li>{@link User#getPassword()}
     *   <li>{@link User#getPhone()}
     *   <li>{@link User#getState()}
     *   <li>{@link User#getUsername()}
     *   <li>{@link User#getVerifyCode()}
     *   <li>{@link User#getZip()}
     * </ul>
     */
    @Test
    void testConstructor() {
        User actualUser = new User();
        actualUser.setAddress("42 Main St");
        HashSet<Authority> authoritySet = new HashSet<>();
        actualUser.setAuthorities(authoritySet);
        actualUser.setCity("Oxford");
        actualUser.setConfirmPassword("iloveyou");
        actualUser.setCountry("GB");
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualUser.setDateCreated(fromResult);
        actualUser.setEmail("jane.doe@example.org");
        actualUser.setEnabled(true);
        actualUser.setFirstName("Jane");
        UUID randomUUIDResult = UUID.randomUUID();
        actualUser.setId(randomUUIDResult);
        actualUser.setLastName("Doe");
        actualUser.setMfa(true);
        actualUser.setPassword("iloveyou");
        actualUser.setPhone("6625550144");
        actualUser.setState("MD");
        actualUser.setUsername("janedoe");
        actualUser.setVerifyCode("Verify Code");
        actualUser.setZip("21654");
        actualUser.toString();
        assertEquals("42 Main St", actualUser.getAddress());
        assertSame(authoritySet, actualUser.getAuthorities());
        assertEquals("Oxford", actualUser.getCity());
        assertEquals("iloveyou", actualUser.getConfirmPassword());
        assertEquals("GB", actualUser.getCountry());
        assertSame(fromResult, actualUser.getDateCreated());
        assertEquals("jane.doe@example.org", actualUser.getEmail());
        assertTrue(actualUser.getEnabled());
        assertEquals("Jane", actualUser.getFirstName());
        assertSame(randomUUIDResult, actualUser.getId());
        assertEquals("Doe", actualUser.getLastName());
        assertTrue(actualUser.getMfa());
        assertEquals("iloveyou", actualUser.getPassword());
        assertEquals("6625550144", actualUser.getPhone());
        assertEquals("MD", actualUser.getState());
        assertEquals("janedoe", actualUser.getUsername());
        assertEquals("Verify Code", actualUser.getVerifyCode());
        assertEquals("21654", actualUser.getZip());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link User#User(UUID, String, String, String, String, String, String, String, String, String, String, String, boolean, boolean)}
     *   <li>{@link User#setAddress(String)}
     *   <li>{@link User#setAuthorities(Set)}
     *   <li>{@link User#setCity(String)}
     *   <li>{@link User#setConfirmPassword(String)}
     *   <li>{@link User#setCountry(String)}
     *   <li>{@link User#setDateCreated(Date)}
     *   <li>{@link User#setEmail(String)}
     *   <li>{@link User#setEnabled(boolean)}
     *   <li>{@link User#setFirstName(String)}
     *   <li>{@link User#setId(UUID)}
     *   <li>{@link User#setLastName(String)}
     *   <li>{@link User#setMfa(boolean)}
     *   <li>{@link User#setPassword(String)}
     *   <li>{@link User#setPhone(String)}
     *   <li>{@link User#setState(String)}
     *   <li>{@link User#setUsername(String)}
     *   <li>{@link User#setVerifyCode(String)}
     *   <li>{@link User#setZip(String)}
     *   <li>{@link User#toString()}
     *   <li>{@link User#getAddress()}
     *   <li>{@link User#getAuthorities()}
     *   <li>{@link User#getCity()}
     *   <li>{@link User#getConfirmPassword()}
     *   <li>{@link User#getCountry()}
     *   <li>{@link User#getDateCreated()}
     *   <li>{@link User#getEmail()}
     *   <li>{@link User#getEnabled()}
     *   <li>{@link User#getFirstName()}
     *   <li>{@link User#getId()}
     *   <li>{@link User#getLastName()}
     *   <li>{@link User#getMfa()}
     *   <li>{@link User#getPassword()}
     *   <li>{@link User#getPhone()}
     *   <li>{@link User#getState()}
     *   <li>{@link User#getUsername()}
     *   <li>{@link User#getVerifyCode()}
     *   <li>{@link User#getZip()}
     * </ul>
     */
    @Test
    void testConstructor2() {
        User actualUser = new User(UUID.randomUUID(), "janedoe", "iloveyou", "Jane", "Doe", "jane.doe@example.org",
                "6625550144", "42 Main St", "Oxford", "MD", "21654", "GB", true, true);
        actualUser.setAddress("42 Main St");
        HashSet<Authority> authoritySet = new HashSet<>();
        actualUser.setAuthorities(authoritySet);
        actualUser.setCity("Oxford");
        actualUser.setConfirmPassword("iloveyou");
        actualUser.setCountry("GB");
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualUser.setDateCreated(fromResult);
        actualUser.setEmail("jane.doe@example.org");
        actualUser.setEnabled(true);
        actualUser.setFirstName("Jane");
        UUID randomUUIDResult = UUID.randomUUID();
        actualUser.setId(randomUUIDResult);
        actualUser.setLastName("Doe");
        actualUser.setMfa(true);
        actualUser.setPassword("iloveyou");
        actualUser.setPhone("6625550144");
        actualUser.setState("MD");
        actualUser.setUsername("janedoe");
        actualUser.setVerifyCode("Verify Code");
        actualUser.setZip("21654");
        actualUser.toString();
        assertEquals("42 Main St", actualUser.getAddress());
        assertSame(authoritySet, actualUser.getAuthorities());
        assertEquals("Oxford", actualUser.getCity());
        assertEquals("iloveyou", actualUser.getConfirmPassword());
        assertEquals("GB", actualUser.getCountry());
        assertSame(fromResult, actualUser.getDateCreated());
        assertEquals("jane.doe@example.org", actualUser.getEmail());
        assertTrue(actualUser.getEnabled());
        assertEquals("Jane", actualUser.getFirstName());
        assertSame(randomUUIDResult, actualUser.getId());
        assertEquals("Doe", actualUser.getLastName());
        assertTrue(actualUser.getMfa());
        assertEquals("iloveyou", actualUser.getPassword());
        assertEquals("6625550144", actualUser.getPhone());
        assertEquals("MD", actualUser.getState());
        assertEquals("janedoe", actualUser.getUsername());
        assertEquals("Verify Code", actualUser.getVerifyCode());
        assertEquals("21654", actualUser.getZip());
    }

    /**
     * Method under test: {@link User#fromUserDetails(UserDetails)}
     */
    @Test
    void testFromUserDetails() {
        User actualFromUserDetailsResult = User.fromUserDetails(new CustomUserDetails(new User()));
        assertNull(actualFromUserDetailsResult.getUsername());
        assertFalse(actualFromUserDetailsResult.getEnabled());
        assertTrue(actualFromUserDetailsResult.getAuthorities().isEmpty());
    }

    /**
     * Method under test: {@link User#fromUserDetails(UserDetails)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testFromUserDetails2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.entity.User.getAuthorities()" because "this.user" is null
        //       at com.microfocus.example.entity.CustomUserDetails.getAuthorities(CustomUserDetails.java:45)
        //       at com.microfocus.example.entity.User.fromUserDetails(User.java:292)
        //   See https://diff.blue/R013 to resolve this issue.

        User.fromUserDetails(new CustomUserDetails(null));
    }

    /**
     * Method under test: {@link User#fromUserDetails(UserDetails)}
     */
    @Test
    void testFromUserDetails3() {
        User actualFromUserDetailsResult = User
                .fromUserDetails(new CustomUserDetails(new User(UUID.randomUUID(), "janedoe", "iloveyou", "Jane", "Doe",
                        "jane.doe@example.org", "6625550144", "42 Main St", "Oxford", "MD", "21654", "GB", true, true)));
        assertEquals("janedoe", actualFromUserDetailsResult.getUsername());
        assertTrue(actualFromUserDetailsResult.getEnabled());
        assertTrue(actualFromUserDetailsResult.getAuthorities().isEmpty());
    }

    /**
     * Method under test: {@link User#fromUserDetails(UserDetails)}
     */
    @Test
    void testFromUserDetails4() {
        User actualFromUserDetailsResult = User.fromUserDetails(
                new org.springframework.security.core.userdetails.User("janedoe", "iloveyou", new ArrayList<>()));
        assertEquals("janedoe", actualFromUserDetailsResult.getUsername());
        assertTrue(actualFromUserDetailsResult.getEnabled());
        assertTrue(actualFromUserDetailsResult.getAuthorities().isEmpty());
    }

    /**
     * Method under test: {@link User#fromUserDetails(UserDetails)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testFromUserDetails5() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalArgumentException: No enum constant com.microfocus.example.entity.AuthorityType.Role
        //       at java.lang.Enum.valueOf(Enum.java:273)
        //       at com.microfocus.example.entity.AuthorityType.valueOf(AuthorityType.java:26)
        //       at com.microfocus.example.entity.User.fromUserDetails(User.java:293)
        //   See https://diff.blue/R013 to resolve this issue.

        ArrayList<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("Role"));
        User.fromUserDetails(
                new org.springframework.security.core.userdetails.User("janedoe", "iloveyou", grantedAuthorityList));
    }

    /**
     * Method under test: {@link User#fromUserDetails(UserDetails)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testFromUserDetails6() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalArgumentException: No enum constant com.microfocus.example.entity.AuthorityType.Role
        //       at java.lang.Enum.valueOf(Enum.java:273)
        //       at com.microfocus.example.entity.AuthorityType.valueOf(AuthorityType.java:26)
        //       at com.microfocus.example.entity.User.fromUserDetails(User.java:293)
        //   See https://diff.blue/R013 to resolve this issue.

        ArrayList<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("Role"));
        grantedAuthorityList.add(new SimpleGrantedAuthority("Role"));
        User.fromUserDetails(
                new org.springframework.security.core.userdetails.User("janedoe", "iloveyou", grantedAuthorityList));
    }
}

