# ms-turno

> Parte del proyecto **Sistema de Gestión de Turnos Médicos** — arquitectura de microservicios con Spring Boot, Spring Cloud, Eureka y Docker.

## Descripción

Microservicio central del sistema: gestiona los turnos médicos (creación, consulta, cambio de estado, cancelación) y expone su propio login/registro de usuarios internos del servicio.

**Es completamente independiente de los demás microservicios**: no realiza llamadas Feign ni depende de que `ms-medicos` (ni ningún otro microservicio) esté disponible para funcionar. Los datos del médico (nombre, especialidad) se reciben directamente en la petición del cliente. El registro en Eureka es opcional y no bloquea el arranque si Eureka no está disponible.

## Tecnologías

- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JJWT
- springdoc-openapi (Swagger)

## Configuración

- **Puerto:** `8086` (configurable vía variable de entorno `PORT`, requerido para desplegar en plataformas como Render)
- **Base de datos:** PostgreSQL — esquema `service_turno_db`
- **Paquete base:** `SistemasDeGestionDeTurnosMedico.msturno`
- **Registro en Eureka:** opcional (`ms-eureka`, puerto 8761) — desactivable con `EUREKA_ENABLED=false`
- **Dependencias de otros microservicios:** ninguna

### Variables de entorno

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `DB_URL` | URL JDBC de conexión a PostgreSQL | `jdbc:postgresql://localhost:5432/service_turno_db` |
| `DB_USERNAME` | Usuario de la base de datos | `postgres` |
| `DB_PASSWORD` | Contraseña de la base de datos | `Duoc.2026` |
| `EUREKA_URL` | URL del Eureka Server | `http://localhost:8761/eureka/` |
| `EUREKA_ENABLED` | Habilita/deshabilita el registro en Eureka | `true` |
| `PORT` | Puerto en el que expone el servicio | `8086` |

## Endpoints principales

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/v1/turnos` | Crea un turno |
| `GET` | `/api/v1/turnos` | Lista todos los turnos |
| `GET` | `/api/v1/turnos/{id}` | Obtiene un turno por id |
| `GET` | `/api/v1/turnos/fecha/{fecha}` | Turnos por fecha |
| `GET` | `/api/v1/turnos/medico/{medicoId}/fecha/{fecha}` | Turnos de un médico en una fecha |
| `GET` | `/api/v1/turnos/paciente/{dni}` | Turnos de un paciente |
| `GET` | `/api/v1/turnos/estado/{estado}` | Turnos por estado |
| `GET` | `/api/v1/turnos/especialidad/{especialidad}` | Turnos por especialidad |
| `GET` | `/api/v1/turnos/rango` | Turnos en un rango de fechas |
| `PUT` | `/api/v1/turnos/{id}` | Actualiza un turno |
| `PATCH` | `/api/v1/turnos/{id}/estado` | Cambia el estado de un turno |
| `PATCH` | `/api/v1/turnos/{id}/cancelar` | Cancela un turno |
| `DELETE` | `/api/v1/turnos/{id}` | Elimina un turno |
| `POST` | `/api/v1/auth/login` | Login de usuario del servicio de turnos |
| `POST` | `/api/v1/auth/register` | Registro de usuario del servicio de turnos |

## Documentación de la API

Una vez levantado el servicio, la documentación Swagger está disponible en:

```
http://localhost:8086/doc/swagger-ui.html
```

## Cómo ejecutar

### Con Docker Compose (recomendado para desarrollo)

Desde la raíz del proyecto (`Docker-compose-main`), junto al `docker-compose.yml`:

```bash
docker-compose up --build
```

Esto levanta este microservicio junto con su propia base de datos PostgreSQL (`db-turno`) y el resto del ecosistema (Eureka, Gateway, demás microservicios). No requiere que ningún otro microservicio esté levantado para que `ms-turno` funcione correctamente.

### De forma local

```bash
./mvnw spring-boot:run
```

Requiere una instancia de PostgreSQL local corriendo en `localhost:5432` con la base `service_turno_db` (o ajustar `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` como variables de entorno).

### Desplegado de forma independiente en Render.com

Como no depende de otros microservicios, `ms-turno` puede desplegarse solo, sin el resto del stack de `docker-compose`, conectado a una base de datos PostgreSQL administrada por Render:

1. Crea una base de datos **PostgreSQL** en Render (versión 16 recomendada, para igualar la del `docker-compose.yml`).
2. Crea un **Web Service** en Render con **Runtime: Docker**, apuntando a este directorio (usa el `Dockerfile` incluido).
3. Configura las siguientes variables de entorno en el Web Service:

   | Variable | Valor |
   |---|---|
   | `DB_URL` | `jdbc:postgresql://<host-de-render>:5432/<nombre-db>` (agregar `?sslmode=require` si usas la URL externa) |
   | `DB_USERNAME` | usuario provisto por Render |
   | `DB_PASSWORD` | contraseña provista por Render |
   | `EUREKA_ENABLED` | `false` |

   No definas `PORT` manualmente: Render lo inyecta automáticamente y la app ya lo lee de forma dinámica.
4. Usa la **URL interna** de la base de datos si el Web Service y la base están en la misma cuenta/región de Render (conexión más rápida y sin necesidad de `sslmode=require`); usa la **URL externa** con `?sslmode=require` si te conectas desde fuera.
5. Una vez desplegado, verifica en `https://<tu-servicio>.onrender.com/doc/swagger-ui.html` que el servicio y la conexión a la base funcionan correctamente.
