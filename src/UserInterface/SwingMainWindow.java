package UserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Controller.AccessController;
import DataAccess.DTO.UsuarioDTO;
import Infrastructure.Config.BNAppMSG;

public class SwingMainWindow extends JFrame implements UIContract {

    private static final long serialVersionUID = 1L;

    private JLabel lblEstado;
    private JLabel lblCamara;
    private JLabel lblFoto;

    private JLabel lblValNombre;
    private JLabel lblValRol;
    private JLabel lblValCedula;
    private JLabel lblValEstadoUsuario;

    private AccessController controller;

    public SwingMainWindow() {
        inicializarVentana();
        inicializarComponentes();
    }

    private void inicializarVentana() {
        setTitle("Sistema de Control de Acceso");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (controller != null) {
                    controller.stop();
                }
            }
        });
    }

    private void inicializarComponentes() {
        Color colorFondo = new Color(245, 247, 250);
        Color colorPanel = Color.WHITE;
        Color colorTitulo = new Color(33, 37, 41);
        Color colorAcento = new Color(0, 102, 204);

        // --- Contenedor Central (GridBagLayout para centrado absoluto) ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(colorFondo);

        // Contenedor Horizontal para agrupar Cámara y Datos
        JPanel containerDashboard = new JPanel();
        containerDashboard.setLayout(new BoxLayout(containerDashboard, BoxLayout.X_AXIS));
        containerDashboard.setOpaque(false);

        // --- Panel de Cámara ---
        JPanel panelCamara = new JPanel(new BorderLayout());
        panelCamara.setPreferredSize(new Dimension(480, 360));
        panelCamara.setBackground(Color.BLACK);
        panelCamara.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Cámara en vivo")
        );

        lblCamara = new JLabel("Vista de cámara", SwingConstants.CENTER);
        lblCamara.setForeground(Color.WHITE);
        lblCamara.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelCamara.add(lblCamara, BorderLayout.CENTER);

        // --- Panel de Datos ---
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBackground(colorPanel);
        panelDatos.setPreferredSize(new Dimension(380, 480));
        panelDatos.setMaximumSize(new Dimension(380, 480));
        panelDatos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel titulo = new JLabel("Datos del Usuario Escaneado");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(colorTitulo);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        lblFoto = new JLabel("SIN FOTO", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(150, 180));
        lblFoto.setMaximumSize(new Dimension(150, 180));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(new Color(235, 235, 235));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Grid para información (Etiqueta: Valor)
        JPanel panelInfo = new JPanel(new GridLayout(4, 2, 10, 15));
        panelInfo.setOpaque(false);
        panelInfo.setMaximumSize(new Dimension(330, 150));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font valueFont = new Font("Segoe UI", Font.BOLD, 14);

        panelInfo.add(crearLabel("Nombre:", labelFont, SwingConstants.RIGHT));
        lblValNombre = crearLabel("----", valueFont, SwingConstants.LEFT);
        panelInfo.add(lblValNombre);

        panelInfo.add(crearLabel("Rol:", labelFont, SwingConstants.RIGHT));
        lblValRol = crearLabel("----", valueFont, SwingConstants.LEFT);
        panelInfo.add(lblValRol);

        panelInfo.add(crearLabel("Estado:", labelFont, SwingConstants.RIGHT));
        lblValEstadoUsuario = crearLabel("----", valueFont, SwingConstants.LEFT);
        panelInfo.add(lblValEstadoUsuario);

        panelInfo.add(crearLabel("Cédula:", labelFont, SwingConstants.RIGHT));
        lblValCedula = crearLabel("----", valueFont, SwingConstants.LEFT);
        panelInfo.add(lblValCedula);

        lblEstado = new JLabel("ESPERANDO LECTURA", SwingConstants.CENTER);
        lblEstado.setOpaque(true);
        lblEstado.setBackground(new Color(220, 220, 220));
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblEstado.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEstado.setMaximumSize(new Dimension(330, 50));
        lblEstado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelDatos.add(titulo);
        panelDatos.add(lblFoto);
        panelDatos.add(Box.createVerticalStrut(20));
        panelDatos.add(panelInfo);
        panelDatos.add(Box.createVerticalStrut(25));
        panelDatos.add(lblEstado);

        // Ensamblado del dashboard centrado
        containerDashboard.add(panelCamara);
        containerDashboard.add(Box.createHorizontalStrut(30));
        containerDashboard.add(panelDatos);

        panelCentral.add(containerDashboard);
        add(panelCentral, BorderLayout.CENTER);

        // --- Panel de Botones (Inferior centrado) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        panelBotones.setBackground(colorPanel);
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton btnManual = new JButton("Registro Manual");
        JButton btnLimpiar = new JButton("Limpiar Pantalla");
        JButton btnExport = new JButton("Exportar CSV");
        JButton btnCerrar = new JButton("Cerrar Sesión");

        btnCerrar.setBackground(colorAcento);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);

        // Lógica de botones
        btnExport.addActionListener(e -> {
            if (controller != null) {
                controller.exportCsvDefault();
        
            }});
        btnLimpiar.addActionListener(e -> limpiarPantalla());

        btnCerrar.addActionListener(e -> {
            if (BNAppMSG.bnShowConfirmYesNo("¿Está seguro que desea cerrar la sesión y apagar la cámara?")) {
                if (controller != null) {
                    controller.stop();
                }
                dispose();
                System.exit(0);
            }
        });

        panelBotones.add(btnManual);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnExport);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto, Font fuente, int alineacion) {
        JLabel lbl = new JLabel(texto, alineacion);
        lbl.setFont(fuente);
        return lbl;
    }

    public void setController(AccessController controller) {
        this.controller = controller;
    }

    @Override
    public void mostrarEstado(String mensaje) {
        lblEstado.setText(mensaje);
        if (mensaje.toUpperCase().contains("GARANTIZADO") || mensaje.toUpperCase().contains("CONCEDIDO")) {
            lblEstado.setBackground(new Color(0, 153, 76));
            lblEstado.setForeground(Color.WHITE);
        } else if (mensaje.toUpperCase().contains("DENEGADO") || mensaje.toUpperCase().contains("ERROR")) {
            lblEstado.setBackground(new Color(204, 0, 0));
            lblEstado.setForeground(Color.WHITE);
        } else {
            lblEstado.setBackground(new Color(220, 220, 220));
            lblEstado.setForeground(Color.DARK_GRAY);
        }
    }

    @Override
    public void updateFrame(BufferedImage frame) {
        if (frame != null) {
            BufferedImage mirrored = mirror(frame);
            Image scaled = mirrored.getScaledInstance(lblCamara.getWidth(), lblCamara.getHeight(), Image.SCALE_SMOOTH);
            lblCamara.setIcon(new ImageIcon(scaled));
            lblCamara.setText("");
        }
    }

    @Override
    public void showGranted(UsuarioDTO usuario, String codigoQR) {
        if (usuario != null) {
            lblValNombre.setText(usuario.getPrimerNombre() + " " + usuario.getPrimerApellido());
            lblValRol.setText(nvl(usuario.getNombreRol()));
            lblValEstadoUsuario.setText(usuario.getEstado());
            lblValCedula.setText(usuario.getCedula());
            setFoto(usuario.getFoto());
            mostrarEstado("ACCESO GARANTIZADO");
        }
    }

    @Override
    public void showDenied(String motivo, String codigoQR) {
        mostrarEstado("ACCESO DENEGADO: " + motivo);
        lblValNombre.setText("----");
        lblValRol.setText("----");
        lblFoto.setIcon(null);
        lblFoto.setText("SIN FOTO");
    }

    @Override
    public void limpiarPantalla() {
        lblEstado.setText("ESPERANDO LECTURA");
        lblEstado.setBackground(new Color(220, 220, 220));
        lblEstado.setForeground(Color.DARK_GRAY);
        lblValNombre.setText("----");
        lblValRol.setText("----");
        lblValCedula.setText("----");
        lblValEstadoUsuario.setText("----");
        lblFoto.setIcon(null);
        lblFoto.setText("SIN FOTO");
    }

    private void setFoto(String path) {
        try {
            if (path == null || path.trim().isEmpty()) {
                lblFoto.setIcon(null);
                lblFoto.setText("SIN FOTO");
                return;
            }
            File f = new File(path);
            if (!f.exists()) {
                f = new File("fotos_usuarios", path);
            }

            if (f.exists()) {
                BufferedImage img = ImageIO.read(f);
                if (img != null) {
                    Image scaled = img.getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH);
                    lblFoto.setIcon(new ImageIcon(scaled));
                    lblFoto.setText("");
                } else {
                    lblFoto.setText("FORMATO INV.");
                }
            } else {
                lblFoto.setIcon(null);
                lblFoto.setText("NO ENCONTRADA");
            }
        } catch (Exception e) {
            lblFoto.setText("ERROR FOTO");
            lblFoto.setIcon(null);
        }
    }

    private BufferedImage mirror(BufferedImage src) {
        int type = src.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : src.getType();
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), type);
        Graphics2D g2d = dest.createGraphics();
        g2d.translate(src.getWidth(), 0);
        g2d.scale(-1, 1);
        g2d.drawImage(src, 0, 0, null);
        g2d.dispose();
        return dest;
    }

    private String nvl(String s) {
        return (s == null) ? "" : s;
    }
}
