package com.expensesnap.app.ui.scan

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Success screen that briefly displays after receipt processing is complete.
 *
 * This screen provides visual feedback to the user that their receipt has been
 * successfully processed and saved. It displays for a short duration before
 * automatically transitioning to the receipt details screen.
 *
 * Features:
 * - Animated checkmark with scale and fade-in effect
 * - Success message confirming receipt save
 * - Loading message for next screen transition
 * - Clean, celebratory design with Material 3 theming
 * - Animations start after a 100ms delay for better visual impact
 */
@Composable
fun SuccessScreen(onAnimationComplete: () -> Unit) {
    // Controls when animations should start (initially false, then true after delay)
    var startAnimation by remember { mutableStateOf(false) }

    // Trigger animations after a small delay for better visual impact
    LaunchedEffect(Unit) {
        delay(100) // Small delay before starting animation to allow screen to render
        startAnimation = true
        delay(1500) // Display success screen for 1.5 seconds
        onAnimationComplete() // Notify parent that animation is complete
    }

    // Main content container - centered vertically and horizontally
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated checkmark icon with spring animation
        AnimatedCheckmark(startAnimation = startAnimation)

        Spacer(modifier = Modifier.height(32.dp))

        // Primary success message
        Text(
            text = "Receipt saved!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Secondary message indicating next action
        Text(
            text = "Opening receipt details...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Animated checkmark icon with scale and fade-in effects.
 *
 * This composable creates a two-layer circular design with animated entrance:
 * - Outer circle: Primary container color with fade-in effect
 * - Inner circle: Primary color with white checkmark icon
 * - Scale animation: Springs from 30% to 100% size with medium bounce
 * - Alpha animation: Fades from transparent to opaque over 300ms
 *
 * The animation creates a celebratory, polished feel for the success state.
 *
 * @param startAnimation When true, triggers the entrance animations. Should be
 *                       controlled by parent to coordinate timing.
 */
@Composable
fun AnimatedCheckmark(startAnimation: Boolean) {
    // Scale animation - springs from small (0.3) to full size (1.0) with bounce
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, // Medium bounce for playful feel
            stiffness = Spring.StiffnessMedium // Medium speed for smooth motion
        ),
        label = "checkmarkScale"
    )

    // Alpha animation - fades from transparent (0) to opaque (1)
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300, // Quick fade-in over 300ms
            easing = FastOutSlowInEasing // Starts fast, ends slow
        ),
        label = "checkmarkAlpha"
    )

    // Outer container - provides the animated scale and primary container background
    Box(
        modifier = Modifier
            .size(120.dp) // Overall size of the checkmark circle
            .scale(scale) // Apply scale animation to entire component
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = alpha),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        // Inner circle - creates depth with solid primary color background
        Box(
            modifier = Modifier
                .size(100.dp) // Slightly smaller than outer circle for layered effect
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Checkmark icon - white for maximum contrast against primary color
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                modifier = Modifier.size(64.dp), // Large icon for visibility
                tint = Color.White // Force white color for better visibility
            )
        }
    }
}