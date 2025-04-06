package org.adastra.curriculum

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.adastra.curriculum.auth.LoginViewModel
import org.adastra.curriculum.client.data.Authority
import org.adastra.curriculum.service.BiographyService
import org.adastra.curriculum.view.BiographyDetail
import org.adastra.curriculum.view.Footer

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun App(loginViewModel: LoginViewModel) {
    val scrollState = rememberScrollState()
    val biographyService = BiographyService(loginViewModel)
    val account = loginViewModel.getAccount()

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(scrollState).padding(bottom = 80.dp)) {
                Text(
                    "Curriculum",
                    style = MaterialTheme.typography.h3,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 20.dp, top = 30.dp).fillMaxWidth()
                )

                if (account != null) {
                    if (account.authorities.contains(Authority.ROLE_ADMIN) == true) {
                        // TODO: Admin view
                    } else {
                        BiographyDetail(biographyService, account)
                    }
                } else {
                    loginViewModel.logout()
                }
            }

            Box(
                modifier = Modifier
                    .height(75.dp)
                    .background(Color.LightGray)
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.BottomCenter
            ) {
                Footer(loginViewModel)
            }
        }
    }
}