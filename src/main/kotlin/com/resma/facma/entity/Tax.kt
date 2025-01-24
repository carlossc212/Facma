package com.resma.facma.entity

class Tax private constructor(val rate: Double, val name: String, val description: String) {
    companion object {
        val IVA = Tax(
            rate = 21.0,
            name = "IVA",
            description = "Impuesto sobre el valor añadido (régimen general)"
        )
        val IPSI = Tax(
            rate = 4.0,
            name = "IPSI",
            description = "Impuesto sobre la Producción, los Servicios y la Importación en Ceuta y Melilla"
        )
        val IGIC = Tax(
            rate = 7.0,
            name = "IGIC",
            description = "Impuesto General Indirecto Canario"
        )
        val IRPF = Tax(
            rate = 15.0,
            name = "IRPF",
            description = "Impuesto sobre la Renta de las Personas Físicas (tipo reducido autónomos)"
        )
        val ITPAJD = Tax(
            rate = 1.5,
            name = "ITPAJD",
            description = "Impuesto sobre Transmisiones Patrimoniales y Actos Jurídicos Documentados"
        )
        val IE = Tax(
            rate = 0.0,
            name = "IE",
            description = "Impuesto Especiales (como alcohol, tabaco o hidrocarburos)"
        )
        val RA = Tax(
            rate = 0.0,
            name = "RA",
            description = "Regímenes aduaneros y otras tasas especiales"
        )
        val IGTECM = Tax(
            rate = 3.0,
            name = "IGTECM",
            description = "Impuesto sobre Grandes Establecimientos Comerciales en algunas CCAA"
        )
        val IECDPCAC = Tax(
            rate = 5.0,
            name = "IECDPCAC",
            description = "Impuesto sobre Determinados Depósitos en las Entidades de Crédito (algunas CCAA)"
        )
        val IIMAB = Tax(
            rate = 0.0,
            name = "IIMAB",
            description = "Impuesto sobre Instalaciones que Impacten el Medio Ambiente (Baleares)"
        )
        val ICIO = Tax(
            rate = 4.0,
            name = "ICIO",
            description = "Impuesto sobre Construcciones, Instalaciones y Obras"
        )
        val IMVDN = Tax(
            rate = 4.0,
            name = "IMVDN",
            description = "Impuesto sobre el Incremento de Valor de los Terrenos (Plusvalía municipal)"
        )
        val IMSN = Tax(
            rate = 1.5,
            name = "IMSN",
            description = "Impuesto sobre la Minería y otros recursos (Navarra)"
        )
        val IMPN = Tax(
            rate = 0.5,
            name = "IMPN",
            description = "Impuesto Medioambiental sobre Producción de Energía Nuclear"
        )
        val REIVA = Tax(
            rate = 5.2,
            name = "REIVA",
            description = "Régimen especial del recargo de equivalencia (aplicable al IVA)"
        )
        val REIGIC = Tax(
            rate = 0.7,
            name = "REIGIC",
            description = "Recargo especial del IGIC (Canarias)"
        )
        val IPCNGRR = Tax(
            rate = 7.0,
            name = "IPCNGRR",
            description = "Impuesto sobre la Producción de Combustible Nuclear Gastado y Residuos Radioactivos"
        )
        val IACNGRR = Tax(
            rate = 6.0,
            name = "IACNGRR",
            description = "Impuesto sobre el Almacenamiento de Combustible Nuclear y Residuos Radioactivos"
        )
        val ILTCAC = Tax(
            rate = 2.0,
            name = "ILTCAC",
            description = "Impuesto sobre Labores del Tabaco"
        )
        val IGFEI = Tax(
            rate = 7.0,
            name = "IGFEI",
            description = "Impuesto sobre el Gas Fluorado de Efecto Invernadero"
        )
        val IRNR = Tax(
            rate = 24.0,
            name = "IRNR",
            description = "Impuesto sobre la Renta de No Residentes"
        )
        val IS = Tax(
            rate = 25.0,
            name = "IS",
            description = "Impuesto sobre Sociedades"
        )
    }

    override fun toString(): String {
        return "$name ($description%)"
    }
}
