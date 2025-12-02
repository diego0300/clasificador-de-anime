# Sistema de Clasificación de Animé

**Trabajo Práctico Final - Paradigma Orientado a Objetos**
**Universidad Argentina de la Empresa (UADE)**

## Descripción del Proyecto
Aplicación de escritorio desarrollada en **Java (Swing)** que permite gestionar, clasificar y recomendar un catálogo personal de animé. El sistema administra tanto series como películas, gestiona listas personalizadas y genera estadísticas de consumo.

El diseño del software prioriza la calidad del código mediante la aplicación de **buenas prácticas**, patrones **GRASP** y principios **SOLID**, logrando una arquitectura desacoplada, mantenible y extensible.

## Características Principales

* **Gestión de Catálogo (CRUD):** Registro de Series (con capítulos) y Películas (con duración), edición unificada de detalles y eliminación segura con confirmación.
* **Polimorfismo:** Tratamiento unificado de `AnimeSerie` y `AnimePelicula` respetando el Principio de Sustitución de Liskov (LSP).
* **Búsqueda Avanzada (Strategy Pattern):** Filtros dinámicos combinables y extensibles (OCP):
    * Por Título (búsqueda parcial).
    * Por Género.
    * Por Estado.
    * Por Rango de Años.
    * Por Calificación Mínima.
* **Listas Personalizadas:** Creación de listas temáticas (ej. "Favoritos", "Para ver en vacaciones") y gestión de agregar/quitar animes.
* **Estadísticas y Métricas:** Visualización de promedios globales de calificación y conteo de animes por estado.
* **Recomendaciones Inteligentes:** Generación automática de un "Top 5" basado en mejores calificaciones y género seleccionado.
* **Persistencia de Datos:** Almacenamiento automático en archivos CSV (`animes.csv` y `listas.csv`).

## 🛠️ Requisitos del Sistema

* **Java Development Kit (JDK):** Versión **21** o superior.
* **Sistema Operativo:** Windows, macOS o Linux.

## ▶️ Instrucciones de Ejecución

### Opción 1: Ejecutar archivo JAR (Recomendado)
El proyecto se puede ejecutar directamente si dispone del archivo empaquetado (`.jar`), sin necesidad de abrir un IDE.

Ejecutando el siguiente comando: java -jar SistemaAnime.jar

## Arquitectura del Proyecto

El sistema sigue una arquitectura en capas estricta para separar responsabilidades:

    model: Contiene las entidades del dominio (Anime, AnimeSerie, AnimePelicula), Enums y excepciones personalizadas.

    service: Contiene la lógica de negocio (AnimeService) y las estrategias de filtrado (CriterioBusqueda). Aplica los patrones Controller y Creator de GRASP.

    repository: Maneja la persistencia en archivos (RepositorioAnimeArchivo, RepositorioListas). Aplica DIP (Dependency Inversion) mediante el uso de interfaces.

    ui: Interfaz gráfica en Swing (VentanaPrincipal, DialogoEditar). Desacoplada de la lógica de negocio.

# Notas sobre la Persistencia

Al ejecutar la aplicación, el sistema generará automáticamente dos archivos en el directorio raíz del proyecto:

    animes.csv: Base de datos de los animes.

    listas.csv: Base de datos de las listas personalizadas.

Si estos archivos no existen, se crearán vacíos. Si ya existen, el programa cargará los datos automáticamente.

## Autor

    Facundo Damián Lara - Legajo: 1176633
