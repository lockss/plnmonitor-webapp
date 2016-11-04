package be.ulb.plnmonitor;

/*
 * ConfigurationManager.java
 *
 * Version: $Revision: 1689 $
 *
 * Date: $Date: 2006-11-24 04:56:53 +0000 (Fri, 24 Nov 2006) $
 *
 * Copyright (c) 2002-2005, Hewlett-Packard Company and Massachusetts
 * Institute of Technology.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of the Hewlett-Packard Company nor the name of the
 * Massachusetts Institute of Technology nor the names of their
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

import be.ulb.plnmonitor.datacontroller.Utils;

/**
 * Class for reading the DSpace system configuration. The main configuration is
 * read in as properties from a standard properties file. Email templates and
 * configuration files for other tools are also be accessed via this class.
 * <P>
 * The main configuration is by default read from the <em>resource</em>
 * <code>/dspace.cfg</code>.
 * To specify a different configuration, the system property
 * <code>dspace.configuration</code> should be set to the <em>filename</em>
 * of the configuration file.
 * <P>
 * Other configuration files are read from the <code>config</code> directory
 * of the DSpace installation directory (specified as the property
 * <code>dspace.dir</code> in the main configuration file.)
 * <P>
 * Configuration files for other tools are kept in <code>config/templates</code>
 * and can contain placeholders for configuration values from
 * <code>dspace.cfg</code>. See <code>installConfigurations</code> for
 * details.
 * 
 * @author Robert Tansley
 * @author Larry Stone - Interpolated values.
 * @version $Revision: 1689 $
 */
public class ConfigurationManager
{
    /** log4j category */
    private static Logger log = null;

    /** The configuration properties */
    private static Properties properties = null;

    /** The default license */
    private static String license;

    // limit of recursive depth of property variable interpolation in
    // configuration; anything greater than this is very likely to be a loop.
    private final static int RECURSION_LIMIT = 9;

    
    /**
     * Get a configuration property
     * 
     * @param property
     *            the name of the property
     * 
     * @return the value of the property, or <code>null</code> if the property
     *         does not exist.
     */
    public static String getProperty(String property)
    {
        if (properties == null)
        {
            loadConfig(null);
        }

        return properties.getProperty(property);
    }

    /**
     * Get a configuration property as an integer
     * 
     * @param property
     *            the name of the property
     * 
     * @return the value of the property. <code>0</code> is returned if the
     *         property does not exist. To differentiate between this case and
     *         when the property actually is zero, use <code>getProperty</code>.
     */
    public static int getIntProperty(String property)
    {
        if (properties == null)
        {
            loadConfig(null);
        }

        String stringValue = properties.getProperty(property);
        int intValue = 0;

        if (stringValue != null)
        {
            try
            {
                intValue = Integer.parseInt(stringValue.trim());
            }
            catch (NumberFormatException e)
            {
                warn("Warning: Number format error in property: " + property);
            }
        }

        return intValue;
    }

    /**
     * Get a configuration property as a boolean. True is indicated if the value
     * of the property is <code>TRUE</code> or <code>YES</code> (case
     * insensitive.)
     * 
     * @param property
     *            the name of the property
     * 
     * @return the value of the property. <code>false</code> is returned if
     *         the property does not exist. To differentiate between this case
     *         and when the property actually is false, use
     *         <code>getProperty</code>.
     */
    public static boolean getBooleanProperty(String property)
    {
        return getBooleanProperty(property, false);
    }

    /**
     * Get a configuration property as a boolean, with default.
     * True is indicated if the value
     * of the property is <code>TRUE</code> or <code>YES</code> (case
     * insensitive.)
     *
     * @param property
     *            the name of the property
     *
     * @param defaultValue
     *            value to return if property is not found.
     *
     * @return the value of the property. <code>default</code> is returned if
     *         the property does not exist. To differentiate between this case
     *         and when the property actually is false, use
     *         <code>getProperty</code>.
     */
    public static boolean getBooleanProperty(String property, boolean defaultValue)
    {
        if (properties == null)
        {
            loadConfig(null);
        }

        String stringValue = properties.getProperty(property);

        if (stringValue != null)
        {
        	stringValue = stringValue.trim();
            return  stringValue.equalsIgnoreCase("true") ||
                    stringValue.equalsIgnoreCase("yes");
        }
        else
        {
            return defaultValue;
        }
    }

    /**
     * Returns an enumeration of all the keys in the DSpace configuration
     * 
     * @return an enumeration of all the keys in the DSpace configuration
     */
    public static Enumeration propertyNames()
    {
        if (properties == null)
            loadConfig(null);

        return properties.propertyNames();
    }


    /**
     * Get the site-wide default license that submitters need to grant
     * 
     * @return the default license
     */
    public static String getDefaultSubmissionLicense()
    {
        if (properties == null)
        {
            loadConfig(null);
        }

        return license;
    }

    /**
     * Get the path for the news files.
     * 
     */
    public static String getNewsFilePath()
    {
        String filePath = ConfigurationManager.getProperty("acoue.dir")
                + File.separator + "config" + File.separator;

        return filePath;
    }


