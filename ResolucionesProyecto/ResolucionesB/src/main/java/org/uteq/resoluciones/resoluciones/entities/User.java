package org.uteq.resoluciones.resoluciones.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tb_user_login")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user_login")
    private Long id;

    @Column(name="name_user", nullable = false, length = 50, unique = true)
    private String user;

    @Column(name="password", nullable = false, length = 200)
    private String password;

    @Column(name = "names", length = 150)
    private String names;

    @Column(name="surname", length = 150)
    private String surnames;

    @Column(name="phone", length = 30)
    private String phone;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "last_access")
    private LocalDateTime lastAccess;

    @Column(name="email", nullable = false, length = 100)
    private String email;

    @Column(name="state")
    private Boolean state;

    @Column(name = "profile_completed", nullable = false)
    private Boolean profileCompleted = false;
}
