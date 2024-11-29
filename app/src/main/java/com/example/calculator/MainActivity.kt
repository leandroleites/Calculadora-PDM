package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CalculatorScreen()
                }
            }
        }

    }
}

@Composable
fun CalculatorScreen() {
    var display by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") } // Variável para armazenar a expressão inteira

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display
        Text(
            text = display,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )

        // Buttons
        val buttons = listOf(
            listOf("C", "(", ")", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "⌫", "=")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                row.forEach { label ->
                    CalculatorButton(
                        label = label,
                        onClick = {
                            when {
                                label == "C" -> {
                                    display = "0"
                                    expression = ""
                                }
                                label == "⌫" -> {
                                    display = if (display.length > 1) display.dropLast(1) else "0"
                                }
                                label in listOf("+", "-", "×", "÷", "(", ")") -> {
                                    expression += label
                                    display = expression
                                }
                                label == "=" -> {
                                    // Quando pressionado o "=" chamamos a função para calcular a expressão
                                    display = evaluateExpression(expression)
                                    expression = display
                                }
                                else -> {
                                    // Adiciona números ou ponto na expressão
                                    display = if (display == "0") label else display + label
                                    expression = display
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .size(80.dp),
        shape = RoundedCornerShape(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
    ) {
        Text(
            text = label,
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

fun evaluateExpression(expression: String): String {
    return try {
        // Usando a exp4j para calcular a expressão
        val result = ExpressionBuilder(expression.replace("×", "*").replace("÷", "/"))
            .build()
            .evaluate()

        // Retorna o resultado como String
        result.toString()
    } catch (e: Exception) {
        // Caso haja erro na avaliação, retorna "Erro"
        "Erro"
    }
}
