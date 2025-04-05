package org.adastra.curriculum.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import org.adastra.curriculum.client.data.BiographyResponse
import org.adastra.curriculum.service.BiographyService

@Composable
fun BiographyDetail(biographyService: BiographyService) {
    var isLoadingBiography by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val biographyState = produceState<BiographyResponse?>(initialValue = null) {
        try {
            value = biographyService.getBiography()
        } catch (e: Exception) {
            error = e.message
            value = null
        } finally {
            isLoadingBiography = false
        }
    }
    val biography = biographyState.value

    Box(
        modifier = Modifier
            .padding()
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        if (error != null) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = error.toString(),
                    color = Color.Red,
                    fontSize = TextUnit(12f, TextUnitType.Sp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        if (isLoadingBiography) {
            Column(modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(
                    color = Color.Blue,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        when {
            biography != null -> {
                val employedFrom = LocalDate.parse(biography.employedFrom)
                val employedFromFormatted = "%02d.%02d.%04d".format(
                    employedFrom.dayOfMonth,
                    employedFrom.monthNumber,
                    employedFrom.year
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
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

                        LanguageList(biographyService, biography)
                        EducationList(biographyService, biography)
                        ProjectList(biographyService, biography)
                        SkillList(biographyService, biography)
                    }
                }
            }
        }
    }
}