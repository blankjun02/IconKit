package com.blacksoft.iconkit

import android.os.Build
import android.os.Bundle
import android.view.RoundedCorner
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.state.ToggleableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.Rule
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.core.graphics.drawable.toBitmap
import com.blacksoft.iconkit.core.IconAdaptation
import com.blacksoft.iconkit.core.IconSource
import com.blacksoft.iconkit.core.InspectedApp
import com.blacksoft.iconkit.core.IconRequestArchive
import com.blacksoft.iconkit.ui.AppsViewModel
import com.blacksoft.iconkit.ui.AppIconDetailScreen
import com.blacksoft.iconkit.ui.AboutScreen
import com.blacksoft.iconkit.ui.ManagerStyleDropdownItem
import com.blacksoft.iconkit.ui.IconDetectionSource
import com.blacksoft.iconkit.ui.iconRenderSizes
import com.blacksoft.iconkit.ui.ManagerBlurredBar
import com.blacksoft.iconkit.ui.rememberManagerBlurBackdrop
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.blacksoft.iconkit.ui.AppPreferences
import com.blacksoft.iconkit.ui.ThemeMode
import com.blacksoft.iconkit.ui.UiStyle
import com.blacksoft.iconkit.ui.StatisticsPage
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.InputField as MiuixInputField
import top.yukonga.miuix.kmp.basic.SearchBar as MiuixSearchBar
import top.yukonga.miuix.kmp.basic.PullToRefresh as MiuixPullToRefresh
import top.yukonga.miuix.kmp.basic.rememberPullToRefreshState as rememberMiuixPullToRefreshState
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator as MiuixCircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Slider as MiuixSlider
import top.yukonga.miuix.kmp.basic.IconButton as MiuixIconButton
import top.yukonga.miuix.kmp.basic.Icon as MiuixIcon
import top.yukonga.miuix.kmp.basic.Checkbox as MiuixCheckbox
import top.yukonga.miuix.kmp.basic.FloatingActionButton as MiuixFloatingActionButton
import top.yukonga.miuix.kmp.basic.NavigationBar as MiuixNavigationBar
import top.yukonga.miuix.kmp.basic.NavigationBarItem as MiuixNavigationBarItem
import top.yukonga.miuix.kmp.basic.NavigationBarDisplayMode
import top.yukonga.miuix.kmp.basic.FloatingNavigationBar
import top.yukonga.miuix.kmp.basic.FloatingNavigationBarItem
import top.yukonga.miuix.kmp.basic.FloatingToolbarDefaults
import top.yukonga.miuix.kmp.basic.DropdownEntry
import top.yukonga.miuix.kmp.basic.DropdownItem
import top.yukonga.miuix.kmp.menu.OverlayIconDropdownMenu
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Refresh
import top.yukonga.miuix.kmp.icon.extended.MoreCircle
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Sort
import com.blacksoft.iconkit.ui.icons.IconKitGlassIcons
import com.blacksoft.iconkit.ui.icons.OuterScreenApps
import com.blacksoft.iconkit.ui.icons.AppData
import com.blacksoft.iconkit.ui.icons.SettingsFill
import com.blacksoft.iconkit.ui.icons.RenderResolutionIcon
import top.yukonga.miuix.kmp.icon.basic.ArrowRight
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.PagerNavigationSpringSpec
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.blur.BlurDefaults
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.progressiveTextureBlur
import top.yukonga.miuix.kmp.blur.ProgressiveBlur
import androidx.compose.ui.graphics.RectangleShape
import top.yukonga.miuix.kmp.glass.GlassNavigationBar
import top.yukonga.miuix.kmp.glass.GlassNavigationItem
import top.yukonga.miuix.kmp.basic.Scaffold as MiuixScaffold
import top.yukonga.miuix.kmp.basic.Text as MiuixText
import top.yukonga.miuix.kmp.basic.TopAppBar as MiuixTopAppBar
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = AppPreferences(this)
        setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides (application as IconKitApplication)) {
                IconKitApp(preferences)
            }
        }
    }
}

@Composable
private fun IconKitApp(preferences: AppPreferences) {
    val activity = LocalContext.current as? ComponentActivity
    // Keep navigation outside the theme subtree so a style/theme replacement cannot reset it.
    val pagerState = rememberPagerState(initialPage = preferences.selectedTab, pageCount = { 3 })
    LaunchedEffect(pagerState.settledPage) {
        preferences.updateSelectedTab(pagerState.settledPage)
    }
    val systemDark = isSystemInDarkTheme()
    val dark = when (preferences.themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val systemDensity = LocalDensity.current
    val scaledDensity = remember(systemDensity, preferences.pageScale) {
        Density(systemDensity.density * preferences.pageScale, systemDensity.fontScale)
    }

    DisposableEffect(dark) {
        activity?.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.Transparent.value.toInt(), Color.Transparent.value.toInt()) { dark },
            navigationBarStyle = SystemBarStyle.auto(Color.Transparent.value.toInt(), Color.Transparent.value.toInt()) { dark },
        )
        activity?.window?.isNavigationBarContrastEnforced = false
        onDispose { }
    }

    CompositionLocalProvider(LocalDensity provides scaledDensity) {
        when (preferences.uiStyle) {
            UiStyle.MATERIAL3 -> MaterialAppTheme(dark, preferences.dynamicColor) {
                MainShell(preferences, pagerState)
            }
            UiStyle.MIUIX -> {
                val mode = when (preferences.themeMode) {
                    ThemeMode.SYSTEM -> if (preferences.miuixMonet) ColorSchemeMode.MonetSystem else ColorSchemeMode.System
                    ThemeMode.LIGHT -> if (preferences.miuixMonet) ColorSchemeMode.MonetLight else ColorSchemeMode.Light
                    ThemeMode.DARK -> if (preferences.miuixMonet) ColorSchemeMode.MonetDark else ColorSchemeMode.Dark
                }
                val controller = remember(mode) { ThemeController(mode) }
                MiuixTheme(controller = controller) { MainShell(preferences, pagerState) }
            }
        }
    }
}

