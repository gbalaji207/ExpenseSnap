package com.expensesnap.app.ui.scan

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Processing screen that displays visual feedback while the receipt is being analyzed by AI.
 *
 * This screen provides an engaging, informative experience while the backend processes
 * the receipt image with OCR and AI analysis. The screen uses multiple animations to
 * communicate progress and keep the user engaged during the 3-5 second processing time.
 *
 * Visual Features:
 * - Large receipt image preview with card elevation and shadow
 * - Animated scanning line that moves vertically to suggest active analysis
 * - Pulsing status messages for dynamic feedback
 * - Optional progress percentage (if available from backend)
 * - Processing step indicators (dots showing current stage)
 * - AI icon for clear communication of automated processing
 *
 * Animation Details:
 * - Scanning animation: 2-second cycle with reverse repeat
 * - Text pulsing: 1-second fade in/out cycle
 * - Processing dots: 600ms scale animation for active steps
 *
 * @param imageBytes Byte array of the captured receipt image to display as preview
 * @param progress Optional progress value (0.0 to 1.0) from backend processing.
 *                 When provided, displays as a percentage to the user.
 */
@Composable
fun ProcessingScreen(
    imageBytes: ByteArray,
    progress: Float? = null
) {
    // Decode byte array to ImageBitmap for display - cached with remember
    val imageBitmap = remember(imageBytes) { imageBytes.decodeToImageBitmap() }

    // Main content container with vertical layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top spacing to push content below status bar area
        Spacer(modifier = Modifier.weight(0.3f))

        // Receipt image container with scanning animation and processing badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp) // Fixed height for consistent scanning animation
        ) {
            // Receipt image card with elevation for depth
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                // Display the captured receipt image
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Receipt preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Animated scanning line overlay - suggests active processing
            ScanningAnimation()
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Status section with AI icon, messages, and progress indicators
        StatusSection(progress = progress)

        // Bottom spacing to balance layout
        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * Status section displaying processing information with animated elements.
 *
 * This component shows:
 * - AI robot icon in a circular container
 * - Pulsing "Analyzing receipt..." message
 * - Optional progress percentage (when provided by backend)
 * - Estimated time message
 * - Processing step indicator dots
 *
 * @param progress Optional progress value (0.0 to 1.0) to display as percentage
 */
@Composable
fun StatusSection(progress: Float? = null) {
    // Pulsing animation for the status text - cycles between 60% and 100% opacity
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing), // 1-second smooth transition
            repeatMode = RepeatMode.Reverse
        ),
        label = "textPulse"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // AI robot icon in circular background - indicates automated processing
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(28.dp) // Perfect circle
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🤖",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // Primary status message with pulsing animation
        Text(
            text = "Analyzing receipt...",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(alpha) // Apply pulsing effect
        )

        // Display progress percentage if backend provides it
        progress?.let {
            Text(
                text = "${(it * 100).toInt()}%",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }

        // Informative message about expected processing time
        Text(
            text = "This usually takes 3-5 seconds",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        // Processing step indicators - visual representation of stages
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            ProcessingDot(isActive = true)  // Step 1: Complete
            ProcessingDot(isActive = true)  // Step 2: In progress
            ProcessingDot(isActive = false) // Step 3: Waiting
        }
    }
}

/**
 * Animated dot indicator showing the current processing step.
 *
 * Active dots pulse with a scale animation to indicate progress.
 * Inactive dots remain static with a muted color.
 *
 * @param isActive When true, the dot pulses and uses primary color.
 *                 When false, the dot is static with outline color.
 */
@Composable
fun ProcessingDot(isActive: Boolean) {
    // Pulsing scale animation for active dots - alternates between 80% and 100%
    val scale by rememberInfiniteTransition(label = "dotScale").animateFloat(
        initialValue = if (isActive) 0.8f else 1f,
        targetValue = if (isActive) 1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600), // 600ms per pulse cycle
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotScale"
    )

    Box(
        modifier = Modifier
            .size(8.dp)
            .scale(if (isActive) scale else 1f) // Apply animation only to active dots
            .background(
                color = if (isActive)
                    MaterialTheme.colorScheme.primary // Bright color for active
                else
                    MaterialTheme.colorScheme.outlineVariant, // Muted color for inactive
                shape = RoundedCornerShape(4.dp) // Rounded square shape
            )
    )
}

/**
 * Animated scanning line that moves vertically across the receipt image.
 *
 * This composable creates a dual-layer scanning effect:
 * 1. A gradient "glow" bar (100dp tall) that provides soft ambient light
 * 2. A solid bright line (3dp tall) at the center for sharp definition
 *
 * The animation:
 * - Moves from top to bottom and back in a 2-second cycle
 * - Uses linear easing for constant scan speed
 * - Position is calculated as a fraction (0.0 to 1.0) of the container height
 * - Both layers move in sync for cohesive visual effect
 *
 * This creates the illusion of a scanner beam actively analyzing the receipt,
 * providing clear feedback that processing is in progress.
 */
@Composable
fun ScanningAnimation() {
    // Infinite animation controlling the vertical position of the scanning line
    val infiniteTransition = rememberInfiniteTransition(label = "scanning")

    // Scan position ranges from 0.0 (top) to 1.0 (bottom)
    val scanPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000, // Full cycle: 2 seconds down, 2 seconds up
                easing = LinearEasing // Constant speed throughout
            ),
            repeatMode = RepeatMode.Reverse // Bounce between top and bottom
        ),
        label = "scanPosition"
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Gradient glow layer - creates a soft "beam" effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // Tall enough to create visible glow
                .align(Alignment.TopStart)
                .offset(y = (450.dp - 100.dp) * scanPosition) // Move with animation
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, // Fade in from top
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), // Peak brightness
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            Color.Transparent // Fade out to bottom
                        )
                    )
                )
        )

        // Solid scanning line - provides sharp focus point in the center of glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp) // Thin line for precision
                .align(Alignment.TopStart)
                .offset(y = (450.dp - 100.dp) * scanPosition + 50.dp) // Centered in glow (100dp / 2)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)) // Nearly opaque
        )
    }
}