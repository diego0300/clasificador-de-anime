package ui;

import model.EstadoAnime;
import model.GeneroAnime;
import service.AnimeService;

import javax.swing.*;
import java.awt.*;

public class DialogoEditar extends JDialog {
    private AnimeService servicio;
    private String tituloAnime;
    private boolean guardado = false; // Para saber si actualizar la tabla al salir

    private JTextField txtEstudio;
    private JComboBox<EstadoAnime> cmbEstado;
    private JSpinner spinnerCalificacion;
    private JLabel lblGeneros;

    public DialogoEditar(Frame parent, AnimeService servicio, String titulo, String estudioActual,
                         EstadoAnime estadoActual, int califActual, String generosActuales) {
        super(parent, "Editar: " + titulo, true); // true = Modal (bloquea la ventana de atrás)
        this.servicio = servicio;
        this.tituloAnime = titulo;

        configurarUI(estudioActual, estadoActual, califActual, generosActuales);
        pack(); // Ajusta el tamaño automáticamente al contenido
        setLocationRelativeTo(parent); // Centrar
    }

    private void configurarUI(String estudio, EstadoAnime estado, int calif, String generos) {
        setLayout(new BorderLayout(10, 10));

        // --- PANEL DE FORMULARIO (CENTRO) ---
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Estudio
        panelForm.add(new JLabel("Estudio:"));
        txtEstudio = new JTextField(estudio);
        panelForm.add(txtEstudio);

        // 2. Estado
        panelForm.add(new JLabel("Estado:"));
        cmbEstado = new JComboBox<>(EstadoAnime.values());
        cmbEstado.setSelectedItem(estado);
        panelForm.add(cmbEstado);

        // 3. Calificación
        panelForm.add(new JLabel("Calificación (0-5):"));
        // Spinner para números
        spinnerCalificacion = new JSpinner(new SpinnerNumberModel(calif, 0, 5, 1));
        panelForm.add(spinnerCalificacion);

        // 4. Géneros
        panelForm.add(new JLabel("Géneros:"));
        JPanel panelGeneros = new JPanel(new BorderLayout());
        lblGeneros = new JLabel(generos);
        JButton btnAddGenero = new JButton("+");

        panelGeneros.add(lblGeneros, BorderLayout.CENTER);
        panelGeneros.add(btnAddGenero, BorderLayout.EAST);
        panelForm.add(panelGeneros);

        add(panelForm, BorderLayout.CENTER);

        // --- PANEL DE BOTONES (ABAJO) ---
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        // --- ACCIONES ---

        // Botón Guardar
        btnGuardar.addActionListener(e -> {
            try {
                // 1. Estudio
                servicio.modificarEstudio(tituloAnime, txtEstudio.getText());

                // 2. Estado
                servicio.modificarEstado(tituloAnime, (EstadoAnime) cmbEstado.getSelectedItem());

                // 3. Calificación
                int nuevaCalif = (int) spinnerCalificacion.getValue();
                servicio.modificarCalificacion(tituloAnime, nuevaCalif);

                guardado = true; // Marcamos éxito
                dispose(); // Cerrar ventana

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
            }
        });

        // Botón Agregar Género
        btnAddGenero.addActionListener(e -> {
            GeneroAnime seleccionado = (GeneroAnime) JOptionPane.showInputDialog(
                    this, "Agregar:", "Género", JOptionPane.PLAIN_MESSAGE, null,
                    GeneroAnime.values(), GeneroAnime.values()[0]);

            if (seleccionado != null) {
                try {
                    servicio.agregarGenero(tituloAnime, seleccionado);
                    // Actualiza la etiqueta visualmente para que el usuario vea el cambio
                    lblGeneros.setText(lblGeneros.getText().replace("]", ", " + seleccionado + "]").replace("[]", "[" + seleccionado + "]"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    public boolean isGuardado() {
        return guardado;
    }
}