@Composable
private fun MaterialAppTheme(dark: Boolean, dynamic: Boolean, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val scheme = when {
        dynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && dark -> dynamicDarkColorScheme(context)
        dynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicLightColorScheme(context)
        dark -> darkColorScheme()
        else -> lightColorScheme()
    }
    MaterialTheme(colorScheme = scheme, content = content)
}

@Composable
private fun MainShell(preferences: AppPreferences, pagerState: PagerState) {
    val appsViewModel = viewModel<AppsViewModel>()
    val selectedApp = appsViewModel.selectedApp
    var displayedApp by remember { mutableStateOf<InspectedApp?>(null) }
    LaunchedEffect(selectedApp) { if (selectedApp != null) displayedApp = selectedApp }
    var backProgress by remember { mutableStateOf(0f) }
    val overlayVisible = preferences.showThemePage || preferences.showAboutPage || selectedApp != null
    val coveredProgress by animateFloatAsState(
        targetValue = if (overlayVisible) 1f - backProgress.coerceIn(0f, 1f) else 0f,
        animationSpec = tween(if (backProgress > 0f) 0 else 220),
        label = "coveredPage",
    )
    val layoutDirection = LocalLayoutDirection.current

    Box(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().graphicsLayer {
            translationX = (if (layoutDirection == LayoutDirection.Rtl) 1f else -1f) * size.width * .25f * coveredProgress
            alpha = 1f - .1f * coveredProgress
        }) {
        when (preferences.uiStyle) {
            UiStyle.MATERIAL3 -> MaterialShell(preferences, false, pagerState)
            UiStyle.MIUIX -> MiuixShell(preferences, false, pagerState)
        }
        }

        PredictivePageOverlay(
            visible = preferences.showThemePage,
            enabled = selectedApp == null && !preferences.showAboutPage,
            onBackProgress = { backProgress = it },
            onBack = preferences::closeThemePage,
        ) {
            when (preferences.uiStyle) {
                UiStyle.MATERIAL3 -> MaterialShell(preferences, true, pagerState)
                UiStyle.MIUIX -> MiuixShell(preferences, true, pagerState)
            }
        }

        PredictivePageOverlay(
            visible = preferences.showAboutPage,
            enabled = selectedApp == null,
            onBackProgress = { backProgress = it },
            onBack = preferences::closeAboutPage,
        ) {
            AboutScreen(preferences.uiStyle, preferences.enableBlur, preferences::closeAboutPage)
        }

        PredictivePageOverlay(
            visible = selectedApp != null,
            onBackProgress = { backProgress = it },
            onBack = appsViewModel::closeDetails,
        ) {
            displayedApp?.let { AppIconDetailScreen(it, preferences.uiStyle, preferences.enableBlur, preferences.iconRenderSize, appsViewModel::closeDetails) }
        }
    }
}

@Composable
private fun PredictivePageOverlay(
    visible: Boolean,
    enabled: Boolean = true,
    onBackProgress: (Float) -> Unit,
    onBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    val backProgress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val cornerRadius = rememberSystemWindowCornerRadius()
    LaunchedEffect(visible) {
        if (!visible && backProgress.value != 0f) {
            delay(350)
            backProgress.snapTo(0f)
        }
    }
    PredictiveBackHandler(enabled = visible && enabled) { events ->
        try {
            events.collect { event -> backProgress.snapTo(event.progress); onBackProgress(event.progress) }
            onBack()
            onBackProgress(0f)
        } catch (_: CancellationException) {
            // The gesture coroutine is cancelled on a cancelled back; recover in a live scope.
            scope.launch {
                backProgress.animateTo(0f, spring()) { onBackProgress(value) }
                onBackProgress(0f)
            }
        }
    }
    AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
        Box(
            Modifier.fillMaxSize()
                .graphicsLayer { alpha = .5f * (1f - backProgress.value.coerceIn(0f, 1f)) }
                .background(Color.Black),
        )
    }
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally { it },
        exit = slideOutHorizontally { it } + fadeOut(),
    ) {
        Box(
            Modifier.fillMaxSize().graphicsLayer {
                translationX = size.width * backProgress.value
                val scale = 1f - backProgress.value * .04f
                scaleX = scale
                scaleY = scale
                shape = RoundedCornerShape(cornerRadius)
                clip = cornerRadius > 0.dp
            }
        ) { content() }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
private fun rememberSystemWindowCornerRadius(): androidx.compose.ui.unit.Dp {
    val view = LocalView.current
    val context = LocalContext.current
    val density = LocalDensity.current
    val insets = view.rootWindowInsets
    val radiusPx = remember(view, insets, context) {
        val rounded = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            insets?.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_LEFT)?.radius?.takeIf { it > 0 }
        } else null
        rounded ?: context.resources.getIdentifier("rounded_corner_radius_bottom", "dimen", "android")
            .takeIf { it != 0 }?.let(context.resources::getDimensionPixelSize) ?: 0
    }
    return with(density) { radiusPx.toDp() }
}

