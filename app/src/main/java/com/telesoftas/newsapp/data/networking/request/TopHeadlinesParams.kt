package com.telesoftas.newsapp.data.networking.request

data class TopHeadlinesParams(
    val country: String = DEFAULT_COUNTRY,
    val page: Int = 1,
    val pageSize: Int = DEFAULT_PAGE_SIZE
) {
    fun toQueryMap(): Map<String, String> = mapOf(
        "country" to country,
        "page" to page.toString(),
        "pageSize" to pageSize.toString()
    )

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val DEFAULT_COUNTRY = "us"
    }
}
