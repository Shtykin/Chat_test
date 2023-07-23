package ru.shtykin.testappchat.data.network


data class ErrorValidation(
    val detail: List<Detail>
) {
    data class Detail(
        val msg: String?
    )
}

