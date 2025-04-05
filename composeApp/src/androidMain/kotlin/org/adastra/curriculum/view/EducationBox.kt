package org.adastra.curriculum.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import org.adastra.curriculum.client.data.EducationResponse
import org.adastra.curriculum.helper.EducationHelper

@Composable
fun EducationBox(education: EducationResponse) {
    val educationHelper = EducationHelper()
    val start = LocalDate.parse(education.start)
    val startFormatted = "%02d.%02d.%04d".format(
        start.dayOfMonth,
        start.monthNumber,
        start.year
    )
    var endFormatted: String? = null
    if (education.end != null) {
        val end = LocalDate.parse(education.end!!)
        endFormatted = "%02d.%02d.%04d".format(
            end.dayOfMonth,
            end.monthNumber,
            end.year
        )
    }
    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    education.school,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    educationHelper.getType(education.type),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                Text(
                    "Od: $startFormatted",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                if (endFormatted != null) {
                    Text(
                        "Do: $endFormatted",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                } else {
                    Text(
                        "Do: -",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }
        }
    }
}