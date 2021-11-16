package hr.fjjukic.template.app_common.enums

import hr.fjjukic.template.app_common.model.response.ErrorResponse

sealed class ResultWrapper {
    data class Success<T>(val value: T) : ResultWrapper()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null) : ResultWrapper()
    object NetworkError : ResultWrapper()
}
