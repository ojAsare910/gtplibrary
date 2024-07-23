package com.justiceasare.gtplibrary.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

        private User user;
        private UserType userType;

        @BeforeEach
        void setUp() {
            user = new User(1, "john_doe", "john@example.com", userType.STAFF);
        }

        @AfterEach
        void tearDown() {
            user = null;
        }

        @Test
        void testGetUserId() {
            assertEquals(1, user.getUserId());
        }

        @Test
        void testSetUserId() {
            user.setUserId(2);
            assertEquals(2, user.getUserId());
        }

        @Test
        void testGetUsername() {
            assertEquals("john_doe", user.getUsername());
        }

        @Test
        void testSetUsername() {
            user.setUsername("jane_doe");
            assertEquals("jane_doe", user.getUsername());
        }

        @Test
        void testGetEmail() {
            assertEquals("john@example.com", user.getEmail());
        }

        @Test
        void testSetEmail() {
            user.setEmail("jane@example.com");
            assertEquals("jane@example.com", user.getEmail());
        }

        @Test
        void testGetUserType() {
            assertEquals(UserType.STAFF, user.getUserType());
        }

        @Test
        void testSetUserType() {
            user.setUserType(UserType.PATRON);
            assertEquals(UserType.PATRON, user.getUserType());
        }
}
