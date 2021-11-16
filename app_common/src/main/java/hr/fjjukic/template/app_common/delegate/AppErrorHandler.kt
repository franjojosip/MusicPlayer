package hr.fjjukic.template.app_common.delegate

import android.widget.Toast
import com.google.gson.Gson
import hr.fjjukic.template.app_common.adapter.ScreenAdapterImpl
import hr.fjjukic.template.app_common.model.EventUI
import hr.fjjukic.template.app_common.router.AppRouter
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

interface AppErrorHandler {
    val screenAdapter: ScreenAdapterImpl
    val gson: Gson
    val router: AppRouter

    fun handleError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> handleHttpException(throwable)
            is UnknownHostException, is IOException, is ConnectException -> handleNoConnection(
                throwable
            )
            else -> handleUnknownError(throwable)
        }
    }

    fun handleHttpException(exception: HttpException) {
        if (exception.code() in 400..499) {
            val errorBody = exception.response()?.errorBody()?.string() ?: ""
            if (errorBody.isEmpty().not()) {
                try {
                    // TODO Implement error handling for showing message
                    screenAdapter.snackbarUI.postValue(
                        EventUI.SnackbarUI(
                            "",
                            Toast.LENGTH_LONG
                        )
                    )
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    fun handleNoConnection(throwable: Throwable) {
    }

    fun handleUnknownError(throwable: Throwable) {
    }
}
