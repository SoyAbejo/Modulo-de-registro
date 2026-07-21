# Documentación de la API REST — PetServices (PetConnect)

**Evidencia:** GA7-220501096-AA5-EV03 — Diseño y desarrollo de servicios web
**Proyecto formativo:** PetConnect (gestión de clientes, mascotas, citas, establecimientos, servicios, productos y pedidos)
**Base URL local:** `http://localhost:8080/petservices`

Todos los servicios reciben y devuelven **JSON** (`Content-Type: application/json`) y siguen
el estilo arquitectónico **REST**: un recurso por URL, verbos HTTP para las operaciones
(`GET`, `POST`, `PUT`, `DELETE`) y códigos de estado HTTP estándar en la respuesta.

Capa técnica: `HttpServlet` (Jakarta/Java Servlet 4.0) + `Gson` para (de)serialización JSON.
Persistencia: DAO en memoria (misma estrategia que la evidencia AA2-EV02), estructurado
para reemplazarse por JDBC contra el modelo físico `petconnect_completo.sql` sin cambiar
los servlets ni los modelos.

---

## Convenciones generales

| Código HTTP | Significado en esta API |
|---|---|
| `200 OK` | Operación de lectura, actualización o eliminación exitosa |
| `201 Created` | Recurso creado correctamente |
| `400 Bad Request` | Faltan campos obligatorios o el valor enviado no es válido |
| `401 Unauthorized` | Credenciales de login incorrectas |
| `404 Not Found` | El recurso solicitado (por id) no existe |
| `409 Conflict` | Violación de una regla de unicidad (ej. correo duplicado) |

Formato de error homogéneo:
```json
{ "error": "descripción del problema" }
```

---

## 1. Autenticación — `/api/auth`

### POST /api/auth/login
Valida credenciales y retorna el cliente autenticado.

**Body:**
```json
{ "correo": "alejandro@gmail.com", "contrasena": "123456" }
```
**Respuesta 200:**
```json
{ "idCliente": 1, "nombre": "Alejandro Puerto", "correo": "alejandro@gmail.com", "fechaRegistro": "2026-01-15" }
```
**Respuesta 401:** `{ "error": "Correo o contraseña incorrectos" }`

---

## 2. Clientes — `/api/clientes`

| Método | URL | Descripción |
|---|---|---|
| GET | `/api/clientes` | Lista todos los clientes |
| GET | `/api/clientes/{id}` | Obtiene un cliente por id |
| POST | `/api/clientes` | Crea un cliente |
| PUT | `/api/clientes/{id}` | Actualiza un cliente |
| DELETE | `/api/clientes/{id}` | Elimina un cliente |

**Body para POST:**
```json
{ "nombre": "Daniel Mejia", "correo": "daniel@gmail.com", "contrasena": "123456" }
```
Reglas: `nombre` y `correo` obligatorios. Si el correo ya existe, responde `409 Conflict`.

---

## 3. Mascotas — `/api/mascotas`

| Método | URL | Descripción |
|---|---|---|
| GET | `/api/mascotas` | Lista todas las mascotas |
| GET | `/api/mascotas/{id}` | Obtiene una mascota por id |
| GET | `/api/mascotas?idCliente={id}` | Lista las mascotas de un cliente |
| POST | `/api/mascotas` | Registra una mascota |
| PUT | `/api/mascotas/{id}` | Actualiza una mascota |
| DELETE | `/api/mascotas/{id}` | Elimina una mascota |

**Body para POST:**
```json
{ "raza": "Labrador", "nombre": "Max", "idCliente": 1 }
```
Reglas: `raza` y `nombre` obligatorios.

---

## 4. Establecimientos — `/api/establecimientos`

| Método | URL | Descripción |
|---|---|---|
| GET | `/api/establecimientos` | Lista todos los establecimientos |
| GET | `/api/establecimientos/{id}` | Obtiene un establecimiento por id |
| POST | `/api/establecimientos` | Crea un establecimiento |
| PUT | `/api/establecimientos/{id}` | Actualiza un establecimiento |
| DELETE | `/api/establecimientos/{id}` | Elimina un establecimiento |

