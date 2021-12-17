package com.owner.dgcverifier.adapters.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.owner.dgcverifier.CommonUtils
import com.owner.dgcverifier.CommonUtils.Companion.getDisplayDate
import com.owner.dgcverifier.R
import dgca.verifier.app.decoder.model.RecoveryStatement
import java.text.SimpleDateFormat
import java.util.*

class RecoveryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val certificateIssuer = itemView.findViewById<AppCompatTextView>(R.id.certificate_issuer_value)
    private val validFrom = itemView.findViewById<AppCompatTextView>(R.id.valid_from_value)
    private val diseaseName = itemView.findViewById<AppCompatTextView>(R.id.disease_name_value)
    private val country = itemView.findViewById<AppCompatTextView>(R.id.country_value)
    private val firstPositive = itemView.findViewById<AppCompatTextView>(R.id.first_positive_value)
    private val certificateIdentifier = itemView.findViewById<AppCompatTextView>(R.id.certificate_identifier_value)
    private val validUntil = itemView.findViewById<AppCompatTextView>(R.id.valid_until_value)

    fun setRecovery(recovery: RecoveryStatement) {
        certificateIssuer.text = recovery.certificateIssuer
        validFrom.text = getDisplayDate(recovery.certificateValidFrom)
        diseaseName.text = CommonUtils.getDiseaseName(recovery.disease)
        country.text = Locale("", recovery.countryOfVaccination).displayCountry
        firstPositive.text = getDisplayDate(recovery.dateOfFirstPositiveTest)
        certificateIdentifier.text = recovery.certificateIdentifier
        validUntil.text = getDisplayDate(recovery.certificateValidUntil)
        updateValidUntilDateColor(recovery.certificateValidUntil)
    }

    fun updateValidUntilDateColor(dateString: String) {
        val now = Date()
        try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(dateString)
            if(date != null && date.time < now.time) {
                validUntil.setTextColor(ContextCompat.getColor(validUntil.context,R.color.red))
            } else {
                validUntil.setTextColor(ContextCompat.getColor(validUntil.context,R.color.green_1))
            }
        } catch (ex: Exception) {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString)
                if(date != null && date.time < now.time) {
                    validUntil.setTextColor(ContextCompat.getColor(validUntil.context,R.color.red))
                } else {
                    validUntil.setTextColor(ContextCompat.getColor(validUntil.context,R.color.green_1))
                }
            } catch (ex: Exception) {
                validUntil.setTextColor(ContextCompat.getColor(validUntil.context,R.color.green_1))
            }
        }
    }
}