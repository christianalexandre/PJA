package com.example.contactdefinitive.network

import com.example.contactdefinitive.ResultModel
import com.example.contactdefinitive.askValueFromJson
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ResultModelAdapter {
    @FromJson
    fun fromJson(json: Map<String, askValueFromJson>): ResultModel {
        return ResultModel(firstCurrencies = json, secondCurrencies = json)
    }

    @ToJson
    fun toJson(resultModel: ResultModel): Map<String, askValueFromJson> {
        return resultModel.firstCurrencies
        return resultModel.secondCurrencies
    }
}
