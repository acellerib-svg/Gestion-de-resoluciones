package org.uteq.resoluciones.resoluciones.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRolId implements Serializable {

    @Column(name = "id_user_login")
    private Long idUser;

    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "id_instance")
    private Long idInstance;
}
