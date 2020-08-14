package ru.netology.ncraftmedia.crud

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.netology.ncraftmedia.R
import ru.netology.ncraftmedia.crud.adapter.PostAdapter
import ru.netology.ncraftmedia.crud.dto.Post
import splitties.activities.start
import splitties.toast.toast

class FeedActivity : AppCompatActivity(),
    PostAdapter.OnLikeBtnClickListener,
    PostAdapter.OnShareBtnClickListener {
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        fab.setOnClickListener {
            start<CreatePostActivity>()
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            dialog = ProgressDialog(this@FeedActivity).apply {
                setMessage(this@FeedActivity.getString(R.string.please_wait))
                setTitle(R.string.downloading_posts)
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            var result:Response<MutableList<Post>>?=null
            try {
                result = Repository.getPosts()
            } catch (e: Exception) {
                toast(R.string.error_occured)
            }
            dialog?.dismiss()
            if(result!=null) {
                if (result.isSuccessful) {
                    with(container) {
                        layoutManager = LinearLayoutManager(this@FeedActivity)
                        adapter = PostAdapter(result.body() ?: mutableListOf<Post>()).apply {
                            likeBtnClickListener = this@FeedActivity
                            shareBtnClickListener = this@FeedActivity
                        }
                    }
                } else {
                    toast(R.string.error_occured)
                }
            }
        }
    }

    override fun onLikeBtnClicked(item: Post, position: Int, list: MutableList<Post>) {
        lifecycleScope.launch {
            with(container) {
                adapter?.notifyItemChanged(position)
                try {
                    list[position] = Repository.likeChange(item)
                } catch (e: Exception) {
                    toast(R.string.error_occured)
                }
                adapter?.notifyItemChanged(position)
            }
        }
    }

    override fun onShareBtnClicked(item: Post, position: Int, list: MutableList<Post>) {
        lifecycleScope.launch {
            with(container) {
                adapter?.notifyItemChanged(position)
                try {
                    startRepostActivity(item, position)
                    Repository.shareChange(item)
                } catch (e: Exception) {
                    toast(R.string.error_occured)
                }
                adapter?.notifyItemChanged(position)
            }
        }
    }

    private fun startRepostActivity(item: Post, position: Int) {
        val int = Intent(
            this,
            CreatePostActivity::class.java
        )
        with(int) {
            putExtra("id", item.id)
            putExtra("authorName", item.authorName)
            putExtra("authorDrawable", item.authorDrawable)
            putExtra("bodyText", item.bodyText)
            putExtra("postDate", item.postDate)
            putExtra("postType", item.postType)
            putExtra("postImage", item.postImage)
        }
        startActivity(int)
    }
}
