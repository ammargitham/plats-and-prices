package com.ammar.platsnprices

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ammar.platsnprices.ui.controllers.ExtSystemUIController
import com.ammar.platsnprices.ui.controllers.ModalBottomSheetController
import com.ammar.platsnprices.ui.controllers.ToolbarController
import com.ammar.platsnprices.ui.screens.home.Home
import com.ammar.platsnprices.ui.screens.imagespager.ImagesPager
import com.ammar.platsnprices.ui.screens.product.Product
import com.ammar.platsnprices.ui.screens.sale.Sale
import com.ammar.platsnprices.ui.screens.videoplayer.VideoPlayer

@OptIn(ExperimentalAnimationApi::class)
sealed class Route(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
    val enterTransition: AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(
            animationSpec = tween(800)
        )
    },
    val exitTransition: AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(
            animationSpec = tween(800)
        )
    },
    val popEnterTransition: AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(
            animationSpec = tween(800)
        )
    },
    val popExitTransition: AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(
            animationSpec = tween(800)
        )
    },
    val content: @Composable (
        PaddingValues,
        NavBackStackEntry,
        NavController,
        ToolbarController,
        ModalBottomSheetController,
        ExtSystemUIController
    ) -> Unit
) {
    object HomeRoute : Route(
        route = "home",
        content = { padding, _, navController, toolbarController, bottomSheetController, _ ->
            Home(
                padding,
                toolbarController,
                bottomSheetController,
                navigateToSale = { saleDbId, name, imgUrl -> navController.navigateToSale(saleDbId, name, imgUrl) },
                navigateToProduct = { ppId, name, imgUrl -> navController.navigateToProduct(ppId, name, imgUrl) },
            )
        }
    )

    object SaleRoute : Route(
        route = "sale/{saleDbId}?name={name}&img={img}",
        arguments = listOf(
            navArgument("saleDbId") { type = NavType.LongType },
            navArgument("name") { nullable = true },
            navArgument("img") { nullable = true },
        ),
        content = { padding, backStackEntry, navController, toolbarController, bottomSheetController, _ ->
            Sale(
                padding,
                toolbarController,
                bottomSheetController,
                backStackEntry.arguments?.getLong("saleDbId"),
                backStackEntry.arguments?.getString("name"),
                backStackEntry.arguments?.getString("img"),
                navigateToProduct = { ppId, name, imgUrl -> navController.navigateToProduct(ppId, name, imgUrl) },
            )
        }
    ) {
        fun getRoute(saleDbId: Long, name: String? = null, img: String? = null): String = "sale/$saleDbId?name=${name ?: ""}&img=${img ?: ""}"
    }

    object ProductRoute : Route(
        route = "product/{ppId}?name={name}&img={img}",
        arguments = listOf(
            navArgument("ppId") { type = NavType.LongType },
            navArgument("name") { nullable = true },
            navArgument("img") { nullable = true },
        ),
        content = { _, backStackEntry, navController, toolbarController, _, _ ->
            Product(
                toolbarController,
                backStackEntry.arguments?.getLong("ppId"),
                backStackEntry.arguments?.getString("name"),
                backStackEntry.arguments?.getString("img"),
                navigateToImagesPager = { urls, index -> navController.navigateToImagesPager(urls, index) },
                navigateToVideoPlayer = { navController.navigateToVideoPlayer(it) },
            )
        }
    ) {
        fun getRoute(ppId: Long, name: String? = null, img: String? = null): String = "product/$ppId?name=${name ?: ""}&img=${img ?: ""}"
    }

    object ImagesPagerRoute : Route(
        route = "images-pager?urls={urls}&index={index}",
        arguments = listOf(
            navArgument("urls") { type = NavType.StringType },
            navArgument("index") { type = NavType.IntType },
        ),
        content = { _, backStackEntry, _, toolbarController, _, _ ->
            ImagesPager(
                toolbarController,
                backStackEntry.arguments?.getString("urls")?.split(",") ?: emptyList(),
                backStackEntry.arguments?.getInt("index") ?: 0,
            )
        }
    ) {
        fun getRoute(urls: List<String>, index: Int = 0): String = "images-pager?urls=${urls.joinToString(",")}&index=$index"
    }

    object VideoPlayerRoute : Route(
        route = "video-player?url={url}",
        content = { _, backStackEntry, _, toolbarController, _, extSystemUIController ->
            VideoPlayer(
                toolbarController,
                backStackEntry.arguments?.getString("url") ?: "",
                setOrientation = { extSystemUIController.setOrientation(it) },
                setImmersive = { extSystemUIController.setImmersive(it) },
            )
        }
    ) {
        fun getRoute(url: String): String = "video-player?url=$url"
    }

    companion object {
        fun allRoutes(): List<Route> {
            return Route::class.nestedClasses
                .asSequence()
                .filter { it.objectInstance is Route }
                .map { it.objectInstance as Route }
                .toList()
        }
    }
}

fun NavController.navigateToSale(saleDbId: Long, name: String? = null, img: String? = null) =
    this.navigate(Route.SaleRoute.getRoute(saleDbId, name, img))

fun NavController.navigateToProduct(ppId: Long, name: String? = null, img: String? = null) =
    this.navigate(Route.ProductRoute.getRoute(ppId, name, img))

fun NavController.navigateToImagesPager(urls: List<String>, index: Int = 0) =
    this.navigate(Route.ImagesPagerRoute.getRoute(urls, index))

fun NavController.navigateToVideoPlayer(url: String) =
    this.navigate(Route.VideoPlayerRoute.getRoute(url))