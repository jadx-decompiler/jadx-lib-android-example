package io.github.skylot.jadx.android.example

import android.content.Context
import android.util.Log
import jadx.api.CommentsLevel
import jadx.api.JadxArgs
import jadx.api.JadxDecompiler
import jadx.api.impl.NoOpCodeCache
import jadx.api.impl.SimpleCodeWriter
import jadx.api.security.JadxSecurityFlag
import jadx.api.security.impl.JadxSecurity
import java.io.File
import java.util.function.Function

object Decompile {

    enum class DecompileMode {
        MAIN_ACTIVITY,
        SAVE_ALL
    }

    fun decompile(context: Context, mode: DecompileMode): String {
        try {
            val sampleFile = loadSampleFile(context, "small.apk")
            val outDir = File(context.cacheDir, "outDir")

            val jadxArgs = JadxArgs()
            jadxArgs.inputFiles = listOf(sampleFile)
            jadxArgs.outDir = outDir

            jadxArgs.threadsCount = 1 // reduce memory usage
            jadxArgs.codeCache = NoOpCodeCache.INSTANCE // code cache not needed
            jadxArgs.codeWriterProvider = Function(::SimpleCodeWriter) // code attributes not needed

            // disable secure xml parser (some features not supported on Android)
            val securityFlags = JadxSecurityFlag.all()
            securityFlags.remove(JadxSecurityFlag.SECURE_XML_PARSER)
            jadxArgs.security = JadxSecurity(securityFlags)

            // disable jadx code comments
            // TODO: inner class processing error
            jadxArgs.commentsLevel = CommentsLevel.NONE

            // (Optional) Class set tree loading can take too much time,
            // but disabling it can reduce result code quality
            jadxArgs.isLoadJadxClsSetFile = false

            JadxDecompiler(jadxArgs).use { jadx ->
                jadx.load()

                when (mode) {
                    DecompileMode.MAIN_ACTIVITY -> {
                        // search and return code of MainActivity
                        for (cls in jadx.classes) {
                            Log.d("SmallApp", "Class: ${cls.name}")
                            if (cls.name == "MainActivity") {
                                return cls.code
                            }
                        }
                    }

                    DecompileMode.SAVE_ALL -> {
                        // decompile and save all files into 'outDir'
                        jadx.save()
                        return "Saved files in $outDir:\n${listFilesInDir(outDir)}"
                    }
                }
            }
            return "Unexpected error. Check logs for details."
        } catch (e: Throwable) {
            return "Decompile error: ${e.message}\n${e.stackTrace}"
        }
    }

    // Load sample apk from assets into temp file.
    private fun loadSampleFile(context: Context, sample: String): File {
        val tmpFile = File.createTempFile("temp", sample, context.cacheDir)
        context.assets.open("samples/$sample").use { input ->
            tmpFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tmpFile
    }

    private fun listFilesInDir(dir: File): String {
        return dir.walkTopDown()
            .toList()
            .filter { it.isFile }
            .joinToString(separator = "\n", transform = { file -> file.toRelativeString(dir) })
    }
}