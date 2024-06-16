package GDatos

interface IDatos {
    fun obtenerRA(): String?
    fun obtenerPorcentajeRA(): Double
    fun obtenerNotasRA(): MutableList<Double>
    fun obtenerCEs(): MutableList<String>
    fun obtenerPorcentajesCE(): MutableList<Double>
    fun notasCEAlumnos(): MutableList<MutableList<Double>>
    fun obtenerModulo(): String?
    fun almacenarIniciales(): MutableList<String>
    fun almacenarNombres(): MutableList<String>
}