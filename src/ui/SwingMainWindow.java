package ui;

import javax.swing.*;
import java.awt.*;

public class SwingMainWindow extends JFrame implements UIContract {

    private JLabel lblEstado;

    public SwingMainWindow() {
        inicializarVentana();
        inicializarComponentes();
    }

    private void inicializarVentana() {
        setTitle("Sistema de Control de Acceso");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {

        Color colorFondo = new Color(245, 247, 250);
        Color colorPanel = Color.WHITE;
        Color colorTitulo = new Color(33, 37, 41);
        Color colorAcento = new Color(0, 102, 204);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Control de Acceso", new JPanel());
        tabs.addTab("Gestión de Usuarios", new JPanel());
        tabs.addTab("Historial de Registros", new JPanel());
        add(tabs, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(colorFondo);

        JPanel panelCamara = new JPanel(new BorderLayout());
        panelCamara.setPreferredSize(new Dimension(480, 0));
        panelCamara.setBackground(Color.BLACK);
        panelCamara.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "Cámara en vivo"
                )
        );

        JLabel lblCamara = new JLabel("Vista de cámara", SwingConstants.CENTER);
        lblCamara.setForeground(Color.WHITE);
        lblCamara.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelCamara.add(lblCamara, BorderLayout.CENTER);

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBackground(colorPanel);
        panelDatos.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)
                )
        );

        JLabel titulo = new JLabel("Datos del Usuario Escaneado");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(colorTitulo);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        JLabel lblFoto = new JLabel("SIN FOTO", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(130, 160));
        lblFoto.setMaximumSize(new Dimension(130, 160));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(new Color(230, 230, 230));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblFoto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelInfo = new JPanel(new GridLayout(4, 2, 10, 10));
        panelInfo.setOpaque(false);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font valueFont = new Font("Segoe UI", Font.BOLD, 13);

        panelInfo.add(crearLabel("Nombre:", labelFont));
        panelInfo.add(crearLabel("----", valueFont));
        panelInfo.add(crearLabel("Rol:", labelFont));
        panelInfo.add(crearLabel("----", valueFont));
        panelInfo.add(crearLabel("Estado:", labelFont));
        panelInfo.add(crearLabel("----", valueFont));
        panelInfo.add(crearLabel("Cédula:", labelFont));
        panelInfo.add(crearLabel("----", valueFont));

        lblEstado = new JLabel("ESPERANDO LECTURA", SwingConstants.CENTER);
        lblEstado.setOpaque(true);
        lblEstado.setBackground(new Color(220, 220, 220));
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblEstado.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));

        panelDatos.add(titulo);
        panelDatos.add(lblFoto);
        panelDatos.add(Box.createVerticalStrut(15));
        panelDatos.add(panelInfo);
        panelDatos.add(Box.createVerticalStrut(20));
        panelDatos.add(lblEstado);

        panelCentral.add(panelCamara, BorderLayout.WEST);
        panelCentral.add(panelDatos, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(colorPanel);

        JButton btnManual = new JButton("Registrar Entrada Manual");
        JButton btnLimpiar = new JButton("Limpiar Pantalla");
        JButton btnCerrar = new JButton("Cerrar Sesión");

        btnCerrar.setBackground(colorAcento);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);

        panelBotones.add(btnManual);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto, Font fuente) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(fuente);
        return lbl;
    }

    @Override
    public void mostrarEstado(String mensaje) {
        lblEstado.setText(mensaje);

        if (mensaje.toUpperCase().contains("CONCEDIDO")) {
            lblEstado.setBackground(new Color(0, 153, 76));
            lblEstado.setForeground(Color.WHITE);
        } else if (mensaje.toUpperCase().contains("DENEGADO")) {
            lblEstado.setBackground(new Color(204, 0, 0));
            lblEstado.setForeground(Color.WHITE);
        } else {
            lblEstado.setBackground(new Color(220, 220, 220));
            lblEstado.setForeground(Color.DARK_GRAY);
        }
    }

    @Override
    public void limpiarPantalla() {
        lblEstado.setText("ESPERANDO LECTURA");
        lblEstado.setBackground(new Color(220, 220, 220));
        lblEstado.setForeground(Color.DARK_GRAY);
    }
}
