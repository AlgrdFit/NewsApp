package com.telesoftas.newsapp.utlis

interface DateFormatter {
    val dateFormat: String
    fun formatDate(date: String): String?
}