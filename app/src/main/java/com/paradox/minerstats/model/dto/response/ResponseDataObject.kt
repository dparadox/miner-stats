package com.paradox.minerstats.model.dto.response

data class ResponseDataObject<T>(val status: String, val data: T? = null)
// TODO using the current Retrofit Result<>, Eventually Improve to use RepositoryResult<T> Loading, Success, Error