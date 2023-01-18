package com.ad.alphablackbox.logic.cryptography;
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ad.alphablackbox.logic.cryptography.LocalKeyGenerator.Companion.getKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec


class UCipher {

    @RequiresApi(Build.VERSION_CODES.M)
    constructor() {
        LocalKeyGenerator.generateAESKeys()

        //val de_data = decrypt(test, iv)
        //val invoiceAdditionalAttribute = UCipher.DataPackage(test, iv)

    }
    companion object {
        // Encrypt the data
        fun encrypt(data: ArrayList<Byte>): Pair<ArrayList<Byte>, ByteArray> {
            Log.d("TAG", data.toString())
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, getKey())
            var encryptedData = cipher.doFinal(data.toByteArray()).toCollection(ArrayList())
            return Pair(encryptedData, cipher.iv)
        }

        // Encrypt the data
        fun decrypt(data: ArrayList<Byte>, Iv: ByteArray): ArrayList<Byte> {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(128, Iv)
            cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)
            val decryptedData = cipher.doFinal(data.toByteArray())
            Log.d("TAG", decryptedData.toCollection(ArrayList()).toString())
            return decryptedData.toCollection(ArrayList())
        }
    }
}