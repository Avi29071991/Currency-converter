package com.avinash.paypay.test.foundation

import com.avinash.paypay.test.foundation.environment.AppInfo
import com.avinash.paypay.test.foundation.environment.Version
import com.avinash.paypay.test.foundation.environment.interfaces.AppInfoProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID

class AppInfoProviderTest {
    @RelaxedMockK
    private lateinit var appInfoProvider: AppInfoProvider

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { appInfoProvider.name } returns UUID.randomUUID().toString()
        every { appInfoProvider.version } returns Version("1.0.0")
        every { appInfoProvider.versionCode } returns System.currentTimeMillis()
        every { appInfoProvider.packageName } returns UUID.randomUUID().toString()
        every { appInfoProvider.isReleaseBuild } returns false
        every { appInfoProvider.fileProviderAuthority } returns UUID.randomUUID().toString()

        AppInfo.register(appInfoProvider)
    }

    @Test
    fun testAppInfoProvider() {
        assertEquals(AppInfo.name(), appInfoProvider.name)
        assertEquals(AppInfo.version().getDescription(), appInfoProvider.version.getDescription())
        Assert.assertTrue(AppInfo.versionCode() == appInfoProvider.versionCode)
        assertEquals(AppInfo.packageName(), appInfoProvider.packageName)
        assertEquals(AppInfo.isReleaseBuild(), appInfoProvider.isReleaseBuild)
        assertEquals(AppInfo.fileProviderAuthority(), appInfoProvider.fileProviderAuthority)

        Assert.assertTrue(AppInfo.eligible)
        Assert.assertTrue(AppInfo.enable)
        Assert.assertTrue(AppInfo.available)
    }

    @After
    fun tearDown() {
        AppInfo.unregister(appInfoProvider)
        unmockkAll()
    }
}