package ru.netology.ncraftmedia.crud.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.coroutines.NonCancellable.start
import ru.netology.ncraftmedia.R
import ru.netology.ncraftmedia.crud.CreatePostActivity
import ru.netology.ncraftmedia.crud.Repository
import ru.netology.ncraftmedia.crud.dto.Post
import ru.netology.ncraftmedia.crud.dto.PostType
import splitties.activities.startActivity
import java.text.SimpleDateFormat

class PostAdapter(val list: List<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var shareBtnClickListener: OnShareBtnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(this, view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.v("test", "position is $position")
        with(holder as PostViewHolder) {
            bind(list[position])
        }
    }

    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: Post, position: Int)
    }
    interface OnShareBtnClickListener {
        fun onShareBtnClicked(item: Post, position: Int)
    }
}


class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                }
            }
            shareBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    adapter.shareBtnClickListener?.onShareBtnClicked(item, currentPosition)
                }
            }
        }
    }

    fun bind(post: Post) {
        with(itemView) {
            val format = SimpleDateFormat("H:mm dd/MM/yyyy")
            authorTv.text = post.authorName
            createdTv.text = format.format(post.postDate)
            contentTv.text = post.bodyText
            if(post.authorDrawable!=null) {
                avatarIv.setImageDrawable(getDrawableFromAbstractInt(this.context,post.authorDrawable))
            }
            commentsTv.text = post.commentCounter.toString()
            likesTv.text = post.likeCounter.toString()
            repostsTv.text = post.shareCounter.toString()

            when {
                post.likedByMe -> {
                    likeBtn.setImageResource(R.drawable.ic_favorite_active_24dp)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
                else -> {
                    likeBtn.setImageResource(R.drawable.ic_favorite_inactive_24dp)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                }
            }
            when (post.postType) {
                PostType.Post -> {
                    locationButton.isVisible = false
                    contentDw.isVisible = false
                }
                PostType.Event -> {
                    locationButton.setOnClickListener {
                        val geo = Intent().apply {
                            data = Uri.parse("geo:${post.location?.first}${post.location?.second}")
                        }
                        context.startActivity(geo)
                    }
                    locationButton.isVisible = true
                    contentDw.isVisible = false

                }
                PostType.Video -> {
                    contentDw.setImageDrawable(
                        getDrawableFromAbstractInt(
                            context,
                            0
                        )
                    )
                    contentDw.setOnClickListener {
                        val watchVideo = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data = Uri.parse(post.link)
                        }
                        context.startActivity(watchVideo)
                    }
                    locationButton.isVisible = false
                    contentDw.isVisible = true

                }
                PostType.Advertising -> {
                    contentDw.setImageDrawable(post.postImage?.let {
                        getDrawableFromAbstractInt(
                            context,
                            it
                        )
                    })
                    contentDw.setOnClickListener {
                        val watchVideo = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data = Uri.parse(post.link)
                        }
                        context.startActivity(watchVideo)
                    }
                    locationButton.isVisible = false
                    contentDw.isVisible = true
                }
            }
            if(post.repostPost==null){
                innerPost.isVisible=false
            }else{
                iauthorTv.text = post.repostPost.authorName
                icreatedTv.text = format.format(post.repostPost.postDate)
                icontentTv.text = post.repostPost.bodyText
                if(post.authorDrawable!=null) {
                    iavatarIv.setImageDrawable(getDrawableFromAbstractInt(this.context,post.authorDrawable))
                }
                when (post.postType) {
                    PostType.Post -> {
                    }
                    PostType.Video -> {
                        icontentDw.setImageDrawable(
                            getDrawableFromAbstractInt(
                                context,
                                0
                            )
                        )
                        icontentDw.setOnClickListener {
                            val watchVideo = Intent().apply {
                                action = Intent.ACTION_VIEW
                                data = Uri.parse(post.link)
                            }
                            context.startActivity(watchVideo)
                        }

                    }
                    PostType.Advertising -> {
                        icontentDw.setImageDrawable(post.postImage?.let {
                            getDrawableFromAbstractInt(
                                context,
                                it
                            )
                        })
                        icontentDw.setOnClickListener {
                            val watchVideo = Intent().apply {
                                action = Intent.ACTION_VIEW
                                data = Uri.parse(post.link)
                            }
                            context.startActivity(watchVideo)
                        }
                    }
                }
            }
        }
    }

    private fun getDrawableFromAbstractInt(context: Context, authorDrawable: Int): Drawable? {
        return when (authorDrawable) {
            0 -> getDrawable(context, R.drawable.ad)
            1 -> getDrawable(context, R.mipmap.ic_launcher_foreground)
            2 -> getDrawable(context, R.drawable.health)
            else -> getDrawable(context, R.drawable.health)
        }
    }
}