private val tabTitles = intArrayOf(R.string.all_apps, R.string.statistics, R.string.settings)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MaterialShell(preferences: AppPreferences, showThemePage: Boolean, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    if (showThemePage) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.theme)) },
                    navigationIcon = {
                        androidx.compose.material3.IconButton(onClick = preferences::closeThemePage) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    }
                )
            }
        ) { padding -> MaterialThemeSettings(preferences, Modifier.padding(padding)) }
        return
    }
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            SlidingMaterialTopBars(preferences, pagerState)
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) {
                val icons = listOf(Icons.Filled.Apps, Icons.Filled.PieChart, Icons.Filled.Settings)
                tabTitles.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = pagerState.navigationSelectedPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        icon = { Icon(icons[index], contentDescription = null) },
                        label = { Text(stringResource(title)) }
                    )
                }
            }
        }
    ) { padding -> Page(preferences, pagerState, Modifier.padding(padding)) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SlidingMaterialTopBars(preferences: AppPreferences, pagerState: PagerState) {
    Box(Modifier.fillMaxWidth().clipToBounds()) {
        tabTitles.forEachIndexed { index, title ->
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth().graphicsLayer {
                    translationX = (index - pagerState.currentPage - pagerState.currentPageOffsetFraction) * size.width
                },
                title = { Text(stringResource(title)) },
                actions = { if (index == 0) MaterialAppBarActions(preferences) },
            )
        }
    }
}

