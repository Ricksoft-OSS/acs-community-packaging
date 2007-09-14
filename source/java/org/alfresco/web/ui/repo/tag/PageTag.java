/*
 * Copyright (C) 2005-2007 Alfresco Software Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of 
 * the GPL, you may redistribute this Program in connection with Free/Libre 
 * and Open Source Software ("FLOSS") applications as described in Alfresco's 
 * FLOSS exception.  You should have recieved a copy of the text describing 
 * the FLOSS exception, and it is also available here: 
 * http://www.alfresco.com/legal/licensing"
 */
package org.alfresco.web.ui.repo.tag;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.alfresco.web.app.Application;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A non-JSF tag library that adds the HTML begin and end tags if running in servlet mode
 * 
 * @author gavinc
 */
public class PageTag extends TagSupport
{
   private static final long serialVersionUID = 8142765393181557228L;
   
   private final static String SCRIPTS_START = "<script type=\"text/javascript\" src=\"";
   private final static String SCRIPTS_END = "\"></script>\n";
   private final static String STYLES_START  = "<link rel=\"stylesheet\" href=\"";
   private final static String STYLES_MAIN  = "/css/main.css\" type=\"text/css\">\n";

   private final static String[] SCRIPTS = 
   {
      // menu javascript
      "/scripts/menu.js",
      // webdav javascript
      "/scripts/webdav.js",
      // base yahoo file
      "/scripts/ajax/yahoo/yahoo/yahoo-min.js",
      // io handling (AJAX)
      "/scripts/ajax/yahoo/connection/connection-min.js",
      // event handling
      "/scripts/ajax/yahoo/event/event-min.js",
      // mootools
      "/scripts/ajax/mootools.v1.11.js",
      // common Alfresco util methods
      "/scripts/ajax/common.js",
      // pop-up panel helper objects
      "/scripts/ajax/summary-info.js"
   };

/**
 * Please ensure you understand the terms of the license before changing the contents of this file.
 */
   
   private final static String ALF_LOGO_HTTP  = "http://www.alfresco.com/images/alfresco_community_horiz21.gif";
   private final static String ALF_LOGO_HTTPS = "https://www.alfresco.com/images/alfresco_community_horiz21.gif";
   private final static String ALF_URL   = "http://www.alfresco.com";
   private final static String SF_LOGO   = "/images/logo/sflogo.php.png";
   private final static String ALF_TEXT  = "Alfresco Community";
   private final static String ALF_COPY  = "Supplied free of charge with " +
        "<a class=footer href='http://www.alfresco.com/services/support/communityterms/#support'>no support</a>, " +
        "<a class=footer href='http://www.alfresco.com/services/support/communityterms/#certification'>no certification</a>, " +
        "<a class=footer href='http://www.alfresco.com/services/support/communityterms/#maintenance'>no maintenance</a>, " +
        "<a class=footer href='http://www.alfresco.com/services/support/communityterms/#warranty'>no warranty</a> and " +
        "<a class=footer href='http://www.alfresco.com/services/support/communityterms/#indemnity'>no indemnity</a> by " +
        "<a class=footer href='http://www.alfresco.com'>Alfresco</a> or its " +
        "<a class=footer href='http://www.alfresco.com/partners/'>Certified Partners</a>. " +
        "<a class=footer href='http://www.alfresco.com/services/support/'>Click here for support</a>. " +
        "Alfresco Software Inc. &copy; 2005-2007 All rights reserved.";
   
   private static Log logger = LogFactory.getLog(PageTag.class);
   private static String alfresco = null;
   private static String loginPage = null;
   
   private long startTime = 0;
   private String title;
   private String titleId;
   
   /**
    * @return The title for the page
    */
   public String getTitle()
   {
      return title;
   }

   /**
    * @param title Sets the page title
    */
   public void setTitle(String title)
   {
      this.title = title;
   }
   
   /**
    * @return The title message Id for the page
    */
   public String getTitleId()
   {
      return titleId;
   }