    /**
     * Writes license to a text file.
     * 
     * @param news
     *            the text to be written to the file.
     */
    public static void writeLicenseFile(String newLicense)
    {
        String licenseFile = getProperty("acoue.dir") + File.separator
                             + "config" + File.separator + "default.license";

        try
        {
            // write the news out to the appropriate file
            FileOutputStream fos = new FileOutputStream(licenseFile);
            OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
            PrintWriter out = new PrintWriter(osr);
            out.print(newLicense);
            out.close();
        }
        catch (IOException e)
        {
            warn("license_write: " + e.getLocalizedMessage());
        }

        license = newLicense;
     }

    private static File loadedFile = null;

    /**
     * Return the file that configuration was actually loaded from. Only returns
     * a valid File after configuration has been loaded.
     * 
     * @return File naming configuration data file, or null if not loaded yet.
     */
    public static File getConfigurationFile()
    {
        // in case it hasn't been done yet.
        loadConfig(null);

        return loadedFile;
    }

    /**
     * Load the DSpace configuration properties. Only does anything if
     * properties are not already loaded. Properties are loaded in from the
     * specified file, or default locations.
     * 
     * @param configFile
     *            The <code>dspace.cfg</code> configuration file to use, or
     *            <code>null</code> to try default locations
     */
    public static void loadConfig(String configFile)
    {
        InputStream is;

        if (properties != null)
        {
            return;
        }
        try
        {
            if (configFile != null)
            {   
                is = new FileInputStream(configFile);
                loadedFile = new File(configFile);
            }
            // Has the default configuration location been overridden?
            else
            {
                // Load configuration from default location
                is = new FileInputStream(Utils.CONFIG_PATH);
                loadedFile = new File(Utils.CONFIG_PATH);
            }

            if (is == null)
            {
                fatal("Cannot find acoue.cfg");
                throw new RuntimeException("Cannot find acoue.cfg");
            }
            else
            {
                properties = new Properties();
                properties.load(is);

                // walk values, interpolating any embedded references.
                for (Enumeration pe = properties.propertyNames(); pe.hasMoreElements(); )
                {
                    String key = (String)pe.nextElement();
                    String value = interpolate(key, 1);
                    if (value != null)
                        properties.setProperty(key, value);
                }
            }

            is.close();
        }
        catch (IOException e)
        {
            fatal("Can't load configuration", e);

            // FIXME: Maybe something more graceful here, but with the
            // configuration we can't do anything
            throw new RuntimeException("Cannot find "+Utils.CONFIG_PATH,e);
        }
    }

    /**
     * Recursively interpolate variable references in value of
     * property named "key".
     * @return new value if it contains interpolations, or null
     *   if it had no variable references.
     */
    private static String interpolate(String key, int level)
    {
        if (level > RECURSION_LIMIT)
            throw new IllegalArgumentException("ConfigurationManager: Too many levels of recursion in configuration property variable interpolation, property="+key);
        String value = (String)properties.get(key);
        int from = 0;
        StringBuffer result = null;
        while (from < value.length())
        {
            int start = value.indexOf("${", from);
            if (start >= 0)
            {
                int end = value.indexOf("}", start);
                if (end < 0)
                    break;
                String var = value.substring(start+2, end);
                if (result == null)
                    result = new StringBuffer(value.substring(from, start));
                else
                    result.append(value.substring(from, start));
                if (properties.containsKey(var))
                {
                    String ivalue = interpolate(var, level+1);
                    if (ivalue != null)
                    {
                        result.append(ivalue);
                        properties.setProperty(var, ivalue);
                    }
                    else
                        result.append((String)properties.getProperty(var));
                }
                else
                {
                    log.warn("Interpolation failed in value of property \""+key+
                             "\", there is no property named \""+var+"\"");
                }
                from = end+1;
            }
            else
                break;
        }
        if (result != null && from < value.length())
            result.append(value.substring(from));
        return (result == null) ? null : result.toString();
    }

    /**
     * Fill out of the configuration file templates in
     * <code>dspace.dir/config/templates</code> with appropriate values from
     * <code>dspace.cfg</code>, and copy to their appropriate destination.
     * The destinations are defined as properties in <code>dspace.cfg</code>
     * called <code>config.template.XXX</code> where <code>XXX</code> is the
     * filename of the template. If this property doesn't exist, the
     * configuration file template is skipped.
     * 
     * @throws IOException
     *             if there was some problem reading the templates or writing
     *             the filled-out configuration files
     */
    public static void installConfigurations() throws IOException
    {
        // Get the templates
        File templateDir = new File(getProperty("dspace.dir") + File.separator
                + "config" + File.separator + "templates");

        File[] templateFiles = templateDir.listFiles();

        for (int i = 0; i < templateFiles.length; i++)
        {
            installConfigurationFile(templateFiles[i].getName());
        }
    }

