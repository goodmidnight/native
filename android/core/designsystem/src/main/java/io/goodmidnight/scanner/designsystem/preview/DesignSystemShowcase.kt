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
 * - 개편된 Soft Monotone 및 Uber 스타일 디자인시스템의 모든 컴포넌트, 컬러, 타이포그래피, 인터랙션을 총망라하여 보여주는 대통합 프리뷰 대시보드 화면입니다.
 * - 새로 추가 및 고도화된 9종의 컴포넌트(SCheckBox, SSegmentedControl, SProgressBar, SCircularProgress, SBadge, SListItem, SAvatar, SAccordion, STooltip, SStepper)가 전부 포함되어 실시간 테스트 가능합니다.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DesignSystemShowcase() {
    val scrollState = rememberScrollState()

    // 대화형 테스트를 위한 기존 상태
    var buttonClickCount by remember { mutableStateOf(0) }
    var textInput by remember { mutableStateOf("") }
    var searchInput by remember { mutableStateOf("") }
    var chipSelected1 by remember { mutableStateOf(true) }
    var chipSelected2 by remember { mutableStateOf(false) }
    var switchChecked by remember { mutableStateOf(true) }
    var radioSelected by remember { mutableStateOf(true) }
    var sliderValue by remember { mutableFloatStateOf(1.0f) }

    // 신규 & 추가 컴포넌트 대화형 상태
    var checkboxChecked by remember { mutableStateOf(false) }
    var segmentedIndex by remember { mutableStateOf(0) }
    var progressVal by remember { mutableFloatStateOf(0.35f) }
    var badgeCount by remember { mutableStateOf(7) }
    var stepperVal by remember { mutableStateOf(3) }
    var tooltipVisible by remember { mutableStateOf(true) }
    var avatarActive by remember { mutableStateOf(true) }

    // 팝업 및 바텀시트 트리거 상태
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(bottom = 56.dp)
    ) {
        // 탑바 프리뷰
        STitleTopBar(
            title = "Uber Monotone DS",
            onBack = {}
        )

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. 컬러 팔레트 소개 카드
            SHeadingXXLargeText(text = "1. Color Palette")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SParagraphMediumText(
                        text = "완전한 블랙/화이트를 배제한 차분한 오프톤(Off-tone) 계열의 모노톤 팔레트입니다. 눈의 피로를 최소화하며, 극도로 정돈된 느낌을 줍니다.",
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

            // 2. 타이포그래피 소개 카드
            SHeadingXXLargeText(text = "2. Typography")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SDisplaySmallText(text = "Display Small (28sp)")
                    SHeadingXXLargeText(text = "Heading XXLarge (24sp)")
                    SHeadingMediumText(text = "Heading Medium (18sp)")
                    SParagraphMediumText(text = "Paragraph Medium (14sp) - 가독성을 지향하는 본문 텍스트 렌더링입니다.")
                    SLabelMediumText(text = "Label Medium (14sp) - 컨트롤 및 조작 계열 타이포그래피")
                }
            }

            // 3. 버튼 및 칩
            SHeadingXXLargeText(text = "3. Buttons & Chips")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SParagraphMediumText(
                        text = "모든 터치 가능 요소는 누르는 즉시 scale 0.96f로 쫀득하게 작아졌다가 돌아옵니다. 곡률은 24dp로 크게 적용되었습니다.",
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
                            text = "최신순 필터",
                            trailingIcon = Icons.ArrowDropDown,
                            selected = chipSelected1,
                            onClick = { chipSelected1 = !chipSelected1 }
                        )
                    }
                }
            }

            // 4. 입력창 (텍스트 필드)
            SHeadingXXLargeText(text = "4. Text Fields")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    SUnderlineTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeHolder = "여기에 글자를 입력해보세요 (Underline)"
                    )

                    SContainerTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeHolder = "24dp 곡률 및 은은한 그림자 입력창"
                    )

                    SSearchTextField(
                        value = searchInput,
                        onValueChange = { searchInput = it },
                        onSearch = {},
                        placeHolder = "통통 튀는 검색 돋보기 필드"
                    )
                }
            }

            // 5. 셀렉터 및 조작도구 고도화 (SCheckBox, SSegmentedControl, SStepper 등 신규 포함)
            SHeadingXXLargeText(text = "5. Selectors & Steppers")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    // Segmented Control (스무스 좌우 슬라이딩)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SLabelLargeText(text = "SSegmentedControl (스프링 썸 슬라이딩)")
                        SSegmentedControl(
                            segments = listOf("자동 스캔", "가져오기", "클라우드"),
                            selectedIndex = segmentedIndex,
                            onSegmentSelected = { segmentedIndex = it }
                        )
                    }

                    // 토글 스위치 및 라디오 버튼
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SLabelLargeText(text = "SToggleSwitch (알약 슬라이딩)")
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
                        SLabelLargeText(text = "SRadioButton (원형 스케일 팽창)")
                        SRadioButton(
                            selected = radioSelected,
                            onClick = { radioSelected = !radioSelected }
                        )
                    }

                    // CheckBox (둥근 사각 체크 팽창)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SLabelLargeText(text = "SCheckBox (둥근 사각 바운스 체크)")
                        SCheckBox(
                            checked = checkboxChecked,
                            onCheckedChange = { checkboxChecked = it }
                        )
                    }

                    // Stepper (통통 튀는 숫자 카운터)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SLabelLargeText(text = "SStepper (수량 햅틱 팽창)")
                        SStepper(
                            value = stepperVal,
                            onValueChange = { stepperVal = it },
                            valueRange = 1..10
                        )
                    }

                    // Slider (정밀 드래그)
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SLabelLargeText(text = "SSlider (커스텀 햅틱 제스처: ${String.format("%.1f", sliderValue)}x)")
                        SSlider(
                            value = sliderValue,
                            onValueChange = { sliderValue = it },
                            valueRange = 0.5f..2.0f,
                            steps = 5
                        )
                    }
                }
            }

            // 6. 진행률 및 표시기 (SProgressBar, SCircularProgress, SBadge, SBadgeBox 포함)
            SHeadingXXLargeText(text = "6. Progress & Badges")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    // ProgressBar (진행률 조절 버튼과 연계)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SLabelLargeText(text = "SProgressBar (스무스 팽창 보간)")
                        SProgressBar(progress = progressVal)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SButton(
                                text = "진행률 20%",
                                type = SButtonType.SECONDARY,
                                onClick = { progressVal = 0.2f },
                                modifier = Modifier.weight(1f)
                            )
                            SButton(
                                text = "진행률 80%",
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
                            SLabelSmallText(text = "무한 팽창 캔버스 로더", color = Theme.colorScheme.secondaryText)
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
                            SLabelSmallText(text = "숫자 변경 시 톡 튀는 스프링 배지", color = Theme.colorScheme.secondaryText)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // 숫자 조절용 미니 버튼들
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
                                    SLabelMediumText("알림")
                                }
                            }
                        }
                    }
                }
            }

            // 7. 레이아웃 및 접이식 콘텐츠 (SListItem, SAccordion, SAvatar 포함)
            SHeadingXXLargeText(text = "7. Lists, Avatars & Foldables")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // SAvatar 전시
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            SLabelLargeText(text = "SAvatar (아바타 / 프로필)")
                            SLabelSmallText(text = "스쿼클 펄싱 액티브 링 적용", color = Theme.colorScheme.secondaryText)
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

                    // SListItem 설정창 예시
                    SLabelLargeText(text = "SListItem (바운스 터치 행 레이아웃)")
                    SListItem(
                        headline = "자동 보정 필터 적용",
                        supportingContent = "이미지 스캔 시 조명 불균형을 자동 해소합니다.",
                        onClick = {},
                        trailingContent = {
                            SToggleSwitch(checked = true, onCheckedChange = {})
                        }
                    )
                    SListItem(
                        headline = "사용자 프로필 설정",
                        supportingContent = "계정 연동 상태: 활성",
                        onClick = {},
                        showDivider = false,
                        leadingContent = {
                            SAvatar(initials = "ME", size = AvatarSize.Small, shape = AvatarShape.Circle)
                        }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // SAccordion 접이식 예시
                    SLabelLargeText(text = "SAccordion (스프링 슬라이딩 아코디언)")
                    SAccordion(title = "고급 이미지 정밀 스캔 팁", initiallyExpanded = false) {
                        SParagraphMediumText(
                            text = "1. 그림자가 지지 않도록 카메라 각도를 약간 비스듬하게 유지해도 자동 보정이 왜곡을 펴줍니다.\n" +
                                    "2. 모노톤 명암비는 흑백 스캔 모드에서 텍스트를 가장 또렷하게 검출합니다.",
                            color = Theme.colorScheme.secondaryText
                        )
                    }
                }
            }

            // 8. 가이드 및 모달 계열 (STooltip, SDialog, SBottomSheet)
            SHeadingXXLargeText(text = "8. Modals, Overlays & Tooltips")
            SCard(style = CardStyle.PRIMARY) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // STooltip 제어
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SLabelLargeText(text = "STooltip (말풍선 도움말 가이드)")
                            SButton(
                                text = if (tooltipVisible) "숨기기" else "보이기",
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
                                text = "스캔 버튼을 누르면 빠른 PDF 공유가 가능합니다",
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
                            text = "의사결정 팝업(SDialog)",
                            onClick = { showDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        SButton(
                            text = "바텀시트(SBottomSheet)",
                            type = SButtonType.SECONDARY,
                            onClick = { showBottomSheet = true },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

    // 대화형 팝업 실시간 렌더링
    if (showDialog) {
        SConfirmDialog(
            title = "디자인 테마 변경",
            message = "정말 Soft Monotone 테마 디자인을 마음에 들어 하시나요? 이 시스템은 우버의 철학을 가득 담아 완전히 독자 제작되었습니다.",
            confirmText = "마음에 듭니다",
            dismissText = "다시 볼래요",
            onConfirm = { showDialog = false },
            onDismissRequest = { showDialog = false }
        )
    }

    // 대화형 바텀시트 실시간 렌더링
    if (showBottomSheet) {
        SModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            SBottomSheetHeader(title = "내보내기 옵션")
            Spacer(modifier = Modifier.height(10.dp))
            SBottomSheetItem(
                text = "PDF 문서로 공유하기",
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
                text = "갤러리에 이미지로 보관하기",
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
                text = "영구히 파일 삭제하기",
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
 * 컬러 정보를 직관적으로 표출하는 헬퍼 컴포저블
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
