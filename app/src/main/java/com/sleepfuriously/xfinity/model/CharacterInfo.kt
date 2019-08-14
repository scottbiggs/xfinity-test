package com.sleepfuriously.xfinity.model

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

/**
 * data structure for a character
 */
class CharacterInfo {

    //------------------------
    //  constants
    //------------------------

    companion object {

        private val DTAG = CharacterInfo::class.java.simpleName

        /** Keys to get the class data from JSON objects  */
        val TEXT_KEY = "Text"
        val ICON_KEY = "Icon"

        /**
         * This sequence of characters define the difference between the name and
         * the description of a character in the JSON string returned by TEXT_KEY.
         */
        private val NAME_DESC_DELIMITER = " - "
    }


    //------------------------
    //  data
    //------------------------

    var name: String? = null
    var desc: String? = null
    var iconUrl: String? = null

    //------------------------
    //  methods
    //------------------------

    /**
     * Basic constructor
     */
    constructor() {

    }

    /**
     * Constructor where the data is supplied
     *
     * @param _name     The character name
     * @param _desc     A description of the character
     * @param _iconUrl  URL where an image can be found
     */
    constructor(_name: String, _desc: String, _iconUrl: String) {
        name = _name
        desc = _desc
        iconUrl = _iconUrl
    }


    /**
     * Constructor from JSON object
     *
     * @param jsonObject    An object where the top level will have fields corresponding
     *                      to TEXT_KEY and ICON_KEY.  The data will be derived from
     *                      that JSON object.  In other words, this will be the object
     *                      that defines a given character.
     */
    constructor(jsonObject: JSONObject) {
        try {
            val text = jsonObject.getString(TEXT_KEY)
            iconUrl = jsonObject.getString(ICON_KEY)

            // The text will need further processing.  The name will be the
            // all the chars up to the delimiter.
            // Everything after the delimiter is part of the description.
            val nameDelimiter = text.indexOf(NAME_DESC_DELIMITER)
            name = text.substring(0, nameDelimiter)
            desc = text.substring(nameDelimiter + NAME_DESC_DELIMITER.length)
        }
        catch (e: JSONException) {
            Log.e(DTAG, "Unable to parse JSON in CharacterInfo!")
            e.printStackTrace()
        }

    }


}
