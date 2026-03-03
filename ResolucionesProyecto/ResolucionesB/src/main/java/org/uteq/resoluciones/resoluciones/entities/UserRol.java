package org.uteq.resoluciones.resoluciones.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user_rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRol {

    @EmbeddedId
    private UserRolId id;

    @ManyToOne(optional = false)
    @MapsId("idUser")
    @JoinColumn(name = "id_user_login",
            foreignKey = @ForeignKey(name = "fk_user_rol_user"))
    private User user4;

    @ManyToOne(optional = false)
    @MapsId("idRol")
    @JoinColumn(name = "id_rol",
            foreignKey = @ForeignKey(name = "fk_user_rol_rol"))
    private Rol rol;

    @ManyToOne(optional = false)
    @MapsId("idInstance")
    @JoinColumn(name = "id_instance",
            foreignKey = @ForeignKey(name = "fk_user_instance"))
    private Instance instance4;
}

