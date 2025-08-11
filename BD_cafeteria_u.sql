
USE master;
GO

-- 1. Eliminar BD si ya existe
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'cafeteria_universitaria')
    DROP DATABASE cafeteria_universitaria;
GO

-- 2. Crear base de datos
CREATE DATABASE cafeteria_universitaria;
GO

-- 3. Usar la base de datos
USE cafeteria_universitaria;    
GO

-- 4. Crear tabla USUARIOS
CREATE TABLE USUARIOS (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) UNIQUE NOT NULL,
    password_hash NVARCHAR(64) NOT NULL, -- SHA-256 = 64 caracteres hex
    rol NVARCHAR(20) NOT NULL CHECK (rol IN ('admin', 'cajero')),
    activo BIT DEFAULT 1,
    creado DATETIME DEFAULT GETDATE()
);
GO

-- 5. Crear tabla PRODUCTOS
CREATE TABLE PRODUCTOS (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL CHECK (precio_unitario >= 0),
    activo BIT DEFAULT 1,
    creado DATETIME DEFAULT GETDATE()
);
GO

-- 6. Crear tabla VENTAS
CREATE TABLE VENTAS (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    fecha_hora DATETIME DEFAULT GETDATE(),
    subtotal DECIMAL(10, 2) NOT NULL,
    impuestoIVA DECIMAL(10, 2) NOT NULL,
    impuestoIVI DECIMAL(10, 2) NOT NULL,
    descuento DECIMAL(10, 2) DEFAULT 0,
    total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USUARIOS(id)
);
GO

-- 7. Crear tabla DETALLES_VENTA
CREATE TABLE DETALLES_VENTA (
    id INT IDENTITY(1,1) PRIMARY KEY,
    venta_id INT NOT NULL,
    product_id INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unit DECIMAL(10, 2) NOT NULL,
    total_linea DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES VENTAS(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES PRODUCTOS(id)
);
GO

-- 8. Crear tabla LOGS
CREATE TABLE LOGS (
    id INT IDENTITY(1,1) PRIMARY KEY,
    fecha_hora DATETIME DEFAULT GETDATE(),
    nivel NVARCHAR(10) NOT NULL CHECK (nivel IN ('INFO', 'ERROR', 'WARN')),
    evento NVARCHAR(100) NOT NULL,
    detalle NVARCHAR(500),
    stacktrace NVARCHAR(MAX) -- para errores
);
GO

-- 9. Insertar datos de prueba

-- Usuarios: admin / 1234  y  cajero / pass
-- Contraseñas en SHA-256 (convertidas de texto plano "1234" y "pass")
INSERT INTO USUARIOS (username, password_hash, rol, activo) VALUES
('admin', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'admin', 1),
('cajero', '5b92e7d9e4a77a27d305180625978e8454893e888d5e3d8c3c762e906a9e1994', 'cajero', 1);
-- Hash de "1234" y "pass" generados con SHA-256

-- Productos de prueba
INSERT INTO PRODUCTOS (nombre, precio_unitario, activo) VALUES
('Café Americano', 1.50, 1),
('Capuchino', 2.50, 1),
('Té Verde', 1.20, 1),
('Sándwich de Jamón y Queso', 3.00, 1),
('Galletas (paquete)', 0.80, 1),
('Jugo Natural', 2.00, 1);

INSERT INTO LOGS (nivel, evento, detalle) VALUES
('INFO', 'Sistema iniciado', 'Aplicación de cafetería iniciada'),
('INFO', 'Login exitoso', 'Usuario: admin, ID: 1'),
('ERROR', 'Login fallido', 'Intento de acceso con usuario: hacker');

-- 10. Confirmación final
PRINT '✅ BASE DE DATOS "cafeteria_universitaria" CREADA CON ÉXITO';
PRINT '➡️  Usuarios: admin / 1234 | cajero / pass';
PRINT '➡️  Tablas creadas: USUARIOS, PRODUCTOS, VENTAS, DETALLES_VENTA, LOGS';
PRINT '➡️  Usa autenticación integrada en Java: integratedSecurity=true';

-- Fin del script

USE cafeteria_universitaria;
GO

-- Insertar múltiples productos de ejemplo
INSERT INTO PRODUCTOS (nombre, precio_unitario, activo, creado) VALUES
('Café Americano', 1.50, 1, GETDATE()),
('Capuchino', 2.50, 1, GETDATE()),
('Té Verde', 1.20, 1, GETDATE()),
('Sándwich de Jamón y Queso', 3.00, 1, GETDATE()),
('Galletas (paquete)', 0.80, 1, GETDATE()),
('Jugo Natural', 2.00, 1, GETDATE()),
('Muffin', 1.80, 1, GETDATE()),
('Agua Mineral', 1.00, 1, GETDATE()),
('Croissant', 2.20, 1, GETDATE()),
('Yogur', 2.40, 1, GETDATE()),
('Bagel con Queso', 3.50, 1, GETDATE()),
('Batido de Frutas', 3.00, 1, GETDATE()),
('Tostado de Pollo', 3.20, 1, GETDATE()),
('Ensalada César', 4.00, 1, GETDATE()),
('Sopa del Día', 2.80, 1, GETDATE());

PRINT '✅ 15 productos insertados con éxito en la tabla PRODUCTOS';


USE cafeteria_universitaria;
SELECT id, nombre, precio_unitario FROM PRODUCTOS ORDER BY id ASC;