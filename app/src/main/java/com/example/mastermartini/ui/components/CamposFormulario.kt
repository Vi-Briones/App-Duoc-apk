package com.example.mastermartini.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Rojo de error con buen contraste sobre fondo claro y su versión clara para el fondo verde
private val RojoError = Color(0xFFB3261E)
private val RojoErrorClaro = Color(0xFFFFB4AB)

/**
 * Colores comunes de los campos. Fondo blanco siempre; el borde se pone dorado/verde
 * al escribir y rojo si hay error. fondoOscuro = true para el login (fondo verde).
 */
@Composable
private fun coloresCampo(fondoOscuro: Boolean): TextFieldColors {
    val rojoBorde = if (fondoOscuro) RojoErrorClaro else RojoError
    return OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        errorContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        errorTextColor = Color.Black,
        focusedBorderColor = if (fondoOscuro) ColoresMarca.Dorado else ColoresMarca.Verde,
        unfocusedBorderColor = if (fondoOscuro) Color.Transparent else Color.LightGray,
        errorBorderColor = rojoBorde,
        cursorColor = ColoresMarca.Verde,
        errorCursorColor = RojoError,
        focusedSupportingTextColor = if (fondoOscuro) Color.LightGray else ColoresMarca.TextoSecundario,
        unfocusedSupportingTextColor = if (fondoOscuro) Color.LightGray else ColoresMarca.TextoSecundario,
        errorSupportingTextColor = rojoBorde,
        focusedPrefixColor = Color.Black,
        unfocusedPrefixColor = Color.Black,
        errorPrefixColor = Color.Black
    )
}

/** Etiqueta en negrita sobre el campo (se lee mejor que un texto gris dentro del campo). */
@Composable
private fun EtiquetaCampo(texto: String, fondoOscuro: Boolean) {
    Text(
        text = texto,
        color = if (fondoOscuro) Color.White else ColoresMarca.TextoPrincipal,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

/** Mensaje bajo el campo: el error si hay, o una ayuda (ej: reglas de la contraseña). */
private fun textoDeApoyo(error: String?, ayuda: String?): (@Composable () -> Unit)? {
    val texto = error ?: ayuda ?: return null
    return { Text(texto, fontSize = 14.sp) }
}

/**
 * Campo de formulario reutilizable: etiqueta arriba, ícono a la izquierda y,
 * si hay error, borde rojo + ícono de alerta + mensaje debajo del campo.
 * Con esPassword = true agrega el ojo para mostrar/ocultar la contraseña.
 */
@Composable
fun CampoFormulario(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    error: String?,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    ayuda: String? = null,
    prefijo: String? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    mayusculas: KeyboardCapitalization = KeyboardCapitalization.None,
    accionTeclado: ImeAction = ImeAction.Next,
    onAccionTeclado: () -> Unit = {},
    esPassword: Boolean = false,
    fondoOscuro: Boolean = false
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        EtiquetaCampo(etiqueta, fondoOscuro)
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            placeholder = placeholder?.let { { Text(it, color = Color.Gray) } },
            prefix = prefijo?.let { { Text(it) } },
            leadingIcon = {
                Icon(
                    imageVector = icono,
                    contentDescription = null, // la etiqueta ya dice qué es
                    tint = if (error != null) RojoError else ColoresMarca.Verde
                )
            },
            trailingIcon = when {
                esPassword -> {
                    {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = if (error != null) RojoError else Color.Gray
                            )
                        }
                    }
                }
                error != null -> {
                    { Icon(Icons.Filled.Error, contentDescription = "Error", tint = RojoError) }
                }
                else -> null
            },
            isError = error != null,
            supportingText = textoDeApoyo(error, ayuda),
            singleLine = true,
            visualTransformation = if (esPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (esPassword) KeyboardType.Password else tipoTeclado,
                capitalization = mayusculas,
                imeAction = accionTeclado
            ),
            keyboardActions = KeyboardActions(onDone = { onAccionTeclado() }),
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            colors = coloresCampo(fondoOscuro),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** Lista desplegable con el mismo estilo que CampoFormulario (ej: tipo de negocio). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorFormulario(
    valor: String,
    opciones: List<String>,
    onSeleccionar: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    error: String?,
    modifier: Modifier = Modifier,
    placeholder: String = "Selecciona una opción"
) {
    var expandido by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        EtiquetaCampo(etiqueta, fondoOscuro = false)
        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = it }
        ) {
            OutlinedTextField(
                value = valor,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(placeholder, color = Color.Gray) },
                leadingIcon = {
                    Icon(icono, contentDescription = null, tint = if (error != null) RojoError else ColoresMarca.Verde)
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                isError = error != null,
                supportingText = textoDeApoyo(error, null),
                singleLine = true,
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                colors = coloresCampo(fondoOscuro = false),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false },
                containerColor = Color.White
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion, color = Color.Black, fontSize = 16.sp) },
                        onClick = {
                            onSeleccionar(opcion)
                            expandido = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * Mensaje general del formulario (no de un campo), con ícono:
 * rojo con alerta si es error, verde con check si salió bien.
 */
@Composable
fun MensajeFormulario(
    texto: String,
    esError: Boolean,
    modifier: Modifier = Modifier,
    fondoOscuro: Boolean = false
) {
    val color = when {
        esError && fondoOscuro -> RojoErrorClaro
        esError -> RojoError
        fondoOscuro -> Color.White
        else -> ColoresMarca.Verde
    }
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (esError) Icons.Filled.Error else Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = texto, color = color, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}
