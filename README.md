# 🐾 PetServices — GA7-220501096-AA2-EV02

## Módulo implementado: Gestión de Clientes (CRUD)

Proyecto web Java con **Servlets + JSP** para la evidencia de competencia del SENA.
Implementa registro, inicio de sesión y gestión completa (CRUD) de clientes del sistema PetServices.

---

## 📁 Estructura del Proyecto

```
petservices/                              ← Raíz del proyecto Maven
│
├── pom.xml                               ← Configuración Maven (dependencias)
│
└── src/
    └── main/
        │
        ├── java/com/petservices/         ← Código fuente Java
        │   ├── modelo/
        │   │   └── Cliente.java          ← Clase de entidad (JavaBean)
        │   ├── dao/
        │   │   └── ClienteDAO.java       ← Capa de acceso a datos (simulada en RAM)
        │   └── servlet/
        │       ├── RegistroServlet.java  ← Controlador: /registro
        │       ├── LoginServlet.java     ← Controlador: /login
        │       ├── ClienteServlet.java   ← Controlador: /clientes (CRUD)
        │       └── LogoutServlet.java    ← Controlador: /logout
        │
        └── webapp/                       ← Recursos web (WEB-INF, JSPs, CSS)
            ├── index.jsp                 ← Redirección automática
            ├── vistas/
            │   ├── registro.jsp          ← Formulario de registro
            │   ├── login.jsp             ← Formulario de inicio de sesión
            │   └── clientes.jsp          ← Panel CRUD de gestión
            └── WEB-INF/
                └── web.xml               ← Descriptor de despliegue
```

---

## 🚀 Cómo ejecutar en Tomcat

### Opción A — Eclipse / IntelliJ con Tomcat integrado
1. Importar como **Maven Project**
2. Configurar un servidor **Apache Tomcat 9.x** en el IDE
3. Click derecho → **Run on Server**
4. Abrir: `http://localhost:8080/petservices/`

### Opción B — Maven + Tomcat manual
```bash
# Compilar y empacar
mvn clean package

# El archivo .war quedará en: target/petservices.war
# Copiarlo a: TOMCAT_HOME/webapps/petservices.war
# Iniciar Tomcat: TOMCAT_HOME/bin/startup.sh (Linux) o startup.bat (Windows)
```

### Cuentas de prueba (precargadas en memoria)
| Correo                    | Contraseña |
|---------------------------|-----------|
| alejandro@gmail.com       | 123456    |
| daniel@gmail.com          | 123456    |
| david@gmail.com           | 123456    |

---

## 🌐 URLs del módulo

| URL                                 | Descripción                      |
|-------------------------------------|----------------------------------|
| `http://localhost:8080/petservices/`        | Redirige al login automáticamente |
| `http://localhost:8080/petservices/login`   | Formulario de inicio de sesión   |
| `http://localhost:8080/petservices/registro`| Formulario de registro           |
| `http://localhost:8080/petservices/clientes`| Panel CRUD (requiere sesión)     |
| `http://localhost:8080/petservices/logout`  | Cierre de sesión                 |

---

## 🔄 Flujo de la aplicación

```
index.jsp
    ↓ (sin sesión)
login.jsp  ←──────────────── logout
    ↓ (doPost: validar)
LoginServlet
    ↓ (sesión creada)
ClienteServlet (doGet: listar)
    ↓
clientes.jsp
    ├── Crear  → doPost accion=crear
    ├── Editar → doGet  accion=editar → doPost accion=actualizar
    └── Borrar → doGet  accion=eliminar
```

---

## 📋 Métodos HTTP implementados

| Servlet           | doGet                              | doPost                         |
|-------------------|------------------------------------|-------------------------------|
| RegistroServlet   | Cargar formulario vacío            | Procesar y guardar nuevo cliente |
| LoginServlet      | Cargar formulario de login         | Validar credenciales, crear sesión |
| ClienteServlet    | Listar, cargar edición, eliminar   | Crear cliente, actualizar cliente |
| LogoutServlet     | Invalidar sesión, redirigir        | —                              |

---

## 🔧 Tecnologías usadas

- **Java 11** — Lógica de negocio y Servlets
- **javax.servlet 4.0** — API de Servlets
- **JSP 2.3** — Vistas dinámicas (scriptlets, expresiones)
- **Apache Tomcat 9.x** — Servidor de aplicaciones
- **Maven** — Gestión de dependencias
- **Simulación DAO en RAM** — Sin necesidad de BD para la demo

---

## 💾 Comandos Git esenciales

```bash
# 1. Inicializar repositorio en la carpeta del proyecto
cd petservices
git init

# 2. Configurar tu identidad (solo la primera vez)
git config user.name  "Tu Nombre"
git config user.email "tucorreo@gmail.com"

# 3. Crear archivo .gitignore para excluir archivos innecesarios
echo "target/
*.class
*.war
.classpath
.project
.settings/
.idea/
*.iml" > .gitignore

# 4. Agregar todos los archivos al área de staging
git add .

# 5. Primer commit — Módulo base
git commit -m "feat: módulo Gestión de Clientes - GA7-220501096-AA2-EV02

- Modelo Cliente (JavaBean)
- ClienteDAO con simulación en memoria (CRUD)
- RegistroServlet: doGet + doPost para registro
- LoginServlet: doGet + doPost con sesión HTTP
- ClienteServlet: CRUD completo (doGet/doPost)
- LogoutServlet: invalidación de sesión
- Vistas JSP: registro.jsp, login.jsp, clientes.jsp
- Elementos JSP: scriptlets, expresiones, atributos de sesión"

# 6. Crear repositorio en GitHub (hazlo desde github.com primero)
#    Luego enlazar el remoto:
git remote add origin https://github.com/TU_USUARIO/petservices-aa2-ev02.git

# 7. Subir a GitHub
git branch -M main
git push -u origin main

# ── Commits futuros (buenas prácticas) ──
# git add .
# git commit -m "fix: corrección de validación en formulario de registro"
# git push
```

---

## 📦 Formato de entrega SENA

Para generar la carpeta comprimida con el nombre correcto:

```
# Estructura interna del ZIP:
APELLIDONOMBRE_AA2_EV02/
├── petservices/          ← Código fuente completo del proyecto
├── capturas/             ← Screenshots del módulo funcionando en Tomcat
│   ├── 01_login.png
│   ├── 02_registro.png
│   ├── 03_lista_clientes.png
│   ├── 04_editar_cliente.png
│   └── 05_eliminar_cliente.png
└── README.md             ← Este archivo
```

```bash
# Comando para crear el ZIP con el nombre correcto (ajusta APELLIDONOMBRE):
# Ejemplo: PUERTOMEJIA_AA2_EV02.zip

cd ..
zip -r APELLIDONOMBRE_AA2_EV02.zip APELLIDONOMBRE_AA2_EV02/
```

> 💡 **Recuerda:** El nombre va en MAYÚSCULAS, primero el APELLIDO luego el NOMBRE, sin espacios ni tildes.
> Ejemplo: `PUERTOSALEJANDRO_AA2_EV02.zip`

---

## 👨‍💻 Datos del proyecto

- **Sistema:** PetServices / PetConnect
- **Ficha SENA:** 3186628
- **Evidencia:** GA7-220501096-AA2-EV02
- **Competencia:** Módulos de software codificados y probados
