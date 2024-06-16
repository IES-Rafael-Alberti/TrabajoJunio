package GDatos

import datosTabla.Modulo
import java.io.File

/**
 * Clase para gestionar los datos de un archivo CSV.
 *
 * @property filePath Ruta del archivo CSV.
 */
class GestionDatos: IDatos {
    private val filePath = "src/jvmMain/kotlin/archivos/un1-PlanificaciónyDiario PRO - 2223 -v1 - 3EV Actividades_Instrumentos.csv"

    /**
     * Obtiene el RA (Resultado de Aprendizaje) de la tercera línea del archivo CSV.
     *
     * @return El valor en la segunda posición de la tercera línea o null si no se encuentra.
     */
    override fun obtenerRA(): String? {
        val file = File(filePath)
        val lines = file.readLines()

        // Verificar que exista al menos una tercera línea en el archivo
        if (lines.size >= 3) {
            // Obtener la tercera línea del archivo
            val raLine = lines[2]

            // Dividir la línea por el delimitador ;
            val columns = raLine.split(";")

            // Verificar que exista al menos una segunda posición en las columnas
            if (columns.size >= 2) {
                // Obtener el valor en la segunda posición (índice 1)
                return columns[1]  // Devolver el valor
            }
        }

        return null // Devolver null si no se encontró la línea o no tiene suficientes columnas
    }

    /**
     * Obtiene el porcentaje del RA de la tercera línea del archivo CSV.
     *
     * @return El porcentaje del RA como un valor Double.
     * @throws IllegalArgumentException Si el archivo no tiene suficientes líneas.
     * @throws Exception Si ocurre un error al obtener el porcentaje.
     */
    override fun obtenerPorcentajeRA(): Double {
        try {
            val file = File(filePath)
            val lines = file.readLines()

            // Verificar que el archivo tenga suficientes líneas
            if (lines.size < 2) {
                throw IllegalArgumentException("El archivo no tiene suficientes líneas para obtener el porcentaje del RA")
            }

            // Obtener el porcentaje del RA de la segunda línea
            val porcentajeString = lines[2].split(";")[6].replace(",", ".").replace("%", "").trim()
            return porcentajeString.toDouble()
        } catch (e: Exception) {
            println("Error al obtener el porcentaje del RA: ${e.message}")
            throw e
        }
    }

    /**
     * Obtiene las notas del RA desde la tercera línea del archivo CSV.
     *
     * @return Una lista de valores Double con las notas del RA.
     * @throws IllegalArgumentException Si el archivo está vacío o no tiene suficientes líneas.
     */
    override fun obtenerNotasRA(): MutableList<Double> {
        val file = File(filePath)
        val lines = file.readLines()

        // Verificar que el archivo no esté vacío
        if (lines.isEmpty()) {
            throw IllegalArgumentException("El archivo está vacío")
        }

        // Verificar que haya suficientes líneas para obtener las notas de RA
        if (lines.size < 3) {
            throw IllegalArgumentException("El archivo no tiene suficientes líneas para obtener las notas de RA")
        }

        // Obtener las notas de RA desde la tercera fila
        val elementos = lines[2].split(";")
        val notasRA = mutableListOf<Double>()

        // Iterar desde la posición 7 hasta el final del arreglo
        for (i in 7 until elementos.size) {
            val nota = elementos[i].replace(",", ".").toDouble()
            notasRA.add(nota)
        }

        return notasRA
    }


    /**
     * Obtiene los criterios de evaluación (CE) a partir de la línea 5 y columna 2 del archivo CSV.
     *
     * @return Una lista de criterios de evaluación (CE).
     * @throws IllegalArgumentException Si el archivo no tiene suficientes líneas.
     * @throws Exception Si ocurre un error al obtener los criterios.
     */
    override fun obtenerCEs(): MutableList<String> {
        try {
            val file = File(filePath)
            val lines = file.readLines()

            // Verificar que el archivo tenga suficientes líneas
            if (lines.size < 5) {
                throw IllegalArgumentException("El archivo no tiene suficientes líneas para obtener los criterios de evaluación")
            }

            // Obtener los criterios de evaluación (CE) a partir de la línea 5, columna 2
            val criterios = mutableListOf<String>()
            for (i in 4 until lines.size) {  // Empezamos desde la línea 5 (índice 4 en la lista)
                val elementos = lines[i].split(";")
                if (elementos.size > 1) {
                    val ce = elementos[1].trim()  // Columna 2, índice 1 en la lista dividida por ";"
                    if (ce.isNotBlank()) {
                        // Detenerse cuando se encuentra un espacio o separador
                        if (ce.contains(' ') || ce.contains(',')) {
                            break
                        }
                        criterios.add(ce)
                    }
                }
            }

            return criterios
        } catch (e: Exception) {
            println("Error al obtener los criterios de evaluación (CE): ${e.message}")
            throw e
        }
    }

