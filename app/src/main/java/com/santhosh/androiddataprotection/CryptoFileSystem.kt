package com.santhosh.androiddataprotection

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets

/**
 * Created by Santhosh on 7/7/2020.
 */
object CryptoFileSystem {

    private const val FILE_NAME = "protected_data.txt"

    fun encryptFile(context: Context, content: String) {
        val file = getFile(context)
        deleteFile(file) // delete file if exists in file directory

        val fileContent = content.toByteArray(StandardCharsets.UTF_8)
        getEncryptedFile(context, file, masterKeyAlias(context)).openFileOutput().apply {
            write(fileContent)
            flush()
            close()
        }
    }

    @Suppress("PlatformExtensionReceiverOfInline")
    fun decryptFile(context: Context): String {
        val inputStream =
            getEncryptedFile(context, getFile(context), masterKeyAlias(context)).openFileInput()
        val byteArrayOutputStream = ByteArrayOutputStream()
        var nextByte: Int = inputStream.read()
        while (nextByte != -1) {
            byteArrayOutputStream.write(nextByte)
            nextByte = inputStream.read()
        }
        return byteArrayOutputStream.toByteArray().toString(StandardCharsets.UTF_8)
    }

    private fun getEncryptedFile(context: Context, file: File, masterKey: MasterKey) =
        EncryptedFile.Builder(
            context,
            file,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build() // return encrypted file

    fun masterKeyAlias(context: Context) =
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build() // return master key

    fun getFile(context: Context) = File(context.getExternalFilesDir(""), FILE_NAME)

    fun isFileExist(file: File) = file.exists()

    private fun deleteFile(file: File): Boolean {
        return if (isFileExist(file)) {
            file.delete()
        } else {
            false
        }
    }
}