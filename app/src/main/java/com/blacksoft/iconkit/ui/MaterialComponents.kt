package com.blacksoft.iconkit.ui

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round

/** Copied from manager's Material Expressive menu anchoring implementation. */
@Composable
private fun OffsetAnchoredExpressiveMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    anchorOffset: IntOffset,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(Constraints())
            val width = if (constraints.hasBoundedWidth) constraints.maxWidth else 0
            layout(width, 0) { placeable.place(anchorOffset.x, anchorOffset.y) }
        }
    ) {
        DropdownMenuPopup(expanded = expanded, onDismissRequest = onDismissRequest) {
            DropdownMenuGroup(
                shapes = MenuDefaults.groupShape(index = 0, count = 1),
                modifier = Modifier.verticalScroll(rememberScrollState()),
                content = content,
            )
        }
    }
}

private fun Modifier.trackPressPosition(onPress: (Offset) -> Unit): Modifier = pointerInput(Unit) {
    awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false)
        onPress(down.position)
    }
}

@Composable
fun ManagerStyleDropdownItem(
    icon: ImageVector,
    title: String,
    summary: String,
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    index: Int,
    count: Int,
    showSelectorIndicator: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }
    var anchorOffset by remember { mutableStateOf(IntOffset.Zero) }
    val safeIndex = selectedIndex.coerceIn(items.indices)

    Box(modifier = Modifier.trackPressPosition { anchorOffset = it.round() }) {
        SegmentedListItem(
            onClick = { expanded = true },
            shapes = ListItemDefaults.segmentedShapes(index, count),
            leadingContent = { Icon(icon, title) },
            content = { Text(title) },
            supportingContent = { Text(summary) },
            trailingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = items[safeIndex],
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    if (showSelectorIndicator) {
                        Icon(
                            Icons.Filled.UnfoldMore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 4.dp).size(18.dp),
                        )
                    }
                }
            }
        )
        OffsetAnchoredExpressiveMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            anchorOffset = anchorOffset,
        ) {
            items.forEachIndexed { itemIndex, text ->
                DropdownMenuItem(
                    text = { Text(text) },
                    selected = itemIndex == safeIndex,
                    onClick = {
                        onItemSelected(itemIndex)
                        expanded = false
                    },
                    shapes = MenuDefaults.itemShape(itemIndex, items.size),
                    selectedLeadingIcon = {
                        Icon(Icons.Filled.Check, contentDescription = null)
                    }
                )
            }
        }
    }
}
