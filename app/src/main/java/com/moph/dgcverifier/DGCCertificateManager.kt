package com.owner.dgcverifier

import android.annotation.SuppressLint
import java.io.*
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object DGCCertificateManager: CertificateManager{
    private const val PUBLIC_KEYS_FILE_NAME = "PUBLIC_KEYS_FILENAME.txt"
    private const val LAST_SYNC_DATE_PREF_KEY = "LAST_SYNC_DATE_PREF_KEY"
    private const val SCAN_SUCCESS_PREF_KEY = "SCAN_SUCCESS_PREF_KEY"
    private const val SCAN_FAIL_PREF_KEY = "SCAN_FAIL_PREF_KEY"
    const val DATE_FORMAT = "dd-MM-yyyy"
    private var publicKeys= hashMapOf<ByteArray,MutableList<Certificate>>()
    private var tempKeys= hashMapOf<ByteArray,MutableList<Certificate>>()

    var successfulScans: Long
        get() = Application.SHARED_PREFERENCES.getLong(SCAN_SUCCESS_PREF_KEY, 0)
        set(value) {
            val editor = Application.SHARED_PREFERENCES.edit()
            editor.putLong(SCAN_SUCCESS_PREF_KEY, value)
            editor.apply()
        }
    var failedScans: Long
        get() = Application.SHARED_PREFERENCES.getLong(SCAN_FAIL_PREF_KEY, 0)
        set(value) {
            val editor = Application.SHARED_PREFERENCES.edit()
            editor.putLong(SCAN_FAIL_PREF_KEY, value)
            editor.apply()
        }

    var lastSyncDate: String
        set(value) {
            val editor = Application.SHARED_PREFERENCES.edit()
            editor?.putString(LAST_SYNC_DATE_PREF_KEY, value)
            editor?.apply()
        }
        get() {
            return Application.SHARED_PREFERENCES.getString(LAST_SYNC_DATE_PREF_KEY, "").orEmpty()
        }

    init{
        readPublicKeysFromFile()
    }

    private fun add(certificate: Certificate){
        try {
            val kid = keyId(certificate)
            kid?.let {
                var certKey = tempKeys.keys.firstOrNull { key -> key.contentEquals(it) }
                if (certKey == null) {
                    certKey = it
                    tempKeys[certKey] = mutableListOf()
                }
                if(tempKeys[certKey]?.contains(certificate) != true) {
                    tempKeys[certKey]?.add(certificate)
                }

            }
        }
        catch(e:Exception){

        }
    }

    override fun keysFor(kid:ByteArray):List<Certificate>?{
        for(k in publicKeys.keys){
            if (k.contentEquals(kid)){
                return publicKeys[k]
            }
        }
        return null
    }

    override fun add(certStrings: List<String>): Boolean {
        tempKeys.clear()
        certStrings.forEach { certStr ->
            val cert = parseCertificate(certStr)
            if (cert != null) {
                add(cert)
            }
        }
        return savePublicKeysOnFile()
    }

    @SuppressLint("NewApi")
    fun parseCertificate(certStr:String): Certificate? {
        val decoded: ByteArray = Base64.getDecoder().decode(
            certStr.replace(
                "-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
        )
        return CertificateFactory.getInstance("X.509")
            .generateCertificate(ByteArrayInputStream(decoded))
    }

    private fun savePublicKeysOnFile(): Boolean {
        return try {
            // create a new file with an ObjectOutputStream
            val file = File(Application.CONTEXT.get()?.filesDir, PUBLIC_KEYS_FILE_NAME)
            val out = FileOutputStream(file)
            val oout = ObjectOutputStream(out)

            // write something in the file
            oout.writeObject(tempKeys)
            oout.flush()
            oout.close()
            publicKeys = tempKeys.clone() as HashMap<ByteArray, MutableList<Certificate>>
            true
        } catch (ex: Exception) {
            false
        }
    }

    private fun readPublicKeysFromFile() {
        try {
            val file = File(Application.CONTEXT.get()?.filesDir, PUBLIC_KEYS_FILE_NAME)
            val ois = ObjectInputStream(FileInputStream(file))
            val savedKeys = ois.readObject() as HashMap<ByteArray, MutableList<Certificate>>
            publicKeys = savedKeys
        } catch (ex: Exception) {

        }
    }

    fun synchIsNeeded(): Boolean {
        try {
            val dateString = lastSyncDate
            if(dateString.isBlank()) return true
            val date = SimpleDateFormat(DATE_FORMAT, Locale.US).parse(dateString) as Date
            return Date().time - date.time > 86400000 // 24 * 3600 * 1000
        } catch (ex: Exception) {
            return true
        }
    }
}