package ui;

import java.awt.image.BufferedImage;

import DataAccess.DTO.UsuarioDTO;

public interface UIContract {
    void mostrarEstado(String mensaje);
    void limpiarPantalla();
    void updateFrame(BufferedImage frame);
    void showGranted(UsuarioDTO u, String codigoQR);
    void showDenied(String motivo, String codigoQR);
}
