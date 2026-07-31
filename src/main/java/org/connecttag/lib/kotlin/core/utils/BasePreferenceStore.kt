package org.connecttag.lib.kotlin.core.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * A base class for DataStore based repositories to handle common boilerplate.
 */
abstract class BasePreferenceStore(
    protected val dataStore: DataStore<Preferences>
) {
    /**
     * Reads a preference value as a [Flow].
     */
    protected fun <T> read(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }

    /**
     * Writes a preference value.
     */
    protected suspend fun <T> write(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    /**
     * Clears all preferences in this store.
     */
    protected suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