**Body para POST:**
```json
{ "nombre": "Veterinaria Norte", "tipo": "veterinaria", "direccion": "Cra 5 #10-20 Tunja", "telefono": "3201234567" }
```
Reglas: `nombre` y `tipo` obligatorios. `tipo` corresponde a `petshop | veterinaria | mixto` (regla definida en el modelo físico).

---

## 5. Servicios — `/api/servicios`

| Método | URL | Descripción |
|---|---|---|
| GET | `/api/servicios` | Lista todos los tipos de servicio |
| GET | `/api/servicios/{id}` | Obtiene un servicio por id |
| POST | `/api/servicios` | Crea un servicio |
| PUT | `/api/servicios/{id}` | Actualiza un servicio |
| DELETE | `/api/servicios/{id}` | Elimina un servicio |

**Body para POST:**
```json
{ "tipo": "Consulta", "nombre": "Desparasitación" }
```
Reglas: `tipo` y `nombre` obligatorios.

---

## 6. Citas — `/api/citas`

| Método | URL | Descripción |
|---|---|---|
| GET | `/api/citas` | Lista todas las citas |
| GET | `/api/citas/{id}` | Obtiene una cita por id |
| GET | `/api/citas?estado={estado}` | Filtra citas por estado |
| POST | `/api/citas` | Agenda una cita |
| PUT | `/api/citas/{id}` | Actualiza fecha, hora o estado |
| DELETE | `/api/citas/{id}` | Cancela (elimina) una cita |

**Body para POST:**
```json
{ "fecha": "2026-08-01", "hora": "10:00:00", "idMascota": 1, "idEstablecimiento": 1 }
```
Reglas: `fecha` y `hora` obligatorios. `estado` solo admite `pendiente | confirmada | cancelada | completada`
(si se envía uno distinto, responde `400 Bad Request`). Si no se envía `estado`, se asigna `pendiente` por defecto.

---

## 7. Productos — `/api/productos`

| Método | URL | Descripción |
|---|---|---|
| GET | `/api/productos` | Lista todos los productos |
| GET | `/api/productos/{id}` | Obtiene un producto por id |
| GET | `/api/productos?stockMenorA={n}` | Lista productos con stock por debajo de `n` |
| POST | `/api/productos` | Crea un producto |
| PUT | `/api/productos/{id}` | Actualiza un producto |
| DELETE | `/api/productos/{id}` | Elimina un producto |

**Body para POST:**
```json
{ "nombre": "Collar antipulgas", "descripcion": "Talla M", "precio": 32000.0, "stock": 25, "idEstablecimiento": 2 }
```
Reglas: `nombre` obligatorio.

---

## 8. Pedidos — `/api/pedidos`

| Método | URL | Descripción |
|---|---|---|
| GET | `/api/pedidos` | Lista todos los pedidos |
| GET | `/api/pedidos/{id}` | Obtiene un pedido por id |
| POST | `/api/pedidos` | Crea un pedido |
| PUT | `/api/pedidos/{id}` | Actualiza estado, total o cantidad |
| DELETE | `/api/pedidos/{id}` | Elimina un pedido |

**Body para POST:**
```json
{ "total": 45000.0, "cantidad": 1, "idCliente": 1, "idEstablecimiento": 2 }
```
Reglas: `total` debe ser mayor que 0.

---

## Ejemplo de prueba con cURL

```bash
# Listar clientes
curl -X GET http://localhost:8080/petservices/api/clientes

# Crear una mascota
curl -X POST http://localhost:8080/petservices/api/mascotas \
     -H "Content-Type: application/json" \
     -d '{"raza":"Bulldog","nombre":"Toby","idCliente":1}'

# Confirmar una cita
curl -X PUT http://localhost:8080/petservices/api/citas/2 \
     -H "Content-Type: application/json" \
     -d '{"estado":"confirmada"}'

# Eliminar un producto
curl -X DELETE http://localhost:8080/petservices/api/productos/3
```

---

## Verificación de compilación

El código fue compilado con `javac` (JDK 21, API objetivo `javax.servlet` 4.0 + Gson 2.10.1)
sin errores ni advertencias, confirmando la corrección sintáctica y de tipos de los 8 servicios
REST y sus respectivos modelos y DAOs.
