package com.example.tarjetascampus_compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarjetascampus_compose.R
import com.example.tarjetascampus_compose.viewmodel.SeleccionViewModel

@Composable
fun HomeScreen(
	seleccionViewModel: SeleccionViewModel,
	onExplorarEdificios: () -> Unit = {}
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(24.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = "Bienvenido al Campus",
			style = MaterialTheme.typography.headlineMedium,
			fontWeight = FontWeight.Bold,
			textAlign = TextAlign.Center
		)
		Text(
			text = "Explora las instalaciones y edificios universitarios.",
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp)
		)

		Card(
			shape = RoundedCornerShape(16.dp),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.secondaryContainer
			),
			modifier = Modifier.padding(vertical = 24.dp)
		) {
			Column(
				modifier = Modifier.padding(20.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "Último edificio consultado:",
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSecondaryContainer
				)
				Text(
					text = seleccionViewModel.edificioSeleccionado,
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.onSecondaryContainer,
					modifier = Modifier.padding(top = 6.dp)
				)
			}
		}

		Button(
			onClick = onExplorarEdificios,
			shape = RoundedCornerShape(12.dp)
		) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				Icon(
					painter = painterResource(id = R.drawable.ic_apartment),
					contentDescription = null,
					modifier = Modifier.size(18.dp)
				)
				Spacer(modifier = Modifier.width(8.dp))
				Text("Explorar Edificios")
			}
		}
	}
}
