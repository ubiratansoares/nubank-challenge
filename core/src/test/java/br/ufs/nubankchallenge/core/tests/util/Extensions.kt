package br.ufs.nubankchallenge.core.tests.util

import com.google.gson.Gson
import kotlin.reflect.KClass

/**
 *
 * Created by @ubiratanfsoares
 *
 */
fun <T : Any> Gson.fromJson(raw: String, klass: KClass<T>): T {
    return fromJson(raw, klass.java)
}