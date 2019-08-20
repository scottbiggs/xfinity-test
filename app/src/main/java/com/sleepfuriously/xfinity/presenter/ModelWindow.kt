package com.sleepfuriously.xfinity.presenter

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.sleepfuriously.xfinity.BuildConfig.BASE_API_URL  // holds url for multiple builds
import com.sleepfuriously.xfinity.model.CharacterInfo
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList


/**
 * All access to data comes through this class (using the
 * kolin Singleton Pattern, which is done simply by declaring it
 * to be an <em>object</em> instead of a class).
 *
 * The network and caching is done here.
 */
object ModelWindow :
    com.android.volley.Response.Listener<org.json.JSONArray>,
    Response.ErrorListener {


    //------------------------
    //  constants
    //------------------------

    private const val DTAG = "ModelWindow"

    /** Probably not used, but provided for completeness.  */
    private const val CHARACTER_RETRIEVED_SUCCESS_MSG = "Character retrieval successful."


    //------------------------
    //  data
    //------------------------

    // not used in Kotlin
//    private var mInstance: ModelWindow? = null

    /** Listener to call once Volley has returned some data */
    private val mListener: ModelWindowCharacterListener? = null

//------------------------
//  methods
//------------------------

    /**
     * Returns a list of all the character data to display.  The data
     * includes all the info about a character, including a url to display
     * something (but not the drawable itself).
     *
     * @param listener  The instance that implements [ModelWindowCharacterListener].
     * Its [ModelWindowCharacterListener.returnCharacterList]
     * method will be called when the value has been retrieved.
     *
     * @param ctx   Ye good ol' Context.
     */
    fun getCharacterInfoList(listener: ModelWindowCharacterListener, ctx: Context) {

        val q = Volley.newRequestQueue(ctx)

        val request = JsonArrayRequest(Request.Method.GET, BASE_API_URL, null,
                this, this)
        q.add(request)
    }



    /**
     * Implementation of Response.listener interface.  When Volley responds
     * to a request, it calls this method. Duh.
     */
    override fun onResponse(response: JSONArray?) {
        if (response == null) {
            Log.e(DTAG, "response is NULL in onResponse()!")
            return
        }

        val charList = ArrayList<CharacterInfo>()
        for (i in 0 until response.length()) {
            try {
                val jsonObject = response.get(i) as JSONObject
                charList.add(CharacterInfo(jsonObject))
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        mListener!!.returnCharacterList(charList, true, CHARACTER_RETRIEVED_SUCCESS_MSG)
    }

    /**
     * When Volley perceives an error, it'll eventually respond here.
     */
    override fun onErrorResponse(error: VolleyError?) {
        mListener!!.returnCharacterList(null, false, error!!.message.toString())
    }


    //------------------------
    //  interfaces
    //------------------------

    /**
     * Implement this interface to receive a callback when the
     * character list is ready.
     */
    interface ModelWindowCharacterListener {

        /**
         * Called when the character list is ready.
         *
         * @param characters     A List of characters
         * @param successful Tells if the request was successful
         * @param msg        If not successful, an error message
         */
        fun returnCharacterList(characters: List<CharacterInfo>?, successful: Boolean, msg: String)
    }



}
