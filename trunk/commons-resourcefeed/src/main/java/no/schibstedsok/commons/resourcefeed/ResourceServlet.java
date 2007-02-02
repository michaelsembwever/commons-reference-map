/* Copyright (2006) Schibsted Søk AS
 * ResourceServlet.java
 *
 * Created on 19 January 2006, 13:51
 */

package no.schibstedsok.commons.resourcefeed;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/** Resource Provider.
 * Serves configuration files (properties, xml), css, gifs, jpgs, javascript,
 *  and velocity templates for search-front-html.
 * Css, images, and javascript require direct access from client.
 * Apache uses rewriting and proxing to double up the virtual hostname to get the correct css/images/javascript
 * for the specified virtual host (SiteSearch).
 *
 *
 * @author <a href="mailto:mick@wever.org">Michael Semb Wever</a>
 * @version $Id: ResourceServlet.java 200 2006-03-30 12:45:10Z mickw $
 */
public final class ResourceServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ResourceServlet.class);

    private static final String REMOTE_ADDRESS_KEY = "REMOTE_ADDR";
    private static final String ERR_RESTRICTED_AREA = "<strong>Restricted Area!</strong>";
    private static final String ERR_TRIED_TO_ACCESS = " tried to access Resource servlet!";
    private static final String ERR_NOT_FOUND = "Failed to find resource ";
    private static final String ERR_TRIED_TO_CROSS_REFERENCE = " tried to cross-reference resource!";

    private static final String DEBUG_DEFAULT_MODIFCATION_TIMESTAMP = "Default modified timestamp set to ";
    private static final String DEBUG_CLIENT_IP = "Client ipaddress ";
    
    private static final Map<String,String> CONTENT_TYPES = new HashMap<String,String>();
    private static final Map<String,String> CONTENT_PATHS = new HashMap<String,String>();
    private static final Set<String> RESTRICTED = new HashSet<String>();

    private long defaultLastModified = 0;
    private String[] ipaddressesAllowed = new String[]{};

    static {
        // The different extension to content type mappings
        // XXX is there an opensource library to do this?
        CONTENT_TYPES.put("properties", "text/plain");
        CONTENT_TYPES.put("xml", "text/xml");
        CONTENT_TYPES.put("css", "text/css");
        CONTENT_TYPES.put("js", "text/javascript");
        CONTENT_TYPES.put("jpg", "image/jpeg");
        CONTENT_TYPES.put("gif", "image/gif");
        CONTENT_TYPES.put("png", "image/png");
        CONTENT_TYPES.put("vm", "text/plain");
        CONTENT_TYPES.put("html", "text/plain");
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * This servlet ignores URL parameters and POST content, as all the information is in the path,
     * so it really doesn't matter if it is a GET or POST.
     *
     * Checks:
     *  - resource exists,
     *  - correct path is being used,
     *  - configuration/template resources are only accessed by schibsted machines,
     *
     * The resource is served to the ServletOutputStream byte by byte from
     *  getClass().getResourceAsStream(..)
     *
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException if ServletException occurs
     * @throws java.io.IOException if IOException occurs
     */
    protected void processRequest(
            final HttpServletRequest request,
            final HttpServletResponse response)
                throws ServletException, IOException {

        ServletOutputStream os = null;
        InputStream is = null;
        
        try  {
            request.setCharacterEncoding("UTF-8"); // correct encoding
            os = response.getOutputStream();
            
            // Get resource name. Also strip the version number out of the resource
            final String configName = request.getPathInfo().replaceAll("/(\\d)+/","/");
            
            if( configName != null && configName.trim().length() > 0 ){
                
            
                final String extension = configName.substring(configName.lastIndexOf('.') + 1).toLowerCase();
                final String ipAddr = null != request.getAttribute(REMOTE_ADDRESS_KEY)
                    ? (String) request.getAttribute(REMOTE_ADDRESS_KEY)
                    : request.getRemoteAddr();

                // Content-Type
                response.setContentType(CONTENT_TYPES.get(extension) + ";charset=UTF-8");

                // Path check. Resource can only be loaded through correct path.
                final String directory = request.getServletPath();
                if (directory.indexOf( CONTENT_PATHS.get(extension)) >= 0) {

                    // ok, check configuration resources are private.
                    LOG.trace(DEBUG_CLIENT_IP + ipAddr);

                    if (RESTRICTED.contains(extension) && !isIpAllowed(ipAddr)) {

                        response.setContentType("text/html;charset=UTF-8");
                        os.print(ERR_RESTRICTED_AREA);
                        LOG.warn(ipAddr + ERR_TRIED_TO_ACCESS);

                    }  else  {


                        /** [TODO] We need to check in some backend if the resource has been modified.
                         * The defaultLastModified timestamp must be overridden at the same time.
                         * See getLastModified method.
                         **/
                        is = getClass().getResourceAsStream( (configName.startsWith("/") ? "" :  '/') + configName);

                        if (is != null) {

                            // Output the resource byte for byte
                            for (int b = is.read(); b >= 0; b = is.read()) {
                                 os.write(b);
                            }
                            response.setStatus(HttpServletResponse.SC_OK);

                        }  else  {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                            LOG.info(ERR_NOT_FOUND + request.getPathInfo());
                        }
                    }
                }  else  {
                    // not allowed to cross-reference resources.
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    LOG.warn(ipAddr + ERR_TRIED_TO_CROSS_REFERENCE);
                }
            }

        }  finally  {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
    
    /**
     * Returns wether we allow the ipaddress or not.
     * @param ipAddr
     */
    private boolean isIpAllowed(String ipAddr) {
        
	 boolean allowed = 
                 ipAddr.startsWith("127.") || ipAddr.startsWith("10.") || ipAddr.startsWith("0:0:0:0:0:0:0:1%0");
         
         for(String s : ipaddressesAllowed){
             allowed |= ipAddr.startsWith(s);
         }
         return allowed;
             
    }
    
    /** {@inheritDoc}
     */
    protected void doGet(
            final HttpServletRequest request,
            final HttpServletResponse response)
                throws ServletException, IOException {

        processRequest(request, response);
    }

    /** {@inheritDoc}
     */
    protected void doPost(
            final HttpServletRequest request,
            final HttpServletResponse response)
                throws ServletException, IOException {

        processRequest(request, response);
    }

    /** {@inheritDoc}
     */
    public String getServletInfo() {
        
        return "Servlet responsible for serving resources. Goes in hand with search-portal/site-spi";
    }

    public void init(final ServletConfig config) {
        
        defaultLastModified = System.currentTimeMillis();
        LOG.info(DEBUG_DEFAULT_MODIFCATION_TIMESTAMP + defaultLastModified);
        
        final String allowed = config.getInitParameter("ipaddresses.allowed");
        LOG.info("allowing ipaddresses " + allowed);
        if( null != allowed && allowed.length() >0 ){
            ipaddressesAllowed = allowed.split(",");
        }
        
        final String restricted = config.getInitParameter("resources.restricted");
        LOG.info("restricted resources " + restricted);
        if( null != restricted && restricted.length()>0 ){
            RESTRICTED.addAll(Arrays.asList(restricted.split(",")));
        }
        
        final String paths = config.getInitParameter("content.paths");
        LOG.info("content path mappings " + paths);
        if( null != paths && paths.length()>0 ){
            final String[] pathArr = paths.split(",");
            for( String path : pathArr){
                final String[] pair = path.split("=");
                CONTENT_PATHS.put(pair[0], pair[1]);
            }
        }
    }


    /** {@inheritDoc}
     */
    protected long getLastModified(final HttpServletRequest req) {
        return defaultLastModified;
    }

}