   /**
    * @param titleId Sets the page title message Id
    */
   public void setTitleId(String titleId)
   {
      this.titleId = titleId;
   }
   
   public void release()
   {
      super.release();
      title = null;
      titleId = null;
   }

   /**
    * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
    */
   public int doStartTag() throws JspException
   {
      if (logger.isDebugEnabled())
         startTime = System.currentTimeMillis();
      
      try
      {
         String reqPath = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
         Writer out = pageContext.getOut();
         
         if (!Application.inPortalServer())
         {
            out.write("<html><head><title>");
            if (this.titleId != null && this.titleId.length() != 0)
            {
               out.write(Application.getMessage(pageContext.getSession(), this.titleId));
            }
            else if (this.title != null && this.title.length() != 0)
            {
               out.write(this.title);
            }
            else
            {
               out.write("Alfresco Web Client");
            }
            out.write("</title>\n");
            out.write("<link rel=\"search\" type=\"application/opensearchdescription+xml\" href=\"" + reqPath + 
                      "/wcservice/api/search/keyword/description.xml\" title=\"Alfresco Keyword Search\">\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n");
         }
         
         // CSS style includes
         out.write(STYLES_START);
         out.write(reqPath);
         out.write(STYLES_MAIN);
         
         for (final String s : PageTag.SCRIPTS)
         {
            out.write(SCRIPTS_START);
            out.write(reqPath);
            out.write(s);
            out.write(SCRIPTS_END);
         }
         
         // set the context path used by some Alfresco script objects
         out.write("<script type=\"text/javascript\">");
         out.write("setContextPath('");
         out.write(reqPath);
         out.write("');</script>\n");

         if (!Application.inPortalServer())
         {
            out.write("</head>");
            out.write("<body>\n");
         }
      }
      catch (IOException ioe)
      {
         throw new JspException(ioe.toString());
      }
      
      return EVAL_BODY_INCLUDE;
   }

   /**
    * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
    */
   public int doEndTag() throws JspException
   {
      try
      {
         HttpServletRequest req = (HttpServletRequest)pageContext.getRequest();
         if (req.getRequestURI().endsWith(getLoginPage()) == false)
         {
            pageContext.getOut().write(getAlfrescoButton());
         }
         
         if (!Application.inPortalServer())
         {
            pageContext.getOut().write("\n</body></html>");
         }
      }
      catch (IOException ioe)
      {
         throw new JspException(ioe.toString());
      }
      
      if (logger.isDebugEnabled())
      {
         long endTime = System.currentTimeMillis();
         logger.debug("Time to generate page: " + (endTime - startTime) + "ms");
      }
      
      return super.doEndTag();
   }
   
   private String getLoginPage()
   {
      if (loginPage == null)
      {
         loginPage = Application.getLoginPage(pageContext.getServletContext());
      }
      
      return loginPage;
   }

/**
 * Please ensure you understand the terms of the license before changing the contents of this file.
 */

   private String getAlfrescoButton()
   {
      if (PageTag.alfresco == null)
      {
         final HttpServletRequest req = (HttpServletRequest)pageContext.getRequest();
         PageTag.alfresco = ("<center><table style='margin: 0px auto;'><tr><td>" +
                             "<a href='" + ALF_URL + "'>" +
                             "<img style='vertical-align:middle;border-width:0px;' alt='' title='" + ALF_TEXT + 
                             "' src='" + ("http".equals(req.getScheme()) ? ALF_LOGO_HTTP : ALF_LOGO_HTTPS) + 
                             "'>" +"</a></td><td align='center'>" +
                             "<span class='footer'>" + ALF_COPY +
                             "</span></td><td><a href='http://sourceforge.net/projects/alfresco'>" +
                             "<img style='vertical-align:middle;border-width:0px' alt='' title='SourceForge' src='" +
                             req.getContextPath() + SF_LOGO + "'></a>" +
                             "</td></tr></table></center>");
      }
      return PageTag.alfresco;
   }
}
