package org.connecttag.lib.kotlin.core.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/** Versioned authenticated encryption. Call on a background dispatcher. */
object SecurityUtils {
    private const val KEY_ALIAS = "connecttag_strings_v2"
    private const val PREFIX = "ctg2:"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private val authenticatedVersion = PREFIX.toByteArray(Charsets.UTF_8)

    @Synchronized private fun key(): SecretKey {
        val store = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        (store.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
            init(KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256).build())
        }.generateKey()
    }

    @Suppress("UNUSED_PARAMETER")
    fun encrypt(context: Context, value: String): String {
        if (value.isEmpty()) return ""
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key())
        cipher.updateAAD(authenticatedVersion)
        val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        return PREFIX + Base64.encodeToString(cipher.iv + encrypted, Base64.NO_WRAP)
    }

    @Suppress("UNUSED_PARAMETER")
    fun decrypt(context: Context, encryptedValue: String): String {
        if (encryptedValue.isEmpty()) return ""
        // Older versions also persisted plaintext after encryption failures. Preserve it
        // without guessing keys or overwriting data; the next write uses the new format.
        if (!encryptedValue.startsWith(PREFIX)) return encryptedValue
        val combined = Base64.decode(encryptedValue.removePrefix(PREFIX), Base64.NO_WRAP)
        require(combined.size >= 12 + 16)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, combined.copyOfRange(0, 12)))
        cipher.updateAAD(authenticatedVersion)
        return cipher.doFinal(combined, 12, combined.size - 12).toString(Charsets.UTF_8)
    }
}
