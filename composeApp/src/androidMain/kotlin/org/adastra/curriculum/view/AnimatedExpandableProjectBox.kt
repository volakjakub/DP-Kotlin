package org.adastra.curriculum.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import org.adastra.curriculum.client.data.ProjectResponse

@Composable
fun AnimatedExpandableProjectBox(project: ProjectResponse) {
    var isExpanded by remember { mutableStateOf(false) }

    val start = LocalDate.parse(project.start)
    val startFormatted = "%02d.%02d.%04d".format(
        start.dayOfMonth,
        start.monthNumber,
        start.year
    )
    var endFormatted: String? = null
    if (project.end != null) {
        val end = LocalDate.parse(project.end!!)
        endFormatted = "%02d.%02d.%04d".format(
            end.dayOfMonth,
            end.monthNumber,
            end.year
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .background(Color.LightGray)
            .animateContentSize(tween(durationMillis = 300)),
        contentAlignment = Alignment.TopCenter
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
                    project.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    project.client,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                if (isExpanded) {
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
                    if (project.description != null) {
                        Text(
                            "Popis projektu: " + project.description!!,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                    Text(
                        "Dovednosti projektu: " + project.skills.joinToString(", ") { skill -> skill.name },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }
        }
    }
}