@Composable
private fun MiuixShell(preferences: AppPreferences, showThemePage: Boolean, pagerState: PagerState) {
    val scrollBehavior = MiuixScrollBehavior()
    val appsBackdrop = rememberManagerBlurBackdrop(preferences.enableBlur)
    val statisticsBackdrop = rememberManagerBlurBackdrop(preferences.enableBlur)
    val settingsBackdrop = rememberManagerBlurBackdrop(preferences.enableBlur)
    // As in the MIUIX demo, the bottom bar samples one layer containing the composed pager.
    val pagerBackdrop = rememberManagerBlurBackdrop(preferences.enableBlur)
    val pageBackdrops = listOf(appsBackdrop, statisticsBackdrop, settingsBackdrop)
    val blurBackdrop = pageBackdrops[pagerState.currentPage]
    if (showThemePage) {
        MiuixScaffold(
            topBar = {
                MiuixTopAppBar(
                    title = stringResource(R.string.theme),
                    navigationIcon = {
                        MiuixIconButton(onClick = preferences::closeThemePage) {
                            MiuixIcon(MiuixIcons.Back, null)
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { padding -> MiuixThemeSettings(preferences, Modifier.padding(padding)) }
        return
    }
    MiuixScaffold(
        topBar = {
            ManagerBlurredBar(if (pagerState.currentPage == 0) null else blurBackdrop) {
                SlidingMiuixTopBars(preferences, pagerState, scrollBehavior, blurBackdrop != null)
            }
        },
        bottomBar = {
            if (preferences.floatingNavigationBar) {
                MiuixFloatingBottomBar(pagerState, pagerBackdrop)
            } else {
                ManagerBlurredBar(pagerBackdrop) {
                    MiuixBottomBar(pagerState, transparent = pagerBackdrop != null)
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().then(if (pagerBackdrop != null) Modifier.layerBackdrop(pagerBackdrop) else Modifier)) {
            Page(preferences, pagerState, Modifier.fillMaxSize(), scrollBehavior, padding, pageBackdrops)
        }
    }
}

@Composable
private fun SlidingMiuixTopBars(
    preferences: AppPreferences,
    pagerState: PagerState,
    scrollBehavior: ScrollBehavior,
    transparent: Boolean,
) {
    Box(Modifier.fillMaxWidth().clipToBounds()) {
        tabTitles.forEachIndexed { index, title ->
            MiuixTopAppBar(
                modifier = Modifier.fillMaxWidth().graphicsLayer {
                    translationX = (index - pagerState.currentPage - pagerState.currentPageOffsetFraction) * size.width
                },
                color = if (transparent) Color.Transparent else MiuixTheme.colorScheme.surface,
                title = stringResource(title),
                actions = { if (index == 0) MiuixAppBarActions(preferences) },
                scrollBehavior = scrollBehavior,
            )
        }
    }
}

@Composable
private fun MiuixFloatingBottomBar(pagerState: PagerState, backdrop: LayerBackdrop?) {
    val scope = rememberCoroutineScope()
    val icons = listOf(IconKitGlassIcons.OuterScreenApps, IconKitGlassIcons.AppData, IconKitGlassIcons.SettingsFill)
    val items = tabTitles.mapIndexed { index, title -> GlassNavigationItem(icon = icons[index], label = stringResource(title)) }
    GlassNavigationBar(
        items = items,
        selectedIndex = pagerState.navigationSelectedPage,
        onSelect = { index -> scope.launch { pagerState.animateScrollToPage(index) } },
        backdrop = backdrop,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
    )
}

@Composable
private fun MiuixBottomBar(pagerState: PagerState, transparent: Boolean = false) {
    val scope = rememberCoroutineScope()
    MiuixNavigationBar(
        color = if (transparent) Color.Transparent else MiuixTheme.colorScheme.surface,
        showDivider = true,
        mode = NavigationBarDisplayMode.IconAndText,
    ) {
        val icons = listOf(IconKitGlassIcons.OuterScreenApps, IconKitGlassIcons.AppData, IconKitGlassIcons.SettingsFill)
        tabTitles.forEachIndexed { index, title ->
            MiuixNavigationBarItem(
                selected = pagerState.navigationSelectedPage == index,
                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                icon = icons[index],
                label = stringResource(title),
            )
        }
    }
}

private val PagerState.navigationSelectedPage: Int
    get() = if (isScrollInProgress) targetPage else currentPage

@Composable
private fun Page(
    preferences: AppPreferences,
    pagerState: PagerState,
    modifier: Modifier,
    miuixScrollBehavior: ScrollBehavior? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    backdrops: List<LayerBackdrop?> = emptyList(),
    onSearchBottomChanged: (androidx.compose.ui.unit.Dp) -> Unit = {},
) {
    val flingBehavior = PagerDefaults.flingBehavior(
        state = pagerState,
        snapAnimationSpec = PagerNavigationSpringSpec,
    )
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        flingBehavior = flingBehavior,
        pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(pagerState, Orientation.Horizontal),
        beyondViewportPageCount = 2,
        verticalAlignment = Alignment.Top,
    ) { page ->
        val backdrop = backdrops.getOrNull(page)
        when (page) {
            0 -> AllAppsPage(preferences, Modifier.fillMaxSize(), miuixScrollBehavior, contentPadding, backdrop, onSearchBottomChanged)
            1 -> StatisticsPage(preferences, Modifier.fillMaxSize().then(if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier), contentPadding)
            else -> when (preferences.uiStyle) {
                UiStyle.MATERIAL3 -> MaterialSettings(preferences, Modifier.fillMaxSize())
                UiStyle.MIUIX -> MiuixSettings(preferences, Modifier.fillMaxSize().then(if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier), miuixScrollBehavior, contentPadding)
            }
        }
    }
}

private enum class AppSort { NAME, PACKAGE }

@Composable
private fun MaterialAppBarActions(preferences: AppPreferences) {
    Box {
        androidx.compose.material3.IconButton(onClick = { preferences.updateShowSortMenu(true) }) {
            Icon(Icons.AutoMirrored.Filled.Sort, stringResource(R.string.sort_by_name))
        }
        DropdownMenu(preferences.showSortMenu, { preferences.updateShowSortMenu(false) }) {
            FilterMenuItem(stringResource(R.string.sort_by_name), !preferences.sortByPackage) {
                preferences.updateSortByPackage(false); preferences.updateShowSortMenu(false)
            }
            FilterMenuItem(stringResource(R.string.sort_by_package), preferences.sortByPackage) {
                preferences.updateSortByPackage(true); preferences.updateShowSortMenu(false)
            }
            FilterMenuItem(stringResource(R.string.reverse_sort), preferences.reverseAppSort) {
                preferences.updateReverseAppSort(!preferences.reverseAppSort); preferences.updateShowSortMenu(false)
            }
        }
    }
    Box {
        androidx.compose.material3.IconButton(onClick = { preferences.updateShowAppFilterMenu(true) }) {
            Icon(Icons.Filled.MoreVert, stringResource(R.string.show_system_apps))
        }
        DropdownMenu(preferences.showAppFilterMenu, { preferences.updateShowAppFilterMenu(false) }) {
            FilterMenuItem(stringResource(R.string.show_system_apps), preferences.showSystemApps) {
                preferences.updateShowSystemApps(!preferences.showSystemApps); preferences.updateShowAppFilterMenu(false)
            }
        }
    }
}

@Composable
private fun MiuixAppBarActions(preferences: AppPreferences) {
    val sortByName = stringResource(R.string.sort_by_name)
    val sortByPackage = stringResource(R.string.sort_by_package)
    val reverseSort = stringResource(R.string.reverse_sort)
    val showSystemApps = stringResource(R.string.show_system_apps)
    val sortEntry = remember(preferences.sortByPackage, preferences.reverseAppSort, sortByName, sortByPackage, reverseSort) {
        DropdownEntry(
            items = listOf(
                DropdownItem(sortByName, selected = !preferences.sortByPackage, onClick = { preferences.updateSortByPackage(false) }),
                DropdownItem(sortByPackage, selected = preferences.sortByPackage, onClick = { preferences.updateSortByPackage(true) }),
                DropdownItem(reverseSort, selected = preferences.reverseAppSort, onClick = {
                    preferences.updateReverseAppSort(!preferences.reverseAppSort)
                }),
            ),
        )
    }
    val filterEntry = remember(preferences.showSystemApps, showSystemApps) {
        DropdownEntry(
            items = listOf(
                DropdownItem(showSystemApps, selected = preferences.showSystemApps, onClick = {
                    preferences.updateShowSystemApps(!preferences.showSystemApps)
                }),
            ),
        )
    }
    OverlayIconDropdownMenu(entry = sortEntry) {
        MiuixIcon(MiuixIcons.Sort, sortByName, modifier = Modifier.size(24.dp))
    }
    Spacer(Modifier.width(8.dp))
    OverlayIconDropdownMenu(entry = filterEntry) {
        MiuixIcon(MiuixIcons.MoreCircle, showSystemApps, modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun AllAppsPage(
    preferences: AppPreferences,
    modifier: Modifier,
    miuixScrollBehavior: ScrollBehavior? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    backdrop: LayerBackdrop? = null,
    onSearchBottomChanged: (androidx.compose.ui.unit.Dp) -> Unit = {},
) {
    val appsViewModel = viewModel<AppsViewModel>()
    val apps = appsViewModel.apps
    val loading = appsViewModel.isRefreshing && !appsViewModel.hasLoaded
    val error = appsViewModel.error != null && !appsViewModel.hasLoaded
    var query by remember { mutableStateOf("") }
    val sort = if (preferences.sortByPackage) AppSort.PACKAGE else AppSort.NAME
    val reversed = preferences.reverseAppSort
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val materialRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    val selected = appsViewModel.selectedPackages
    BackHandler(enabled = selected.isNotEmpty()) { appsViewModel.clearSelection() }
    val onAppClick: (InspectedApp) -> Unit = { app ->
        if (selected.isNotEmpty()) appsViewModel.toggleSelection(app) else appsViewModel.openDetails(app)
    }
    val onAppLongClick: (InspectedApp) -> Unit = appsViewModel::toggleSelection
    val shareSelection: () -> Unit = {
        val chosen = apps.filter { it.packageName in selected }
        scope.launch {
            val archive = withContext(Dispatchers.IO) { IconRequestArchive.create(context, chosen) }
            IconRequestArchive.share(context, archive)
        }
    }

    LaunchedEffect(Unit) { appsViewModel.loadIfNeeded() }

    val visibleApps = remember(apps, query, sort, reversed, preferences.showSystemApps) {
        val comparator = when (sort) {
            AppSort.NAME -> Comparator<InspectedApp> { first, second ->
                String.CASE_INSENSITIVE_ORDER.compare(first.label.toString(), second.label.toString())
            }
            AppSort.PACKAGE -> Comparator<InspectedApp> { first, second ->
                String.CASE_INSENSITIVE_ORDER.compare(first.packageName, second.packageName)
            }
        }
        apps.asSequence()
            .filter { preferences.showSystemApps || !it.isSystemApp }
            .filter { query.isBlank() || it.label.contains(query, true) || it.packageName.contains(query, true) }
            .sortedWith(if (reversed) comparator.reversed() else comparator)
            .toList()
    }

    if (preferences.uiStyle == UiStyle.MIUIX) {
        MiuixAppsContent(preferences, modifier, visibleApps, query, { query = it }, appsViewModel,
            selected, onAppClick, onAppLongClick, shareSelection, miuixScrollBehavior, contentPadding, backdrop, onSearchBottomChanged)
        return
    }

    Box(modifier.fillMaxSize()) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                placeholder = { Text(stringResource(R.string.search_apps)) }
            )
        }
        Spacer(Modifier.height(12.dp))

        when {
            loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            error -> EmptyAppsState(stringResource(R.string.load_failed))
            visibleApps.isEmpty() -> EmptyAppsState(stringResource(R.string.no_apps))
            else -> PullToRefreshBox(
                isRefreshing = appsViewModel.isRefreshing,
                onRefresh = { haptic.performHapticFeedback(HapticFeedbackType.VirtualKey); appsViewModel.refresh() },
                modifier = Modifier.fillMaxSize(),
                state = materialRefreshState,
                indicator = {
                    PullToRefreshDefaults.LoadingIndicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = appsViewModel.isRefreshing,
                        state = materialRefreshState,
                    )
                },
            ) { MaterialAppList(visibleApps, preferences.iconDetectionSource, selected, onAppClick, onAppLongClick) }
        }
    }
    if (selected.isNotEmpty()) {
        FloatingActionButton(
            onClick = shareSelection,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
        ) { Icon(Icons.AutoMirrored.Filled.Send, stringResource(R.string.send_selected_apps, selected.size)) }
    }
    }
}

@Composable
private fun MiuixAppsContent(
    preferences: AppPreferences,
    modifier: Modifier,
    apps: List<InspectedApp>,
    query: String,
    onQuery: (String) -> Unit,
    viewModel: AppsViewModel,
    selected: Set<String>,
    onClick: (InspectedApp) -> Unit,
    onLongClick: (InspectedApp) -> Unit,
    onShare: () -> Unit,
    scrollBehavior: ScrollBehavior?,
    contentPadding: PaddingValues,
    backdrop: LayerBackdrop?,
    onSearchBottomChanged: (androidx.compose.ui.unit.Dp) -> Unit,
) {
    val refreshState = rememberMiuixPullToRefreshState()
    var searchExpanded by remember { mutableStateOf(false) }
    var searchHeight by remember { mutableStateOf(0) }
    val searchHeightDp = with(LocalDensity.current) { searchHeight.toDp() }
    val listTop = contentPadding.calculateTopPadding() + searchHeightDp
    LaunchedEffect(listTop) { onSearchBottomChanged(listTop) }
    Box(modifier.fillMaxSize()) {
        MiuixPullToRefresh(
                isRefreshing = viewModel.isRefreshing && viewModel.isManualRefresh,
                onRefresh = { if (!viewModel.isRefreshing) viewModel.refresh() },
                pullToRefreshState = refreshState,
                modifier = Modifier.fillMaxSize().then(if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier),
                contentPadding = PaddingValues(top = listTop),
                refreshTexts = listOf(
                    stringResource(R.string.refresh_pulling),
                    stringResource(R.string.refresh_release),
                    stringResource(R.string.refresh_refresh),
                    stringResource(R.string.refresh_complete),
                ),
            ) { MiuixAppList(apps, preferences.iconDetectionSource, selected, onClick, onLongClick, scrollBehavior, contentPadding, listTop, viewModel) }
    if (backdrop != null && searchHeight > 0) {
        Box(
            Modifier.fillMaxWidth().height(listTop)
                .textureBlur(
                    backdrop = backdrop,
                    shape = RectangleShape,
                    blurRadius = 25f,
                    colors = BlurDefaults.blurColors(blendColors = listOf(BlendColorEntry(MiuixTheme.colorScheme.surface.copy(alpha = .8f)))),
                ),
        )
    }
    Box(Modifier.fillMaxWidth().padding(top = contentPadding.calculateTopPadding())) {
    Column(
        Modifier.fillMaxWidth()
            .onSizeChanged { searchHeight = it.height }
            .then(if (backdrop == null) Modifier.background(MiuixTheme.colorScheme.surface) else Modifier),
    ) {
        MiuixSearchBar(
            modifier = Modifier.fillMaxWidth(),
            inputField = {
                MiuixInputField(
                    query = query, onQueryChange = onQuery,
                    onSearch = { searchExpanded = false }, expanded = searchExpanded,
                    onExpandedChange = { searchExpanded = it }, label = stringResource(R.string.search_apps),
                )
            },
            expanded = searchExpanded, onExpandedChange = { searchExpanded = it }, content = {},
        )
        Spacer(Modifier.height(12.dp))
    }
    }
    if (selected.isNotEmpty()) {
        MiuixFloatingActionButton(
            onClick = onShare,
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = contentPadding.calculateBottomPadding() + 20.dp, end = 20.dp),
        ) { MiuixIcon(Icons.AutoMirrored.Filled.Send, stringResource(R.string.send_selected_apps, selected.size), tint = Color.White) }
    }
    }
}

@Composable
private fun MiuixEmptyState(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { MiuixText(text) }
}

@Composable
private fun FilterMenuItem(text: String, checked: Boolean, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(text) },
        onClick = onClick,
        leadingIcon = { if (checked) Icon(Icons.Filled.Check, null) else Spacer(Modifier.size(24.dp)) }
    )
}

@Composable
private fun EmptyAppsState(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text) }
}

