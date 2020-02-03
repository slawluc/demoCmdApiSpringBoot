@file:Suppress("unused")

package com.example.demo.framework.common


inline class CustomerIdentificationNumber(val value: String)

/**
 * Inline class for the identity token string literal
 * Place to hang jwt conversion logic off.  (Better than passing around magic strings)
 */
inline class IdentityToken(val token: String) {
    //TODO THIS IS JUST AN EXAMPLE OF GETTING PRINCIPAL FROM THE TOKEN!!
    //TODO LIKELY WILL NEED PARSING OUT OF THE PRINCIPAL
    val customerId: CustomerIdentificationNumber get() = CustomerIdentificationNumber(token)
}

/**
 * Calling Context contract
 */
interface CallingContext {
    val channelId: String
    val identityToken: IdentityToken
}