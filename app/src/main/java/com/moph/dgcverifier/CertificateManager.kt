package com.owner.dgcverifier

import java.lang.IllegalArgumentException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateEncodingException

interface CertificateManager {
    fun keysFor(kid: ByteArray): List<Certificate>?

    fun add(certStrings: List<String>): Boolean

    fun keyId(certificate: Certificate): ByteArray? {
        return try {
            val encoderCert: ByteArray = certificate.encoded
            val hash: ByteArray = MessageDigest.getInstance("SHA-256").digest(encoderCert)
            hash.copyOfRange(0, 8)
        } catch (e: CertificateEncodingException) {
            throw IllegalArgumentException("can not gen keyid", e)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException("can not gen keyid", e)
        }
    }
}