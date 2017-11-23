package br.ufs.hiring.nubankchallenge.uitests.util

import android.content.pm.PackageManager
import android.os.IBinder
import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.UiDevice
import android.util.Log

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class DisableSystemAnimations {
    private val ANIMATION_PERMISSION = "android.permission.SET_ANIMATION_SCALE"
    private val DISABLED = 0.0f

    init {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        UiDevice
                .getInstance(instrumentation)
                .executeShellCommand(
                        "pm grant "
                                + targetContext.packageName
                                + ANIMATION_PERMISSION
                )

    }

    operator fun invoke() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val permStatus = targetContext.checkCallingOrSelfPermission(ANIMATION_PERMISSION)
        if (permStatus == PackageManager.PERMISSION_GRANTED) {
            setSystemAnimationsScale(DISABLED)
        }
    }

    private fun setSystemAnimationsScale(animationScale: Float) {
        try {
            val stubClazz = Class.forName("android.view.IWindowManager\$Stub")
            val asInterface = stubClazz.getDeclaredMethod("asInterface", IBinder::class.java)
            val smClazz = Class.forName("android.os.ServiceManager")
            val getService = smClazz.getDeclaredMethod("getService", String::class.java)
            val wmClazz = Class.forName("android.view.IWindowManager")
            val animScales = wmClazz.getDeclaredMethod("setAnimationScales", FloatArray::class.java)
            val getAnimationScales = wmClazz.getDeclaredMethod("getAnimationScales")

            val windowManagerBinder = getService.invoke(null, "window") as IBinder
            val windowManagerObj = asInterface.invoke(null, windowManagerBinder)
            val currentScales = getAnimationScales.invoke(windowManagerObj) as FloatArray
            for (i in currentScales.indices) {
                currentScales[i] = animationScale
            }
            animScales.invoke(windowManagerObj, arrayOf<Any>(currentScales))
        } catch (e: Exception) {
            Log.e("SystemAnimations",
                    "Could not change animation scale to $animationScale :'("
            )
        }

    }
}