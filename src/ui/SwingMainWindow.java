package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
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
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import Controller.AccessController;
import DataAccess.DTO.UsuarioDTO;
//Ventana principal
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
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (controller != null) controller.stop();
            }
        });
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
        panelCamara.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Cámara en vivo")
        );

        lblCamara = new JLabel("Vista de cámara", SwingConstants.CENTER);
        lblCamara.setForeground(Color.WHITE);
        lblCamara.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelCamara.add(lblCamara, BorderLayout.CENTER);

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBackground(colorPanel);
        panelDatos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titulo = new JLabel("Datos del Usuario Escaneado");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(colorTitulo);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        lblFoto = new JLabel("SIN FOTO", SwingConstants.CENTER);
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
        lblValNombre = crearLabel("----", valueFont); 
        panelInfo.add(lblValNombre);

        panelInfo.add(crearLabel("Rol:", labelFont));
        lblValRol = crearLabel("----", valueFont);    
        panelInfo.add(lblValRol);

        panelInfo.add(crearLabel("Estado:", labelFont));
        lblValEstadoUsuario = crearLabel("----", valueFont); 
        panelInfo.add(lblValEstadoUsuario);

        panelInfo.add(crearLabel("Cédula:", labelFont));
        lblValCedula = crearLabel("----", valueFont); 
        panelInfo.add(lblValCedula);

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
        JButton btnExport = new JButton("Exportar CSV");
        JButton btnCerrar = new JButton("Cerrar Sesión");

        btnCerrar.setBackground(colorAcento);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);

        btnExport.addActionListener(e -> {
            if(controller != null) controller.exportCsvDefault();
        });
        btnLimpiar.addActionListener(e -> limpiarPantalla());
        btnCerrar.addActionListener(e -> {
            if (controller != null) controller.stop();
            dispose();
            System.exit(0);
        });

        panelBotones.add(btnManual);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnExport);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto, Font fuente) {
        JLabel lbl = new JLabel(texto);
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
            lblEstado.setBackground(new Color(0, 153, 76)); // Verde
            lblEstado.setForeground(Color.WHITE);
        } else if (mensaje.toUpperCase().contains("DENEGADO") || mensaje.toUpperCase().contains("ERROR")) {
            lblEstado.setBackground(new Color(204, 0, 0)); // Rojo
            lblEstado.setForeground(Color.WHITE);
        } else {
            lblEstado.setBackground(new Color(220, 220, 220)); // Gris
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
            String nombreCompleto = usuario.getPrimerNombre() + " " + usuario.getPrimerApellido();
            
            lblValNombre.setText(nombreCompleto);
            lblValRol.setText(nvl(usuario.getNombreRol()));
            lblValEstadoUsuario.setText(usuario.getEstado());
            lblValCedula.setText(usuario.getCedula());

            //Carga la foto
            setFoto(usuario.getFoto());

            mostrarEstado("ACCESO GARANTIZADO");
        }
    }

    @Override
    public void showDenied(String motivo, String codigoQR) {
        mostrarEstado("ACCESO DENEGADO: " + motivo);
        lblValNombre.setText("----");
        lblValRol.setText("----");
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
        
        // Si no existe tal cual viene en la BD, buscamos en la carpeta predefinida
        if (!f.exists()) {
            f = new File("fotos_usuarios", path);
        }

        if (f.exists()) {
            BufferedImage img = ImageIO.read(f);
            if (img != null) {
                // Ajustamos la imagen al tamaño del label (130x160)
                Image scaled = img.getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(scaled));
                lblFoto.setText("");
            } else {
                lblFoto.setText("FORMATO INV.");
            }
        } else {
            lblFoto.setIcon(null);
            lblFoto.setText("NO ENCONTRADA");
            // Imprime en consola para debug: ayuda a ver qué ruta está intentando leer
            System.out.println("No se encontró la foto en: " + f.getAbsolutePath());
        }
    } catch (Exception e) {
        lblFoto.setText("ERROR FOTO");
        lblFoto.setIcon(null);
        e.printStackTrace();
    }
}

private BufferedImage mirror(BufferedImage src) {
        // Obtenemos el tipo de imagen original
        int type = src.getType();
        
        // CORRECCIÓN: Si el tipo es 0 (TYPE_CUSTOM), forzamos a INT_ARGB para evitar el error
        if (type == 0) {
            type = BufferedImage.TYPE_INT_ARGB;
        }

        BufferedImage dest = new BufferedImage(
            src.getWidth(), 
            src.getHeight(), 
            type
        );

        Graphics2D g2d = dest.createGraphics();
        
        // Transformación para efecto espejo
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

