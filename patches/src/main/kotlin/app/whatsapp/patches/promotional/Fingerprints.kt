package app.whatsapp.patches.promotional

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * Fingerprints for identifying promotional and advertising-related UI components in WhatsApp.
 *
 * These fingerprints use string-based matching to find methods that handle
 * promotional content display. Only verified, identifiable elements are targeted.
 *
 * WhatsApp obfuscates class and method names between versions, so we rely on
 * string constants that are more stable across updates.
 */

/**
 * Matches the method responsible for determining whether to show promotional/sponsored
 * content in the Updates (Status) tab.
 *
 * WhatsApp uses internal string constants to configure promotional display behavior.
 * This fingerprint targets methods containing promotional configuration strings.
 */
object PromotionalContentFingerprint : Fingerprint(
    returnType = "Z",
    isStatic = false,
    filters = listOf(
        string("sponsored_content"),
    )
)

/**
 * Matches methods related to ad banner inflation in the Updates tab.
 * Targets methods that reference ad-related layout identifiers.
 */
object AdBannerSetupFingerprint : Fingerprint(
    returnType = "V",
    isStatic = false,
    filters = listOf(
        string("ad_banner"),
    )
)

/**
 * Matches methods that handle promotional card rendering.
 * These cards appear as suggestions or recommendations within the app.
 */
object PromotionalCardFingerprint : Fingerprint(
    returnType = "V",
    isStatic = false,
    filters = listOf(
        string("promo_card"),
    )
)