@Composable
private fun MaterialAppList(
    apps: List<InspectedApp>, detectionSource: IconDetectionSource, selected: Set<String>,
    onClick: (InspectedApp) -> Unit, onLongClick: (InspectedApp) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().overScrollVertical(),
        overscrollEffect = null,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        itemsIndexed(apps, key = { _, app -> app.packageName }) { index, app ->
            SegmentedListItem(
                onClick = { onClick(app) },
                onLongClick = { onLongClick(app) },
                shapes = ListItemDefaults.segmentedShapes(index, apps.size),
                leadingContent = { AppIcon(app) },
                content = { Text(app.label.toString(), maxLines = 1) },
                supportingContent = { Text(app.packageName, maxLines = 1) },
                trailingContent = {
                    if (selected.isNotEmpty()) Checkbox(app.packageName in selected, { onClick(app) })
                    else MaterialAdaptationTrailing(app, detectionSource)
                }
            )
        }
    }
}

@Composable
private fun MiuixAppList(
    apps: List<InspectedApp>, detectionSource: IconDetectionSource, selected: Set<String>,
    onClick: (InspectedApp) -> Unit, onLongClick: (InspectedApp) -> Unit,
    scrollBehavior: ScrollBehavior?,
    contentPadding: PaddingValues,
    listTop: androidx.compose.ui.unit.Dp,
    viewModel: AppsViewModel,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (scrollBehavior != null) Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                else Modifier,
            )
            .overScrollVertical(),
        overscrollEffect = null,
        contentPadding = PaddingValues(top = listTop, bottom = contentPadding.calculateBottomPadding() + 16.dp)
    ) {
        if (viewModel.isRefreshing && !viewModel.hasLoaded) {
            item { Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) { MiuixCircularProgressIndicator() } }
        } else if (viewModel.error != null && !viewModel.hasLoaded) {
            item { Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) { MiuixText(stringResource(R.string.load_failed)) } }
        } else if (apps.isEmpty()) {
            item { Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) { MiuixText(stringResource(R.string.no_apps)) } }
        }
        itemsIndexed(apps, key = { _, app -> app.packageName }) { _, app ->
            top.yukonga.miuix.kmp.basic.Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                onClick = { onClick(app) },
                onLongPress = { onLongClick(app) },
            ) {
                BasicComponent(
                    title = app.label.toString(),
                    summary = app.packageName,
                    startAction = { Box(Modifier.padding(end = 4.dp)) { AppIcon(app) } },
                    endActions = {
                        if (selected.isNotEmpty()) {
                            MiuixCheckbox(
                                state = if (app.packageName in selected) ToggleableState.On else ToggleableState.Off,
                                onClick = { onClick(app) },
                            )
                        } else {
                        MiuixAdaptationTrailing(app, detectionSource)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun AppIcon(app: InspectedApp) {
    val drawable = app.runtimeIcon.drawable ?: app.apkIcon.drawable
    if (drawable != null) {
        val bitmap = remember(drawable) { drawable.toBitmap(96, 96).asImageBitmap() }
        Image(bitmap, app.label.toString(), Modifier.size(44.dp))
    } else {
        Spacer(Modifier.size(44.dp))
    }
}

@Composable
private fun MaterialAdaptationTrailing(app: InspectedApp, detectionSource: IconDetectionSource) {
    val icon = app.getIcon(if (detectionSource == IconDetectionSource.APK) IconSource.APK_RESOURCE else IconSource.RUNTIME)
    val adaptive = icon.adaptation == IconAdaptation.ADAPTIVE
    val label = stringResource(if (adaptive) R.string.adaptive_icon else R.string.legacy_icon)
    val background = if (adaptive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    val foreground = if (adaptive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.padding(end = 4.dp).background(background, RoundedCornerShape(4.dp))) {
            Text(
                label,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmallEmphasized,
                color = foreground,
            )
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun MiuixAdaptationTrailing(app: InspectedApp, detectionSource: IconDetectionSource) {
    val icon = app.getIcon(if (detectionSource == IconDetectionSource.APK) IconSource.APK_RESOURCE else IconSource.RUNTIME)
    val adaptive = icon.adaptation == IconAdaptation.ADAPTIVE
    val label = stringResource(if (adaptive) R.string.adaptive_icon else R.string.legacy_icon)
    val background = if (adaptive) MiuixTheme.colorScheme.tertiaryContainer.copy(alpha = .6f)
        else MiuixTheme.colorScheme.secondaryContainer.copy(alpha = .8f)
    val foreground = if (adaptive) MiuixTheme.colorScheme.onTertiaryContainer.copy(alpha = .8f)
        else MiuixTheme.colorScheme.onSecondaryContainer
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.background(background, RoundedCornerShape(6.dp))) {
            MiuixText(
                text = label,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                color = foreground,
                fontSize = 9.sp,
                fontWeight = FontWeight(750),
                maxLines = 1,
                softWrap = false,
            )
        }
        MiuixIcon(
            imageVector = MiuixIcons.Basic.ArrowRight,
            contentDescription = null,
            tint = MiuixTheme.colorScheme.onSurfaceVariantActions,
            modifier = Modifier.padding(start = 8.dp).size(width = 10.dp, height = 16.dp),
        )
    }
}

@Composable
private fun MiuixSettings(
    preferences: AppPreferences,
    modifier: Modifier,
    scrollBehavior: ScrollBehavior? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier
            .fillMaxSize()
            .then(
                if (scrollBehavior != null) Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                else Modifier,
            )
            .overScrollVertical()
            .padding(horizontal = 12.dp),
        overscrollEffect = null,
        contentPadding = contentPadding,
    ) {
        item {
            top.yukonga.miuix.kmp.basic.Card(Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                OverlayDropdownPreference(
                    title = stringResource(R.string.ui_style),
                    summary = stringResource(R.string.ui_style_summary),
                    startAction = { top.yukonga.miuix.kmp.basic.Icon(Icons.Filled.Dashboard, null) },
                    items = listOf(stringResource(R.string.miuix), stringResource(R.string.material3)),
                    selectedIndex = preferences.uiStyle.ordinal,
                    onSelectedIndexChange = { preferences.updateUiStyle(UiStyle.entries[it]) }
                )
                ArrowPreference(
                    title = stringResource(R.string.theme),
                    summary = stringResource(R.string.theme_summary),
                    startAction = { top.yukonga.miuix.kmp.basic.Icon(Icons.Filled.Palette, null) },
                    onClick = preferences::openThemePage
                )
                OverlayDropdownPreference(
                    title = stringResource(R.string.adaptive_detection),
                    summary = stringResource(R.string.adaptive_detection_summary),
                    startAction = { top.yukonga.miuix.kmp.basic.Icon(Icons.AutoMirrored.Filled.Rule, null) },
                    items = listOf(stringResource(R.string.apk_resource), stringResource(R.string.system_api)),
                    selectedIndex = preferences.iconDetectionSource.ordinal,
                    onSelectedIndexChange = { preferences.updateIconDetectionSource(IconDetectionSource.entries[it]) }
                )
                OverlayDropdownPreference(
                    title = stringResource(R.string.icon_render_resolution),
                    summary = stringResource(R.string.icon_render_resolution_summary),
                    startAction = { top.yukonga.miuix.kmp.basic.Icon(RenderResolutionIcon, null) },
                    items = iconRenderSizes.map { "$it × $it px" },
                    selectedIndex = iconRenderSizes.indexOf(preferences.iconRenderSize),
                    onSelectedIndexChange = { preferences.updateIconRenderSize(iconRenderSizes[it]) },
                )
                ArrowPreference(
                    title = stringResource(R.string.about),
                    startAction = { top.yukonga.miuix.kmp.basic.Icon(Icons.Filled.Info, null) },
                    onClick = preferences::openAboutPage,
                )
            }
        }
    }
}

@Composable
private fun MaterialSettings(preferences: AppPreferences, modifier: Modifier) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        item {
            Column(Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                ManagerStyleDropdownItem(
                    icon = Icons.Filled.Dashboard,
                    title = stringResource(R.string.ui_style),
                    summary = stringResource(R.string.ui_style_summary),
                    items = listOf(stringResource(R.string.miuix), stringResource(R.string.material3)),
                    selectedIndex = preferences.uiStyle.ordinal,
                    onItemSelected = { preferences.updateUiStyle(UiStyle.entries[it]) },
                    index = 0,
                    count = 5,
                )
                SegmentedListItem(
                    onClick = preferences::openThemePage,
                    shapes = ListItemDefaults.segmentedShapes(1, 5),
                    leadingContent = { Icon(Icons.Filled.Palette, null) },
                    content = { Text(stringResource(R.string.theme)) },
                    supportingContent = { Text(stringResource(R.string.theme_summary)) },
                    trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null) }
                )
                ManagerStyleDropdownItem(
                    icon = Icons.AutoMirrored.Filled.Rule,
                    title = stringResource(R.string.adaptive_detection),
                    summary = stringResource(R.string.adaptive_detection_summary),
                    items = listOf(stringResource(R.string.apk_resource), stringResource(R.string.system_api)),
                    selectedIndex = preferences.iconDetectionSource.ordinal,
                    onItemSelected = { preferences.updateIconDetectionSource(IconDetectionSource.entries[it]) },
                    index = 2,
                    count = 5,
                    showSelectorIndicator = true,
                )
                ManagerStyleDropdownItem(
                    icon = RenderResolutionIcon,
                    title = stringResource(R.string.icon_render_resolution),
                    summary = stringResource(R.string.icon_render_resolution_summary),
                    items = iconRenderSizes.map { "$it × $it px" },
                    selectedIndex = iconRenderSizes.indexOf(preferences.iconRenderSize),
                    onItemSelected = { preferences.updateIconRenderSize(iconRenderSizes[it]) },
                    index = 3,
                    count = 5,
                    showSelectorIndicator = true,
                )
                SegmentedListItem(
                    onClick = preferences::openAboutPage,
                    shapes = ListItemDefaults.segmentedShapes(4, 5),
                    leadingContent = { Icon(Icons.Filled.Info, null) },
                    content = { Text(stringResource(R.string.about)) },
                    trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null) },
                )
            }
        }
    }
}

