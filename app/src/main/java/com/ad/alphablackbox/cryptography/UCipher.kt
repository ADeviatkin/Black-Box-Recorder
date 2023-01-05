package com.ad.alphablackbox.cryptography;
import android.util.Log
import com.ad.alphablackbox.cryptography.KeyGenerator.Companion.generateIv
import java.security.Key
import javax.crypto.Cipher

class UCipher {
    private lateinit var cipherEncryptor: Cipher
    private lateinit var cipherDecryptor: Cipher

    constructor(transformation: String, key: Key?, BITS_PER_SAMPLE: Short) {
        try {
            val iVector = generateIv(BITS_PER_SAMPLE)
            this.cipherEncryptor = Cipher.getInstance(transformation)
            this.cipherDecryptor = Cipher.getInstance(transformation)
            this.cipherEncryptor.init(Cipher.ENCRYPT_MODE, key, iVector)
            this.cipherDecryptor.init(Cipher.DECRYPT_MODE, key, iVector)
        } catch (e: java.lang.Exception) {
            Log.d("Exception", "${this.javaClass.simpleName} ${object {}.javaClass.enclosingMethod.name} ${e.toString()}")
        }
    }

    // Encrypt the data
    fun encrypt(data: ArrayList<Byte>): ArrayList<Byte> {
        val encryptedData = cipherEncryptor.doFinal(data.toByteArray())
        return encryptedData.toCollection(ArrayList())
    }

    // Decrypt the data
    fun decrypt(data: ArrayList<Byte>): ArrayList<Byte> {
        val decryptedData = cipherDecryptor.doFinal(data.toByteArray())
        return decryptedData.toCollection(ArrayList())
    }
}