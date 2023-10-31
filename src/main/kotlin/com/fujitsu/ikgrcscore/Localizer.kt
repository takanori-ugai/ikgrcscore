package com.fujitsu.ikgrcscore

import gg.jte.support.LocalizationSupport
import java.util.Locale
import java.util.ResourceBundle

/**
 * Provides localization support for the application.
 *
 * @property bundle The resource bundle containing localized strings.
 */
class Localizer(locale: Locale) : LocalizationSupport {
    private val bundle: ResourceBundle = ResourceBundle.getBundle("localization", locale)

    override fun lookup(key: String): String = bundle.getString(key)
}
