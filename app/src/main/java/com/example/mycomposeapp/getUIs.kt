package com.example.mycomposeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

val listItems = listOf(
    Question(
        labelText = "First Name is required*",
        hintText = "Enter First Name",
        defaultText = ""
    ),
    Question(
        labelText = "Middle Name is required*",
        defaultText = "",
        hintText = "Enter Middle Name"
    ),
    Question(labelText = "Last Name is required*", defaultText = "", hintText = "Enter Last Name")
)

data class Question(
    val labelText: String,
    val isEnable: Boolean = true,
    val defaultText: String,
    val hintText: String
)

@Preview(showBackground = true)
@ExperimentalMaterialApi
@Composable
fun SimpleColumnList1() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Bottom),
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        listItems.forEach {
            GetFirstUI(
                labelText = it.labelText,
                hintText = it.hintText,
                inputText = it.defaultText,
                isEnable = it.isEnable
            )
        }
    }
}

@Composable
fun GetFirstUI(labelText: String, inputText: String, isEnable: Boolean, hintText: String) {
    val labelValue = remember { mutableStateOf(labelText) }
    val inputValue = remember { mutableStateOf(TextFieldValue(inputText)) }
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
            .background(if (isEnable) Color.Transparent else Color.LightGray)
    ) {
        val (label, input) = createRefs()
        val annotatedText = buildAnnotatedString {
            //append your initial text
            append(labelValue.value.substring(0, labelValue.value.length - 1))
            //Start of the pushing annotation which you want to color and make them clickable later
            pushStringAnnotation(
                tag = labelValue.value,// provide tag which will then be provided when you click the text
                annotation = labelValue.value
            )
            //add text with your different color/style
            withStyle(
                style = SpanStyle(
                    color = Color.Red,
                )
            ) {
                append(labelValue.value[labelValue.value.length - 1].toString())
            }
            // when pop is called it means the end of annotation with current tag
            pop()
        }

        Text(
            text = annotatedText,
            modifier = Modifier
                .constrainAs(label) {

                }.fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        BasicTextField(
            value = inputValue.value,
            /*placeholder = {
                Text(text = hintText, modifier = Modifier.wrapContentWidth())
            },*/
            onValueChange = {
                inputValue.value = it
            }, enabled = isEnable,
          /*  shape = RoundedCornerShape(CornerSize(20.dp)),*/
            modifier = Modifier
                .constrainAs(input) {
                    top.linkTo(label.bottom)
                }.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Row(
                    Modifier
                        .background(Color.LightGray, RoundedCornerShape(percent = 30))
                        .padding(0.dp)
                ) {

                    if (inputValue.value.text.isEmpty()) {
                        Text("Label")
                    }
                    innerTextField()  //<-- Add this
                }
            },
           /* colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Gray,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )*/
        )
    }
}