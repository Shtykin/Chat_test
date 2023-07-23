package ru.shtykin.testappchat.data.network


data class ErrorResponse(
    val detail: Detail
) {
    data class Detail(
        val message: String?,
    )
}