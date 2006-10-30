/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import no.schibstedsok.log4j.Log4jDelayFilter;

import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;

/**
 * Unit test class for <code>Log4jDelayFilter</code>.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Meckelborg Rognerud</a>
 * @version <tt>$Revision: $</tt>
 */
public final class Log4jDelayFilterTest extends TestCase {

    /** The original standard out. */
    private PrintStream stdout;

    /** The new standard out used by the test. */
    private ByteArrayOutputStream testStdout;

    /**
     * Create the test case.
     *
     * @param testName name of the test case
     */
    public Log4jDelayFilterTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(Log4jDelayFilterTest.class);
    }

    /**
     * Test setup method that overrides standard out.
     */
    public void setUp() {
        System.out.println("Setup - overriding stdout.");
        stdout = System.out;
        testStdout = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(testStdout);
        System.setOut(printStream);
    }

    /**
     * Test tear down method that restores standard out.
     */
    public void tearDown() {
        System.setOut(stdout);
        testStdout = null;
        System.out.println("Stdout restored.");
    }

    /**
     * Method that creates a logger that logs to a string writer and that includes a delay filter.
     *
     * @param writer writer that the logger should log messages to
     * @param secondsDelay seconds sent to the delay filter
     * @param logNonSendingToConsole should denied messages be logged or not
     * @return the new logger
     */
    private Logger createLogger(final StringWriter writer, final int secondsDelay,
            final boolean logNonSendingToConsole) {
        final Log4jDelayFilter delayFilter = new Log4jDelayFilter();
        delayFilter.setSecondsDelay(secondsDelay);
        delayFilter.setLogNonSendingToConsole(logNonSendingToConsole);

        final WriterAppender appender = new WriterAppender(new SimpleLayout(), writer);
        appender.addFilter(delayFilter);

        final Logger newLogger = Logger.getLogger("TestLogger");
        newLogger.addAppender(appender);
        return newLogger;
    }

    /**
     * Method that returns the text that a SimpleLayout logs on a info logging.
     *
     * @param message the message to log
     * @return the text that simple layout logs
     */
    private String getLogOutput(final String message) {
        return "INFO - " + message + System.getProperty("line.separator");
    }

    /**
     * Method that tests if messages is delayed the correct time, without logging denied messages.
     */
    public void testDelayFilterWithoutConsoleLogging() {
        final StringWriter logWriter = new StringWriter();
        final Logger logger = createLogger(logWriter, 1, false);
        final StringBuilder assertString = new StringBuilder();

        // Nothing logged yet.
        assertEquals(assertString.toString(), logWriter.toString());

        // Message that should be logged.
        logger.info("AAA");
        assertString.append(getLogOutput("AAA"));
        assertEquals(assertString.toString(), logWriter.toString());

        // Nothing should be logged the next second.
        for (int i = 0; i < 2; i++) {
            logger.info("BBB");
            assertEquals(assertString.toString(), logWriter.toString());

            try {
                Thread.sleep(510);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // This message should be logged.
        logger.info("CCC");
        assertString.append(getLogOutput("CCC"));
        assertEquals(assertString.toString(), logWriter.toString());
    }

    /**
     * Method that tests if deny messages are logged to the console.
     */
    public void testDeniedConsoleLogging() {
        final StringWriter logWriter = new StringWriter();
        final Logger logger = createLogger(logWriter, 1, true);

        // Nothing logged yet.
        assertEquals("", logWriter.toString());
        assertEquals("", testStdout.toString());

        // Message that should be logged.
        logger.info("AAA");
        assertEquals(getLogOutput("AAA"), logWriter.toString());
        assertEquals("", testStdout.toString());

        // Message that should not be logged, but a deny message should appear in the console.
        logger.info("BBB");
        assertEquals(getLogOutput("AAA"), logWriter.toString());
        assertTrue(testStdout.toString().indexOf("denies") != -1);
        assertTrue(testStdout.toString().indexOf("BBB") != -1);
    }

}
