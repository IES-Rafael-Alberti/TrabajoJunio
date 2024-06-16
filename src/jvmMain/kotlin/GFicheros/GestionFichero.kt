package GFicheros

import java.io.File
import java.text.DecimalFormat

/**
 * Clase para gestionar la manipulación de archivos CSV relacionados con las notas de los estudiantes.
 */
class GestionFichero: IFichero {
    private val filePath = "src/jvmMain/kotlin/archivos/un1-PlanificaciónyDiario PRO - 2223 -v1 - 3EV Actividades_Instrumentos.csv"

    /**
     * Método para rellenar los datos en el archivo CSV con las notas calculadas.
     */
    override fun rellenarDatos(){
//        val file = File(filePath)
//        val lines = file.readLines().toMutableList()
        val lines = File(filePath).readLines().toMutableList()
        var line: String
        var columns: List<String>
        var celda: String
        val apartados: MutableList<String> = mutableListOf()
        val porcentajeApartados: MutableList<String> = mutableListOf()
        var saltoLinea = 0


        //For para empezar a comprobar apartados de las unidades empezando por la línea 5
        //También obtiene los porcentajes de cada apartado
        for (i in 4..31) {
            line = lines[i]
            columns = line.split(";")
            celda = columns[1]

            //Si la celda no está vacía, añade a la lista de apartados la letra al quitarle la parte de UDX. dividiendo el String por el '.'
            //y quedandose con lo que tiene a su derecha.
            //Además aprovecha que está en esa fila y coge el porcentaje con columns[5] porque siempre están en la columna 6
            if (celda != "") {
                apartados.add(celda.split(".").last())
                celda = columns[5]
                porcentajeApartados.add(celda)
            }
            //Cuando la celda está vacía, elimina las últimas que se añadieron a la lista porque serían la palabra RAX y una entrada vacía en los porcentajes
            //También prepara un valor para saltarse todas las líneas vacías para el siguiente apartado
            else {
                apartados.removeLast()
                porcentajeApartados.removeLast()
                saltoLinea = i
                break
            }

        }

        //For que continua donde se quedó el anterior para saltarse las celdas vacías
        for (i in saltoLinea..100) {
            line = lines[i]
            columns = line.split(";")
            celda = columns[1]

            if(celda == ""){
                saltoLinea++
            }
            else {
                break
            }
        }

        //Tras saltarse las líneas vacías, obtiene todos los apartados que componen los distintos porcentajes
        line = lines[saltoLinea]
        columns = line.split(";")
        var porcentaje1 = columns[6].trim().removeSuffix("%").replace(',','.').toDouble()/100
        var apartadosPorcentaje1 = columns[1].split(",")

        line = lines[saltoLinea+1]
        columns = line.split(";")
        var porcentaje2 = columns[6].removeSuffix("%").replace(',','.').toDouble()/100
        var apartadosPorcentaje2 = columns[1].split(",")

        line = lines[saltoLinea+2]
        columns = line.split(";")
        var porcentaje3 = columns[6].removeSuffix("%").replace(',','.').toDouble()/100
        var apartadosPorcentaje3 = columns[1].split(",")

        //Rellena el archivo con las notas
        var columna: Int
        var nota :Double

        var sumaNota :Double

        //For para iterar por todas las columnas empezando por la 8 columna
        for(i in 7..columns.size-1) {
            columna = i
            sumaNota = 0.0

            //Adquiere las tres notas del alumno, sustituye las ',' por '.' y las convierte en double para poder operar con ellas
            val nota1 = lines[saltoLinea].split(";")[columna].replace(',','.').toDouble()
            val nota2 = lines[saltoLinea+1].split(";")[columna].replace(',','.').toDouble()
            val nota3 = lines[saltoLinea+2].split(";")[columna].replace(',','.').toDouble()

            //For que itera por todas las líneas empezando por la 5
            for(j in 4..apartados.size+3) {
                nota = 0.0
                line = lines[j]
                columns = line.split(";").toMutableList()
                //Adquiere el porcentaje y lo prepara para operar con él
                val porcentaje = porcentajeApartados[j-4].trim().removeSuffix("%").replace(',','.').toDouble()/100
                //Adquiere el apartado para determinar el porcentaje de lo que cuenta la nota
                val apartadoAlum = 'a' + (j - 3) - 1

                //Realiza las operaciones de las notas según qué porcentajes se les debe ralizar
                if(apartadoAlum.toString() in apartadosPorcentaje1) {
                    nota += (nota1 * porcentaje1)
                }
                if(apartadoAlum.toString() in apartadosPorcentaje2) {
                    nota += (nota2 * porcentaje2)
                }
                if(apartadoAlum.toString() in apartadosPorcentaje3) {
                    nota += (nota3 * porcentaje3)
                }
                //Hace el último porcentaje
                nota *= porcentaje
                //Le da formato para que solo haya 4 decimales
                columns[columna] = DecimalFormat("#.####").format(nota)
                lines[j] = columns.joinToString(";")

                //Va sumando todas las notas
                sumaNota += nota

            }

            //Añade la nota final del alumno
            line = lines[2]
            columns = line.split(";").toMutableList()
            columns[columna] = DecimalFormat("#.##").format(sumaNota)
            lines[2] = columns.joinToString(";")


        }
        //Actualiza el archivo
        File(filePath).writeText(lines.joinToString("\n"))

    }
}