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

import org.asciidoctor.Asciidoctor
import org.asciidoctor.Options
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.asciidoctor.extension.ImageDiffCalculator.compareImages

/**
 * Integration test for substituting attributes in the screenshot macro.
 */
class ScreenshotMacroWebDriverTest extends Specification {

    private static final String url = ScreenshotMacroWebDriverTest.classLoader.getResource("browser.html").toString()
    private static final String document1 = """= Test substitutions

:geb.driver: chrome

screenshot::${url}[name="chrome1"]

screenshot::${url}[name="firefox", geb.driver='firefox']

screenshot::${url}[name="chrome2"]

"""

    @Rule
    TemporaryFolder tmpFolder = new TemporaryFolder()

    private File outputDir
    private Options options
    private Asciidoctor asciidoctor

    void setup() {
        outputDir = tmpFolder.newFolder()
        options = new Options()
        options.setDestinationDir(outputDir.absolutePath)

        asciidoctor = Asciidoctor.Factory.create()
    }

    def "test keys in geb block"() {
        when:
          String html = asciidoctor.convert(document1, options)
          html = html.replaceAll('\n', ' ')

        then:
          html.contains('chrome1.png')
          html.contains('firefox.png')
          html.contains('chrome2.png')
        File screenshotFolder = new File(outputDir, 'screenshots')
          compareImages(new File(screenshotFolder, 'chrome1.png'), 'screenshot_browser_chrome.png')
          compareImages(new File(screenshotFolder, 'firefox.png'), 'screenshot_browser_firefox.png')
          compareImages(new File(screenshotFolder, 'chrome2.png'), 'screenshot_browser_chrome.png')

    }
}
