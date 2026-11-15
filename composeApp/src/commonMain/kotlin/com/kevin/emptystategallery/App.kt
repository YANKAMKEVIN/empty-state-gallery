package com.kevin.emptystategallery

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun App() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF1B5E4A),
            secondary = Color(0xFF6750A4),
            surface = Color(0xFFFFFBFE),
            background = Color(0xFFF6F2EA),
            error = Color(0xFFB3261E)
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            EmptyStateGallery()
        }
    }
}

private data class EmptyStatePattern(
    val title: String,
    val context: String,
    val message: String,
    val primaryAction: String,
    val secondaryAction: String,
    val uxNote: String,
    val icon: ImageVector,
    val accent: Color
)

private val patterns = listOf(
    EmptyStatePattern(
        title = "First run",
        context = "No content yet",
        message = "Invite the user to create the first item. Keep the copy optimistic and action-oriented.",
        primaryAction = "Create item",
        secondaryAction = "View sample",
        uxNote = "Best when the user has not done anything wrong.",
        icon = Icons.Filled.Add,
        accent = Color(0xFF1B5E4A)
    ),
    EmptyStatePattern(
        title = "No results",
        context = "Search returned nothing",
        message = "Repeat the query context and offer a softer next step instead of blaming the search.",
        primaryAction = "Clear search",
        secondaryAction = "Adjust filters",
        uxNote = "Best after user input, filters, or sorting.",
        icon = Icons.Filled.Search,
        accent = Color(0xFF2F5DA8)
    ),
    EmptyStatePattern(
        title = "Offline",
        context = "Data cannot refresh",
        message = "Explain what is still available and make retry obvious without hiding cached content.",
        primaryAction = "Retry",
        secondaryAction = "Use offline mode",
        uxNote = "Best when the app can partially work.",
        icon = Icons.Filled.Refresh,
        accent = Color(0xFF8A5A00)
    ),
    EmptyStatePattern(
        title = "Permission",
        context = "Feature is locked",
        message = "Connect the permission to the benefit before sending the user to system settings.",
        primaryAction = "Allow access",
        secondaryAction = "Maybe later",
        uxNote = "Best before a feature needs camera, location, files, or notifications.",
        icon = Icons.Filled.Lock,
        accent = Color(0xFF6D3FB8)
    ),
    EmptyStatePattern(
        title = "Error",
        context = "Something failed",
        message = "Keep the error calm, preserve the user's work, and provide a recovery path.",
        primaryAction = "Try again",
        secondaryAction = "Contact support",
        uxNote = "Best when the user cannot fix the state alone.",
        icon = Icons.Filled.Warning,
        accent = Color(0xFFB3261E)
    )
)

@Composable
private fun EmptyStateGallery() {
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    val selected = patterns[selectedIndex]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Header()
        }
        item {
            PatternPicker(
                selectedIndex = selectedIndex,
                onSelected = { selectedIndex = it }
            )
        }
        item {
            AnimatedContent(
                targetState = selected,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "empty-state-preview"
            ) { pattern ->
                EmptyStatePreview(pattern = pattern)
            }
        }
        item {
            UxNotes(pattern = selected)
        }
    }
}

@Composable
private fun Header() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Empty State Gallery",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            color = Color(0xFF171B18)
        )
        Text(
            text = "Five tiny UI patterns for moments where the screen has no content.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF5D665F)
        )
    }
}

@Composable
private fun PatternPicker(
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        patterns.forEachIndexed { index, pattern ->
            val selected = selectedIndex == index
            Surface(
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .selectable(selected = selected, role = Role.Tab, onClick = { onSelected(index) }),
                shape = RoundedCornerShape(50),
                color = if (selected) pattern.accent else Color.White,
                border = BorderStroke(1.dp, if (selected) pattern.accent else Color(0xFFE2DDD4))
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    text = pattern.title,
                    color = if (selected) Color.White else Color(0xFF303632),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun EmptyStatePreview(pattern: EmptyStatePattern) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            EmptyIllustration(pattern = pattern)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = pattern.context,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = pattern.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF656D67),
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f).height(50.dp),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = pattern.accent),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(pattern.primaryAction, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f).height(50.dp),
                    onClick = {},
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(pattern.secondaryAction)
                }
            }
        }
    }
}

@Composable
private fun EmptyIllustration(pattern: EmptyStatePattern) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.74f)
            .aspectRatio(1.2f)
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        pattern.accent.copy(alpha = 0.18f),
                        Color(0xFFF8F4EE)
                    )
                )
            )
            .border(1.dp, pattern.accent.copy(alpha = 0.22f), RoundedCornerShape(26.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(pattern.accent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = pattern.icon,
                contentDescription = null,
                tint = pattern.accent
            )
        }
    }
}

@Composable
private fun UxNotes(pattern: EmptyStatePattern) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF171B18)),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Filled.MailOutline,
                contentDescription = null,
                tint = Color(0xFFB7F2D1)
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "UX note",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = pattern.uxNote,
                    color = Color(0xFFD6DDD8),
                    lineHeight = 20.sp
                )
            }
        }
    }
}
