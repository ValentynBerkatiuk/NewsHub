package com.simpleappsdev.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseDTO<E>(
    @SerialName("status") val status: String,
    @SerialName("totalResults") val totalResults: String,
    @SerialName("articles") val articles: List<E>
)
