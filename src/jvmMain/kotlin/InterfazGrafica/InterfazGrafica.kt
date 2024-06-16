package InterfazGrafica

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

class InterfazGrafica {

    @Composable
    fun App() {
        MaterialTheme {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    // Primera columna
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        var studentInitial by remember { mutableStateOf("") }
                        var module by remember { mutableStateOf("") }
                        var ra by remember { mutableStateOf("") }

                        TextField(
                            value = studentInitial,
                            onValueChange = { studentInitial = it },
                            label = { Text("Inicial del datosTabla.Alumno") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        TextField(
                            value = module,
                            onValueChange = { module = it },
                            label = { Text("Módulo") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        TextField(
                            value = ra,
                            onValueChange = { ra = it },
                            label = { Text("RA") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Segunda columna
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        var studentName by remember { mutableStateOf("") }
                        var grade by remember { mutableStateOf("") }
                        var raPercentage by remember { mutableStateOf("") }

                        TextField(
                            value = studentName,
                            onValueChange = { studentName = it },
                            label = { Text("Nombre del datosTabla.Alumno") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        TextField(
                            value = grade,
                            onValueChange = { grade = it },
                            label = { Text("Nota") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        TextField(
                            value = raPercentage,
                            onValueChange = { raPercentage = it },
                            label = { Text("Porcentaje del RA") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Tercera columna
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Spacer(modifier = Modifier.height(128.dp)) // Espacio para alinear con los otros TextFields
                        var raGrade by remember { mutableStateOf("") }

                        TextField(
                            value = raGrade,
                            onValueChange = { raGrade = it },
                            label = { Text("Nota del RA") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        }

}

@Composable
@Preview
fun AppPreview() {
    InterfazGrafica().App()
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        AppPreview()
    }
}