@Composable
private fun MaterialThemeSettings(preferences: AppPreferences, modifier: Modifier) {
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { EnumSetting(stringResource(R.string.theme_mode), ThemeMode.entries.toList(), preferences.themeMode, preferences::updateThemeMode) { when(it) { ThemeMode.SYSTEM -> stringResource(R.string.follow_system); ThemeMode.LIGHT -> stringResource(R.string.light); ThemeMode.DARK -> stringResource(R.string.dark) } } }
        item { SwitchSetting(stringResource(R.string.dynamic_color), preferences.dynamicColor, preferences::updateDynamicColor) }
        item { ScaleSetting(preferences) }
    }
}

@Composable
private fun MiuixThemeSettings(preferences: AppPreferences, modifier: Modifier) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 12.dp)) {
        item {
            top.yukonga.miuix.kmp.basic.Card(Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                OverlayDropdownPreference(
                    title = stringResource(R.string.theme_mode),
                    items = listOf(stringResource(R.string.follow_system), stringResource(R.string.light), stringResource(R.string.dark)),
                    selectedIndex = preferences.themeMode.ordinal,
                    onSelectedIndexChange = { preferences.updateThemeMode(ThemeMode.entries[it]) }
                )
                SwitchPreference(title = stringResource(R.string.miuix_monet), checked = preferences.miuixMonet, onCheckedChange = preferences::updateMiuixMonet)
                SwitchPreference(title = stringResource(R.string.blur), checked = preferences.enableBlur, onCheckedChange = preferences::updateEnableBlur)
                SwitchPreference(title = stringResource(R.string.floating_navigation_bar), checked = preferences.floatingNavigationBar, onCheckedChange = preferences::updateFloatingNavigationBar)
            }
            ScaleSetting(preferences)
        }
    }
}

