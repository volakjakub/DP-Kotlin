@file:OptIn(ExperimentalTime::class)

package org.adastra.curriculum

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adastra.curriculum.auth.LoginViewModel
import org.adastra.curriculum.client.data.Authority
import org.adastra.curriculum.client.data.BiographyResponse
import org.adastra.curriculum.service.BiographyService
import kotlin.time.ExperimentalTime
import kotlinx.datetime.*
import org.adastra.curriculum.client.data.LanguageResponse
import org.adastra.curriculum.helper.ExpertiseHelper
import org.adastra.curriculum.helper.LanguageHelper

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun App(loginViewModel: LoginViewModel) {
    val expertiseHelper = ExpertiseHelper()
    val languageHelper = LanguageHelper()
    val biographyService = BiographyService(loginViewModel)
    var isLoadingBiography by remember { mutableStateOf(true) }
    var isLoadingLanguages by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val biographyState = produceState<BiographyResponse?>(initialValue = null) {
        if (loginViewModel.getAccount()?.authorities?.contains(Authority.ROLE_ADMIN) == true) {
            isLoadingBiography = false
            value = null
        } else {
            try {
                value = biographyService.getBiography()
            } catch (e: Exception) {
                error = e.message
                value = null
            } finally {
                isLoadingBiography = false
            }
        }
    }
    val biography = biographyState.value

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Curriculum",
                    style = MaterialTheme.typography.h3,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 20.dp, top = 30.dp).fillMaxWidth()
                )

                if (error != null) {
                    Text(
                        text = error.toString(),
                        color = Color.Red,
                        fontSize = TextUnit(12f, TextUnitType.Sp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                if (loginViewModel.getAccount()?.authorities?.contains(Authority.ROLE_USER) == true) {
                    Box(
                        modifier = Modifier
                            .padding()
                            .background(Color.White),
                        contentAlignment = Alignment.TopStart
                    ) {
                        if (isLoadingBiography) {
                            Column(Modifier.fillMaxWidth().padding(10.dp)) {
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

                                        val languagesState =
                                            produceState<List<LanguageResponse>?>(initialValue = null) {
                                                if (loginViewModel.getAccount()?.authorities?.contains(
                                                        Authority.ROLE_ADMIN
                                                    ) == true
                                                ) {
                                                    isLoadingLanguages = false
                                                    value = null
                                                } else {
                                                    try {
                                                        value =
                                                            biographyService.getLanguagesByBiography(
                                                                biography.id
                                                            )
                                                    } catch (e: Exception) {
                                                        error = e.message
                                                        value = null
                                                    } finally {
                                                        isLoadingLanguages = false
                                                    }
                                                }
                                            }
                                        val languages = languagesState.value

                                        Text(
                                            "Jazyky",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.h5,
                                            textAlign = TextAlign.Left,
                                            color = Color.Black,
                                            modifier = Modifier.padding(bottom = 10.dp, top = 15.dp)
                                                .fillMaxWidth()
                                        )

                                        if (isLoadingLanguages) {
                                            CircularProgressIndicator(
                                                color = Color.Blue,
                                                modifier = Modifier.align(Alignment.CenterHorizontally)
                                            )
                                        }

                                        when {
                                            languages != null -> {
                                                languages.forEachIndexed { index, language ->
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
                                                    }
                                                    if (index < languages.lastIndex) {
                                                        Divider(
                                                            color = Color.Gray,
                                                            thickness = 1.dp,
                                                            modifier = Modifier.padding(vertical = 5.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .height(75.dp)
                    .background(Color.LightGray)
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Login: ${loginViewModel.getAccount()?.login}", fontSize = 15.sp, color = Color.Black)
                        if (loginViewModel.getAccount()?.authorities != null && loginViewModel.getAccount()?.authorities?.contains(
                                Authority.ROLE_ADMIN) == true
                        ) {
                            Text(
                                "Administrátor",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        } else {
                            Text(
                                "Uživatel",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        onClick = {
                            loginViewModel.logout()
                        }) {
                        Text("Odhlásit")
                    }
                }
            }
        }
    }
}