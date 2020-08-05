package ru.netology.ncraftmedia.crud.dto

enum class AttachmentType {
  IMAGE, AUDIO, VIDEO
}

data class AttachmentModel(val id: String, val url: String, val type: AttachmentType)

data class PostModel(
  val id: Int,
  val source: PostModel? = null,
  val ownerId: Long,
  val ownerName: String,
  val created: Int,
  val content: String? = null,
  var likes: Int = 0,
  var likedByMe: Boolean = false,
  val reposts: Int = 0,
  val repostedByMe: Boolean = false,
  val link: String? = null,
  val type: PostType = PostType.Post,
  val attachment: AttachmentModel?
) {
  var likeActionPerforming = false
  fun updateLikes(updatedModel: PostModel) {
    if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
    likes = updatedModel.likes
    likedByMe = updatedModel.likedByMe
  }
}
