# Master Martini – App de contenidos técnicos

MVP de una aplicación Android para **Master Martini Chile SPA**, comercializadora de insumos para alimentos. La app entrega videos técnicos (chocolatería, masas de hoja, cremas, etc.) a clientes y emprendedores de panaderías, pastelerías y negocios gastronómicos, y registra su perfil e intereses para futuras acciones de contacto.

Está pensada para usuarios con distintos niveles de manejo digital, incluidos emprendedores mayores de 50 años: botones grandes, textos claros y buen contraste.


## Integrantes

- Vicente Briones
- Matías González
- Luisana Rivero

## Funcionalidades

- **Login** con validación de correo y contraseña, y mensaje si las credenciales son incorrectas.
- **Registro** con nombre, correo, teléfono (+56), tipo de negocio y contraseña con confirmación.
- **Recuperar contraseña** (envío simulado).
- **Menú principal** con carrusel de destacados y accesos a las secciones. Recetas, Cursos y Eventos aparecen como "Próximamente".
- **Videos**: lista con miniaturas, buscador por nombre o categoría (ignora tildes y mayúsculas) y reproductor que soporta videos locales, streaming y YouTube.
- **Perfil**: datos del usuario, tipo de negocio, videos guardados y cierre de sesión.
- **Formularios validados por campo**: cada campo tiene ícono, borde rojo y mensaje de error. Los errores desaparecen al corregir el campo.
- **Animaciones** en los mensajes de error, en el botón "Guardar video" y en los cambios de contenido.
- **Recurso nativo**: vibración al guardar un video (permiso `VIBRATE`).
- El reproductor mantiene el video al girar la pantalla y se pausa cuando la app pasa a segundo plano.

## Tecnologías

- Kotlin + Jetpack Compose
- Material 3 con los colores de la marca
- Navigation Compose
- ViewModel + StateFlow (arquitectura MVVM)
- Media3 ExoPlayer para la reproducción de video

## Estructura del proyecto

```
app/src/main/java/com/example/mastermartini/
├── data/          Repositorios (usuarios, videos y videos guardados)
├── model/         Modelos de datos (Video, FuenteVideo)
├── navigation/    Rutas y NavHost de la app
├── ui/
│   ├── components/  Componentes reutilizables (campos de formulario, tarjetas de video)
│   ├── screens/     Pantallas (Login, Registro, Menú, Videos, Reproductor, Perfil)
│   └── theme/       Tema Material 3 y tipografía
├── validacion/    Reglas de validación de los formularios
└── viewmodel/     ViewModels y estados de cada pantalla
```

La validación está separada de la interfaz: las pantallas solo muestran el estado, el `AuthViewModel` controla los formularios y `ValidadorFormularios` contiene las reglas.

## Cómo ejecutar

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Vi-Briones/App-Duoc-apk.git
   ```
2. Abrir la carpeta en **Android Studio** y esperar la sincronización de Gradle.
3. Ejecutar en un emulador o dispositivo con **Android 7.0 (API 24) o superior** usando el botón **Run ▶**.
4. Para los videos de streaming y YouTube se necesita conexión a internet.

**Cuenta de prueba:**

| Correo | Contraseña |
|---|---|
| `usuario@mastermartini.cl` | `123456` |

También se puede crear una cuenta nueva desde **Crear una cuenta** e iniciar sesión con ella.

## Alcance del MVP

- Los usuarios y los videos guardados se mantienen en memoria mientras la app está abierta. No hay base de datos ni backend.
- Recetas, Cursos y Eventos quedan para una etapa posterior.
- La arquitectura por repositorios permite conectar a futuro una API o la plataforma web de Master Martini sin cambiar las pantallas.
