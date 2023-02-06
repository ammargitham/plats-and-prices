package com.ammar.platsnprices

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.ammar.platsnprices.ui.composables.BackHandler
import com.ammar.platsnprices.ui.controllers.BottomSheetCallback
import com.ammar.platsnprices.ui.controllers.ExtSystemUIController
import com.ammar.platsnprices.ui.controllers.ModalBottomSheetController
import com.ammar.platsnprices.ui.controllers.ToolbarController
import com.ammar.platsnprices.ui.controllers.rememberExtSystemUIController
import com.ammar.platsnprices.ui.controllers.rememberModalBottomSheetController
import com.ammar.platsnprices.ui.controllers.rememberToolbarController
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme
import com.ammar.platsnprices.utils.LocalBackPressedDispatcher
import com.ammar.platsnprices.work.DbCleanupWorker
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var composeView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        composeView = ComposeView(this).apply {
            setContent {
                CompositionLocalProvider(LocalBackPressedDispatcher provides onBackPressedDispatcher) {
                    val defaultToolbarTitle: @Composable () -> Unit = { Text(stringResource(id = R.string.app_name)) }

                    val navController = rememberAnimatedNavController()
                    val toolbarController = rememberToolbarController()
                    val bottomSheetCallback = remember { BottomSheetCallback() }
                    val bottomSheetState = rememberModalBottomSheetState(
                        initialValue = ModalBottomSheetValue.Hidden,
                        confirmStateChange = {
                            Handler(Looper.getMainLooper()).postDelayed({
                                bottomSheetCallback.performStateChange(it)
                            }, 500)
                            return@rememberModalBottomSheetState true
                        }
                    )
                    val bottomSheetController = rememberModalBottomSheetController(bottomSheetState, bottomSheetCallback)
                    val extSystemUIController = rememberExtSystemUIController(this@MainActivity, window, composeView)
                    val coroutineScope = rememberCoroutineScope()

                    BackHandler(enabled = bottomSheetState.isVisible) {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    }

                    MainContent(
                        navController = navController,
                        title = toolbarController.title.value ?: defaultToolbarTitle,
                        toolbarBackgroundColor = toolbarController.backgroundColor.value,
                        toolbarElevation = toolbarController.elevation.value,
                        toolbarActions = toolbarController.actions.value ?: {},
                        toolbarVisible = toolbarController.visible.value,
                        bottomSheetState = bottomSheetState,
                        bottomSheetContent = bottomSheetController.modalBottomSheetContent.value,
                    ) {
                        PlatsNPricesNavHost(navController, toolbarController, bottomSheetController, it, extSystemUIController)
                    }
                }
            }
        }
        setContentView(composeView)
        DbCleanupWorker.enqueue(this)
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun MainContent(
    navController: NavController,
    title: @Composable () -> Unit,
    toolbarBackgroundColor: Color?,
    toolbarElevation: Dp?,
    toolbarActions: @Composable (RowScope.() -> Unit),
    toolbarVisible: Boolean,
    bottomSheetState: ModalBottomSheetState,
    bottomSheetContent: @Composable (ColumnScope.() -> Unit),
    content: @Composable (PaddingValues) -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    var canPop by remember { mutableStateOf(false) }
    val destinationChangedListener = remember {
        NavController.OnDestinationChangedListener { controller, _, _ -> canPop = controller.previousBackStackEntry != null }
    }

    DisposableEffect(navController) {
        navController.addOnDestinationChangedListener(destinationChangedListener)
        onDispose { navController.removeOnDestinationChangedListener(destinationChangedListener) }
    }

    val navigationIcon: (@Composable () -> Unit)? = if (canPop) {
        {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }
    } else {
        null
    }

    PlatsNPricesTheme {
        // val useDarkIcons = MaterialTheme.colors.isLight
        val systemBarsColor = MaterialTheme.colors.primarySurface

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = systemBarsColor,
                darkIcons = false
            )
        }

        ModalBottomSheetLayout(
            sheetState = bottomSheetState,
            sheetContent = bottomSheetContent,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
            MainScaffold(
                title = title,
                toolbarBackgroundColor = toolbarBackgroundColor,
                toolbarElevation = toolbarElevation,
                navigationIcon = navigationIcon,
                toolbarActions = toolbarActions,
                toolbarVisible = toolbarVisible,
                content = content,
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun MainScaffold(
    title: @Composable () -> Unit,
    toolbarBackgroundColor: Color? = null,
    toolbarElevation: Dp? = null,
    navigationIcon: @Composable (() -> Unit)?,
    toolbarActions: @Composable (RowScope.() -> Unit),
    toolbarVisible: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    // val topRowHeight = 54
    // val topRowHeightDp = topRowHeight.dp
    // val topRowHeightPx = topRowHeight.dpToPx()
    // val topRowOffsetHeightPx = remember { mutableStateOf(0f) }
    // val nestedScrollConnection = remember {
    //     object : NestedScrollConnection {
    //         override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
    //             val delta = available.y
    //             val newOffset = topRowOffsetHeightPx.value + delta
    //             topRowOffsetHeightPx.value = newOffset.coerceIn(-topRowHeightPx, 0f)
    //             return Offset.Zero
    //         }
    //     }
    // }

    // actions = {
    //     actions.map {
    //         IconButton(onClick = it.onClick) {
    //             Icon(
    //                 painter = painterResource(id = it.iconRes),
    //                 contentDescription = it.title,
    //             )
    //         }
    //     }
    // },

    // val offset = IntOffset(x = 0, y = topRowOffsetHeightPx.value.roundToInt())

    Scaffold(
        // modifier = Modifier.nestedScroll(nestedScrollConnection),
        scaffoldState = scaffoldState,
        topBar = {
            AnimatedVisibility(visible = toolbarVisible) {
                TopAppBar(
                    // modifier = Modifier
                    //     .height(topRowHeightDp + offsetDp),
                    // .offset { offset },
                    title = title,
                    backgroundColor = toolbarBackgroundColor ?: MaterialTheme.colors.primarySurface,
                    elevation = toolbarElevation ?: AppBarDefaults.TopAppBarElevation,
                    navigationIcon = navigationIcon,
                    actions = toolbarActions,
                )
            }
        },
        content = content
    )
}

@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun PlatsNPricesNavHost(
    navController: NavHostController,
    toolbarController: ToolbarController,
    modalBottomSheetController: ModalBottomSheetController,
    innerPadding: PaddingValues,
    extSystemUIController: ExtSystemUIController,
) {
    AnimatedNavHost(navController = navController, startDestination = Route.HomeRoute.route) {
        Route.allRoutes().forEach {
            composable(
                route = it.route,
                enterTransition = it.enterTransition,
                exitTransition = it.exitTransition,
                popEnterTransition = it.popEnterTransition,
                popExitTransition = it.popExitTransition,
                arguments = it.arguments,
                deepLinks = it.deepLinks,
                content = { backStackEntry ->
                    it.content(
                        innerPadding,
                        backStackEntry,
                        navController,
                        toolbarController,
                        modalBottomSheetController,
                        extSystemUIController,
                    )
                },
            )
        }
    }
}
