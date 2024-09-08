package spring.pos.entity;

import org.junit.jupiter.api.Test;

import spring.pos.model.role.RoleEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

public class RoleEntityTest {

    @Test
    public void testRoleEntity() {
        RoleEntity role = new RoleEntity(1L, "admin", LocalDateTime.parse("2024-09-08T02:23:05"),
                LocalDateTime.parse("2024-09-08T02:23:05"));

        assertEquals(1L, role.getRoleId());
        assertEquals("admin", "admin");
        assertEquals(LocalDateTime.parse("2024-09-08T02:23:05"), role.getCreatedAt());
        assertEquals(LocalDateTime.parse("2024-09-08T02:23:05"), role.getUpdatedAt());

        role.setName("user");
        assertEquals("user", role.getName());
    }
}