package io.goodmidnight.scanner.designsystem.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.component.AvatarShape
import io.goodmidnight.scanner.designsystem.component.AvatarSize
import io.goodmidnight.scanner.designsystem.component.CardStyle
import io.goodmidnight.scanner.designsystem.component.SAccordion
import io.goodmidnight.scanner.designsystem.component.SAvatar
import io.goodmidnight.scanner.designsystem.component.SBadge
import io.goodmidnight.scanner.designsystem.component.SBadgeBox
import io.goodmidnight.scanner.designsystem.component.SBottomSheetHeader
import io.goodmidnight.scanner.designsystem.component.SBottomSheetItem
import io.goodmidnight.scanner.designsystem.component.SButton
import io.goodmidnight.scanner.designsystem.component.SButtonType
import io.goodmidnight.scanner.designsystem.component.SCard
import io.goodmidnight.scanner.designsystem.component.SCheckBox
import io.goodmidnight.scanner.designsystem.component.SChip
import io.goodmidnight.scanner.designsystem.component.SCircularProgress
import io.goodmidnight.scanner.designsystem.component.SConfirmDialog
import io.goodmidnight.scanner.designsystem.component.SContainerTextField
import io.goodmidnight.scanner.designsystem.component.SDisplaySmallText
import io.goodmidnight.scanner.designsystem.component.SFilterChip
import io.goodmidnight.scanner.designsystem.component.SHeadingMediumText
import io.goodmidnight.scanner.designsystem.component.SHeadingSmallText
import io.goodmidnight.scanner.designsystem.component.SHeadingXXLargeText
import io.goodmidnight.scanner.designsystem.component.SIcon
import io.goodmidnight.scanner.designsystem.component.SLabelLargeText
import io.goodmidnight.scanner.designsystem.component.SLabelMediumText
import io.goodmidnight.scanner.designsystem.component.SLabelSmallText
import io.goodmidnight.scanner.designsystem.component.SListItem
import io.goodmidnight.scanner.designsystem.component.SModalBottomSheet
import io.goodmidnight.scanner.designsystem.component.SParagraphMediumText
import io.goodmidnight.scanner.designsystem.component.SProgressBar
import io.goodmidnight.scanner.designsystem.component.SRadioButton
import io.goodmidnight.scanner.designsystem.component.SSearchTextField
import io.goodmidnight.scanner.designsystem.component.SSegmentedControl
import io.goodmidnight.scanner.designsystem.component.SSlider
import io.goodmidnight.scanner.designsystem.component.SStepper
import io.goodmidnight.scanner.designsystem.component.STitleTopBar
import io.goodmidnight.scanner.designsystem.component.SToggleSwitch
import io.goodmidnight.scanner.designsystem.component.STooltip
import io.goodmidnight.scanner.designsystem.component.SUnderlineTextField
import io.goodmidnight.scanner.designsystem.component.TooltipDirection
import io.goodmidnight.scanner.designsystem.theme.Icons
import io.goodmidnight.scanner.designsystem.theme.Theme

