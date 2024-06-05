package com.shengdan.base_lib.api

data class StationEntity(
    val areaId: String,
    val areaName: String,
    val companyId: String,
    val companyName: String,
    val createTimestamp: String,
    val createUsername: String,
    val delStatus: Int,
    val desc: String,
    val id: String,
    val ipAddress: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val number: String,
    val place: String,
    val updateTimestamp: String,
    val updateUsername: String
)