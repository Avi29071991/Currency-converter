package com.avinash.paypay.test.foundation

import com.avinash.paypay.test.foundation.environment.Version
import org.junit.Assert
import org.junit.Test
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.Collections

class VersionTest {
    @Test
    fun testVersionNumbers() {
        val versionA = Version("1.1")
        Assert.assertEquals(versionA.getDescription(), "1.1")

        val versionB = Version("1.1.1")
        // versionA < versionB
        Assert.assertEquals(versionA.compareTo(versionB), -1)
        Assert.assertFalse(versionA == versionB)
        Assert.assertTrue(versionA.beforeOrEquals(versionB))
        Assert.assertFalse(versionA.afterOrEquals(versionB))
        Assert.assertTrue(versionA.before(versionB))
        Assert.assertTrue(versionB.after(versionA))

        val versionC = Version("2.0")
        val versionD = Version("1.15.9")

        // versionC > versionD
        Assert.assertEquals(versionC.compareTo(versionD), 1)
        Assert.assertFalse(versionC == versionD)
        Assert.assertFalse(versionC.beforeOrEquals(versionD))
        Assert.assertTrue(versionC.afterOrEquals(versionD))

        val versionE = Version("1.0")
        val versionF = Version("1")

        // versionE == versionF
        Assert.assertEquals(versionE.compareTo(versionF), 0)
        Assert.assertTrue(versionE == versionF)
        Assert.assertTrue(versionE.beforeOrEquals(versionF))
        Assert.assertTrue(versionE.afterOrEquals(versionF))

        val versionG = Version("1.0.0")
        val versionH = Version("1")

        // versionG == versionH
        Assert.assertEquals(versionG.compareTo(versionH), 0)
        Assert.assertTrue(versionG == versionH)
        Assert.assertTrue(versionG.beforeOrEquals(versionH))
        Assert.assertTrue(versionG.afterOrEquals(versionH))

        val versionI = Version("1.0")
        val versionJ = Version("1.0.0")

        // versionI == versionJ
        Assert.assertEquals(versionI.compareTo(versionJ), 0)
        Assert.assertTrue(versionI == versionJ)
        Assert.assertTrue(versionI.beforeOrEquals(versionI))
        Assert.assertTrue(versionI.afterOrEquals(versionJ))
    }

    @Test
    fun testMinMaxVersions() {
        val versions = mutableListOf(
            Version("2"),
            Version("1.0.5"),
            Version("1.1.0"),
            Version("1.0.1"),
            Version("2.6"),
        )

        Assert.assertEquals(Collections.max(versions).getDescription(), "2.6")
        Assert.assertEquals(Collections.min(versions).getDescription(), "1.0.1")
    }

    @Test
    fun testSecondaryConstructor() {
        val version = Version("0.1.0-Android.debug")
        Assert.assertEquals(version.getDescription(), "0.1.0")

        val versionWithoutReleaseName = Version("1.0.0.debug")
        Assert.assertEquals(versionWithoutReleaseName.getDescription(), "1.0.0")

        val versionWithoutPatch = Version("1.0.0-Android")
        Assert.assertEquals(versionWithoutPatch.getDescription(), "1.0.0")

        val versionWithNumeric = Version("0.0.1")
        Assert.assertEquals(versionWithNumeric.getDescription(), "0.0.1")
    }

    @Test
    fun testPrimaryConstructor() {
        val version = Version(1, 12, 9, "Android", "debug")
        Assert.assertEquals(version.getDescription(), "1.12.9")
        Assert.assertEquals(version.name, "1.12.9-Android.debug")

        val versionWithoutPatch = Version(1, 12, preReleaseName = "Android", build = "debug")
        Assert.assertEquals(versionWithoutPatch.getDescription(), "1.12")
        Assert.assertEquals(versionWithoutPatch.name, "1.12-Android.debug")

        val versionWithoutReleaseName = Version(1, 12, build = "debug")
        Assert.assertEquals(versionWithoutReleaseName.getDescription(), "1.12")
        Assert.assertEquals(versionWithoutReleaseName.name, "1.12.debug")

        val versionWithNumeric = Version(0, 0, 9)
        Assert.assertEquals(versionWithNumeric.getDescription(), "0.0.9")
        Assert.assertEquals(versionWithNumeric.name, "0.0.9")
    }

    @Test
    fun testInvalidVersion() {
        try {
            Version("1.a.1")

            // If exception is not thrown then assert fail
            Assert.fail("Version value was illegal")
        } catch (e: Exception) {
            // If exception is thrown then assert true by checking exception type
            Assert.assertTrue(e.localizedMessage, e is IllegalArgumentException)
        }
    }
}