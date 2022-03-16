package com.paradox.minerstats.model.dto.response

data class ResponseDataList<T>(val status: String, val data: List<T>? = null)
// TODO using the current Retrofit Result<>, Eventually Improve to use RepositoryResult<T> Loading, Success, Error