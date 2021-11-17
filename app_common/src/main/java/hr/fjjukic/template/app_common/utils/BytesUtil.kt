package hr.fjjukic.template.app_common.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * Util for working with ByteArray
 */
class BytesUtil {
    companion object {
        fun <T> toByteArray(items: ArrayList<T>): ByteArray {
            val bos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(bos)

            oos.writeObject(items)
            val bytes = bos.toByteArray()

            oos.close()
            bos.close()

            return bytes
        }

        fun <T> toObjectArray(bytes: ByteArray): ArrayList<T> {
            val bis = ByteArrayInputStream(bytes)
            val ois = ObjectInputStream(bis)

            val message = ois.readObject() as ArrayList<T>

            bis.close()
            ois.close()

            return message
        }

        fun <T> toByteArray(item: T): ByteArray {
            val bos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(bos)

            oos.writeObject(item)
            val bytes = bos.toByteArray()

            oos.close()
            bos.close()

            return bytes
        }

        fun <T> toObject(bytes: ByteArray): T {
            val bis = ByteArrayInputStream(bytes)
            val ois = ObjectInputStream(bis)

            val message = ois.readObject() as T

            bis.close()
            ois.close()

            return message
        }
    }
}