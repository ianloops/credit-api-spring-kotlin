package com.api.ianloops.credit.application.exception

data class BusinessException(override val message: String?) : RuntimeException(message){
}