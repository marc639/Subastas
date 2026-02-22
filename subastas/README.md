# API REST – Gestión de Subastas de Bienes Incautados

**Autor:** Marc Penades – DAM A  
**Tecnología:** Spring Boot 3.x + MySQL

---

## Descripción del Proyecto

API REST que gestiona el ciclo completo de subastas públicas de bienes incautados judicialmente:
registro del bien → creación de subasta → pujas → adjudicación → recursos legales.

## Problema de Negocio

La administración pública necesita transparencia y control en las subastas de bienes incautados, evitando errores en pujas y adjudicaciones. El sistema implementa estados irreversibles (ABIERTA → CERRADA → ADJUDICADA/DESIERTA) y validaciones económicas estrictas.

---

## Requisitos Previos

- Java 17+
- Maven
- MySQL en localhost:3306

## Instrucciones de Ejecución

1. Crear la base de datos en MySQL:
```sql
CREATE DATABASE SubastasDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Ajustar credenciales en `src/main/resources/application.properties` si es necesario.

3. Lanzar la aplicación:
```bash
mvn spring-boot:run
```

La API queda disponible en: `http://localhost:8080`

---

## Endpoints Principales

### Usuarios
| Método | URL | Descripción |
|--------|-----|-------------|
| POST | /api/usuario/create | Crear usuario |
| GET | /api/usuario/lista | Listar usuarios |
| GET | /api/usuario/detail/{id} | Ver usuario |
| PUT | /api/usuario/update/{id} | Actualizar usuario |
| DELETE | /api/usuario/delete/{id} | Eliminar usuario |

### Bienes
| Método | URL | Descripción |
|--------|-----|-------------|
| POST | /api/bien/create | Registrar bien incautado |
| GET | /api/bien/lista | Listar bienes |
| GET | /api/bien/detail/{id} | Ver bien |
| PUT | /api/bien/update/{id} | Actualizar bien |
| DELETE | /api/bien/delete/{id} | Eliminar bien |

### Subastas
| Método | URL | Descripción |
|--------|-----|-------------|
| POST | /api/subasta/create | Crear subasta |
| GET | /api/subasta/lista | Listar subastas |
| GET | /api/subasta/activas | Subastas en estado ABIERTA |
| GET | /api/subasta/detail/{id} | Ver subasta |
| PUT | /api/subasta/cerrar/{id} | Cerrar subasta |
| DELETE | /api/subasta/delete/{id} | Eliminar subasta |
| POST | /api/subasta/{id}/puja | Realizar puja |
| GET | /api/subasta/{id}/pujas | Ver historial de pujas |
| GET | /api/subasta/{id}/mejor-puja | Ver puja más alta |
| POST | /api/subasta/{id}/adjudicar | Adjudicar al mejor postor |
| POST | /api/subasta/{id}/desierta | Declarar subasta desierta |

### Adjudicaciones
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/adjudicacion/lista | Listar adjudicaciones |
| GET | /api/adjudicacion/detail/{id} | Ver adjudicación |

### Recursos Legales
| Método | URL | Descripción |
|--------|-----|-------------|
| POST | /api/recurso/create | Registrar recurso |
| GET | /api/recurso/lista | Listar recursos |
| GET | /api/recurso/detail/{id} | Ver recurso |
| PUT | /api/recurso/resolver/{id}?estado=RESUELTO | Resolver/desestimar recurso |

---

## Ejemplo de Uso con Postman

**1. Crear usuario:**
```json
POST /api/usuario/create
{
  "nombre": "Juan García",
  "email": "juan@correo.com",
  "nif": "12345678A"
}
```

**2. Registrar bien:**
```json
POST /api/bien/create
{
  "descripcion": "Vehículo BMW Serie 3 - 2020",
  "valorInicial": 25000.00
}
```

**3. Crear subasta:**
```json
POST /api/subasta/create
{
  "bienId": 1,
  "fechaInicio": "2025-06-01T10:00:00",
  "fechaFin": "2025-06-15T18:00:00",
  "precioBase": 15000.00
}
```

**4. Pujar:**
```json
POST /api/subasta/1/puja
{
  "usuarioId": 1,
  "importe": 16500.00
}
```

**5. Cerrar y adjudicar:**
```
PUT /api/subasta/cerrar/1
POST /api/subasta/1/adjudicar
```