    /**
     * Install the given configuration file template in its required location.
     * Configuration values in the template, specified as
     * <code>@@property.name@@</code> are filled out with appropriate properties from
     *                   the configuration. The filled-out configuration file is
     *                   written to the file named by the property
     *                   <code>config.template.XXX</code> where
     *                   <code>XXX</code> is the name of the template as
     *                   passed in to this method.
     * 
     * @param template
     *            the name of the configuration template. This must correspond
     *            to the filename in <code>dspace.dir/config/templates</code>
     *            and the property starting with <code>config.template.</code>.
     * 
     * @throws IOException
     *             if there was some problem reading the template or writing the
     *             filled-out configuration file
     */
    private static void installConfigurationFile(String template)
            throws IOException
    {
        // Get the destination: specified in property config.template.XXX
        String destination = getProperty("config.template." + template);

        if (destination == null)
        {
            // If no destination is specified
            info("Not processing config file template " + template
                    + " because no destination specified (no property "
                    + "config.template." + template + ")");

            return;
        }

        info("Installing configuration file template " + template + " to "
                + destination);

        // Open the template
        BufferedReader in = new BufferedReader(new FileReader(
                getProperty("dspace.dir") + File.separator + "config"
                        + File.separator + "templates" + File.separator
                        + template));

        // Open the destination for writing
        PrintWriter out = new PrintWriter(new FileWriter(destination));

        // We'll keep track of line numbers for error messages etc.
        int lineNumber = 0;
        String line;

        // Copy the template, filling out config values
        while ((line = in.readLine()) != null)
        {
            lineNumber++;

            // Find configuration values
            boolean moreValues = true;

            while (moreValues)
            {
                // Look for "@@"
                int first = line.indexOf("@@");

                if (first > -1)
                {
                    // Look for the "@@" on the other side
                    int second = line.indexOf("@@", first + 2);

                    if (second > -1)
                    {
                        // We have a property
                        String propName = line.substring(first + 2, second);

                        String propValue = getProperty(propName);

                        if (propValue == null)
                        {
                            warn(template + " line " + lineNumber
                                    + ": Property " + propName
                                    + " not defined in DSpace configuration - "
                                    + "using empty string");

                            propValue = "";
                        }

                        // Fill in the value
                        line = line.substring(0, first) + propValue
                                + line.substring(second + 2);
                    }
                    else
                    {
                        // There's a "@@" with no second one... just leave as-is
                        warn(template + " line " + lineNumber
                                + ": Single @@ - leaving as-is");
                        moreValues = false;
                    }
                }
                else
                {
                    // No more @@'s
                    moreValues = false;
                }
            }

            // Write the line
            out.println(line);
        }

        in.close();
        out.close();
    }

    /**
     * Command-line interface for running configuration tasks. Possible
     * arguments:
     * <ul>
     * <li><code>-installTemplates</code> processes and installs the
     * configuration file templates for other tools</li>
     * <li><code>-property name</code> prints the value of the property
     * <code>name</code> from <code>dspace.cfg</code> to the standard
     * output. If the property does not exist, nothing is written.</li>
     * </ul>
     * 
     * @param argv
     *            command-line arguments
     */
    public static void main(String[] argv)
    {
        if ((argv.length == 1) && argv[0].equals("-installTemplates"))
        {
            try
            {
                info("Installing configuration files for other tools");
                installConfigurations();
                System.exit(0);
            }
            catch (IOException ie)
            {
                warn("Error installing configuration files", ie);
            }
        }
        else if ((argv.length == 2) && argv[0].equals("-property"))
        {
            String val = getProperty(argv[1]);

            if (val != null)
            {
                System.out.println(val);
            }
            else
            {
                System.out.println("");
            }

            System.exit(0);
        }
        else
        {
            System.err
                    .println("Usage: ConfigurationManager OPTION\n  -installTemplates    install config files for external tools\n  -property prop.name  get value of prop.name from dspace.cfg");
        }

        System.exit(1);
    }

    private static void info(String string)
    {
        if (log == null)
        {
            System.out.println("INFO: " + string);
        }
        else
        {
            log.info(string);
        }
    }

    private static void warn(String string, Exception e)
    {
        if (log == null)
        {
            System.out.println("WARN: " + string);
            e.printStackTrace();
        }
        else
        {
            log.warn(string, e);
        }
    }

    private static void warn(String string)
    {
        if (log == null)
        {
            System.out.println("WARN: " + string);
        }
        else
        {
            log.warn(string);
        }
    }

    private static void error(String string)
    {
        if (log == null)
        {
            System.err.println("ERROR: " + string);
        }
        else
        {
            log.error(string);
        }
    }

    private static void fatal(String string, Exception e)
    {
        if (log == null)
        {
            System.out.println("FATAL: " + string);
            e.printStackTrace();
        }
        else
        {
            log.fatal(string, e);
        }
    }

    private static void fatal(String string)
    {
        if (log == null)
        {
            System.out.println("FATAL: " + string);
        }
        else
        {
            log.fatal(string);
        }
    }

    private static void initLog()
    {
        log = Logger.getLogger(ConfigurationManager.class);
    }

}
