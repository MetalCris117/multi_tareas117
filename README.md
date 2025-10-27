Multi Tareas 117 (Prototipo JavaFX)
Prototipo de aplicación de escritorio para el proyecto "Multi Tareas 117". Esta aplicación está construida con JavaFX y utiliza un skin moderno inspirado en dashboards web como AdminLTE para la interfaz de usuario.

🚀 Tecnologías Utilizadas
Java 17

JavaFX 21

Maven (para la gestión de dependencias)

AtlantaFX (Tema PrimerDark) - La biblioteca que proporciona el "look & feel" moderno.

Ikonli - Para los iconos (FontAwesome).

ControlsFX - Para componentes de UI adicionales.

📋 Requisitos
Para poder compilar y ejecutar este proyecto, necesitarás tener instalado:

JDK 17 (o superior).

Apache Maven.

▶️ Cómo Ejecutar el Proyecto
Clona el repositorio en tu máquina local:

Bash

git clone https://github.com/MetalCris117/multi_tareas117.git
Navega a la carpeta raíz del proyecto:

Bash

cd multi_tareas117
Usa Maven para compilar y ejecutar la aplicación. El plugin de JavaFX se encargará de todo:

Bash

mvn clean javafx:run
✨ Características Actuales
El prototipo actual incluye el siguiente flujo:

Pantalla de Login: Una interfaz de inicio de sesión de pantalla dividida (Split-Screen).

Autenticación (Simulada): La lógica de login está conectada. Puedes usar las siguientes credenciales (sin base de datos por ahora):

Usuario: admin

Contraseña: 1234

Dashboard Principal: Una vez autenticado, se carga el esqueleto del dashboard principal, que incluye:

Tema oscuro (PrimerDark).

Cabecera (Header) con título.

Menú lateral (Sidebar) que se puede contraer y expandir con el "botón de hamburguesa".

Un pie de página (Footer) reutilizable.
