package br.ufs.nubankchallenge.core.infrastructure

import com.google.gson.Gson
import kotlin.reflect.KClass

/**
 *
 * Created by @ubiratanfsoares
 *
 */
fun <T : Any> Gson.fromJson(raw: String, klass: KClass<T>): T = fromJson(raw, klass.java)