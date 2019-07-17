package cn.eakay.service.network

data class ResponseWrapper<T>(var status: Int, var data: T, var message: String)