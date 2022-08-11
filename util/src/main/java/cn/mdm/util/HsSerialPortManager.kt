package cn.mdm.util

import android.util.Log
import com.kongqw.serialportlibrary.SerialPortManager
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener.Status
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener
import java.io.File
import java.util.*

class HsSerialPortManager(var path:String = "/dev/ttyS2", var baudRate:Int = 9600) {

    //初始化
    private var mSerialPortManager:SerialPortManager = SerialPortManager()
    //是否打开
    private var isOpen = false
    //两次扫码的间隔时间 以防短时间多次扫码，默认设置为1秒
    var intervalTime = 1000L

    //记录每次扫码的开始时间
    private var startTime = 0L
    /**
     * 打开串口
     * @param dataReceived 监听串口读取数据
     * @param dataSent 监听发送到串口的数据
     * @param openSuccess 打开串口成功
     * @param error  打开串口失败
     * @return 当前对象
     */
    fun openSerialPort(dataReceived: ((bytes:ByteArray)->Unit)?,dataSent:((bytes:ByteArray)->Unit)?=null,
                       openSuccess:((file: File)->Unit)?=null,
                       error:((file: File, status: Status)->Unit)?=null):HsSerialPortManager{
        if(isOpen){
            //若是已经打开了，就直接设置到监听中
            setOnDataListener(dataReceived,dataSent)
            return this
        }
        mSerialPortManager.setOnOpenSerialPortListener(object : OnOpenSerialPortListener{
            override fun onSuccess(file: File) {
                if(openSuccess!= null)openSuccess(file)
                setOnDataListener(dataReceived,dataSent)
            }
            override fun onFail(file: File, status: Status) {
                if(error != null)error(file,status)
            }
        })
        isOpen = mSerialPortManager.openSerialPort(File(path),baudRate)
        return this
    }
    var index = 0

    /**
     * 设置串口数据监听器
     * @param dataReceived 监听串口读取数据
     * @param dataSent 监听发送到串口的数据
     * @return 当前对象
     */
    fun setOnDataListener(dataReceived: ((bytes:ByteArray)->Unit)?,dataSent:((bytes:ByteArray)->Unit)?=null):HsSerialPortManager{
        mSerialPortManager.setOnSerialPortDataListener(object : OnSerialPortDataListener{
            override fun onDataReceived(bytes: ByteArray?) {
                if(System.currentTimeMillis() - startTime > intervalTime) {
                    startTime = System.currentTimeMillis()
                    if (dataReceived != null) dataReceived(bytes ?: ByteArray(0))
                    Log.i("setOnDataListener","次数${index++}")
                }
            }

            override fun onDataSent(bytes: ByteArray?) {
                if(dataSent != null)dataSent(bytes?:ByteArray(0))
            }

        })
        return this
    }

    //发送串口数据
    fun sendBytes(bytes:ByteArray):Boolean{
        if(!isOpen)return false
        return mSerialPortManager.sendBytes(bytes)
    }


    //关闭串口
    fun close(){
        mSerialPortManager.closeSerialPort()
        isOpen = false
    }
    companion object{
        val SERIAL_PORT_PATH_LIST = listOf(
            "/dev/ttyG3", "/dev/ttyG2", "/dev/ttyG1", "/dev/ttyG0","/dev/ttyS4",
            "/dev/ttyS3", "/dev/ttyS2", "/dev/ttyS1", "/dev/ttyS0", "/dev/ttyUSB10")
        val SERIAL_PORT_BAUD_RATE_LIST = listOf(
            2400, 4800, 9600, 19200, 38400, 57600, 115200,
            230400, 460800, 500000, 576000, 921600)

        @JvmStatic
        fun hexStringToBytes(hexString: String): ByteArray {
            var hex = hexString
            var length = hex.length
            val result: ByteArray
            //奇数
            if (length and 0x1 == 1) {
                length++
                result = ByteArray(length / 2)
                hex = "0$hexString"
            } else { //偶数
                result = ByteArray(length / 2)
            }
            var j = 0
            var i = 0
            while (i < length) {
                result[j] = hex.substring(i, i + 2).toInt(16).toByte()
                j++
                i += 2
            }
            return result
        }

        @JvmStatic
        fun bytesToHexString(src: ByteArray?): String {
            val stringBuilder = StringBuilder()
            if (src == null || src.isEmpty()) return ""
            for (b in src) {
                val v: Int = b.toInt() and 0xFF
                val hv = Integer.toHexString(v)
                if (hv.length < 2) stringBuilder.append(0)
                stringBuilder.append(hv)
            }
            return stringBuilder.toString()
        }

        @JvmStatic
        fun convertHexToASCII(hex: String): String {
            return try {
                val sb = StringBuilder()
                var i = 0
                while (i < hex.length - 1) {
                    //grab the hex in pairs
                    val output = hex.substring(i, i + 2)
                    //convert hex to decimal
                    val decimal = output.toInt(16)
                    //convert the decimal to character
                    sb.append(decimal.toChar())
                    i += 2
                }
                sb.toString().trim { it <= ' ' }
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        //Ascii码转化为16进制字符串
        @JvmStatic
        fun convertASCIIToHex(str: String): String {
            return try {
                val chars = str.toCharArray()
                val hex = StringBuilder()
                for (aChar in chars) {
                    val hv = Integer.toHexString(aChar.toInt())
                    if (hv.length < 2) hex.append(0)
                    hex.append(hv)
                }
                hex.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        //获取普通卡号
        @JvmStatic
        fun getByte2Card(src: ByteArray):String{
            return try{
                //字符串就是ASCII码
                return convertHexToASCII(bytesToHexString(src))
            }catch(e:Exception) {
                Log.e("HsSerialPortManager",e.message.toString())
                ""
            }
        }

        /**
         * 吊挂的系统 卡号
         */
        @JvmStatic
        fun getDiaoGuaCardNo(src: ByteArray): String? {
            return try{
                val asciiStr = convertHexToASCII(bytesToHexString(src))
                //字符串就是ASCII码,截取前面的2个字符到最后一个字前的中间字符作为16进制字符串转化为10进制，作为卡号
                return asciiStr.substring(2,asciiStr.length-1).toLong(16).toString()
            }catch(e:Exception) {
                Log.e("HsSerialPortManager",e.message.toString())
                ""
            }
        }
    }
}