package org.adastra.curriculum.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import org.adastra.curriculum.client.data.BiographyResponse

@Composable
fun BiographyInfo(biography: BiographyResponse) {
    val employedFrom = LocalDate.parse(biography.employedFrom)
    val employedFromFormatted = "%02d.%02d.%04d".format(
        employedFrom.dayOfMonth,
        employedFrom.monthNumber,
        employedFrom.year
    )

    Text(
        "Titul",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Text(
        biography.title ?: "",
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
    Text(
        "Jméno",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Text(
        biography.firstName,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
    Text(
        "Příjmení",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Text(
        biography.lastName,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
    Text(
        "E-Mail",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Text(
        biography.email,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
    Text(
        "Telefon",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Text(
        biography.phone,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
    Divider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Text(
        "Ulice",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Text(
        biography.street,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
    Text(
        "Město",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Text(
        biography.city,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
    Text(
        "Stát",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Text(
        biography.country,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
    Divider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Text(
        "Pracovní pozice",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Text(
        biography.position,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
    Text(
        "Zaměstnán/a od",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    Text(
        employedFromFormatted,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
}