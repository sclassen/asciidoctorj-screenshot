/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Stephan Classen, Markus Schlichting
 * Copyright (c) 2014 François-Xavier Thoorens
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.asciidoctor.extension

import geb.Browser
import geb.driver.CachingDriverFactory

/**
 * Block macro to take a screenshot.
 */
trait DriverSelector {

    private static final String GEB_DRIVER = 'geb.driver'
    private static final String GEB_DRIVER2 = 'gebdriver'

    void setGebDriver(Browser browser, Map<String, Object> globalAttributes, Map<String, Object> attributes) {
        if (attributes.containsKey(GEB_DRIVER)) {
            setGebDriverTo(browser, attributes[GEB_DRIVER])
        } else if (globalAttributes.containsKey(GEB_DRIVER)) {
            setGebDriverTo(browser, globalAttributes[GEB_DRIVER])
        } else if (globalAttributes.containsKey(GEB_DRIVER2)) {
            setGebDriverTo(browser, globalAttributes[GEB_DRIVER2])
        }
    }

    private void setGebDriverTo(Browser browser, Object driver) {
        Properties props = browser.config.properties
        Object oldDriver = props[GEB_DRIVER]

        if (oldDriver == null || !oldDriver.equals(driver)) {
            CachingDriverFactory.clearCacheAndQuitDriver()
            browser.config.driver = null
            props[GEB_DRIVER] = driver
        }
    }
}