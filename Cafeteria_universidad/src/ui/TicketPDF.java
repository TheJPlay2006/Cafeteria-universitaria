/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import dominio.DetalleVenta;
import dominio.Producto;
import dominio.Venta;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author Emesis
 */
public class TicketPDF {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void generarPDF(Venta venta, String rutaArchivo) {
        try {
            // Crear el documento PDF
            PdfWriter writer = new PdfWriter(rutaArchivo);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Fuente
            PdfFont fuenteNormal = PdfFontFactory.createFont();
            PdfFont fuenteNegrita = PdfFontFactory.createFont();

            // Título
            Paragraph titulo = new Paragraph("TICKET DE VENTA")
                    .setFont(fuenteNegrita)
                    .setFontSize(18)
                    .setFontColor(new DeviceRgb(0, 102, 204))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(titulo);

            // Línea separadora
            LineSeparator linea = new LineSeparator(new SolidLine())
                    .setMarginTop(5).setMarginBottom(10);
            document.add(linea);

            // Información general
            Table infoTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
            infoTable.addHeaderCell(crearCelda("ID de Venta:", true));
            infoTable.addCell(crearCelda(String.format("#%06d", venta.getId()), false));

            infoTable.addHeaderCell(crearCelda("Fecha:", true));
            infoTable.addCell(crearCelda(venta.getFechaHora().format(FORMATO_FECHA), false));

            infoTable.addHeaderCell(crearCelda("Cajero:", true));
            infoTable.addCell(crearCelda(venta.getUsuario().getNombreUsuario(), false));

            document.add(infoTable);
            document.add(new Paragraph(" ").setMarginBottom(10));

            // Detalles de productos
            Table tablaProductos = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(10);

            tablaProductos.addHeaderCell(crearCelda("Producto", true));
            tablaProductos.addHeaderCell(crearCelda("Precio", true));
            tablaProductos.addHeaderCell(crearCelda("Cant", true));
            tablaProductos.addHeaderCell(crearCelda("Total", true));

            for (DetalleVenta detalle : venta.getDetalles()) {
                Producto p = detalle.getProducto();
                double totalLinea = p.getPrecioUnitario() * detalle.getCantidad();

                tablaProductos.addCell(crearCelda(p.getNombre(), false));
                tablaProductos.addCell(crearCelda(String.format("%.2f", p.getPrecioUnitario()), false));
                tablaProductos.addCell(crearCelda(String.valueOf(detalle.getCantidad()), false));
                tablaProductos.addCell(crearCelda(String.format("%.2f", totalLinea), false));
            }
            document.add(tablaProductos);

            // Resumen
            Table resumen = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
            resumen.addCell(crearCelda("Subtotal:", true));
            resumen.addCell(crearCelda(String.format("$ %.2f", venta.getSubtotal()), false));

            resumen.addCell(crearCelda("IVA (7%):", true));
            resumen.addCell(crearCelda(String.format("$ %.2f", venta.getImpuestoIVA()), false));

            resumen.addCell(crearCelda("IVI (13%):", true));
            resumen.addCell(crearCelda(String.format("$ %.2f", venta.getImpuestoIVI()), false));

            resumen.addCell(crearCelda("Descuento:", true));
            resumen.addCell(crearCelda(String.format("$ %.2f", venta.getDescuento()), false));

            // Línea gruesa antes del total
            Cell celdaTotal = crearCelda("TOTAL:", true);
            celdaTotal.setFontColor(ColorConstants.RED)
                    .setFontSize(14);
            resumen.addCell(celdaTotal);

            Cell valorTotal = crearCelda(String.format("$ %.2f", venta.getTotal()), false);
            valorTotal.setFontColor(ColorConstants.RED)
                    .setFontSize(14)
                    .setBold();
            resumen.addCell(valorTotal);

            document.add(resumen);

            // Mensaje final
            Paragraph gracias = new Paragraph("¡Gracias por su compra!")
                    .setFont(fuenteNormal)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20)
                    .setItalic();
            document.add(gracias);

            // Cerrar
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage());
        }
    }

    private static Cell crearCelda(String contenido, boolean esCabecera) {
        Cell cell = new Cell()
                .add(new Paragraph(contenido))
                .setPadding(5)
                .setBorder(Border.NO_BORDER);
        if (esCabecera) {
            cell.setBold()
                .setFontColor(ColorConstants.DARK_GRAY)
                .setBackgroundColor(new DeviceRgb(240, 240, 240));
        }
        return cell;
    }
}
