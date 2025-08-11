    package dominio;

    import java.util.List;
    import java.util.Objects;

    /**
     * Representa una venta realizada por un usuario.
     */
    public class Venta {
        private int id;
        private Usuario usuario;
        private java.time.LocalDateTime fechaHora;
        private double subtotal;
        private double impuestoIVA;  // 7%
        private double impuestoIVI;  // 13%
        private double descuento;
        private double total;
        private List<DetalleVenta> detalles;

        /**
         * Constructor completo para inicializar todos los atributos.
         */
        public Venta(int id, Usuario usuario, java.time.LocalDateTime fechaHora, double subtotal,
                     double impuestoIVA, double impuestoIVI, double descuento, double total,
                     List<DetalleVenta> detalles) {
            this.id = id;
            this.usuario = Objects.requireNonNull(usuario, "El usuario no puede ser nulo.");
            this.fechaHora = Objects.requireNonNull(fechaHora, "La fecha y hora no pueden ser nulas.");
            setSubtotal(subtotal);
            setImpuestoIVA(impuestoIVA);
            setImpuestoIVI(impuestoIVI);
            setDescuento(descuento);
            this.total = total;
            this.detalles = detalles;
        }

        /**
         * Constructor simplificado para crear una venta sin ID ni detalles.
         */
        public Venta(Usuario usuario, java.time.LocalDateTime fechaHora, double subtotal,
                     double impuestoIVA, double impuestoIVI, double descuento) {
            this(0, usuario, fechaHora, subtotal, impuestoIVA, impuestoIVI, descuento, 0, null);
        }

        // Getters
        public int getId() {
            return id;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public java.time.LocalDateTime getFechaHora() {
            return fechaHora;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public double getImpuestoIVA() {
            return impuestoIVA;
        }

        public double getImpuestoIVI() {
            return impuestoIVI;
        }

        public double getDescuento() {
            return descuento;
        }

        public double getTotal() {
            return total;
        }

        public List<DetalleVenta> getDetalles() {
            return detalles;
        }

        // Setters con validaciones
        public void setId(int id) {
            if (id < 0) {
                throw new IllegalArgumentException("El ID no puede ser negativo.");
            }
            this.id = id;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = Objects.requireNonNull(usuario, "El usuario no puede ser nulo.");
        }

        public void setFechaHora(java.time.LocalDateTime fechaHora) {
            this.fechaHora = Objects.requireNonNull(fechaHora, "La fecha y hora no pueden ser nulas.");
        }

        public void setSubtotal(double subtotal) {
            if (subtotal < 0) {
                throw new IllegalArgumentException("El subtotal no puede ser negativo.");
            }
            this.subtotal = subtotal;
            recalcularTotal();
        }

        public void setImpuestoIVA(double impuestoIVA) {
            if (impuestoIVA < 0) {
                throw new IllegalArgumentException("El impuesto IVA no puede ser negativo.");
            }
            this.impuestoIVA = impuestoIVA;
            recalcularTotal();
        }

        public void setImpuestoIVI(double impuestoIVI) {
            if (impuestoIVI < 0) {
                throw new IllegalArgumentException("El impuesto IVI no puede ser negativo.");
            }
            this.impuestoIVI = impuestoIVI;
            recalcularTotal();
        }

        public void setDescuento(double descuento) {
            if (descuento < 0) {
                throw new IllegalArgumentException("El descuento no puede ser negativo.");
            }
            if (descuento > subtotal) {
                throw new IllegalArgumentException("El descuento no puede ser mayor al subtotal.");
            }
            this.descuento = descuento;
            recalcularTotal();
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public void setDetalles(List<DetalleVenta> detalles) {
            this.detalles = detalles;
        }

        /**
         * Recalcula el total de la venta basado en el subtotal, impuestos y descuento.
         */
        private void recalcularTotal() {
            this.total = subtotal + impuestoIVA + impuestoIVI - descuento;
        }

        /**
         * Sobrescribe el método toString para facilitar la depuración.
         */
        @Override
        public String toString() {
            return "Venta{" +
                    "id=" + id +
                    ", usuario=" + usuario +
                    ", fechaHora=" + fechaHora +
                    ", subtotal=" + subtotal +
                    ", impuestoIVA=" + impuestoIVA +
                    ", impuestoIVI=" + impuestoIVI +
                    ", descuento=" + descuento +
                    ", total=" + total +
                    ", detalles=" + detalles +
                    '}';
        }
    }