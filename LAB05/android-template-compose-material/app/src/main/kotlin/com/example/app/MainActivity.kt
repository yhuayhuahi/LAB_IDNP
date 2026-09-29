package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			AppTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					MainScreen()
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
	var count by remember { mutableIntStateOf(0) }

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text("Jetpack Compose Material 3") },
				colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
				)
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(16.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			ElevatedCard(
				modifier = Modifier
					.fillMaxWidth()
					.padding(8.dp)
			) {
				Column(
					modifier = Modifier.padding(24.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = "Plantilla Compose + Material 3",
						style = MaterialTheme.typography.titleLarge,
						color = MaterialTheme.colorScheme.primary
					)
					Spacer(modifier = Modifier.height(12.dp))
					Text(
						text = "Has presionado el botón:",
						style = MaterialTheme.typography.bodyMedium
					)
					Spacer(modifier = Modifier.height(8.dp))
					Text(
						text = "$count veces",
						style = MaterialTheme.typography.headlineMedium,
						color = MaterialTheme.colorScheme.secondary
					)
					Spacer(modifier = Modifier.height(16.dp))
					Button(onClick = { count++ }) {
						Text("Incrementar contador")
					}
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
	AppTheme {
		MainScreen()
	}
}