@Composable
private fun SwitchSetting(title: String, checked: Boolean, onChange: (Boolean) -> Unit, enabled: Boolean = true) {
    Card(colors = CardDefaults.cardColors()) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(title, Modifier.weight(1f)); Switch(checked, onChange, enabled = enabled)
        }
    }
}

@Composable
private fun <T> EnumSetting(title: String, values: List<T>, selected: T, onSelected: (T) -> Unit, label: @Composable (T) -> String) {
    var expanded by remember { mutableStateOf(false) }
    Card(onClick = { expanded = true }) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text(title); Text(label(selected), style = MaterialTheme.typography.bodySmall) }
            DropdownMenu(expanded, { expanded = false }) {
                values.forEach { value -> DropdownMenuItem(text = { Text(label(value)) }, onClick = { onSelected(value); expanded = false }, trailingIcon = { RadioButton(value == selected, null) }) }
            }
        }
    }
}

@Composable
private fun ScaleSetting(preferences: AppPreferences) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        val title = stringResource(R.string.page_scale)
        when (preferences.uiStyle) {
            UiStyle.MATERIAL3 -> Text("$title ${(preferences.pageScale * 100).toInt()}%")
            UiStyle.MIUIX -> MiuixText("$title ${(preferences.pageScale * 100).toInt()}%")
        }
        when (preferences.uiStyle) {
            UiStyle.MATERIAL3 -> Slider(value = preferences.pageScale, onValueChange = preferences::updatePageScale, valueRange = .85f..1.20f, steps = 6)
            UiStyle.MIUIX -> MiuixSlider(value = preferences.pageScale, onValueChange = preferences::updatePageScale, valueRange = .85f..1.20f, steps = 6, showKeyPoints = true)
        }
    }
}
