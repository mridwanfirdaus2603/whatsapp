package app.whatsapp.patches.metaai

import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.returnEarly
import app.whatsapp.patches.shared.Constants.WHATSAPP_COMPATIBILITY

/**
 * Meta AI UI Cleanup patch.
 *
 * Hides supported Meta AI-related interface entry points in WhatsApp.
 * Only modifies verified UI components. Does not disable backend
 * infrastructure, authentication, or Meta AI services.
 *
 * Preserves:
 * - Normal WhatsApp search functionality.
 * - Personal and group chats.
 * - Message sending and receiving.
 * - Contact and navigation functionality.
 * - Notifications and account features.
 *
 * Limitations:
 * - Only hides UI entry points identifiable via fingerprints.
 * - Does not disable Meta AI backend services.
 * - Fingerprints may not resolve on untested WhatsApp versions.
 */
@Suppress("unused")
val metaAICleanupPatch = patch(
    name = "Meta AI UI Cleanup",
    description = "Hides supported Meta AI-related interface entry points.",
    default = true
) {
    compatibleWith(WHATSAPP_COMPATIBILITY)

    execute {
        // Attempt to find and disable Meta AI UI setup methods.
        // Each fingerprint targets a specific Meta AI interface element.
        // Using OrNull variants for graceful handling when fingerprints
        // cannot match on a given WhatsApp version.

        // Disable Meta AI button/entry point setup.
        MetaAIButtonFingerprint.methodOrNull?.let { method ->
            method.returnEarly()
        }

        // Disable Meta AI shortcut component creation.
        MetaAIShortcutFingerprint.methodOrNull?.let { method ->
            method.returnEarly()
        }

        // Disable Meta AI promotional element display.
        MetaAIPromotionFingerprint.methodOrNull?.let { method ->
            method.returnEarly()
        }
    }
}
