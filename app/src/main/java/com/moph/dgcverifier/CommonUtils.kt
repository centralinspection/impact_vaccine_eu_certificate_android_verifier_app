package com.owner.dgcverifier

import com.owner.dgcverifier.DGCCertificateManager.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

class CommonUtils {
    companion object {
        fun getDisplayDate(dateString: String): String {
            return try {
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(dateString)
                if(date != null) {
                    SimpleDateFormat(DATE_FORMAT, Locale.US).format(date)
                } else {
                    dateString
                }
            } catch (ex: Exception) {
                try {
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString)
                    if(date != null) {
                        SimpleDateFormat(DATE_FORMAT, Locale.US).format(date)
                    } else {
                        dateString
                    }
                } catch (ex: Exception) {
                    dateString
                }
            }
        }

        fun getDiseaseName(targetCode: String): String {
            return when (targetCode) {
                "840539006" -> "COVID-19"
                else -> ""
            }
        }
    }
}