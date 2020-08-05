package ru.netology.ncraftmedia.crud.dto

import java.util.*

data class Post(
    val id:Int?,
    val authorName: String?,
    val authorDrawable: Int?,
    val bodyText: String,
    val postDate: Date = Date(),
    val repostPost: Post?,
    val postType: PostType,
    var likeCounter: Int,
    var likedByMe: Boolean = false,
    val commentCounter: Int,
    var shareCounter: Int,
    val location: Pair<Double, Double>?,
    val link: String?,
    val postImage: Int?

) {
    fun likeChange(counter:Int): Post =
        copy(likeCounter=counter)

    fun commentChange(counter:Int) : Post =
        copy(commentCounter = counter)

    fun shareChange(counter:Int) : Post =
        copy(shareCounter = counter)

    var likeActionPerforming = false
    fun updateLikes(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        likeCounter = updatedModel.likes
        likedByMe = updatedModel.likedByMe
    }
}