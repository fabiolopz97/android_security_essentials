package com.fabiolopz.security_essentials

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User(
    var UID: String? = "",
    var name: String? = "",
    var email: String? = ""
): Serializable