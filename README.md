# 🏫 Sistema de Caja - Cafetería Universitaria

## 📋 Descripción del Proyecto

Sistema de punto de venta desarrollado en Java con interfaz gráfica Swing para gestionar las operaciones de una cafetería universitaria. El sistema incluye autenticación segura, gestión de productos, registro de ventas, calculadora integrada y generación de reportes.

## 👥 Colaboradores

- **Emesis Mairena Sevilla**
- **Jairo Herrera Romero**

## 🎯 Características Principales

### ✅ Funcionalidades Implementadas

- **🔐 Autenticación Segura**: Login con encriptación SHA-256
- **📦 Gestión de Productos**: CRUD completo con activación/desactivación
- **💰 Registro de Ventas**: Sistema completo de facturación con IVA (7%) e IVI (13%)
- **🧮 Calculadora Integrada**: Operaciones básicas + potencia y porcentaje
- **📊 Consultas y Reportes**: Ventas del día y productos más vendidos
- **🎫 Generación de Tickets**: Exportación en formato TXT/PDF
- **📝 Sistema de Logs**: Registro de eventos y errores en base de datos

## 🏗️ Arquitectura del Sistema

### Estructura de Paquetes

```
src/
├── main/
│   ├── Main.java                    # Punto de entrada de la aplicación
│   ├── datos/                       # Capa de persistencia
│   │   ├── ConexionBD.java
│   │   ├── RepositorioProducto.java
│   │   ├── RepositorioUsuario.java
│   │   └── RepositorioVenta.java
│   ├── dominio/                     # Entidades del modelo
│   │   ├── DetalleVenta.java
│   │   ├── Producto.java
│   │   ├── Usuario.java
│   │   └── Venta.java
│   ├── gui/                         # Interfaz gráfica
│   │   ├── gui_calcu.java
│   │   ├── gui_consulta_dia.java
│   │   ├── gui_gestion_productos.java
│   │   ├── gui_login.java
│   │   ├── gui_principal.java
│   │   └── gui_registro_venta.java
│   ├── infraestructura/             # Servicios de infraestructura
│   │   └── ConexionBD.java
│   ├── servicio/                    # Lógica de negocio
│   │   ├── ProductoServicio.java
│   │   └── VentaServicio.java
│   └── utilidades/                  # Utilidades y helpers
│       └── HashUtil.java
```

## 🛠️ Tecnologías Utilizadas

- **Java SE 11+**
- **Swing** - Interfaz gráfica de usuario
- **JDBC** - Conectividad con base de datos
- **SQL Server** - Sistema de gestión de base de datos
- **Apache Ant** - Herramienta de construcción y automatización
- **JUnit** - Pruebas unitarias
- **Git/GitHub** - Control de versiones

## 📊 Base de Datos

### Estructura de Tablas

```sql
-- Tabla de usuarios
USUARIOS(
    id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) UNIQUE NOT NULL,
    password_hash NVARCHAR(64) NOT NULL, -- SHA-256 hash
    rol NVARCHAR(20) NOT NULL CHECK (rol IN ('admin', 'cajero')),
    activo BIT DEFAULT 1,
    creado DATETIME DEFAULT GETDATE()
)

-- Tabla de productos
PRODUCTOS(
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario >= 0),
    activo BIT DEFAULT 1,
    creado DATETIME DEFAULT GETDATE()
)

-- Tabla de ventas (cabecera)
VENTAS(
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    fecha_hora DATETIME DEFAULT GETDATE(),
    subtotal DECIMAL(10,2) NOT NULL,
    impuestoIVA DECIMAL(10,2) NOT NULL, -- 7%
    impuestoIVI DECIMAL(10,2) NOT NULL, -- 13%
    descuento DECIMAL(10,2) DEFAULT 0,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USUARIOS(id)
)

-- Tabla de detalles de venta
DETALLES_VENTA(
    id INT IDENTITY(1,1) PRIMARY KEY,
    venta_id INT NOT NULL,
    product_id INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unit DECIMAL(10,2) NOT NULL,
    total_linea DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES VENTAS(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES PRODUCTOS(id)
)

-- Tabla de logs del sistema
LOGS(
    id INT IDENTITY(1,1) PRIMARY KEY,
    fecha_hora DATETIME DEFAULT GETDATE(),
    nivel NVARCHAR(10) NOT NULL CHECK (nivel IN ('INFO', 'ERROR', 'WARN')),
    evento NVARCHAR(100) NOT NULL,
    detalle NVARCHAR(500),
    stacktrace NVARCHAR(MAX) -- para errores
)
```

