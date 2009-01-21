/** Copyright (2005-2007) Schibsted Søk AS
 *   This file is part of Sesat Commons.
 *
 *   Sesat Commons is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Sesat Commons is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Sesat Commons.  If not, see <http://www.gnu.org/licenses/>.
**/
package no.schibstedsok.log4j;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/** View the <b>Log4j</b> Loggers loaded in the Context.
 * Also allows editing of the logger's level at runtime.
 *
 * This Servlet is shared between projects so it is essential that it does not use/import other schibsted classes.
 *
 * Requires Log4J 1.2.13
 *
 * TODO i18n
 *
 * @version $Id: LoggingServlet.java 51 2006-02-13 16:35:10Z mickw $
 */
public final class LoggingServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(LoggingServlet.class);
    private static final MessageFormat OPTIONS = new MessageFormat(
            "<option value=\"ALL\" {0}>ALL</option>"
            + "<option value=\"TRACE\" {1}>TRACE</option>"
            + "<option value=\"DEBUG\" {2}>DEBUG</option>"
            + "<option value=\"INFO\" {3}>INFO</option>"
            + "<option value=\"WARN\"{4}>WARN</option>"
            + "<option value=\"ERROR\" {5}>ERROR</option>"
            + "<option value=\"FATAL\" {6}>FATAL</option>"
            + "<option value=\"OFF\"{7}>OFF</option>");
    private static final String SELECTED = "selected";

    private static final String REMOTE_ADDRESS_KEY = "REMOTE_ADDR";
    private static final String ERR_RESTRICTED_AREA = "<strong>Restricted Area!</strong>";
    private static final String ERR_TRIED_TO_ACCESS = " tried to access Log servlet!";
    private static final String DEBUG_CLIENT_IP = "Client ipaddress ";
    private static final String PACKAGE_FILTER = "show";
    
    
    private String[] ipaddressesAllowed = new String[]{};

    @Override
    public void init(final ServletConfig config) throws ServletException {
        
        super.init(config);
        
        final String allowed = config.getInitParameter("ipaddresses.allowed");
        LOG.warn("allowing ipaddresses " + allowed);
        if (null != allowed && allowed.length() >0) {
            ipaddressesAllowed = allowed.split(",");
        }
    }

    
    
    /** {@inheritDoc}
     */
    @Override
    public void doGet(
            final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException, IOException {

        LOG.trace("start doGet");
        request.setCharacterEncoding("UTF-8"); // correct encoding

        // If we are behind a ajp13 connection request.getServerName() won't work!
        // httpd.conf needs: "JkEnvVar REMOTE_ADDR" inside the virtual host directive.
        final String ipAddr = null != request.getAttribute(REMOTE_ADDRESS_KEY)
            ? (String) request.getAttribute(REMOTE_ADDRESS_KEY)
            : request.getRemoteAddr();
        LOG.debug(DEBUG_CLIENT_IP + ipAddr);

        // restricted to only schibsted internal network.
        // TODO Move this into servlet properties
        if (!isIpAllowed(ipAddr)) {

            final ServletOutputStream ss = response.getOutputStream();
            response.setContentType("text/html;charset=UTF-8");
            ss.print(ERR_RESTRICTED_AREA + ": " + ipAddr);
            ss.close();
            LOG.warn(ipAddr + ERR_TRIED_TO_ACCESS);
            return ; 
        }  

        final Enumeration en = LogManager.getCurrentLoggers();

        Logger log;
        // Sort first
        final HashMap<String,Level> unsorted = new HashMap<String,Level>();
        while (en.hasMoreElements()) {
            log = (Logger) en.nextElement();
            unsorted.put(log.getName(), log.getEffectiveLevel());
        }
        final List<String> sortedList = new ArrayList<String>(unsorted.keySet());
        final StringBuilder buffer = new StringBuilder();
        Collections.sort(sortedList);

        try  {

            // developers will get the hang of how to change the non-displayed loggers if they want.
            final String pakage = request.getParameter(PACKAGE_FILTER);
            if(null == pakage || 0 == pakage.length()){
                buffer.append("<tr><td>"
                        + "<b>Use the <i>show</i> parameter to display loggers.</b><br/>"
                        + "For example <i>show=no.sesat</i> to display all no.sesat.. loggers."
                        + "</td></tr>");
            }

            for( String key : sortedList ){
                Level level = unsorted.get(key);
                String value = level.toString();
                
                // update if in request parameters
                final String param = request.getParameter(key);
                if (param != null && !param.equals(value)) {
                    final Level newLevel = Level.toLevel(param);
                    Logger.getLogger(key).setLevel(newLevel);
                    LOG.warn("Logger " + key + " has been changed to level: " + param);
                    level = newLevel;
                    value = param;
                }
                // output html
                if(null != pakage && 0 < pakage.length() && key.startsWith(pakage) ){
                    final int option = getOption(level.toInt());
                    final String[] values = new String[]{"", "", "", "", "", "", "", ""};
                    values[option] = SELECTED;
                    // The MessageFormat constant does not support synchronous usage.
                    synchronized (OPTIONS) {
                        buffer.append("<tr><td><b>" + key + "</b></td><td><select size=\"1\" name=\"" + key + "\">" 
                                + OPTIONS.format(values) + "</select></td></tr>");
                    }
                }
            }


            final ServletOutputStream ss = response.getOutputStream();
            response.setContentType("text/html;charset=UTF-8");
            ss.print("<form action=\"Log\"><div style=\"float: left;\"><table>");
            ss.print(buffer.toString());
            ss.print("</table></div><div style=\"float: right;\">"
                    + "<input class=\"submit\" type=\"submit\" value=\"Update\"/></div></form>");
            ss.close();

        }  catch (IOException io) {
            LOG.error("doGet ", io);
        }
    }

    private static int getOption(final int priority) {

        int option;
        switch(priority) {
            case Level.ALL_INT:
                option = 0;
                break;
            case Level.TRACE_INT:
                option = 1;
                break;
            case Level.DEBUG_INT:
                option = 2;
                break;
            case Level.INFO_INT:
                option = 3;
                break;
            case Level.WARN_INT:
                option = 4;
                break;
            case Level.ERROR_INT:
                option = 5;
                break;
            case Level.FATAL_INT:
                option = 6;
                break;
            case Level.OFF_INT:
            default:
                option = 7;
                break;
        }
        return option;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }


    /**
     * Returns wether we allow the ipaddress or not.
     * @param ipAddr the ipaddress to check.
     *
     * @return returns true if the ip address is trusted.
     */
   private boolean isIpAllowed(final String ipAddr) {

	 boolean allowed =
                 ipAddr.startsWith("127.") || ipAddr.startsWith("10.") || ipAddr.startsWith("0:0:0:0:0:0:0:1%0");

     for(String s : ipaddressesAllowed){
         allowed |= ipAddr.startsWith(s);
     }
     return allowed;

    }
   
}