    /**
     * Obtiene los porcentajes de los criterios de evaluación (CE) a partir de la línea 5, columna 5 del archivo CSV.
     *
     * @return Una lista de porcentajes de CE como valores Double.
     * @throws IllegalArgumentException Si el archivo no tiene suficientes líneas.
     * @throws Exception Si ocurre un error al obtener los porcentajes.
     */
    override fun obtenerPorcentajesCE(): MutableList<Double> {
        try {
            val file = File(filePath)
            val lines = file.readLines()

            // Verificar que el archivo tenga suficientes líneas
            if (lines.size < 5) {
                throw IllegalArgumentException("El archivo no tiene suficientes líneas para obtener los porcentajes de los CE")
            }

            // Obtener los porcentajes de los CE a partir de la línea 5, columna 5
            val porcentajesCE = mutableListOf<Double>()
            for (i in 4 until lines.size) {  // Empezamos desde la línea 5 (índice 4 en la lista)
                val elementos = lines[i].split(";")
                if (elementos.size > 4) {
                    val porcentajeStr = elementos[4].replace(",", ".").replace("%", "").trim()
                    if (porcentajeStr.isNotBlank()) {
                        porcentajesCE.add(porcentajeStr.toDouble())
                    }
                }
            }

            return porcentajesCE
        } catch (e: Exception) {
            println("Error al obtener los porcentajes de los criterios de evaluación (CE): ${e.message}")
            throw e
        }
    }

    /**
     * Obtiene las notas de CE (Criterios de Evaluación) de los alumnos desde el archivo CSV.
     *
     * @return Una lista de listas de notas de CE por alumno.
     * @throws IllegalArgumentException Si el archivo está vacío.
     */
    override fun notasCEAlumnos(): MutableList<MutableList<Double>> {
        val file = File(filePath)
        val lines = file.readLines()

        // Verificar que el archivo no esté vacío
        if (lines.isEmpty()) {
            throw IllegalArgumentException("El archivo está vacío")
        }

        // Inicializar listas para almacenar las notas de CE de cada alumno
        val notasCE = mutableListOf<MutableList<Double>>()

        // Determinar la cantidad de alumnos
        val numAlumnos = lines[4].split(";").size - 7

        // Inicializar las listas para cada alumno
        for (i in 0 until numAlumnos) {
            notasCE.add(mutableListOf())
        }

        // Leer las notas de CE por columnas
        var colIndex = 7
        while (colIndex < lines[4].split(";").size) {
            for (lineIndex in 4 until lines.size) {
                val elementos = lines[lineIndex].split(";")
                if (colIndex < elementos.size) {
                    val notaStr = elementos[colIndex]
                    if (notaStr.isBlank()) {
                        // Si encontramos una celda en blanco, salimos del bucle interno
                        break
                    }
                    val nota = notaStr.replace(",", ".").toDoubleOrNull()
                    if (nota != null) {
                        notasCE[colIndex - 7].add(nota)
                    }
                }
            }
            colIndex++
        }

        return notasCE
    }

    /**
     * Obtiene el módulo desde el nombre del archivo CSV.
     *
     * @return El identificador del módulo o null si no se encuentra.
     */
    override fun obtenerModulo(): String? {
        val file = filePath

        // Definir la expresión regular para capturar la parte variable entre "unX-PlanificaciónyDiario " y " - 2223 -v1 - 3EV Actividades_Instrumentos.csv"
        val pattern = Regex("""un\d+-PlanificaciónyDiario\s*(.*?)\s*- 2223 -v1 - 3EV Actividades_Instrumentos\.csv""", RegexOption.IGNORE_CASE)

        // Buscar el patrón en el nombre del archivo
        val matchResult = pattern.find(file)

        // Extraer la parte variable si se encontró el patrón
        var result = matchResult?.groupValues?.get(1)

        // Se le da valor al id del modulo
        var modulo= Modulo(result)

        //Devuelve el id del modulo
        if (result != null) {
            return modulo.id
        }

        return null
    }

