package com.reactlibrary

import android.app.Activity
import android.content.Intent
import com.facebook.react.bridge.BaseActivityEventListener
import com.facebook.react.bridge.Promise
import com.judokit.android.JUDO_ERROR
import com.judokit.android.JUDO_RESULT
import com.judokit.android.PAYMENT_ERROR
import com.judokit.android.PAYMENT_SUCCESS
import com.judokit.android.model.JudoError
import com.judokit.android.model.JudoResult

class JudoReactNativeActivityEventListener : BaseActivityEventListener() {

    internal var transactionPromise: Promise? = null

    override fun onActivityResult(
        activity: Activity,
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {

        if (requestCode != JUDO_PAYMENT_WIDGET_REQUEST_CODE) {
            return
        }

        when (resultCode) {
            PAYMENT_ERROR -> {
                val error = data.getParcelableExtra<JudoError>(JUDO_ERROR)
                transactionPromise?.reject(JUDO_PROMISE_REJECTION_CODE, error?.message)
            }
            PAYMENT_SUCCESS -> {
                val result = data.getParcelableExtra<JudoResult>(JUDO_RESULT)
                transactionPromise?.resolve(getMappedResult(result))
            }
        }

        transactionPromise = null
    }
}
