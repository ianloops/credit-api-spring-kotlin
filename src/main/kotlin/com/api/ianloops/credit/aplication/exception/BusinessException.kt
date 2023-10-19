package com.api.ianloops.credit.aplication.exception

data class BusinessException(override val message: String?) : RuntimeException(message){
}