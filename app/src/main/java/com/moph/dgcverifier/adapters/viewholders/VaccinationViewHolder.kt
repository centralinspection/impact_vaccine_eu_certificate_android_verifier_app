package com.owner.dgcverifier.adapters.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.owner.dgcverifier.CommonUtils
import com.owner.dgcverifier.CommonUtils.Companion.getDisplayDate
import com.owner.dgcverifier.R
import dgca.verifier.app.decoder.model.Vaccination
import java.util.*

class VaccinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val certificateIssuer = itemView.findViewById<AppCompatTextView>(R.id.certificate_issuer_value)
    private val medicalProduct = itemView.findViewById<AppCompatTextView>(R.id.medical_product_value)
    private val diseaseName = itemView.findViewById<AppCompatTextView>(R.id.disease_name_value)
    private val country = itemView.findViewById<AppCompatTextView>(R.id.country_value)
    private val dateOfVaccination = itemView.findViewById<AppCompatTextView>(R.id.date_of_vaccination_value)
    private val doseNumber = itemView.findViewById<AppCompatTextView>(R.id.dose_number_value)
    private val certificateIdentifier = itemView.findViewById<AppCompatTextView>(R.id.certificate_identifier_value)
    private val marketingAuthorization = itemView.findViewById<AppCompatTextView>(R.id.marketing_authorization_value)
    private val vaccineName = itemView.findViewById<AppCompatTextView>(R.id.vaccine_name_value)

    fun setVaccine(vaccine: Vaccination) {
        certificateIssuer.text = vaccine.certificateIssuer
        medicalProduct.text = getMedicalProduct(vaccine.medicinalProduct)
        diseaseName.text = CommonUtils.getDiseaseName(vaccine.disease)
        country.text = Locale("", vaccine.countryOfVaccination).displayCountry
        dateOfVaccination.text = getDisplayDate(vaccine.dateOfVaccination)
        certificateIdentifier.text = vaccine.certificateIdentifier
        marketingAuthorization.text = getMarketingAuthorization(vaccine.manufacturer)
        vaccineName.text = getVaccineName(vaccine.vaccine)
        if(vaccine.doseNumber < vaccine.totalSeriesOfDoses) {
            doseNumber.text = String.format("%s/%s", vaccine.doseNumber.toString(), vaccine.totalSeriesOfDoses.toString())
            doseNumber.setTextColor(ContextCompat.getColor(doseNumber.context,R.color.red))
        } else {
            doseNumber.setTextColor(ContextCompat.getColor(doseNumber.context,R.color.green_1))
            // It is possible to have dose number > totalSeriesOfDoses
            // In this case we must display the dose number taken as the maximum total for expected doses
            doseNumber.text = String.format("%s/%s", vaccine.doseNumber.toString(), vaccine.doseNumber.toString())
        }
    }

    fun getMedicalProduct(code: String): String {
        return when(code.trim()) {
            "EU/1/20/1528" -> "Comirnaty"
            "EU/1/20/1507" -> "Spikevax (previously COVID-19 Vaccine Moderna)"
            "EU/1/21/1529" -> "Vaxzevria"
            "EU/1/20/1525" -> "COVID-19 Vaccine Janssen"
            "CVnCoV" -> "CVnCoV"
            "NVX-CoV2373" -> "NVX-CoV2373"
            "Sputnik-V" -> "Sputnik V"
            "Convidecia" -> "Convidecia"
            "EpiVacCorona" -> "EpiVacCorona"
            "BBIBP-CorV" -> "BBIBP-CorV"
            "InactivatedSARS-CoV-2-Vero-Cell" -> "Inactivated SARS-CoV-2 (Vero Cell)"
            "CoronaVac" -> "CoronaVac"
            "Covaxin" -> "Covaxin (also known as BBV152 A, B, C)"
            "Covishield" -> "Covishield (ChAdOx1_nC oV-19)"
            "Covid-19-recombinant" -> "Covid-19 (recombinant)"
            "R-COVI" -> "R-COVI"
            "CoviVac" -> "CoviVac"
            "Sputnik-Light" -> "Sputnik Light"
            "Hayat-Vax" -> "Hayat-Vax"
            "Abdala" -> "Abdala"
            "WIBP-CorV" -> "WIBP-CorV"
            else -> ""
        }
    }

    fun getMarketingAuthorization(code: String): String {
        return when(code.trim()) {
            "ORG-100001699" -> "AstraZeneca AB"
            "ORG-100030215" -> "Biontech Manufacturing GmbH"
            "ORG-100001417" -> "Janssen-Cilag International"
            "ORG-100031184" -> "Moderna Biotech Spain S.L."
            "ORG-100006270" -> "Curevac AG"
            "ORG-100013793" -> "CanSino Biologics"
            "ORG-100020693" -> "China Sinopharm International Corp. - Beijing location"
            "ORG-100010771" -> "Sinopharm Weiqida Europe Pharmaceutical s.r.o. - Prague location"
            "ORG-100024420" -> "0 Sinopharm Zhijun (Shenzhen) Pharmaceutical Co. Ltd. - Shenzhen location"
            "ORG-100032020" -> "Novavax CZ AS"
            "Gamaleya-Research-Institute" -> "Gamaleya Research Institute"
            "Vector-Institute" -> "Vector Institute"
            "Sinovac-Biotech" -> "Sinovac Biotech"
            "Bharat-Biotech" -> "Bharat Biotech"
            "ORG-100001981" -> "Serum Institute Of India Private Limited"
            "Fiocruz" -> "Fiocruz"
            "ORG-100007893" -> "R-Pharm CJSC"
            "Chumakov-Federal-Scientific-Center" -> "Chumakov Federal Scientific Center for Research and Development of Immuneand-Biological Products"
            "ORG-100023050" -> "Gulf Pharmaceutical Industries"
            "CIGB" -> "Center for Genetic Engineering and Biotechnology (CIGB)"
            "Sinopharm-WIBP" -> "Sinopharm - Wuhan Institute of Biological Products"
            else -> ""
        }
    }

    fun getVaccineName(code: String): String {
        return when(code.trim()) {
            "1119305005" -> "SARS-CoV2 antigen vaccine"
            "1119349007" -> "SARS-CoV2 mRNA vaccine"
            "J07BX03" -> "covid-19 vaccines"
            else -> ""
        }
    }
}