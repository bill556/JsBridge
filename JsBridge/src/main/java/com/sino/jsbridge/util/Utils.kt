package com.sino.jsbridge.util

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore.MediaColumns
import android.util.Base64
import java.security.MessageDigest


class Utils {
    companion object {

        private val HEX_DIGITS = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f'
        )

        /**
         * 对String进行SHA1加密
         * @param paramsStr
         */
        fun signatureSHA1(paramsStr: String): String {
            return try {
                val digest = MessageDigest.getInstance("SHA-1")
                digest.update(paramsStr.toByteArray())
                toHexString(digest.digest())
            } catch (e: Exception) {
                ""
            }
        }

        /**
         * 十六进制转换
         */
        private fun toHexString(bData: ByteArray): String {
            val sb = StringBuilder(bData.size * 2)
            for (i in bData.indices) {
                sb.append(HEX_DIGITS[(bData[i].toInt() shr 4) and 0x0f])
                sb.append(HEX_DIGITS[bData[i].toInt() and 0x0f])
            }
            return sb.toString()
        }

        /**
         * base64
         */
        fun base64Encode(str: String): String {
            return Base64.encodeToString(str.toByteArray(), Base64.NO_WRAP)
        }

        /**
         * Gets the corresponding path to a file from the given content:// URI
         * @param selectedVideoUri The content:// URI to find the file path from
         * @param contentResolver The content resolver to use to perform the query.
         * @return the file path as a string
         */
        fun getFilePathFromContentUri(
            selectedVideoUri: Uri?,
            contentResolver: ContentResolver
        ): String? {
            val filePath: String
            val filePathColumn = arrayOf(MediaColumns.DATA)
            val cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null)
            //也可用下面的方法拿到cursor
            //Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            filePath = cursor.getString(columnIndex)
            cursor.close()
            return filePath
        }
    }
}