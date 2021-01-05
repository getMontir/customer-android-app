package com.getmontir.lib.data.data.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class StationRegisterContactDto(
    @JsonProperty(value = "name")
    val name: String?,

    @JsonProperty(value = "phone")
    val phone: String?,
)
