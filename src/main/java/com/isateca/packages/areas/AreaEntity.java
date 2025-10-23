package com.isateca.packages.areas;

import com.isateca.packages.tickets.*; // Asumiendo el nombre de tu entidad de ticket
import com.isateca.packages.users.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "areas")
public class AreaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
    private Set<UserEntity> users;

    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
    private Set<Ticket> tickets;
}
