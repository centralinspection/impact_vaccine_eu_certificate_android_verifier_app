package com.owner.dgcverifier.adapters.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.owner.dgcverifier.CommonUtils
import com.owner.dgcverifier.CommonUtils.Companion.getDisplayDate
import com.owner.dgcverifier.R
import dgca.verifier.app.decoder.model.Test
import java.util.*

class PcrViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val certificateIssuer = itemView.findViewById<AppCompatTextView>(R.id.certificate_issuer_value)
    private val testingCenter = itemView.findViewById<AppCompatTextView>(R.id.testing_center_value)
    private val diseaseName = itemView.findViewById<AppCompatTextView>(R.id.disease_name_value)
    private val country = itemView.findViewById<AppCompatTextView>(R.id.country_value)
    private val testType = itemView.findViewById<AppCompatTextView>(R.id.test_type_value)
    private val collectionTime = itemView.findViewById<AppCompatTextView>(R.id.collection_time_value)
    private val certificateIdentifier = itemView.findViewById<AppCompatTextView>(R.id.certificate_identifier_value)
    private val testName = itemView.findViewById<AppCompatTextView>(R.id.test_name_value)
    private val detected = itemView.findViewById<AppCompatTextView>(R.id.detected_value)
    private val testIdentifier = itemView.findViewById<AppCompatTextView>(R.id.test_identifier_value)

    fun setPCR(test: Test) {
        certificateIssuer.text = test.certificateIssuer
        testingCenter.text = test.testingCentre
        if (test.typeOfTest == "LP6464-4") {
            testType.text = "Nucleic acid amplification with probe detection"
        } else if (test.typeOfTest == "LP217198-3"){
            testType.text = "Rapid immunoassay"
        }
        diseaseName.text = CommonUtils.getDiseaseName(test.disease)
        country.text = Locale("", test.countryOfVaccination).displayCountry
        collectionTime.text = getDisplayDate(test.dateTimeOfCollection)
        certificateIdentifier.text = test.certificateIdentifier
        testName.text = test.testName
        if (test.testResult == "260415000") {
            detected.text = detected.context.getString(R.string.no)
            detected.setTextColor(ContextCompat.getColor(detected.context,R.color.green_1))
        } else if (test.testResult == "260373001") {
            detected.text = detected.context.getString(R.string.yes)
            detected.setTextColor(ContextCompat.getColor(detected.context,R.color.red))
        }
        testIdentifier.text = test.testNameAndManufacturer
    }
}