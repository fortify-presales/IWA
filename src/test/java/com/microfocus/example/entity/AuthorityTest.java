package com.microfocus.example.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class AuthorityTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Authority#Authority()}
     *   <li>{@link Authority#setId(UUID)}
     *   <li>{@link Authority#setName(AuthorityType)}
     *   <li>{@link Authority#toString()}
     *   <li>{@link Authority#getId()}
     *   <li>{@link Authority#getName()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Authority actualAuthority = new Authority();
        UUID randomUUIDResult = UUID.randomUUID();
        actualAuthority.setId(randomUUIDResult);
        actualAuthority.setName(AuthorityType.ROLE_ADMIN);
        actualAuthority.toString();
        assertSame(randomUUIDResult, actualAuthority.getId());
        assertEquals(AuthorityType.ROLE_ADMIN, actualAuthority.getName());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Authority#Authority(AuthorityType)}
     *   <li>{@link Authority#setId(UUID)}
     *   <li>{@link Authority#setName(AuthorityType)}
     *   <li>{@link Authority#toString()}
     *   <li>{@link Authority#getId()}
     *   <li>{@link Authority#getName()}
     * </ul>
     */
    @Test
    void testConstructor2() {
        Authority actualAuthority = new Authority(AuthorityType.ROLE_ADMIN);
        UUID randomUUIDResult = UUID.randomUUID();
        actualAuthority.setId(randomUUIDResult);
        actualAuthority.setName(AuthorityType.ROLE_ADMIN);
        actualAuthority.toString();
        assertSame(randomUUIDResult, actualAuthority.getId());
        assertEquals(AuthorityType.ROLE_ADMIN, actualAuthority.getName());
    }
}

