/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok.commons.seam;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.jboss.mx.util.MBeanProxy;
import org.jboss.mx.util.MBeanProxyCreationException;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.portal.cms.CMS;
import org.jboss.portal.cms.CMSException;
import org.jboss.portal.cms.Command;
import org.jboss.portal.cms.model.File;
import org.jboss.portal.cms.model.Folder;


/**
 * Connect to the jboss CMS system and list uploaded files that
 * ¨can be imported into the database.
 * 
 * @author <tt>Ola Marius Sagli</tt> <a mailto="hoff@hoffsagli.com"> hoff@hoffsagli.com </a>
 * @version $Revision$
 */
public final class CMSImportsSupport {

    /** The path to where textanalysis files are stored */
    private final String cmsPath;

    /** Logger */
    private static final Logger LOG = Logger.getLogger(CMSImportsSupport.class);
    
    /** TODO: Service Injection, and schibstedsok proxy instead of direct jboss calls */
    private final CMS service; 
   
    public CMSImportsSupport(final String cmsPath) {
        
       final MBeanServer mbeanServer = MBeanServerLocator.locateJBoss();
       CMS service = null;
       try{
            service = (CMS)MBeanProxy.get(CMS.class, new ObjectName("portal:service=CMS"), mbeanServer);  
            
       }catch(MalformedObjectNameException mone){
            LOG.error(mone.getMessage(), mone);
       }catch(MBeanProxyCreationException mbpce){
            LOG.error(mbpce.getMessage());
       }catch(NullPointerException npe){
            LOG.error(npe.getMessage(), npe);
       }
       this.service = service;
       this.cmsPath = cmsPath;
    }
    
    /**
     * Return all uploaded files in cmsPath directory. The admin should be responsible
     * for deleting or moving old files.
     */
    public List<CmsImportBean> findCmsImports() {
      
        LOG.debug("getCmsImports()");
        
        //assert service != null : "CMS null";
        if( null != service ){
            final Command cmdList = service.getCommandFactory().createFolderGetListCommand(cmsPath);

            try {
               Folder folder  = (Folder)service.execute(cmdList);

               if(folder == null) {
                   // TODO recreate it
               }

               //assert null != folder;
               LOG.info("CMS Files: " + folder.getFiles().size());

               final List<File> files = folder.getFiles();
               final List<CmsImportBean> retValue = new ArrayList<CmsImportBean>();

               for(File f : files) {
                   retValue.add(new CmsImportBean(f));
               }

                return retValue;

            } catch (CMSException e) {
                LOG.error(e);
            }
        }
        
        return Collections.EMPTY_LIST;
    }
    
    public InputStream getCmsFile(final CmsImportBean cmsBean){
        
        assert service != null;
        assert cmsBean != null;
        assert cmsBean.getCmsFile() != null;
               
        final File file = cmsBean.getCmsFile();
        
        if( null != service ){
            
            final Command cmd 
                    = service.getCommandFactory().createFileGetCommand(file.getBasePath(), Locale.getDefault());

            try {
                final File  f = (File) service.execute(cmd);

                assert f != null;
                assert f.getContent() != null;

                return f.getContent().getStream();

            } catch (CMSException e) {
                LOG.debug(e.getMessage(), e);
            }
        }
        
        // dummy value for failover.
        return new ByteArrayInputStream(new byte[0]);
    }
    
    public final class CmsImportBean implements Serializable {

        final File cmsFile;

        public CmsImportBean(File f) {
            cmsFile = f;
        }

        public File getCmsFile() {
            return cmsFile;
        }

        public String getDisplayName() {
            return cmsFile.getName();
        }

        public long getSize() {
            return 0;
        }

        public Date getDate() {
            return new Date(cmsFile.getCreationDate().getTime());
        }
        
        public String toString(){
            return getDisplayName();
        }
    }
}