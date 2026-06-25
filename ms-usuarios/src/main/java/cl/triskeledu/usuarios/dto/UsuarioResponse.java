package cl.triskeledu.usuarios.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO de respuesta para Usuario con soporte HATEOAS.
 *
 * Al extender RepresentationModel, Jackson serializa el campo "_links"
 * cuando el controlador agrega links con .add(Link...).
 * La contraseña nunca se expone aquí.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UsuarioResponse extends RepresentationModel<UsuarioResponse> {

    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String rol;
    private Boolean activo;
}
