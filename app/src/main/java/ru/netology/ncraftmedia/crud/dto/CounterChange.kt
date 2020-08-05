package ru.netology.ncraftmedia.crud.dto

class CounterChange(
    val id:Int,
    val counter:Int,
    val counterType: CounterType
)

enum class CounterType {
    Like,Comment,Share
}