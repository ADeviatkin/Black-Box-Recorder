package com.ad.alphablackbox.cryptography;

import java.security.Key
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

class KeyGenerator {
    companion object {
        fun generateRSAKeys(keySize: Int = 2048): KeyPair? {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(keySize)
            return keyPairGenerator.generateKeyPair()
        }

        fun generateAESKeys(keySize: Int = 128): Key? {
            val keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(keySize) // Generate a 128-bit key
            return keyGenerator.generateKey()
        }

        fun generateIv(BITS_PER_SAMPLE: Short): IvParameterSpec {
            val iv = ByteArray(BITS_PER_SAMPLE.toInt())
            SecureRandom().nextBytes(iv)
            return IvParameterSpec(iv)
        }
    }
}