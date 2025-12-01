package ui;

import model.Anime;
import model.AnimeSerie;
import model.EstadoAnime;
import service.AnimeService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private AnimeService servicio;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VentanaPrincipal(AnimeService servicio) {
        this.servicio = servicio;
        configurarUI();
    }

    private void configurarUI() {
        setTitle("Sistema de Clasificación de Animé");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. PANEL SUPERIOR (Botones) ---
        JPanel panelBotones = new JPanel();

        JButton btnListar = new JButton("Actualizar");
        JButton btnAgregar = new JButton("Nuevo Animé");
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setForeground(Color.RED);

        JButton btnEditar = new JButton("📝 Editar / Detalles");

        JButton btnFiltrar = new JButton("🔍 Filtrar");
        JButton btnListas = new JButton("📂 Listas");
        JButton btnOrdenar = new JButton("⇅ Ordenar");
        JButton btnStats = new JButton("📊 Estadísticas");
        JButton btnRecomendar = new JButton("💡 Recomendaciones");

        // 1. Lista desplegables
        JPopupMenu menuListas = new JPopupMenu();
        JPopupMenu menuFiltros = new JPopupMenu();
        JPopupMenu menuOrden = new JPopupMenu();

        // 2. Crea las opciones de las listas desplegables
        JMenuItem itemNueva = new JMenuItem("Crear Nueva Lista");
        JMenuItem itemAgregar = new JMenuItem("Agregar seleccionado a Lista...");
        JMenuItem itemQuitar = new JMenuItem("Quitar seleccionado de Lista...");
        JMenuItem itemVer = new JMenuItem("Ver contenido de Listas");

        JMenuItem itemGenero = new JMenuItem("Por Género");
        JMenuItem itemTitulo = new JMenuItem("Por Título");
        JMenuItem itemAnio = new JMenuItem("Por Rango de Años");
        JMenuItem itemLimpiar = new JMenuItem("Quitar Filtros");
        JMenuItem itemEstado = new JMenuItem("Por Estado");
        JMenuItem itemCalif = new JMenuItem("Por Calificación Mínima");

        JMenuItem itemOrdTitulo = new JMenuItem("Por Título (A-Z)");
        JMenuItem itemOrdCalif = new JMenuItem("Por Calificación (5-1)");
        JMenuItem itemOrdAnio = new JMenuItem("Por Año (Nuevo-Viejo)");

        // 3. Agregar las opciones a las listas desplegables
        menuListas.add(itemNueva);
        menuListas.addSeparator();
        menuListas.add(itemAgregar);
        menuListas.add(itemQuitar);
        menuListas.addSeparator();
        menuListas.add(itemVer);

        menuFiltros.add(itemGenero);
        menuFiltros.add(itemTitulo);
        menuFiltros.add(itemAnio);
        menuFiltros.add(itemEstado);
        menuFiltros.add(itemCalif);
        menuFiltros.addSeparator();
        menuFiltros.add(itemLimpiar);

        menuOrden.add(itemOrdTitulo);
        menuOrden.add(itemOrdCalif);
        menuOrden.add(itemOrdAnio);

        panelBotones.add(btnListar);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnFiltrar);
        panelBotones.add(btnListas);
        panelBotones.add(btnOrdenar);
        panelBotones.add(btnStats);
        panelBotones.add(btnRecomendar);

        add(panelBotones, BorderLayout.NORTH);

        // --- 2. TABLA CENTRAL ---
        String[] columnas = {"Título", "Año", "Estudio", "Tipo", "Estado", "Calificación", "Géneros", "Duración Total"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- 3. EVENTOS ---

        // Evento: Listar
        btnListar.addActionListener(e -> cargarTabla(null));

        // Evento: Agregar
        btnAgregar.addActionListener(e -> {
            // 1. Preguntar el tipo primero
            String[] opciones = {"Serie", "Película"};
            int seleccion = JOptionPane.showOptionDialog(
                    this,
                    "¿Qué tipo de animé desea registrar?",
                    "Nuevo Registro",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (seleccion == JOptionPane.CLOSED_OPTION) return;

            boolean esSerie = (seleccion == 0); // 0 es Serie, 1 es Película

            // 2. Prepara el formulario dinámico
            JTextField txtTitulo = new JTextField();
            JTextField txtAnio = new JTextField();
            JTextField txtEstudio = new JTextField();
            JTextField txtDatoExtra = new JTextField(); // Este campo cambia de significado

            JPanel panelForm = new JPanel(new GridLayout(4, 2, 5, 5));

            panelForm.add(new JLabel("Título:"));
            panelForm.add(txtTitulo);

            panelForm.add(new JLabel("Año:"));
            panelForm.add(txtAnio);

            panelForm.add(new JLabel("Estudio:"));
            panelForm.add(txtEstudio);

            // Cambia la etiqueta según lo que eligió
            if (esSerie) {
                panelForm.add(new JLabel("Cantidad de Capítulos:"));
            } else {
                panelForm.add(new JLabel("Duración (minutos):"));
            }
            panelForm.add(txtDatoExtra);

            // 3. Muestra el formulario
            int result = JOptionPane.showConfirmDialog(
                    this,
                    panelForm,
                    "Registrar nueva " + (esSerie ? "Serie" : "Película"),
                    JOptionPane.OK_CANCEL_OPTION
            );

            // 4. Procesa los datos si dio OK
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String titulo = txtTitulo.getText();
                    // Valida inputs numéricos
                    int anio = Integer.parseInt(txtAnio.getText());
                    String estudio = txtEstudio.getText();
                    if (estudio.trim().isEmpty()) estudio = "Desconocido";

                    int datoExtra = Integer.parseInt(txtDatoExtra.getText());

                    // 5. Llamada Polimórfica al Servicio
                    if (esSerie) {
                        servicio.registrarSerie(titulo, anio, estudio, datoExtra); // datoExtra = Caps
                    } else {
                        servicio.registrarPelicula(titulo, anio, estudio, datoExtra); // datoExtra = Duración
                    }

                    JOptionPane.showMessageDialog(this, "¡Registrado con éxito!");
                    cargarTabla(null); // Refrescar

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Error: Año y " + (esSerie ? "Capítulos" : "Duración") + " deben ser números enteros.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // Evento: eliminar
        btnEliminar.addActionListener(e -> {
            // 1. Verifica selección
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un animé para eliminar.");
                return;
            }

            // 2. Obtiene datos
            String titulo = (String) tabla.getValueAt(fila, 0);

            // 3. Pide confirmación
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro que deseas eliminar permanentemente a '" + titulo + "'?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    // 4. Llama al servicio
                    servicio.eliminarAnime(titulo);

                    // 5. Refresca la tabla
                    cargarTabla(null);

                    JOptionPane.showMessageDialog(this, "Animé eliminado correctamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
                }
            }
        });

        // Evento: editar
        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un animé para ver sus detalles.");
                return;
            }

            try {
                // 1. Obtiene datos de la tabla
                // Orden: 0:Título, 1:Año, 2:Estudio, 3:Tipo, 4:Estado, 5:Calif, 6:Géneros

                String titulo = (String) tabla.getValueAt(fila, 0);
                String estudio = (String) tabla.getValueAt(fila, 2);

                model.EstadoAnime estado = (model.EstadoAnime) tabla.getValueAt(fila, 4);

                int calif = (int) tabla.getValueAt(fila, 5);
                String generos = (String) tabla.getValueAt(fila, 6);

                // 2. Abre el diálogo
                DialogoEditar dialogo = new DialogoEditar(
                        this,       // Ventana padre
                        servicio,   // Servicio
                        titulo,     // Título
                        estudio,    // Estudio
                        estado,     // Estado
                        calif,      // Calificación
                        generos     // Géneros
                );

                dialogo.setVisible(true);

                // 3. Si se guardaron cambios, actualizar la tabla
                if (dialogo.isGuardado()) {
                    cargarTabla(null);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al abrir edición: " + ex.getMessage());
            }
        });

        // Crear nueva lista
        itemNueva.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre de la nueva lista:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                try {
                    servicio.crearListaPersonalizada(nombre);
                    JOptionPane.showMessageDialog(this, "Lista '" + nombre + "' creada con éxito.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // Agregar a la lista
        itemAgregar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un animé de la tabla primero.");
                return;
            }
            String titulo = (String) tabla.getValueAt(fila, 0);

            try {
                var listas = servicio.obtenerListas();
                if (listas.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No tiene listas. Cree una primero.");
                    return;
                }
                // Selector de listas
                String[] nombres = listas.stream().map(l -> l.getNombreLista()).toArray(String[]::new);
                String listaDestino = (String) JOptionPane.showInputDialog(this,
                        "Agregar '" + titulo + "' a:", "Agregar a Lista",
                        JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);

                if (listaDestino != null) {
                    servicio.agregarAnimeALista(listaDestino, titulo);
                    JOptionPane.showMessageDialog(this, "Agregado correctamente.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Quitar de lista
        itemQuitar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un animé primero.");
                return;
            }
            String titulo = (String) tabla.getValueAt(fila, 0);

            try {
                var listas = servicio.obtenerListas();
                if (listas.isEmpty()) return;

                String[] nombres = listas.stream().map(l -> l.getNombreLista()).toArray(String[]::new);
                String listaOrigen = (String) JOptionPane.showInputDialog(this,
                        "Quitar '" + titulo + "' de:", "Quitar de Lista",
                        JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);

                if (listaOrigen != null) {
                    servicio.quitarAnimeDeLista(listaOrigen, titulo);
                    JOptionPane.showMessageDialog(this, "Quitado correctamente.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Aviso: " + ex.getMessage());
            }
        });

        // Ver listas
        itemVer.addActionListener(e -> {
            try {
                var listas = servicio.obtenerListas();
                StringBuilder sb = new StringBuilder();
                if (listas.isEmpty()) sb.append("No hay listas creadas.");

                for (model.ListaPersonalizada l : listas) {
                    sb.append("📂 ").append(l.getNombreLista()).append(" (").append(l.getAnimes().size()).append(" animes):\n");
                    for (model.Anime a : l.getAnimes()) {
                        sb.append("   • ").append(a.getTitulo()).append("\n");
                    }
                    sb.append("\n");
                }

                JTextArea area = new JTextArea(sb.toString());
                area.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Mis Listas", JOptionPane.PLAIN_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // --- Conecta la lista desplegable al botón ---
        btnListas.addActionListener(e -> {
            menuListas.show(btnListas, 0, btnListas.getHeight());
        });

        btnFiltrar.addActionListener(e -> {
            menuFiltros.show(btnFiltrar, 0, btnFiltrar.getHeight());
        });

        btnOrdenar.addActionListener(e -> {
            menuOrden.show(btnOrdenar, 0, btnOrdenar.getHeight());
        });

        // A. POR TÍTULO
        itemOrdTitulo.addActionListener(e -> {
            try {
                java.util.List<model.Anime> listaOrdenada = servicio.listarOrdenadoPorTitulo();
                actualizarModelo(listaOrdenada);
                // Mensaje opcional (para confirmar visualmente)
                JOptionPane.showMessageDialog(this, "Lista ordenada alfabéticamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // B. POR CALIFICACIÓN
        itemOrdCalif.addActionListener(e -> {
            try {
                java.util.List<model.Anime> listaOrdenada = servicio.listarOrdenadoPorCalificacion();
                actualizarModelo(listaOrdenada);
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // C. POR AÑO
        itemOrdAnio.addActionListener(e -> {
            try {
                java.util.List<model.Anime> listaOrdenada = servicio.listarOrdenadoPorAnio();
                actualizarModelo(listaOrdenada);
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // A. POR GÉNERO
        itemGenero.addActionListener(e -> {
            model.GeneroAnime[] opciones = model.GeneroAnime.values();
            model.GeneroAnime seleccionado = (model.GeneroAnime) JOptionPane.showInputDialog(
                    this, "Seleccione género:", "Filtrar por Género",
                    JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (seleccionado != null) {
                aplicarFiltro(new service.FiltroGenero(seleccionado));
            }
        });

        // B. POR TÍTULO
        itemTitulo.addActionListener(e -> {
            String texto = JOptionPane.showInputDialog(this, "Ingrese el título a buscar:");
            if (texto != null && !texto.trim().isEmpty()) {
                aplicarFiltro(new service.FiltroTitulo(texto));
            }
        });

        // C. POR RANGO DE AÑOS
        itemAnio.addActionListener(e -> {
            JPanel panelFechas = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField txtDesde = new JTextField("1990");
            JTextField txtHasta = new JTextField("2025");

            panelFechas.add(new JLabel("Desde el año:"));
            panelFechas.add(txtDesde);
            panelFechas.add(new JLabel("Hasta el año:"));
            panelFechas.add(txtHasta);

            int result = JOptionPane.showConfirmDialog(this, panelFechas,
                    "Filtrar por Años", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int min = Integer.parseInt(txtDesde.getText());
                    int max = Integer.parseInt(txtHasta.getText());

                    aplicarFiltro(new service.FiltroAnio(min, max));

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor ingrese años válidos.");
                }
            }
        });

        // D. POR ESTADO
        itemEstado.addActionListener(e -> {
            // Usa el Enum para llenar la lista desplegable automáticamente
            model.EstadoAnime[] opciones = model.EstadoAnime.values();
            model.EstadoAnime seleccionado = (model.EstadoAnime) JOptionPane.showInputDialog(
                    this,
                    "Seleccione el estado a buscar:",
                    "Filtrar por Estado",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            if (seleccionado != null) {
                aplicarFiltro(new service.FiltroEstado(seleccionado));
            }
        });

        // E. POR CALIFICACIÓN MÍNIMA
        itemCalif.addActionListener(e -> {
            // Usa un spinner para asegurar que el usuario elija un número válido (1-5)
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(4, 1, 5, 1)); // Default: 4

            int result = JOptionPane.showConfirmDialog(
                    this,
                    spinner,
                    "Mostrar animes con calificación MAYOR o IGUAL a:",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                int min = (int) spinner.getValue();
                aplicarFiltro(new service.FiltroCalificacion(min));
            }
        });

        // F. LIMPIAR (Volver a listar todo)
        itemLimpiar.addActionListener(e -> cargarTabla(null));

        // Evento: mostrar estadísticas
        btnStats.addActionListener(e -> {
            try {
                // 1. Obtiene datos del servicio
                var mapaEstados = servicio.getEstadisticasPorEstado();
                double promedio = servicio.getPromedioCalificacion();

                // 2. Formatea el mensaje
                StringBuilder sb = new StringBuilder();
                sb.append("=== RESUMEN DEL CATÁLOGO ===\n\n");

                sb.append("⭐ Calificación Promedio Global: ")
                        .append(String.format("%.2f", promedio)) // Muestra solo 2 decimales
                        .append(" / 5.00\n\n");

                sb.append("📂 Cantidad por Estado:\n");

                // Recorre todos los estados posibles para mostrar incluso los que están en 0
                for (model.EstadoAnime estado : model.EstadoAnime.values()) {
                    // Si no hay datos, devuelve 0
                    long cantidad = mapaEstados.getOrDefault(estado, 0L);
                    sb.append(String.format("   • %-12s : %d\n", estado, cantidad));
                }

                // 3. Muestra ventana emergente
                JTextArea area = new JTextArea(sb.toString());
                area.setEditable(false);
                area.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fuente tipo código para que se alinee bien

                JOptionPane.showMessageDialog(this, area, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error calculando estadísticas: " + ex.getMessage());
            }
        });

        btnRecomendar.addActionListener(e -> {
            // 1. Pregunta género
            model.GeneroAnime[] generos = model.GeneroAnime.values();
            // Agrega una opción cualquiera al principio
            Object[] opciones = new Object[generos.length + 1];
            opciones[0] = "Cualquier Género";
            System.arraycopy(generos, 0, opciones, 1, generos.length);

            Object seleccion = JOptionPane.showInputDialog(
                    this, "Quiero ver el Top 5 de:", "Recomendador",
                    JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (seleccion != null) {
                try {
                    model.GeneroAnime generoFiltro = null;
                    if (seleccion instanceof model.GeneroAnime) {
                        generoFiltro = (model.GeneroAnime) seleccion;
                    }

                    // 2. Pedir al servicio
                    java.util.List<model.Anime> recomendados = servicio.generarRecomendacion(generoFiltro, 5);

                    // 3. Mostrar en la tabla
                    actualizarModelo(recomendados);

                    String mensaje = (recomendados.isEmpty())
                            ? "No hay suficientes datos para recomendar."
                            : "Top 5 mejor calificados";
                    JOptionPane.showMessageDialog(this, mensaje);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // --- Doble click para editar ---
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Si es doble click (clickCount == 2)
                if (e.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
                    btnEditar.doClick();
                }
            }
        });
    }

    // Método auxiliar para llenar la tabla
    private void cargarTabla(List<Anime> datosExternos) {
        try {
            List<Anime> lista = (datosExternos == null) ? servicio.obtenerTodos() : datosExternos;
            actualizarModelo(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + e.getMessage());
        }
    }

    private void actualizarModelo(List<Anime> lista) {
        modeloTabla.setRowCount(0); // Limpiar
        for (Anime a : lista) {
            String tipo = (a instanceof AnimeSerie) ? "Serie" : "Película";

            // Convierte el set de géneros a un string
            String generosStr = a.getGeneros().toString();

            Object[] fila = {
                    a.getTitulo(),
                    a.getAnioLanzamiento(),
                    a.getEstudio(),
                    tipo,
                    a.getEstado(),
                    a.getCalificacion(),
                    generosStr,
                    a.getDuracionTotal() + " min"
            };
            modeloTabla.addRow(fila);
        }
    }

    // Método helper para ejecutar cualquier estrategia de búsqueda
    private void aplicarFiltro(service.CriterioBusqueda criterio) {
        try {
            java.util.List<model.Anime> filtrados = servicio.filtrarAnimes(criterio);
            actualizarModelo(filtrados);

            if (filtrados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
            } else {
                JOptionPane.showMessageDialog(this, "Se encontraron " + filtrados.size() + " animes.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al filtrar: " + ex.getMessage());
        }
    }
}