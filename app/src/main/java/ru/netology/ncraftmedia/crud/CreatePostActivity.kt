package ru.netology.ncraftmedia.crud

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.netology.ncraftmedia.R
import ru.netology.ncraftmedia.crud.dto.Post
import ru.netology.ncraftmedia.crud.dto.PostType
import splitties.toast.toast
import java.io.IOException
import java.util.*

class CreatePostActivity : AppCompatActivity() {

    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        createPostBtn.setOnClickListener {
            lifecycleScope.launch {
                // Показываем крутилку
                dialog = ProgressDialog(this@CreatePostActivity).apply {
                    setMessage(this@CreatePostActivity.getString(R.string.please_wait))
                    setTitle(R.string.create_new_post)
                    setCancelable(false)
                    setProgressBarIndeterminate(true)
                    show()
                }
                try {
                    lateinit var result: Response<Void>
                    if(intent.hasExtra("id")){
                        result = Repository.createPost(
                            Post(
                                null,
                                getSharedPreferences(
                                    API_SHARED_FILE,
                                    Context.MODE_PRIVATE
                                ).getString(
                                    USER_NAME, ""
                                ),
                                null,
                                contentEdt.text.toString(),
                                Date(),
                                    Post(
                                        intent.getIntExtra("id",0),
                                        intent.getStringExtra("authorName"),
                                        intent.getIntExtra("authorDrawable",0),
                                        intent.getStringExtra("bodyText"),
                                        intent.getSerializableExtra("postDate") as Date,
                                        null,
                                        intent.getSerializableExtra("postType") as PostType,
                                        0,
                                        false,
                                        0,
                                        0,
                                        null,
                                        null,
                                        intent.getIntExtra("postImage",0)
                                    ),
                                PostType.Post,
                                0,
                                false,
                                0,
                                0,
                                null,
                                null,
                                null
                            )
                        )
                    }else {
                        result = Repository.createPost(
                            Post(
                                null,
                                getSharedPreferences(
                                    API_SHARED_FILE,
                                    Context.MODE_PRIVATE
                                ).getString(
                                    USER_NAME, ""
                                ),
                                null,
                                contentEdt.text.toString(),
                                Date(),
                                null,
                                PostType.Post,
                                0,
                                false,
                                0,
                                0,
                                null,
                                null,
                                null
                            )
                        )
                    }
                    if (result.isSuccessful) {
                        // обрабатываем успешное создание поста
                        handleSuccessfullResult()
                    } else {
                        // обрабоатываем ошибку
                        handleFailedResult()
                    }
                } catch (e: IOException) {
                    // обрабатываем ошибку
                    handleFailedResult()
                } finally {
                    // закрываем диалог
                    dialog?.dismiss()
                }

            }
        }

    }

    private fun handleSuccessfullResult() {
        toast(R.string.post_created_successfully)
        finish()
    }

    private fun handleFailedResult() {
        toast(R.string.error_occured)
    }
}
