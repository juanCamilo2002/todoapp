# Proyecto TODO App

## Descripción
Este proyecto es una aplicación de gestión de tareas con autenticación de usuarios, seguridad con JWT y diversas funcionalidades avanzadas.

---

## 🚀 Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3** (Spring Security, Spring Data JPA, Spring Validation)
- **PostgreSQL** (o cualquier base de datos compatible con JPA)
- **Maven** (gestión de dependencias)
- **JWT** (Json Web Token para autenticación)
- **Lombok** (reducción de código boilerplate)
- **MapStruct** (mapeo de DTOs)
- **Mockito** (pruebas unitarias)
- **Cloudinary** (almacenamiento de imágenes)

---

## 📌 Módulos y Funcionalidades

### ✅ Módulo Users
- CRUD completo (Create, Read, Update, Delete)
- Soft delete (campo `isActive` para desactivar usuarios sin eliminarlos)
- Validaciones con `@Valid` y uso de DTOs
- Mappers con MapStruct
- Filtros con paginación: `username`, `role`, `isActive`
- Global Exception Handler (`@ControllerAdvice`)
- Endpoint de perfil (usuario logueado)
- Reactivar usuario (`isActive = true`)
- DTOs: `UserRequestDTO`, `UserResponseDTO`, `UserUpdateDTO`, etc.
- Seguridad con Spring Security (`UserDetails` con roles)

### ✅ Módulo Auth
- Registro de usuario
- Login (JWT o autenticación básica según configuración)
- Recuperación de contraseña (`PasswordResetService` + tests unitarios con Mockito)
- Refrescar token JWT

### ✅ Módulo Tasks (Gestión de tareas)
- Crear estructura del módulo `tasks`
- Crear entidad `Task` (campos: título, descripción, estado, fecha, etc.)
- CRUD de tareas
- Validaciones y DTOs para tareas
- Relación `User → Task` (un usuario tiene muchas tareas)
- Filtros y paginación para tareas
- Soft delete de tareas (opcional)
- Acceso por usuario autenticado (cada usuario ve solo sus tareas)

### ✅ Extras
- Cambiar contraseña (desde perfil)
- Verificación por email / recuperación con token
- Subida de imagen de perfil
- Almacenamiento local y en Cloudinary (configurable por perfil)

---

## 🛠️ Instalación y Configuración

### 🔹 Requisitos
- Java 17+
- Spring Boot 3+
- PostgreSQL (o cualquier base de datos compatible con JPA)
- Maven

### 🔹 Configuración del Proyecto
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/usuario/todo-app.git
   cd todo-app
   ```
2. Configurar las variables de entorno en un archivo `.env` o en `application.properties`:

   #### 🔹 Perfil de Desarrollo (`dev`):
   ```properties
   spring.datasource.url=database-url
   spring.datasource.username=tu-username
   spring.datasource.password=tu-password
   spring.datasource.driverClassName=org.postgresql.Driver
   
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.show-sql=true
   
   jwt.secret=tu-secret-key
   
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=tu-email-test
   spring.mail.password=tu-password-email
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   
   
   # Almacenamiento local
   storage.type=local
   spring.servlet.multipart.max-file-size=1232131MB
   spring.servlet.multipart.max-request-size=3432432Mb
   
   spring.web.resources.static-locations=classpath:/static/,file:uploads/
   spring.mvc.static-path-pattern=/uploads/**
   ```

   #### 🔹 Perfil de Producción (`prod`):
   ```properties
    spring.datasource.url=database-url
   spring.datasource.username=tu-username
   spring.datasource.password=tu-password
   spring.datasource.driverClassName=org.postgresql.Driver
   
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.show-sql=true
   
   jwt.secret=tu-secret-key
   
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=tu-email-test
   spring.mail.password=tu-password-email
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   
   # Almacenamiento en Cloudinary
   storage.type=cloudinary
   cloudinary.cloud-name=cloud-name
   cloudinary.api-key=cloudinary-api-key
   cloudinary.api-secret=cloudinary-api-secret
   ```

3. Ejecutar el proyecto con Maven:
   ```bash
   mvn spring-boot:run
   ```

### 🔹 Endpoints Principales
- **Autenticación**
    - `POST /api/auth/register` → Registro de usuario
    - `POST /api/auth/login` → Iniciar sesión
    - `POST /api/auth/refresh` → Refrescar token
    - `POST /api/auth/forgot-password` → Recuperación de contraseña
    - `POST /api/auth/reset-password` → Restablecer contraseña

- **Usuarios**
    - `GET /api/users` → Listar usuarios (admin)
    - `GET /api/users/{id}` → Obtener usuario por ID
    - `PATCH /api/users/{id}` → Actualizar usuario
    - `DELETE /api/users/{id}` → Desactivar usuario (soft delete)

- **Tareas**
    - `GET /api/tasks` → Listar tareas del usuario autenticado
    - `POST /api/tasks` → Crear una nueva tarea
    - `PUT /api/tasks/{id}` → Actualizar tarea
    - `DELETE /api/tasks/{id}` → Eliminar tarea

---

## 📜 Licencia
Este proyecto está bajo la licencia MIT. ¡Siéntete libre de contribuir y mejorarlo! 🚀
