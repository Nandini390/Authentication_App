package org.example.authapp.Dtos;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoleDto {
    private UUID id;
    private String name;
}