/**
 * [DesignSystemShowcase]
 * - A comprehensive preview dashboard showcasing all components, colors, typography, and interactions of the Soft Monotone design system.
 * - Includes all 9 new and enhanced components (SCheckBox, SSegmentedControl, SProgressBar, SCircularProgress, SBadge, SListItem, SAvatar, SAccordion, STooltip, SStepper) for live testing.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DesignSystemShowcase() {
    val scrollState = rememberScrollState()

    // Existing state for interactive testing
    var buttonClickCount by remember { mutableStateOf(0) }
    var textInput by remember { mutableStateOf("") }
    var searchInput by remember { mutableStateOf("") }
    var chipSelected1 by remember { mutableStateOf(true) }
    var chipSelected2 by remember { mutableStateOf(false) }
    var switchChecked by remember { mutableStateOf(true) }
    var radioSelected by remember { mutableStateOf(true) }
    var sliderValue by remember { mutableFloatStateOf(1.0f) }

    // New & additional component interactive state
    var checkboxChecked by remember { mutableStateOf(false) }
    var segmentedIndex by remember { mutableStateOf(0) }
    var progressVal by remember { mutableFloatStateOf(0.35f) }
    var badgeCount by remember { mutableStateOf(7) }
    var stepperVal by remember { mutableStateOf(3) }
    var tooltipVisible by remember { mutableStateOf(true) }
    var avatarActive by remember { mutableStateOf(true) }

    // Popup and bottom sheet trigger state
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(bottom = 56.dp)
    ) {
        // Top bar preview
        STitleTopBar(
            title = "Uber Monotone DS",
            onBack = {}
        )

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Color palette introduction card
            SHeadingXXLargeText(text = "1. Color Palette")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SParagraphMediumText(
                        text = "A calm, off-tone monotone palette that avoids pure black/white. Minimizes eye strain and delivers an extremely refined feel.",
                        color = Theme.colorScheme.secondaryText
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ColorInfoChip("BG", Theme.colorScheme.background)
                        ColorInfoChip("Surface", Theme.colorScheme.surface)
                        ColorInfoChip("Text 1", Theme.colorScheme.primaryText)
                        ColorInfoChip("Text 2", Theme.colorScheme.secondaryText)
                        ColorInfoChip("Border", Theme.colorScheme.outline)
                    }
                }
            }

            // 2. Typography introduction card
            SHeadingXXLargeText(text = "2. Typography")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SDisplaySmallText(text = "Display Small (28sp)")
                    SHeadingXXLargeText(text = "Heading XXLarge (24sp)")
                    SHeadingMediumText(text = "Heading Medium (18sp)")
                    SParagraphMediumText(text = "Paragraph Medium (14sp) - Body text rendering optimized for readability.")
                    SLabelMediumText(text = "Label Medium (14sp) - Control and action typography")
                }
            }

            // 3. Buttons and chips
            SHeadingXXLargeText(text = "3. Buttons & Chips")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SParagraphMediumText(
                        text = "All touchable elements scale down to 0.96f with a bouncy feel on press. Corner radius is set to a generous 24dp.",
                        color = Theme.colorScheme.secondaryText
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SButton(
                            text = "Primary (Clicked: $buttonClickCount)",
                            onClick = { buttonClickCount++ },
                            modifier = Modifier.weight(1f)
                        )
                        SButton(
                            text = "Secondary",
                            type = SButtonType.SECONDARY,
                            onClick = { buttonClickCount = 0 },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SChip(
                            text = "Selected Tag",
                            selected = chipSelected1,
                            onClick = { chipSelected1 = !chipSelected1 }
                        )
                        SChip(
                            text = "Unselected Tag",
                            selected = chipSelected2,
                            onClick = { chipSelected2 = !chipSelected2 }
                        )
                        SFilterChip(
                            text = "Latest Filter",
                            trailingIcon = Icons.ArrowDropDown,
                            selected = chipSelected1,
                            onClick = { chipSelected1 = !chipSelected1 }
                        )
                    }
                }
            }

            // 4. Input fields (text fields)
            SHeadingXXLargeText(text = "4. Text Fields")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    SUnderlineTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeHolder = "Type something here (Underline)"
                    )

                    SContainerTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeHolder = "24dp radius with subtle shadow"
                    )

                    SSearchTextField(
                        value = searchInput,
                        onValueChange = { searchInput = it },
                        onSearch = {},
                        placeHolder = "Bouncy search magnifier field"
                    )
                }
            }

            // 5. Selectors and controls (including SCheckBox, SSegmentedControl, SStepper)
            SHeadingXXLargeText(text = "5. Selectors & Steppers")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    // Segmented Control (smooth horizontal sliding)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SLabelLargeText(text = "SSegmentedControl (spring thumb sliding)")
                        SSegmentedControl(
                            segments = listOf("Auto Scan", "Import", "Cloud"),
                            selectedIndex = segmentedIndex,
                            onSegmentSelected = { segmentedIndex = it }
                        )
                    }

                    // Toggle switch and radio button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SLabelLargeText(text = "SToggleSwitch (pill sliding)")
                        SToggleSwitch(
                            checked = switchChecked,
                            onCheckedChange = { switchChecked = it }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SLabelLargeText(text = "SRadioButton (circle scale expansion)")
                        SRadioButton(
                            selected = radioSelected,
                            onClick = { radioSelected = !radioSelected }
                        )
                    }

                    // CheckBox (rounded square bounce check)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SLabelLargeText(text = "SCheckBox (rounded bounce check)")
                        SCheckBox(
                            checked = checkboxChecked,
                            onCheckedChange = { checkboxChecked = it }
                        )
                    }

                    // Stepper (bouncy numeric counter)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SLabelLargeText(text = "SStepper (quantity haptic bounce)")
                        SStepper(
                            value = stepperVal,
                            onValueChange = { stepperVal = it },
                            valueRange = 1..10
                        )
                    }

                    // Slider (precision drag)
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SLabelLargeText(text = "SSlider (custom haptic gesture: ${String.format("%.1f", sliderValue)}x)")
                        SSlider(
                            value = sliderValue,
                            onValueChange = { sliderValue = it },
                            valueRange = 0.5f..2.0f,
                            steps = 5
                        )
                    }
                }
            }

            // 6. Progress indicators (SProgressBar, SCircularProgress, SBadge, SBadgeBox)
            SHeadingXXLargeText(text = "6. Progress & Badges")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    // ProgressBar (linked with progress control buttons)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SLabelLargeText(text = "SProgressBar (smooth expansion interpolation)")
                        SProgressBar(progress = progressVal)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SButton(
                                text = "Progress 20%",
                                type = SButtonType.SECONDARY,
                                onClick = { progressVal = 0.2f },
                                modifier = Modifier.weight(1f)
                            )
                            SButton(
                                text = "Progress 80%",
                                type = SButtonType.SECONDARY,
                                onClick = { progressVal = 0.8f },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Circular Progress & Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            SLabelLargeText(text = "SCircularProgress")
                            SLabelSmallText(text = "Infinite expansion canvas loader", color = Theme.colorScheme.secondaryText)
                        }
                        SCircularProgress()
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            SLabelLargeText(text = "SBadge & SBadgeBox")
                            SLabelSmallText(text = "Spring badge that bounces on count change", color = Theme.colorScheme.secondaryText)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Mini buttons for adjusting count
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                SButton(
                                    text = "-",
                                    type = SButtonType.SECONDARY,
                                    onClick = { if (badgeCount > 0) badgeCount-- },
                                    modifier = Modifier.size(width = 36.dp, height = 32.dp)
                                )
                                SButton(
                                    text = "+",
                                    type = SButtonType.SECONDARY,
                                    onClick = { badgeCount++ },
                                    modifier = Modifier.size(width = 36.dp, height = 32.dp)
                                )
                            }

                            SBadgeBox(
                                badge = {
                                    SBadge(count = badgeCount, visible = badgeCount > 0)
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Theme.colorScheme.outlineVariant, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    SLabelMediumText("Notifications")
                                }
                            }
                        }
                    }
                }
            }

            // 7. Layout and collapsible content (SListItem, SAccordion, SAvatar)
            SHeadingXXLargeText(text = "7. Lists, Avatars & Foldables")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // SAvatar showcase
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            SLabelLargeText(text = "SAvatar (avatar / profile)")
                            SLabelSmallText(text = "Squircle pulsing active ring applied", color = Theme.colorScheme.secondaryText)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SAvatar(
                                initials = "AV",
                                size = AvatarSize.Medium,
                                shape = AvatarShape.Squircle,
                                isActive = avatarActive,
                                onClick = { avatarActive = !avatarActive }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // SListItem settings example
                    SLabelLargeText(text = "SListItem (bounce touch row layout)")
                    SListItem(
                        headline = "Apply Auto Correction Filter",
                        supportingContent = "Automatically corrects lighting imbalance during image scanning.",
                        onClick = {},
                        trailingContent = {
                            SToggleSwitch(checked = true, onCheckedChange = {})
                        }
                    )
                    SListItem(
                        headline = "User Profile Settings",
                        supportingContent = "Account link status: Active",
                        onClick = {},
                        showDivider = false,
                        leadingContent = {
                            SAvatar(initials = "ME", size = AvatarSize.Small, shape = AvatarShape.Circle)
                        }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // SAccordion collapsible example
                    SLabelLargeText(text = "SAccordion (spring sliding accordion)")
                    SAccordion(title = "Advanced Precision Scan Tips", initiallyExpanded = false) {
                        SParagraphMediumText(
                            text = "1. Even if you tilt the camera slightly to avoid shadows, auto-correction will fix the distortion.\n" +
                                    "2. Monotone contrast detects text most clearly in black-and-white scan mode.",
                            color = Theme.colorScheme.secondaryText
                        )
                    }
                }
            }

            // 8. Guides and modals (STooltip, SDialog, SBottomSheet)
            SHeadingXXLargeText(text = "8. Modals, Overlays & Tooltips")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // STooltip control
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SLabelLargeText(text = "STooltip (speech bubble guide)")
                            SButton(
                                text = if (tooltipVisible) "Hide" else "Show",
                                type = SButtonType.SECONDARY,
                                onClick = { tooltipVisible = !tooltipVisible },
                                modifier = Modifier.size(width = 80.dp, height = 32.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .background(Theme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            STooltip(
                                text = "Press the scan button for quick PDF sharing",
                                direction = TooltipDirection.Top,
                                visible = tooltipVisible
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SButton(
                            text = "Decision Popup (SDialog)",
                            onClick = { showDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        SButton(
                            text = "Bottom Sheet (SBottomSheet)",
                            type = SButtonType.SECONDARY,
                            onClick = { showBottomSheet = true },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

    // Interactive popup live rendering
    if (showDialog) {
        SConfirmDialog(
            title = "Change Design Theme",
            message = "Do you really like the Soft Monotone theme design? This system was entirely custom-built with a refined design philosophy.",
            confirmText = "I love it",
            dismissText = "Let me reconsider",
            onConfirm = { showDialog = false },
            onDismissRequest = { showDialog = false }
        )
    }

    // Interactive bottom sheet live rendering
    if (showBottomSheet) {
        SModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            SBottomSheetHeader(title = "Export Options")
            Spacer(modifier = Modifier.height(10.dp))
            SBottomSheetItem(
                text = "Share as PDF Document",
                icon = {
                    SIcon(
                        imageVector = Icons.Share,
                        contentDescription = null,
                        tint = Theme.colorScheme.primaryIcon,
                        modifier = Modifier.size(22.dp)
                    )
                },
                onClick = { showBottomSheet = false }
            )
            SBottomSheetItem(
                text = "Save Image to Gallery",
                icon = {
                    SIcon(
                        imageVector = Icons.Folder,
                        contentDescription = null,
                        tint = Theme.colorScheme.primaryIcon,
                        modifier = Modifier.size(22.dp)
                    )
                },
                onClick = { showBottomSheet = false }
            )
            SBottomSheetItem(
                text = "Permanently Delete File",
                icon = {
                    SIcon(
                        imageVector = Icons.Delete,
                        contentDescription = null,
                        tint = Theme.colorScheme.error,
                        modifier = Modifier.size(22.dp)
                    )
                },
                onClick = { showBottomSheet = false }
            )
        }
    }
}

/**
 * Helper composable that visually displays color information
 */
@Composable
private fun ColorInfoChip(
    name: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .border(1.dp, Theme.colorScheme.outline, RoundedCornerShape(16.dp))
            .background(Theme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(color)
                .border(0.5.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        SLabelSmallText(
            text = name,
            color = Theme.colorScheme.primaryText
        )
    }
}

@Composable
@ComponentPreview
fun DesignSystemShowcasePreview() {
    Theme {
        DesignSystemShowcase()
    }
}
