package com.owner.dgcverifier.model

import com.google.gson.annotations.SerializedName

data class PublicKey(@SerializedName("rawData") val keyValue: String)