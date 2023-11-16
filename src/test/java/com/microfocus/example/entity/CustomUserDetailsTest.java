package com.microfocus.example.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

class CustomUserDetailsTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link CustomUserDetails#CustomUserDetails(User)}
     *   <li>{@link CustomUserDetails#getUserDetails()}
     *   <li>{@link CustomUserDetails#isAccountNonExpired()}
     *   <li>{@link CustomUserDetails#isAccountNonLocked()}
     *   <li>{@link CustomUserDetails#isCredentialsNonExpired()}
     * </ul>
     */
    @Test
    void testConstructor() {
        User user = new User();
        user.setAddress("42 Main St");
        user.setAuthorities(new HashSet<>());
        user.setCity("Oxford");
        user.setConfirmPassword("iloveyou");
        user.setCountry("GB");
        user.setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setId(UUID.randomUUID());
        user.setLastName("Doe");
        user.setMfa(true);
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setState("MD");
        user.setUsername("janedoe");
        user.setVerifyCode("Verify Code");
        user.setZip("21654");
        CustomUserDetails actualCustomUserDetails = new CustomUserDetails(user);
        assertSame(user, actualCustomUserDetails.getUserDetails());
        assertTrue(actualCustomUserDetails.isAccountNonExpired());
        assertTrue(actualCustomUserDetails.isAccountNonLocked());
        assertTrue(actualCustomUserDetails.isCredentialsNonExpired());
    }

    /**
     * Method under test: {@link CustomUserDetails#getAuthorities()}
     */
    @Test
    void testGetAuthorities() {
        assertTrue((new CustomUserDetails(new User())).getAuthorities().isEmpty());
    }

    /**
     * Method under test: {@link CustomUserDetails#getAuthorities()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetAuthorities2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.entity.User.getAuthorities()" because "this.user" is null
        //       at com.microfocus.example.entity.CustomUserDetails.getAuthorities(CustomUserDetails.java:45)
        //   See https://diff.blue/R013 to resolve this issue.

        (new CustomUserDetails(null)).getAuthorities();
    }

    /**
     * Method under test: {@link CustomUserDetails#getAuthorities()}
     */
    @Test
    void testGetAuthorities3() {
        Authority authority = new Authority();
        authority.setId(UUID.randomUUID());
        authority.setName(AuthorityType.ROLE_ADMIN);

        HashSet<Authority> authoritySet = new HashSet<>();
        authoritySet.add(authority);

        User user = new User();
        user.setAuthorities(authoritySet);
        Collection<? extends GrantedAuthority> actualAuthorities = (new CustomUserDetails(user)).getAuthorities();
        assertEquals(1, actualAuthorities.size());
        assertEquals("ROLE_ADMIN", ((List<? extends GrantedAuthority>) actualAuthorities).get(0).getAuthority());
    }

    /**
     * Method under test: {@link CustomUserDetails#getAuthorities()}
     */
    @Test
    void testGetAuthorities4() {
        Authority authority = new Authority();
        authority.setId(UUID.randomUUID());
        authority.setName(AuthorityType.ROLE_ADMIN);

        Authority authority1 = new Authority();
        authority1.setId(UUID.randomUUID());
        authority1.setName(AuthorityType.ROLE_USER);

        HashSet<Authority> authoritySet = new HashSet<>();
        authoritySet.add(authority1);
        authoritySet.add(authority);

        User user = new User();
        user.setAuthorities(authoritySet);
        assertEquals(2, (new CustomUserDetails(user)).getAuthorities().size());
    }

    /**
     * Method under test: {@link CustomUserDetails#getId()}
     */
    @Test
    void testGetId() {
        assertNull((new CustomUserDetails(new User())).getId());
    }

    /**
     * Method under test: {@link CustomUserDetails#getId()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetId2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.entity.User.getId()" because "this.user" is null
        //       at com.microfocus.example.entity.CustomUserDetails.getId(CustomUserDetails.java:49)
        //   See https://diff.blue/R013 to resolve this issue.

        (new CustomUserDetails(null)).getId();
    }

    /**
     * Method under test: {@link CustomUserDetails#getPassword()}
     */
    @Test
    void testGetPassword() {
        assertNull((new CustomUserDetails(new User())).getPassword());
    }

    /**
     * Method under test: {@link CustomUserDetails#getPassword()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetPassword2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.entity.User.getPassword()" because "this.user" is null
        //       at com.microfocus.example.entity.CustomUserDetails.getPassword(CustomUserDetails.java:54)
        //   See https://diff.blue/R013 to resolve this issue.

        (new CustomUserDetails(null)).getPassword();
    }

    /**
     * Method under test: {@link CustomUserDetails#getUsername()}
     */
    @Test
    void testGetUsername() {
        assertNull((new CustomUserDetails(new User())).getUsername());
    }

    /**
     * Method under test: {@link CustomUserDetails#getUsername()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetUsername2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.entity.User.getUsername()" because "this.user" is null
        //       at com.microfocus.example.entity.CustomUserDetails.getUsername(CustomUserDetails.java:59)
        //   See https://diff.blue/R013 to resolve this issue.

        (new CustomUserDetails(null)).getUsername();
    }

    /**
     * Method under test: {@link CustomUserDetails#getName()}
     */
    @Test
    void testGetName() {
        assertNull((new CustomUserDetails(new User())).getName());
    }

    /**
     * Method under test: {@link CustomUserDetails#getName()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetName2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.entity.User.getFirstName()" because "this.user" is null
        //       at com.microfocus.example.entity.CustomUserDetails.getName(CustomUserDetails.java:78)
        //   See https://diff.blue/R013 to resolve this issue.

        (new CustomUserDetails(null)).getName();
    }

    /**
     * Method under test: {@link CustomUserDetails#getEmail()}
     */
    @Test
    void testGetEmail() {
        assertNull((new CustomUserDetails(new User())).getEmail());
    }

    /**
     * Method under test: {@link CustomUserDetails#getEmail()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetEmail2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.entity.User.getEmail()" because "this.user" is null
        //       at com.microfocus.example.entity.CustomUserDetails.getEmail(CustomUserDetails.java:82)
        //   See https://diff.blue/R013 to resolve this issue.

        (new CustomUserDetails(null)).getEmail();
    }

    /**
     * Method under test: {@link CustomUserDetails#getMobile()}
     */
    @Test
    void testGetMobile() {
        assertNull((new CustomUserDetails(new User())).getMobile());
    }

    /**
     * Method under test: {@link CustomUserDetails#getMobile()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetMobile2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.entity.User.getPhone()" because "this.user" is null
        //       at com.microfocus.example.entity.CustomUserDetails.getMobile(CustomUserDetails.java:86)
        //   See https://diff.blue/R013 to resolve this issue.

        (new CustomUserDetails(null)).getMobile();
    }

    /**
     * Method under test: {@link CustomUserDetails#getMfa()}
     */
    @Test
    void testGetMfa() {
        assertFalse((new CustomUserDetails(new User())).getMfa());
        assertTrue((new CustomUserDetails(new User(UUID.randomUUID(), "janedoe", "iloveyou", "Jane", "Doe",
                "jane.doe@example.org", "6625550144", "42 Main St", "Oxford", "MD", "21654", "GB", true, true))).getMfa());
    }

    /**
     * Method under test: {@link CustomUserDetails#getMfa()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetMfa2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.entity.User.getMfa()" because "this.user" is null
        //       at com.microfocus.example.entity.CustomUserDetails.getMfa(CustomUserDetails.java:89)
        //   See https://diff.blue/R013 to resolve this issue.

        (new CustomUserDetails(null)).getMfa();
    }

    /**
     * Method under test: {@link CustomUserDetails#isEnabled()}
     */
    @Test
    void testIsEnabled() {
        assertFalse((new CustomUserDetails(new User())).isEnabled());
        assertTrue((new CustomUserDetails(new User(UUID.randomUUID(), "janedoe", "iloveyou", "Jane", "Doe",
                "jane.doe@example.org", "6625550144", "42 Main St", "Oxford", "MD", "21654", "GB", true, true)))
                .isEnabled());
    }

    /**
     * Method under test: {@link CustomUserDetails#isEnabled()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testIsEnabled2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.microfocus.example.entity.User.getEnabled()" because "this.user" is null
        //       at com.microfocus.example.entity.CustomUserDetails.isEnabled(CustomUserDetails.java:91)
        //   See https://diff.blue/R013 to resolve this issue.

        (new CustomUserDetails(null)).isEnabled();
    }
}

