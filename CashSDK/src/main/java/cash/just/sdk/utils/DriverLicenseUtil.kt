package cash.just.sdk.utils

class DriverLicenseUtil {
    companion object {

        const val CITY = "DAI"
        const val STATE = "DAJ"
        const val STREET = "DAG"
        const val ZIP = "DAK"
        const val BIRTH_DATE = "DBB"
        const val EXPIRY_DATE = "DBA"
        const val FIRST_NAME = "DAC"
        const val GENDER = "DBC"
        const val ISSUE_DATE = "DBD"
        const val ISSUING_COUNTRY = "DCG"
        const val LAST_NAME = "DCS"
        const val LICENSE_NUMBER = "DAQ"
        const val MIDDLE_NAME = "DAD"

        private val DRIVER_LICENSE_INFO: LinkedHashMap<String, String> =
            object : LinkedHashMap<String, String>() {
                init {
                    put(EXPIRY_DATE, "Document Expiration Date:")
                    put(LAST_NAME, "Customer Last Name:")
                    put(FIRST_NAME, "Customer First Name:")
                    put(MIDDLE_NAME, "Customer Middle Name(s):")
                    put(ISSUE_DATE, "Document Issue Date:")
                    put(BIRTH_DATE, "Date of Birth:")
                    put(GENDER, "Physical Description – Sex:")
                    put(STREET, "Address – Street 1:")
                    put(CITY, "Address – City:")
                    put(STATE, "Address – Jurisdiction Code:")
                    put(ZIP, "Address – Postal Code:")
                    put(LICENSE_NUMBER, "Customer ID Number:")
                    put(ISSUING_COUNTRY, "Country Identification:")
                }
            }

        fun readDriverLicense(resultText: String): HashMap<String, String>? {
            var result = resultText
            val resultMap = HashMap<String, String>()
            result = result.substring(result.indexOf("\n") + 1)
            val end = result.indexOf("\n")
            val firstLine = result.substring(0, end + 1)
            var findFirstLine = false
            for ((key) in DRIVER_LICENSE_INFO.entries) {
                try {
                    val startIndex = result.indexOf(
                        """
                    
                    $key
                    """.trimIndent()
                    )
                    if (startIndex != -1) {
                        val endIndex = result.indexOf("\n", startIndex + key.length + 1)
                        val value =
                            result.substring(startIndex + key.length + 1, endIndex)
                        resultMap[key] = value
                    } else if (!findFirstLine) {
                        val index = firstLine.indexOf(key)
                        if (index != -1) {
                            val endIndex = firstLine.indexOf("\n", key.length + 1)
                            val value =
                                firstLine.substring(index + key.length, endIndex)
                            resultMap[key] = value
                            findFirstLine = true
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            return resultMap
        }
    }
}