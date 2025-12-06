package com.caloriesnap.presentation.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.caloriesnap.data.local.PreferencesManager
import com.caloriesnap.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var config by remember(state.apiConfig) { mutableStateOf(state.apiConfig) }
    var showApiKey by remember { mutableStateOf(false) }

    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { context.contentResolver.openInputStream(it)?.bufferedReader()?.readText()?.let { json -> viewModel.importData(json) } }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("设置") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "返回") }
        })
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("AI识别功能", style = MaterialTheme.typography.titleMedium)
                        Switch(checked = config.enabled, onCheckedChange = { config = config.copy(enabled = it); viewModel.updateApiConfig(config) })
                    }
                    if (config.enabled) {
                        Spacer(Modifier.height(16.dp))
                        Text("API预设", style = MaterialTheme.typography.bodySmall)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("OpenAI" to "https://api.openai.com/v1", "Claude" to "https://api.anthropic.com/v1", "通义千问" to "https://dashscope.aliyuncs.com/compatible-mode/v1").forEach { (name, url) ->
                                FilterChip(selected = config.baseUrl == url, onClick = { config = config.copy(baseUrl = url); viewModel.updateApiConfig(config) }, label = { Text(name) })
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = config.baseUrl, onValueChange = { config = config.copy(baseUrl = it) }, label = { Text("API Base URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = config.apiKey,
                            onValueChange = { config = config.copy(apiKey = it) },
                            label = { Text("API Key") },
                            singleLine = true,
                            visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = { IconButton(onClick = { showApiKey = !showApiKey }) { Icon(if (showApiKey) Icons.Default.VisibilityOff else Icons.Default.Visibility, "显示") } },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = config.model, onValueChange = { config = config.copy(model = it) }, label = { Text("模型名称") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        Text("常用: gpt-4o, gpt-4o-mini, claude-3-5-sonnet-20241022, qwen-vl-max", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = config.timeout.toString(), onValueChange = { it.toIntOrNull()?.let { v -> config = config.copy(timeout = v) } }, label = { Text("超时(秒)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { viewModel.updateApiConfig(config) }) { Text("保存配置") }
                            OutlinedButton(onClick = { viewModel.testConnection() }, enabled = !state.isTesting) {
                                if (state.isTesting) CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp) else Text("测试连接")
                            }
                        }
                        state.testResult?.let { Text(it, color = if (it.startsWith("✓")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error) }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("数据管理", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { viewModel.exportData() }) { Icon(Icons.Default.Upload, "导出"); Spacer(Modifier.width(4.dp)); Text("导出数据") }
                        OutlinedButton(onClick = { importLauncher.launch("application/json") }) { Icon(Icons.Default.Download, "导入"); Spacer(Modifier.width(4.dp)); Text("导入数据") }
                    }
                    state.exportResult?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, style = MaterialTheme.typography.bodySmall, color = if (it.contains("成功")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("外观", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    Text("主题模式", style = MaterialTheme.typography.bodySmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(PreferencesManager.ThemeMode.SYSTEM to "跟随系统", PreferencesManager.ThemeMode.LIGHT to "浅色", PreferencesManager.ThemeMode.DARK to "深色").forEach { (mode, name) ->
                            FilterChip(selected = state.themeMode == mode, onClick = { viewModel.updateThemeMode(mode) }, label = { Text(name) })
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("关于", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    InfoRow("应用名称", "CalorieSnap")
                    InfoRow("版本", "1.0.0")
                }
            }
        }
    }
}
