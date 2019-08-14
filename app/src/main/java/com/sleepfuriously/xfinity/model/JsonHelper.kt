package com.sleepfuriously.xfinity.model

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * contains functions that help when parsing the JSON files
 * returned by server queries
 */

//------------------------
//  constants
//------------------------

private const val DTAG = "JsonHelper"


/** Keys for accessing parts of the JSON object  */
private const val TOP_LEVEL_KEY = "RelatedTopics"


//------------------------
//  functions
//------------------------

/**
 * Use this to parse all the character info from the base JSONObject
 * that the server returns.
 *
 * @param jobject   The JSONObject just as it was served (hehe).
 *
 * @return  List of CharacterInfo items--what we really want!
 *          Returns null on error.
 */
fun getCharacterList(jobject: JSONObject): List<CharacterInfo>? {

    var list: MutableList<CharacterInfo>? = null

    val charJsonArray: JSONArray

    try {
        list = ArrayList()
        charJsonArray = jobject.getJSONArray(TOP_LEVEL_KEY)

        for (i in 0 until charJsonArray.length()) {
            val character = CharacterInfo(charJsonArray.getJSONObject(i))
            list.add(character)
        }
    } catch (e: JSONException) {
        Log.e(DTAG, "Unable to parse JSON data in getCharacterList()!")
        e.printStackTrace()
        list = null
    }

    return list

}
