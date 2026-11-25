package app.whatsapp.patches.metaai

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * Fingerprints for identifying Meta AI interface entry points in WhatsApp.
 *
 * These fingerprints use string-based matching to locate methods responsible
 * for displaying Meta AI UI elements such as navigation buttons, search bar
 * integrations, and promotional Meta AI components.
 *
 * Only verified, identifiable UI elements are targeted.
 * Normal WhatsApp search functionality is preserved.
 */

/**
 * Matches the method that sets up the Meta AI button/entry point
 * in the WhatsApp navigation or toolbar area.
 */
object MetaAIButtonFingerprint : Fingerprint(
    returnType = "V",
    isStatic = false,
    filters = listOf(
        string("meta_ai"),
    )
)

/**
 * Matches the method that creates or inflates Meta AI-related
 * shortcut components in the chat interface.
 */
object MetaAIShortcutFingerprint : Fingerprint(
    returnType = "V",
    isStatic = false,
    filters = listOf(
        string("ai_chat"),
    )
)

/**
 * Matches methods that display Meta AI promotional elements
 * or suggestions within the app interface.
 */
object MetaAIPromotionFingerprint : Fingerprint(
    returnType = "V",
    isStatic = false,
    filters = listOf(
        string("meta_ai_promo"),
    )
)
