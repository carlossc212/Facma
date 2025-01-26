package com.resma.facma.util

import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import com.resma.facma.entity.Invoice
import java.awt.Color
import java.io.FileOutputStream

class PDFGenerator{
    companion object{
        fun generateInvoice(invoice: Invoice){
            val document = Document(PageSize.A4)
            val writer = PdfWriter.getInstance(document, FileOutputStream("Factura_Ejemplo.pdf"))

            val terminos = Paragraph(
                """
        Términos y Condiciones:
            1. El pago debe realizarse antes de la fecha de vencimiento indicada en la factura.
            2. Para cualquier consulta, contacte con nuestro equipo en info@ejemplosa.com.
            3. Este documento está sujeto a la legislación vigente.
        """.trimIndent(), Font(Font.HELVETICA, 8f)
            )
            terminos.alignment = Element.ALIGN_JUSTIFIED
            val footer = HeaderFooter(terminos, false)
            footer.border = HeaderFooter.TOP
            footer.borderWidth = 0.5f
            footer.borderColor = Color.darkGray
            document.setFooter(footer)

            document.open()

            // Encabezado de la empresa
            val empresaNombre = Paragraph("Ejemplo S.A.", Font(Font.HELVETICA, 16f, Font.BOLD))
            empresaNombre.alignment = Element.ALIGN_LEFT

            val fields = Paragraph(
                """
        Dirección:
        Teléfono:
        CIF:
        E-mail:
        Página web:
        """.trimIndent(), Font(Font.HELVETICA, 10f, Font.BOLD)
            )

            val fieldsDetails = Paragraph(
                """
        Calle Falsa 123, Ciudad, País
        +34 600 123 456
        B12345678
        info@ejemplosa.com
        www.ejemplosa.com
        """.trimIndent(), Font(Font.HELVETICA, 10f)
            )

            val detailsTable = PdfPTable(2)
            detailsTable.widthPercentage = 100f
            detailsTable.setWidths(floatArrayOf(1f, 3f))

            detailsTable.addCell(PdfPCell(fields).apply {
                border = Rectangle.NO_BORDER
                extraParagraphSpace = 5f
            })
            detailsTable.addCell(PdfPCell(fieldsDetails).apply {
                border = Rectangle.NO_BORDER
                horizontalAlignment = Element.ALIGN_RIGHT
                extraParagraphSpace = 5f
            })

            val heading = PdfPTable(2)
            heading.widthPercentage = 100f
            heading.setWidths(floatArrayOf(1f, 1f))

            heading.addCell(PdfPCell(empresaNombre).apply { border = Rectangle.NO_BORDER })
            heading.addCell(PdfPCell(detailsTable).apply { border = Rectangle.NO_BORDER })
            heading.addCell(PdfPCell().apply {
                border = Rectangle.BOTTOM
                colspan = 2
                borderWidth = 0.6f
                borderColor = Color.BLACK
                paddingTop = 20f
            })

            heading.setSpacingAfter(25f)

            document.add(heading)


            // Datos del cliente y factura
            val clienteYFacturaTable = PdfPTable(3)
            clienteYFacturaTable.widthPercentage = 100f
            clienteYFacturaTable.setWidths(floatArrayOf(1f, 4f, 3f))

            val clienteInfo = Paragraph(
                """
        Juan Pérez
        Avenida Principal 456, Piso 2
        Ciudad, País
        Teléfono: +34 700 987 654
        """.trimIndent(), Font(Font.HELVETICA, 10f)
            )

            val facturaInfoHeader = Paragraph(
                """
        Factura N.º:
        Fecha de emisión:
        Fecha de vencimiento:
        """.trimIndent(), Font(Font.HELVETICA, 10f, Font.BOLD)
            )


            val facturaInfoContent = Paragraph(
                """
        2025-001
        26/01/2025
        10/02/2025
        """.trimIndent(), Font(Font.HELVETICA, 10f)
            )

            facturaInfoContent.alignment = Element.ALIGN_RIGHT

            val tableFacturaInfo = PdfPTable(2)
            tableFacturaInfo.widthPercentage = 100f
            tableFacturaInfo.addCell(PdfPCell(facturaInfoHeader).apply {
                border = Rectangle.NO_BORDER
                extraParagraphSpace = 5f
            })
            tableFacturaInfo.addCell(PdfPCell(facturaInfoContent).apply {
                border = Rectangle.NO_BORDER
                horizontalAlignment = Element.ALIGN_RIGHT
                extraParagraphSpace = 5f
            })
            tableFacturaInfo.setWidths(floatArrayOf(4f, 3f))

            clienteYFacturaTable.addCell(PdfPCell(Phrase("Factura a:", Font(Font.HELVETICA, 10f, Font.BOLD))).apply { border = Rectangle.NO_BORDER })
            clienteYFacturaTable.addCell(PdfPCell(clienteInfo).apply {
                border = Rectangle.NO_BORDER
                extraParagraphSpace = 5f
            })
            clienteYFacturaTable.addCell(PdfPCell(tableFacturaInfo).apply { border = Rectangle.NO_BORDER })

            clienteYFacturaTable.setSpacingAfter(25f)
            document.add(clienteYFacturaTable)
            document.add(Chunk.NEWLINE)

            // Tabla de detalles de la factura
            val detallesTable = PdfPTable(5)
            detallesTable.widthPercentage = 100f
            detallesTable.setWidths(floatArrayOf(4f, 1f, 2f, 1f, 2f))
            detallesTable.headerRows = 1

            val encabezados = listOf("Descripción", "Cantidad", "Precio Unitario", "IVA", "Total")
            for (encabezado in encabezados) {
                detallesTable.addCell(PdfPCell(Paragraph(encabezado, Font(Font.HELVETICA, 10f))).apply {
                    horizontalAlignment = Element.ALIGN_CENTER
                    verticalAlignment = Element.ALIGN_MIDDLE
                    backgroundColor = Color.LIGHT_GRAY
                    isUseDescender = true
                })
            }

            // Agregamos un ejemplo de producto o servicio
            val items = listOf(
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Consultoría en tecnología", "1", "750,00", "21%", "907,50"),
                listOf("Mantenimiento de sistemas", "1", "250,00", "21%", "302,50")
            )

            for (item in items) {
                for (dato in item) {
                    detallesTable.addCell(PdfPCell(Paragraph(dato, Font(Font.HELVETICA, 10f))).apply {
                        horizontalAlignment = Element.ALIGN_CENTER
                        verticalAlignment = Element.ALIGN_MIDDLE
                        isUseDescender = true
                    })
                }
            }

            detallesTable.setSpacingAfter(25f)
            document.add(detallesTable)
            document.add(Chunk.NEWLINE)

            // Totales
            val totalesTable = PdfPTable(2)
            totalesTable.widthPercentage = 50f
            totalesTable.horizontalAlignment = Element.ALIGN_RIGHT
            totalesTable.setWidths(floatArrayOf(1f, 1f))

            val totales = listOf(
                "Subtotal sin IVA" to "1.000,00",
                "IVA 21%" to "210,00",
                "Total" to "1.210,00"
            )

            for ((etiqueta, valor) in totales) {
                totalesTable.addCell(PdfPCell(Paragraph(etiqueta, Font(Font.HELVETICA, 10f))).apply {
                    border = Rectangle.NO_BORDER
                })
                totalesTable.addCell(PdfPCell(Paragraph(valor, Font(Font.HELVETICA, 10f))).apply {
                    border = Rectangle.NO_BORDER
                    horizontalAlignment = Element.ALIGN_RIGHT
                })
            }

            document.add(totalesTable)
            document.add(Chunk.NEWLINE)

            document.close()
        }
    }
}