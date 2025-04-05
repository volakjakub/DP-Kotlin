package org.adastra.curriculum.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import org.adastra.curriculum.client.data.BiographyResponse
import org.adastra.curriculum.client.data.LanguageResponse
import org.adastra.curriculum.service.BiographyService

@Composable
fun LanguageList(biographyService: BiographyService, biography: BiographyResponse) {
    var isLoadingLanguages by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val languagesState =
        produceState<List<LanguageResponse>?>(initialValue = null) {
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
    if (isLoadingLanguages) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    when {
        languages != null -> {
            languages.forEachIndexed { index, language ->
                LanguageBox(language)
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