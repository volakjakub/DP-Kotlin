package org.adastra.curriculum.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TabRowDefaults.Divider
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
import org.adastra.curriculum.client.data.EducationResponse
import org.adastra.curriculum.helper.EducationHelper

@Composable
fun EducationBox(education: EducationResponse, onShowForm: (EducationResponse) -> Unit, onDeleteSubmit: (EducationResponse) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    var showDeleteMessage by remember { mutableStateOf(false) }
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
            .fillMaxWidth()
            .clickable {
                isExpanded = !isExpanded
                showDeleteMessage = false
            }
            .animateContentSize(tween(durationMillis = 300)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
                        Divider(
                            color = Color.Black,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )

                        if (showDeleteMessage) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(7.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    "Opravdu chcete odstranit toto vzdělání?",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(top = 11.dp).padding(end = 10.dp)
                                )
                                Button(
                                    colors = ButtonDefaults.buttonColors(Color.Red),
                                    onClick = {
                                        onDeleteSubmit(education)
                                        showDeleteMessage = false
                                    }) {
                                    Text("Odstranit")
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(7.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    colors = ButtonDefaults.buttonColors(Color.Blue),
                                    onClick = {
                                        onShowForm(education)
                                    }) {
                                    Text(
                                        "Upravit",
                                        color = Color.White,
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    colors = ButtonDefaults.buttonColors(Color.Red),
                                    onClick = {
                                        showDeleteMessage = true
                                    }) {
                                    Text("Odstranit")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}