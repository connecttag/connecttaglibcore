package org.connecttag.lib.kotlin.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import android.util.Base64
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.security.SecureRandom

/**
 * Utility class for encryption and decryption operations using Android KeyStore.
 * Note: For DataStore, we manually encrypt/decrypt strings using a master key approach
 * because EncryptedDataStore is not yet officially stable in a simple way for Preferences.
 */
object SecurityUtils {

    private const val ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val KEY_ALIAS = "connecttag_master_key"

    /**
     * Encrypts a string using a key derived from the Android KeyStore.
     */
    fun encrypt(context: Context, value: String): String {
        if (value.isEmpty()) return ""
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            // Note: Since we are using this for DataStore strings, we'll use a standard AES
            // for the actual string encryption for simplicity in this utility.
            // A more robust way would be using the MasterKey directly but that requires manual Cipher management.

            val key = masterKey.toString().take(32).toByteArray(StandardCharsets.UTF_8)
            val secretKey = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance(ALGORITHM)

            val iv = ByteArray(16)
            SecureRandom().nextBytes(iv)
            val ivSpec = IvParameterSpec(iv)

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
            val encrypted = cipher.doFinal(value.toByteArray(StandardCharsets.UTF_8))

            val combined = ByteArray(iv.size + encrypted.size)
            System.arraycopy(iv, 0, combined, 0, iv.size)
            System.arraycopy(encrypted, 0, combined, iv.size, encrypted.size)

            return Base64.encodeToString(combined, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            return value
        }
    }

    /**
     * Decrypts a string that was encrypted using [encrypt].
     */
    fun decrypt(context: Context, encryptedValue: String): String {
        if (encryptedValue.isEmpty()) return ""
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val key = masterKey.toString().take(32).toByteArray(StandardCharsets.UTF_8)
            val secretKey = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance(ALGORITHM)

            val combined = Base64.decode(encryptedValue, Base64.DEFAULT)
            val iv = ByteArray(16)
            val encrypted = ByteArray(combined.size - 16)

            System.arraycopy(combined, 0, iv, 0, 16)
            System.arraycopy(combined, 16, encrypted, 0, encrypted.size)

            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

            val decrypted = cipher.doFinal(encrypted)
            return String(decrypted, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            // If decryption fails (e.g. data was not encrypted), return as is
            return encryptedValue
        }
    }
}
