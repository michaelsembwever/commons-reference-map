/*
 * Copyright (2005-2007) Schibsted Søk AS
 *   This file is part of SESAT.
 *
 *   SESAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SESAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with SESAT.  If not, see <http://www.gnu.org/licenses/>.
 */
package no.schibstedsok.log4j;

import java.util.Date;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * This filter accepts only one logging per N seconds. It can be used as a safety net i.e. when sending
 * mail with the SMTPAppender. If something serious happens, we don't want to send thousands
 * of emails.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Meckelborg Rognerud</a>
 * @version <tt>$Id$</tt>
 */
public final class Log4jDelayFilter extends Filter {

    /** Default value for <code>logNonSendingToConsole</code>. */
    private static final boolean DEFAULT_LOG_NON_SENDING_TO_CONSOLE = false;

    /** Default value for <code>secondsDelay</code>. */
    private static final int DEFAULT_SECONDS_DELAY = 5 * 60;

    /** Decides if the denies of events should be logged to the console. */
    private boolean logNonSendingToConsole = DEFAULT_LOG_NON_SENDING_TO_CONSOLE;

    /** The number of seconds it has to be between log events, orelse the events are denied. */
    private int secondsDelay = DEFAULT_SECONDS_DELAY;

    /** Timestamp last event logged. */
    private Date timestampLastEventLogged = null;

    /**
     * The constructor.
     */
    public Log4jDelayFilter() {
        super();
    }

    /**
     * The decide method of the log4j filter.
     *
     * @param event the log event to decide
     * @return the decide value
     */
    public int decide(final LoggingEvent event) {
        // Check if the message should be denied.
        if (denyLogEvent()) {
            if (logNonSendingToConsole) {
                System.out.println("Log4jDelayFilter - denies message: " + event.getMessage());
            }
            return Filter.DENY;
        }

        // Accept the log event and update the timestamp.
        timestampLastEventLogged = new Date();
        return Filter.NEUTRAL;
    }

    /**
     * Method that checks if the log event should be denied or not by checking the timestamp
     * for the last accepted logging.
     *
     * @return if the log event should be denied or not
     */
    private boolean denyLogEvent() {
        if (timestampLastEventLogged == null) {
            return false;
        } else {
            return (new Date().getTime() - timestampLastEventLogged.getTime() < secondsDelay * 1000);
        }
    }

    /**
     * Set <code>secondsDelay</code>.
     *
     * @param secondsDelay new value for <code>secondsDelay</code>
     */
    public void setSecondsDelay(final int secondsDelay) {
        this.secondsDelay = secondsDelay;
    }

    /**
     * Get <code>secondsDelay</code>.
     *
     * @return <code>secondsDelay</code>
     */
    public int getSecondsDelay() {
        return secondsDelay;
    }

    /**
     * Set <code>logNonSendingToConsole</code>.
     *
     * @param logNonSendingToConsole new value for <code>logNonSendingToConsole</code>
     */
    public void setLogNonSendingToConsole(final boolean logNonSendingToConsole) {
        this.logNonSendingToConsole = logNonSendingToConsole;
    }

    /**
     * Get <code>logNonSendingToConsole</code>.
     *
     * @return <code>logNonSendingToConsole</code>
     */
    public boolean isLogNonSendingToConsole() {
        return logNonSendingToConsole;
    }

}