## ⚙️ Requisitos del Sistema

### Prerrequisitos

- **Java JDK 11** o superior
- **SQL Server 2019** o superior
- **Apache Ant 1.10+**
- **IDE** recomendado: IntelliJ IDEA, Eclipse o NetBeans

### Librerías Incluidas

- `mssql-jdbc` - Driver JDBC para SQL Server
- `junit` - Framework de pruebas unitarias
- `openpdf` - Generación de documentos PDF (opcional)

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/[usuario]/cafeteria-universitaria.git
cd cafeteria-universitaria
```

### 2. Configurar Base de Datos

```bash
# Conectar a SQL Server
sqlcmd -S localhost -E

# Crear base de datos ejecutando el script
sqlcmd -S localhost -E -i scripts/create_database.sql
```

### 3. Configurar Conexión

Editar el archivo de configuración de base de datos en:
```java
// Archivo: ConexionBD.java
private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=cafeteria_universitaria;integratedSecurity=true";
private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
```

### 4. Compilar y Ejecutar

#### Con Ant:
```bash
# Compilar el proyecto
ant compile

# Ejecutar la aplicación
ant run

# Generar JAR
ant jar
# El JAR se genera en dist/cafeteria-universitaria.jar

# Limpiar archivos compilados
ant clean
```

## 👨‍💻 Uso del Sistema

### Credenciales por Defecto

- **Usuario**: `admin` / **Contraseña**: `1234`
- **Usuario**: `cajero` / **Contraseña**: `pass`

> Las contraseñas están encriptadas en SHA-256 en la base de datos

### Flujo de Trabajo

1. **Iniciar Sesión**: Autenticación con credenciales encriptadas
2. **Gestionar Productos**: Crear, editar, activar/desactivar productos
3. **Registrar Ventas**: Agregar ítems, calcular impuestos y generar factura
4. **Consultar Reportes**: Ver ventas del día y estadísticas
5. **Usar Calculadora**: Herramienta integrada con operaciones avanzadas

## 🧪 Pruebas

```bash
# Compilar y ejecutar pruebas
ant test

# Generar reporte de pruebas
ant test-report
```

## 📁 Archivos de Configuración

### Archivos de Configuración

### Scripts SQL

- `scripts/create_database.sql` - Creación completa de BD con datos
- `scripts/backup.sql` - Respaldo completo de la BD

### Archivos de Build

- `build.xml` - Configuración principal de Ant
- `build.properties` - Properties del proyecto (opcional)
- `lib/` - Directorio con librerías JAR

## 🔧 Características Técnicas

### Patrones Implementados

- **MVC (Model-View-Controller)**: Separación clara de responsabilidades
- **Repository Pattern**: Abstracción de acceso a datos
- **Service Layer**: Encapsulación de lógica de negocio
- **DAO (Data Access Object)**: Abstracción de operaciones de BD

### Seguridad

- **Encriptación SHA-256** para contraseñas
- **PreparedStatements** para prevenir inyección SQL
- **Validación de entrada** en todos los formularios
- **Logs de seguridad** para auditoría

## 🐛 Solución de Problemas

### Errores Comunes

1. **Error de conexión a BD**: Verificar que SQL Server esté ejecutándose y la autenticación integrada esté habilitada
2. **ClassNotFoundException**: Asegurar que el driver JDBC de SQL Server esté en el classpath
3. **División por cero**: La calculadora maneja automáticamente este caso
4. **Violación de clave foránea**: Verificar integridad referencial antes de eliminar

### Logs del Sistema

Los logs se almacenan en:
- **Base de datos**: Tabla `LOGS`
- **Consola**: Output estándar durante desarrollo

## 📄 Licencia

Este proyecto es desarrollado con fines académicos para la Universidad Tecnológica Nacional (UTN).

## 🤝 Contribuciones

Este proyecto fue desarrollado como práctica académica. Para sugerencias o mejoras:

1. Fork del repositorio
2. Crear rama para feature (`git checkout -b feature/nueva-caracteristica`)
3. Commit de cambios (`git commit -am 'Agregar nueva característica'`)
4. Push a la rama (`git push origin feature/nueva-caracteristica`)
5. Crear Pull Request

---

**Desarrollado con ❤️ para UTN - Programación 1**
