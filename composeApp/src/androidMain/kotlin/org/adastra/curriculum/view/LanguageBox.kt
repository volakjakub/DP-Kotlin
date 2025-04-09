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
import org.adastra.curriculum.client.data.LanguageResponse
import org.adastra.curriculum.helper.ExpertiseHelper
import org.adastra.curriculum.helper.LanguageHelper

@Composable
fun LanguageBox(language: LanguageResponse, onShowForm: (LanguageResponse) -> Unit, onDeleteSubmit: (LanguageResponse) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    var showDeleteMessage by remember { mutableStateOf(false) }
    val languageHelper = LanguageHelper()
    val expertiseHelper = ExpertiseHelper()

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
                        languageHelper.getLanguage(
                            language.name
                        ),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        expertiseHelper.getExpertise(
                            language.expertise
                        ),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )

                }
            }
            if (isExpanded) {
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
                            "Opravdu chcete odstranit tento jazyk?",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 11.dp).padding(end = 10.dp)
                        )
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Red),
                            onClick = {
                                onDeleteSubmit(language)
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
                                onShowForm(language)
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