package com.example.mystorage.utils

import com.example.mystorage.retrofit.model.UserHomeInfoResponse
import org.json.JSONObject

object LoadInfoForSpinner {
    fun userHomeInfoLoadResponse(response: UserHomeInfoResponse): MutableList<String> {
        val items = mutableListOf<String>()
        if (response.status == "true") {
            val jsonObjectRoom = JSONObject(response.roomNames)
            val roomNames = jsonObjectRoom.getJSONArray("room_names")
            for (i in 0 until roomNames.length()) {
                val roomName = roomNames.getString(i)
                items.add(roomName)
            }

            val jsonObjectBath = JSONObject(response.bathroomNames)
            val bathNames = jsonObjectBath.getJSONArray("bath_names")
            for (i in 0 until bathNames.length()) {
                val bathName = bathNames.getString(i)
                items.add(bathName)
            }

            if (response.livingRoom != "없음") items.add("거실")
            if (response.kitchen != "없음") items.add("주방")
            if (response.storage != "없음") items.add("창고")

            val jsonObjectEtc = JSONObject(response.etc_name)
            val etcNames = jsonObjectEtc.getJSONArray("etc_name")
            for (i in 0 until etcNames.length()) {
                val etcName = etcNames.getString(i)
                items.add(etcName)
            }
        }
        return items
    }

    fun userHomeInfoLoadResponseToChange(response: UserHomeInfoResponse): MutableList<MutableList<String>> {
        val items = mutableListOf<MutableList<String>>()
        if (response.status == "true") {
            val jsonObjectRoom = JSONObject(response.roomNames)
            val roomNames = jsonObjectRoom.getJSONArray("room_names")
            val roomItems = mutableListOf<String>()
            for (i in 0 until roomNames.length()) {
                val roomName = roomNames.getString(i)
                roomItems.add(roomName)
            }

            val jsonObjectBath = JSONObject(response.bathroomNames)
            val bathNames = jsonObjectBath.getJSONArray("bath_names")
            val bathItems = mutableListOf<String>()
            for (i in 0 until bathNames.length()) {
                val bathName = bathNames.getString(i)
                bathItems.add(bathName)
            }

            val jsonObjectEtc = JSONObject(response.etc_name)
            val etcNames = jsonObjectEtc.getJSONArray("etc_name")
            val etcItems = mutableListOf<String>()
            for (i in 0 until etcNames.length()) {
                val etcName = etcNames.getString(i)
                etcItems.add(etcName)
            }

            items.add(roomItems)
            items.add(bathItems)
            items.add(etcItems)
        }
        return items
    }
}