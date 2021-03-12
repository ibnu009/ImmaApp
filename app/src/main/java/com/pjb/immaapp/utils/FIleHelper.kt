package com.pjb.immaapp.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.text.TextUtils
import timber.log.Timber
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class FIleHelper {
    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    fun createImageFile(context: Context): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".png", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = path
        }
    }

    fun getImageUri(context: Context, img: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(context.contentResolver, img, "Title", null)
        return Uri.parse(path)
    }

    fun getRealImagePathFromURI(context: Context, uri: Uri?): String? {
        var path = ""
        if (context.contentResolver != null) {
            val cursor: Cursor? =
                uri?.let { context.contentResolver.query(it, null, null, null, null) }
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }
//
//    fun getRealImagePathFromURI(
//        context: Context, uri: Uri?
//    ): String? {
//        val contentResolver = context.contentResolver ?: return null
//
//        // Create file path inside app's data dir
//        val filePath = (context.applicationInfo.dataDir + File.separator
//                + System.currentTimeMillis())
//        val file = File(filePath)
//        try {
//            val inputStream = contentResolver.openInputStream(uri!!) ?: return null
//            val outputStream: OutputStream = FileOutputStream(file)
//            val buf = ByteArray(1024)
//            var len: Int
//            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
//            outputStream.close()
//            inputStream.close()
//        } catch (ignore: IOException) {
//            return null
//        }
//        return file.absolutePath
//    }

    fun getFilePathFromURI(context: Context?, contentUri: Uri?): String? {
//        Stream directori file mentah ke dir android
        val fileName: String = getFileName(contentUri)!!
        val dir = File(
            context?.externalCacheDir.toString()
        )
        // membuat dir otomatis jika file yang didapat adalah null
        if (!dir.exists()) {
            dir.mkdirs()
        }
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile = File(dir.toString() + File.separator + fileName)
            copy(context!!, contentUri, copyFile)
            return copyFile.absolutePath
        }
        return null
    }


    private fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    private fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
            val outputStream: OutputStream = FileOutputStream(dstFile)
            copyStream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(java.lang.Exception::class, IOException::class)
    private fun copyStream(input: InputStream?, output: OutputStream?): Int {
        val buffer = ByteArray(BUFFER_SIZE)
        val `in` = BufferedInputStream(input, BUFFER_SIZE)
        val out = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            while (`in`.read(buffer, 0, BUFFER_SIZE).also { n = it } != -1) {
                out.write(buffer, 0, n)
                count += n
            }
            out.flush()
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
                Timber.e(e.toString())
            }
            try {
                `in`.close()
            } catch (e: IOException) {
                Timber.e(e.toString())
            }
        }
        return count
    }

    companion object {
        const val BUFFER_SIZE: Int = 1024 * 2
    }
}