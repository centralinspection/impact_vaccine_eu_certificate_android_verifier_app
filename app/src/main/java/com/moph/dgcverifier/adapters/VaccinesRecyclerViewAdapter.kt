package com.owner.dgcverifier.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.owner.dgcverifier.CommonUtils.Companion.getDisplayDate
import com.owner.dgcverifier.R
import com.owner.dgcverifier.adapters.viewholders.*
import dgca.verifier.app.decoder.model.GreenCertificate
import dgca.verifier.app.decoder.model.RecoveryStatement
import dgca.verifier.app.decoder.model.Test
import dgca.verifier.app.decoder.model.Vaccination

class VaccinesRecyclerViewAdapter(val certificate: GreenCertificate): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewType(val value: Int) {
        Vaccination(0),
        PCR(1),
        Recovery(2),
        PersonalInfo(3)
    }

    val itemList: MutableList<Any> = arrayListOf()

    init {
        certificate.vaccinations?.forEach {
            itemList.add(it)
        }
        certificate.recoveryStatements?.forEach {
            itemList.add(it)
        }
        certificate.tests?.forEach {
            itemList.add(it)
        }

        val person = PersonalInfo(certificate.person.givenName.orEmpty(),
            certificate.person.familyName.orEmpty(),
            getDisplayDate(certificate.dateOfBirth))
            itemList.add(person)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.Vaccination.value -> {
                val itemView = LayoutInflater.from(parent.context).inflate(
                    R.layout.vaccination_item_layout,
                    parent, false)
                VaccinationViewHolder(itemView)
            }
            ViewType.PCR.value -> {
                val itemView = LayoutInflater.from(parent.context).inflate(
                    R.layout.pcr_item_layout,
                    parent, false)
                PcrViewHolder(itemView)
            }
            ViewType.Recovery.value -> {
                val itemView = LayoutInflater.from(parent.context).inflate(
                    R.layout.recovery_item_layout,
                    parent, false)
                RecoveryViewHolder(itemView)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context).inflate(
                    R.layout.personal_info_item_layout,
                    parent, false)
                PersonalInfoViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is VaccinationViewHolder && itemList[position] is Vaccination) {
            holder.setVaccine(itemList[position] as Vaccination)
        } else if(holder is PersonalInfoViewHolder && itemList[position] is PersonalInfo) {
            holder.setPersonalInfo(itemList[position] as PersonalInfo)
        } else if(holder is RecoveryViewHolder && itemList[position] is RecoveryStatement) {
            holder.setRecovery(itemList[position] as RecoveryStatement)
        } else if(holder is PcrViewHolder && itemList[position] is Test) {
            holder.setPCR(itemList[position] as Test)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            itemList[position] is Vaccination -> {
                ViewType.Vaccination.value
            }
            itemList[position] is Test -> {
                ViewType.PCR.value
            }
            itemList[position] is RecoveryStatement -> {
                ViewType.Recovery.value
            }
            else -> {
                ViewType.PersonalInfo.value
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}