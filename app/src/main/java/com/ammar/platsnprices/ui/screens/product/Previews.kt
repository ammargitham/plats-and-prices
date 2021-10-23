package com.ammar.platsnprices.ui.screens.product

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import coil.annotation.ExperimentalCoilApi
import com.ammar.platsnprices.data.entities.OpenCriticGame
import com.ammar.platsnprices.data.entities.Tier
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme

@ExperimentalCoilApi
@ExperimentalUnitApi
@Composable
@Preview(showBackground = true)
fun HeaderRowPreview() {
    PlatsNPricesTheme {
        HeaderRow(
            imgUrl = tempGame.coverArtUrl,
            title = tempGame.productName,
            developer = tempGame.developer,
            publisher = tempGame.publisher,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun GenresRowPreview() {
    PlatsNPricesTheme {
        GenresRow(
            genres = tempGame.genres,
            isPs4 = tempGame.isPs4,
            isPs5 = tempGame.isPs5,
            isVr = tempGame.isVrInt,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun OpenCriticRowPreview() {
    PlatsNPricesTheme {
        OpenCriticRow(
            openCriticGame = tempOpenCriticGame,
        )
    }
}

@ExperimentalCoilApi
@Composable
@Preview(showBackground = true)
fun ScreenshotsRowPreview() {
    PlatsNPricesTheme {
        ScreenshotsRow(
            videoUrl = tempGame.previewVideoUrl,
            screenshots = tempGame.screenShotUrls,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DescriptionRowPreview() {
    PlatsNPricesTheme {
        DescriptionRow(
            description = tempGame.description,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TrophiesRowPreview() {
    PlatsNPricesTheme {
        TrophiesRow(
            trophies = tempGame.trophies,
            trophyGuideUrl = tempGame.trophyGuideUrl,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DetailsSectionPreview() {
    PlatsNPricesTheme {
        DetailsSection(product = tempGame)
    }
}

@Composable
@Preview
fun CriticsRecommendCircularProgressPreview() {
    CriticsRecommendCircularProgress()
}

@Composable
@Preview
fun StoreButtonsPreview() {
    PlatsNPricesTheme {
        StoreButtons(
            formattedBasePrice = "$10",
            formattedSalePrice = "$9",
            formattedPlusPrice = "$8",
            psStoreUrl = tempGame.psStoreUrl,
            platPricesUrl = tempGame.platPricesUrl,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun OtherGuidesSectionPreview() {
    PlatsNPricesTheme {
        OtherGuidesSection(tempGame.guides)
    }
}

val tempGame = com.ammar.platsnprices.data.entities.Product(
    ppId = 5633,
    psnId = "UP2080-CUSA02789_00-0000000000000001",
    gameId = "CUSA02789",
    platPricesUrl = "https://platprices.com/en-us/game/5633-alekhines-gun",
    psStoreUrl = "https://store.playstation.com/en-us/product/UP2080-CUSA02789_00-0000000000000001",
    productName = "Alekhine's Gun",
    gameName = "Alekhine's Gun",
    publisher = "Maximum Games",
    developer = "Ubisoft Quebec",
    releaseDateStr = "2016-03-01",
    description = "Make Your Move! It is the height of the Cold War, a time of global uncertainty and civil unrest. You are Agent Alekhine, a highly skilled Russian assassin working alongside American CIA agents in a covert operation unsanctioned by the country you have served.<br />With the tension of a nuclear standoff growing with each passing minute, your mission is clear…though how you accomplish it is anything but.<br />Only one thing is certain: Time won't stop, and it's too late to go backward. As you unravel the conspiracy unfolding in front of your very eyes, the fate of America, and quite possibly the world, lies within your hands.<br /><br />Maximum Games, LLC. All other trademarks or registered trademarks belong to their respective owners. All rights reserved.",
    isPs4Int = 1,
    isPs5Int = 0,
    isDlcInt = 0,
    isDemoOrSoundtrackInt = 0,
    isVrInt = 1,
    isMoveInt = 1,
    isVitaCrossBuyInt = 0,
    ps4SizeBytes = 6790709248,
    ps5SizeBytes = 0,
    onlinePlayTypeInt = 1,
    offlinePlayers = 1,
    onlinePlayers = 1,
    isPsPlusNeededInt = 1,
    voiceLanguages = listOf("en"),
    subtitleLanguages = listOf("en", "pt_BR", "fr", "es"),
    rating = "ESRB Mature",
    ratingDescription = listOf(
        "Violencia intensa",
        "Interacciones online no clasificadas por la ESRB",
        "Sangre y gore",
        "Lenguaje fuerte",
        "Temas sexuales",
        "Uso de alcohol"
    ),
    openCriticId = 1803,
    metacriticUrl = "",
    imgUrl = "https://image.api.playstation.com/cdn/UP2080/CUSA02789_00/Ljaf0yXMPmeYtE7aK0gdBPzVNz6R8rWu.png",
    coverArtUrl = "https://image.api.playstation.com/gs2-sec/appkgo/prod/CUSA04381_00/3/i_49d8400e3473ddd69a7787c833569fda4896d37591dff8ba28a77031bb350b95/i/pic0.png",
    logoImgUrl = "",
    screenShotUrl1 = "https://image.api.playstation.com/cdn/UP2080/CUSA02789_00/FREE_CONTENT5MdMs5OZoVdbeahjZY3y/PREVIEW_SCREENSHOT1_110988.jpg",
    screenShotUrl2 = "https://image.api.playstation.com/cdn/UP2080/CUSA02789_00/FREE_CONTENT8LQOgEfxmnBryAba6HSY/PREVIEW_SCREENSHOT2_110988.jpg",
    screenShotUrl3 = "https://image.api.playstation.com/cdn/UP2080/CUSA02789_00/FREE_CONTENTVeaqBPZtisbInPxrsnnC/PREVIEW_SCREENSHOT3_110988.jpg",
    screenShotUrl4 = "https://image.api.playstation.com/cdn/UP2080/CUSA02789_00/FREE_CONTENTmivlI7sL4cNnd5eHJHVl/PREVIEW_SCREENSHOT4_110988.jpg",
    screenShotUrl5 = "https://image.api.playstation.com/cdn/UP2080/CUSA02789_00/FREE_CONTENTyonRLPbjEho9gaUD00kl/PREVIEW_SCREENSHOT5_110988.jpg",
    screenShotUrl6 = "https://image.api.playstation.com/cdn/UP2080/CUSA02789_00/FREE_CONTENTE85WRstZ5WykYsBBF8F4/PREVIEW_SCREENSHOT6_110988.jpg",
    screenShotUrl7 = "https://image.api.playstation.com/cdn/UP2080/CUSA02789_00/FREE_CONTENTE7lIdCzeDcVXueple67s/PREVIEW_SCREENSHOT7_110988.jpg",
    screenShotUrl8 = "https://image.api.playstation.com/cdn/UP2080/CUSA02789_00/FREE_CONTENTQePEQLjjzwozscDnfQOL/PREVIEW_SCREENSHOT8_110988.jpg",
    screenShotUrl9 = "https://image.api.playstation.com/cdn/UP2080/CUSA02789_00/FREE_CONTENTZc3kuqg80QGX26jA680T/PREVIEW_SCREENSHOT9_110988.jpg",
    previewVideoUrl = "https://apollo2.dl.playstation.net/cdn/UP2080/CUSA02789_00/FREE_CONTENT6Gsel8X31qIOl5Sd0CjP/PREVIEW_GAMEPLAY_VIDEO_110988.mp4",
    genreActionInt = 0,
    genreAdventureInt = 0,
    genreArcadeInt = 0,
    genreFightingInt = 0,
    genreFpsInt = 0,
    genreHorrorInt = 0,
    genreStoryInt = 0,
    genreMmoInt = 0,
    genreMusicInt = 0,
    genrePlatformerInt = 0,
    genrePuzzleInt = 0,
    genreRacingInt = 0,
    genreRpgInt = 0,
    genreSimulationInt = 0,
    genreSportsInt = 0,
    genreStrategyInt = 0,
    genreTpsInt = 1,
    trophyBronzeCount = 8,
    trophySilverCount = 13,
    trophyGoldCount = 5,
    trophyPlatinumCount = 1,
    difficulty = 3,
    hoursLow = 8f,
    hoursHigh = 12f,
    trophyGuideUrl = "https://psnprofiles.com/trophies/4331-alekhines-gun",
    // guidePsnProfiles = "4679-alekhines-gun-trophy-guide",
    guidePlaystationTrophiesUrl = "https://www.playstationtrophies.org/game/alekhines-gun/guide/",
    guidePs3ImportsUrl = "http://ps3imports.org/forum/index.php?/topic/9139-scribblenauts-showdown-trophy-guide/",
    guidePowerPyxUrl = "https://www.powerpyx.com/metamorphosis-trophy-guide-roadmap/",
    guideKnoef = "https://knoef.info/trophy-guides/ps4-guides/evil-inside-trophy-guide/",
    guideYoutube = "1Q5f9tG9YEE", // "00000000000"
    guideTrophiesDe = "https://www.trophies.de/forum/thema/171450-troph%C3%A4en-leitfaden-morbid-the-seven-acolytes",
    guideDex = "https://dexdotexe.com/smoots-summer-games-trophy-guide/",
    guideCust = "https://platget.com/guides/car-demolition-clicker-trophy-guide/",
    guideCustLabel = "PlatGet",
    region = "US",
    basePrice = 1999,
    plusPrice = 299,
    salePrice = 299,
    formattedBasePrice = "$19.99",
    formattedSalePrice = "$2.99",
    formattedPlusPrice = "$2.99",
    discountPct = 85,
    lastDiscountedStr = "2021-09-01 08:11:28",
    discountedUntilStr = "2021-09-16 06:59:00",
    error = 0,
    errorDesc = "",
    apiLimit = 500,
    apiUsage = 5
)

val tempOpenCriticGame = OpenCriticGame(
    id = 6222,
    name = "Assassin's Creed Odyssey",
    percentRecommended = 82.58064f,
    numReviews = 163,
    numTopCriticReviews = 113,
    numUserReviews = 2,
    medianScore = 88.0f,
    averageScore = 84.25f,
    topCriticScore = 84.25664f,
    tier = Tier.MIGHTY,
)