    /**
     * Almacena las iniciales de los alumnos desde la primera línea del archivo CSV.
     *
     * @return Una lista de iniciales de los alumnos.
     * @throws IllegalArgumentException Si el archivo está vacío o la primera línea no tiene suficientes elementos.
     */
    override fun almacenarIniciales(): MutableList<String> {
        val file = File(filePath)
        val lines = file.readLines()

        //Lee el archivo CSV y verifica si está vacío
        if (lines.isEmpty()) {
            throw IllegalArgumentException("El archivo está vacío")
        }

        //Obtiene la primera línea y la divide por ;
        val primeraLinea = lines[0]
        val elementos = primeraLinea.split(";")

        // Verifica que la linea tiene al menos 8 posiciones
        if (elementos.size < 7) {
            throw IllegalArgumentException("La línea no tiene suficientes elementos")
        }

        //Extrae los elementos desde la posicion 8 y los pasa a una mutablelist
        val valoresExtraidos = elementos.subList(7, elementos.size).toMutableList()

        return valoresExtraidos
    }

    /**
     * Almacena los nombres de los alumnos desde la segunda línea del archivo CSV.
     *
     * @return Una lista de nombres de los alumnos.
     * @throws IllegalArgumentException Si el archivo está vacío o la segunda línea no tiene suficientes elementos.
     */
    override fun almacenarNombres(): MutableList<String> {
        val file = File(filePath)
        val lines = file.readLines()

        //Lee el archivo CSV y verifica si está vacío
        if (lines.isEmpty()) {
            throw IllegalArgumentException("El archivo está vacío")
        }

        //Obtiene la segunda línea y la divide por ;
        val primeraLinea = lines[1]
        val elementos = primeraLinea.split(";")

        // Verifica que la linea tiene al menos 8 posiciones
        if (elementos.size < 7) {
            throw IllegalArgumentException("La línea no tiene suficientes elementos")
        }

        //Extrae los elementos desde la posicion 8 y los pasa a una mutablelist
        val valoresExtraidos = elementos.subList(7, elementos.size).toMutableList()

        return valoresExtraidos
    }

    /**
     * Muestra los datos almacenados en el archivo CSV de manera formateada.
     */
    companion object {
        fun mostrarDatos(gestionDatos: GestionDatos) {
            val iniciales = gestionDatos.almacenarIniciales()
            val nombres = gestionDatos.almacenarNombres()
            val modulo = gestionDatos.obtenerModulo()
            val ra = gestionDatos.obtenerRA()
            val porcentajeRA = gestionDatos.obtenerPorcentajeRA()
            val notasRA = gestionDatos.obtenerNotasRA()
            val ces = gestionDatos.obtenerCEs()
            val porcentajesCE = gestionDatos.obtenerPorcentajesCE()
            val notasCEAlumnos = gestionDatos.notasCEAlumnos()

            for (i in iniciales.indices) {
                // Imprimir iniciales y nombre del alumno
                println("┌──────────────────────────────┬────────────────────────────────┐")
                println("│ ${iniciales[i].padEnd(28)} │ ${nombres[i].padEnd(30)} │")
                println("└──────────────────────────────┴────────────────────────────────┘")

                // Imprimir módulo
                println("┌──────────────────────────────┬────────────────────────────────┐")
                println("│ ${modulo?.padEnd(28) ?: "".padEnd(28)} │                                │")
                println("└──────────────────────────────┴────────────────────────────────┘")

                // Imprimir RA
                println("┌──────────────────────────────┬────────────────────┬───────────────────┐")
                println("│ ${ra?.padEnd(28) ?: "".padEnd(28)} │${porcentajeRA.toString().padEnd(20)}│${notasRA[i].toString().padEnd(17)}  │")
                println("└──────────────────────────────┴────────────────────┴───────────────────┘")

                // Imprimir Criterios de Evaluación
                for (j in ces.indices) {
                    println("┌──────────────────────────────┬────────────────────┬───────────────────┐")
                    println("│ ${ces[j].padEnd(28)} │${porcentajesCE[j].toString().padEnd(20)}│ ${notasCEAlumnos[i][j].toString().padEnd(17)} │")
                    println("└──────────────────────────────┴────────────────────┴───────────────────┘")
                    println()
                }
            }
        }
    }

}


