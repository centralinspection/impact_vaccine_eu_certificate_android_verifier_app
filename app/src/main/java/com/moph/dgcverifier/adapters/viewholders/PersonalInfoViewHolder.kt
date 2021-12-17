package com.owner.dgcverifier.adapters.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.owner.dgcverifier.R

class PersonalInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val givenName = itemView.findViewById<AppCompatTextView>(R.id.given_name_value)
    private val familyName = itemView.findViewById<AppCompatTextView>(R.id.family_name_value)
    private val dateOfBirth = itemView.findViewById<AppCompatTextView>(R.id.date_of_birth_value)

    fun setPersonalInfo(person: PersonalInfo) {
        givenName.text = person.givenName
        familyName.text = person.familyName
        dateOfBirth.text = person.dateOfBirth
    }
}

data class PersonalInfo(val givenName: String,
                        val familyName: String,
                        val dateOfBirth: String)