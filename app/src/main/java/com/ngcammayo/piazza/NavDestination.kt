package com.ngcammayo.piazza

import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import dev.hotwire.turbo.config.TurboPathConfigurationProperties
import dev.hotwire.turbo.config.context
import dev.hotwire.turbo.nav.TurboNavDestination
import dev.hotwire.turbo.nav.TurboNavPresentationContext
import java.net.URL

interface NavDestination: TurboNavDestination {

    override fun shouldNavigateTo(newLocation: String): Boolean {
        return when {
            isExternal(newLocation) -> {
                //TODO: open in browser
                false
            }
            isPathDirective(newLocation) -> {
                executePathDirective(newLocation)
                false
            }
            else -> true
        }
    }

    private fun executePathDirective(url: String) {
        val url = URL(url)
        when (url.path) {
            "/recede_historical_location" -> navigateUp()
            "/refresh_historical_location" -> refresh()
        }
    }

    private fun isPathDirective(url: String): Boolean {
        return url.contains("_historical_location")
    }

    private fun isExternal(location: String): Boolean {
        return !location.startsWith(Api.rootUrl)
    }

    fun dismissLoginScreen() {
        if (this.location == Api.loginUrl) {
            navigateUp()
        }
    }

    override fun getNavigationOptions(
        newLocation: String,
        newPathProperties: TurboPathConfigurationProperties
        ): NavOptions {
        return when (newPathProperties.context) {
            TurboNavPresentationContext.MODAL -> slideAnimation()
            else -> super.getNavigationOptions(newLocation, newPathProperties)
        }
    }

    private fun slideAnimation(): NavOptions {
        return navOptions {
            anim {
                enter = R.anim.nav_slide_enter
                exit = R.anim.nav_slide_exit
                popEnter = R.anim.nav_slide_pop_enter
                popExit = R.anim.nav_slide_pop_exit
            }
        }
    }
}