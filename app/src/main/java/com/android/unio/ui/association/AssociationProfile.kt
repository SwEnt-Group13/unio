package com.android.unio.ui.association

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.android.unio.ui.navigation.NavigationAction

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AssociationProfile(navigationAction: NavigationAction) {
  Scaffold(
      topBar = {
        TopAppBar(
            title = {
              Text("Association Profile", modifier = Modifier.testTag("AssociationProfileTitle"))
            },
            navigationIcon = {
              IconButton(
                  onClick = { navigationAction.goBack() },
                  modifier = Modifier.testTag("goBackButton")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Go back")
                  }
            })
      },
      modifier = Modifier.testTag("AssociationScreen")) {
        Text("Association screen")
      }
}
