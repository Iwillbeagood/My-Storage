package com.example.mystorage.utils

import com.example.mystorage.retrofit.response.UserHomeInfoResponse
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

            if (response.livingRoomName != "없음") items.add("거실")
            if (response.kitchenName != "없음") items.add("주방")
            if (response.storageName != "없음") items.add("창고")

            val jsonObjectEtc = JSONObject(response.bathroomNames)
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
            items.add(roomItems)
            items.add(bathItems)

            if (response.etc_name != "없음")
                items.add(mutableListOf(response.etc_name))
            else
                items.add(mutableListOf(""))
        }
        return items
    }
}