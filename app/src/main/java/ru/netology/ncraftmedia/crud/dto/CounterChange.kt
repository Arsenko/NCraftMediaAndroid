package ru.netology.ncraftmedia.crud.dto

class CounterChange(
    val id:Int,
    val increase:Boolean,
    val counterType: CounterType
)

enum class CounterType {
    Like,Comment,Share
}