package com.github.shaart.pstorage.multiplatform.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.shaart.pstorage.multiplatform.config.ApplicationContext
import com.github.shaart.pstorage.multiplatform.dto.UserSettingViewDto
import com.github.shaart.pstorage.multiplatform.enums.AppSettings
import com.github.shaart.pstorage.multiplatform.enums.SettingType
import com.github.shaart.pstorage.multiplatform.exception.AppException
import com.github.shaart.pstorage.multiplatform.logger
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.preview.PreviewData

//TODO fix non-changing checkboxes
@Composable
fun SettingsView(
    appContext: ApplicationContext,
    authentication: Authentication,
    onSettingsChange: (List<UserSettingViewDto>) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth().fillMaxHeight(),
) {
    val settingsService = appContext.settingsService()
    val globalExceptionHandler = appContext.globalExceptionHandler()

    MaterialTheme {
        Box(modifier = modifier) {
            val columnState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                item {
                    Row {
                        Text(
                            text = "Settings",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
                        )
                    }
                }
                items(authentication.user.settings) { aSetting ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (aSetting.settingType == SettingType.TOGGLE) {
                            val isChecked by remember { mutableStateOf(aSetting.value.toBoolean()) }
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { newValue ->
                                    globalExceptionHandler.runSafely {
                                        logger().info(
                                            "Changed '{}' to '{}'",
                                            aSetting.name,
                                            newValue
                                        )
                                        aSetting.changeValue(newValue.toString())
                                        val savedSetting =
                                            settingsService.saveSetting(aSetting, authentication)
                                        onSettingsChange(
                                            authentication.user.settings.stream()
                                                .filter { it.name != aSetting.name }
                                                .map { it.copy() }
                                                .toList()
                                                .plus(savedSetting)
                                        )
                                    }()
                                }
                            )
                            Text(text = AppSettings.getBySettingName(aSetting.name).settingDescription)
                        } else {
                            throw AppException("Unexpected setting type = " + aSetting.settingType)
                        }
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = columnState)
            )
        }
    }
}

@Preview
@Composable
fun previewSettingsView() {
    SettingsView(
        appContext = PreviewData.previewApplicationContext(),
        authentication = PreviewData.previewAuthentication(),
        onSettingsChange = {}
    )
}