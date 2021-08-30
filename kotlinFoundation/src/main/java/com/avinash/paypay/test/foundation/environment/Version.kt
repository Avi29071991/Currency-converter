package com.avinash.paypay.test.foundation.environment

data class Version(
    private var major: Int,
    private var minor: Int,
    private var patch: Int? = null,
    private var preReleaseName: String? = null,
    private var build: String? = null
) : Comparable<Version> {

    /**
     * @constructor Secondary constructor which constructs app version using string
     * @property version String value of app version
     */
    constructor(version: String) : this(0, 0) {
        val versionPreReleaseSplit = version.split("-")
        val splitVersionNumber = versionPreReleaseSplit[0].split(".")
        if (splitVersionNumber.isNotEmpty()) {
            this.major = splitVersionNumber[0].toInt()
            if (splitVersionNumber.size >= maxVersionSplit - 1) {
                this.minor = splitVersionNumber[1].toInt()
                if (splitVersionNumber.size >= maxVersionSplit) {
                    this.patch = splitVersionNumber[2].toInt()
                    if (splitVersionNumber.size >= maxVersionSplit + 1) {
                        this.build = splitVersionNumber[maxVersionSplit]
                    }
                }
            }
        } else {
            throw IllegalArgumentException("Invalid version format")
        }

        if (versionPreReleaseSplit.size >= 2) {
            val releaseNames = versionPreReleaseSplit[1].split(".")
            if (releaseNames.isNotEmpty()) {
                this.preReleaseName = releaseNames[0]
                if (releaseNames.size >= 2) {
                    this.build = releaseNames[1]
                }
            }
        }
    }

    /**
     * Function to provide the value of app version in `major.minor.patch` format
     */
    fun getDescription(): String {
        return listOf(major, minor, patch).mapNotNull { it }.joinToString(separator = ".")
    }

    /**
     * Provides value for app version in `major.minor.patch-preReleaseName.build` format
     */
    val name: String
        get() {
            val computedName = listOf(getDescription(), preReleaseName).mapNotNull { it }.joinToString(separator = "-")
            return listOf(computedName, build).mapNotNull { it }.joinToString(separator = ".")
        }

    /**
     * Function to compare current app version with the provided app version object
     * @param other App version object which needs to be compared to current app version
     * @return Integer value `0` if equals, `1` if is greater, `-1` if is less
     */
    override fun compareTo(other: Version): Int {
        val versionPart = getDescription().split(".")
        val otherVersionPart = other.getDescription().split(".")
        val length = versionPart.size.coerceAtLeast(otherVersionPart.size)

        for (i in 0 until length) {
            val thisPart = if (i < versionPart.size) versionPart[i].toInt() else 0
            val thatPart = if (i < otherVersionPart.size) otherVersionPart[i].toInt() else 0
            if (thisPart < thatPart) return -1
            if (thisPart > thatPart) return 1
        }

        return 0
    }

    /**
     * Function used to check if the current app version is == (equal to) provided version number
     * @param other object which needs to be compared to current app version
     * @return Boolean value provided object is of type AppVersion and is equal to current app version
     */
    override fun equals(other: Any?): Boolean {
        if (other !is Version) return false

        return other.compareTo(this) == 0
    }

    /**
     * Function used to check if the current app version is > (greater then) provided version number
     * @param other App version object which needs to be compared to current app version
     * @return Boolean value if the current app version > provided app version object
     */
    fun after(other: Version): Boolean {
        return this > other
    }

    /**
     * Function used to check if the current app version is >= (greater then OR equal to) provided version number
     * @param other App version object which needs to be compared to current app version
     * @return Boolean value if the current app version >= provided app version object
     */
    fun afterOrEquals(other: Version): Boolean {
        return this == other || this > other
    }

    /**
     * Function used to check if the current app version is < (less then) provided version number
     * @param other App version object which needs to be compared to current app version
     * @return Boolean value if the current app version < provided app version object
     */
    fun before(other: Version): Boolean {
        return this < other
    }

    /**
     * Function used to check if the current app version is <= (less then OR equal to) provided version number
     * @param other App version object which needs to be compared to current app version
     * @return Boolean value if the current app version <= provided app version object
     */
    fun beforeOrEquals(other: Version): Boolean {
        return this == other || this < other
    }

    /**
     * Function which returns hashcode value of the object OR zero if null
     * @return Integer value of hash code
     */
    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        const val maxVersionSplit = 3
    }
}
