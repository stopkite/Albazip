package com.example.albazip.src.mypage.common.workerdata.taskinfo.data

import com.example.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetMonthTaskRateResponse (
    @SerializedName("data")val data:ArrayList<MonthTaskResult>
        ):BaseResponse()

data class MonthTaskResult(
    @SerializedName("month")val month:String,
    @SerializedName("day")val day:String,
    @SerializedName("totalCount")val totalCount:Int,
    @SerializedName("completeCount")val completeCount:Int
)