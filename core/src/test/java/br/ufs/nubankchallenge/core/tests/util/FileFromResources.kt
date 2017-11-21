package br.ufs.nubankchallenge.core.tests.util

import java.io.File
import java.util.*

/**
 *
 * Created by @ubiratanfsoares
 *
 */

object FileFromResources {

    operator fun invoke(fileName: String): String {

        val result = StringBuilder()
        val classLoader = FileFromResources::class.java.classLoader
        val file = File(classLoader.getResource(fileName).file)

        try {
            val scanner = Scanner(file)
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                result.append(line).append("\n")
            }

            scanner.close()
            return result.toString()

        } catch (e: Throwable) {
            e.printStackTrace()
        }

        throw RuntimeException("Cannot read file" + fileName)
    }

}