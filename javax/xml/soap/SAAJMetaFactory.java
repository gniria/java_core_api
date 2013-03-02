/*
 * $Id: SAAJMetaFactory.java,v 1.4 2006/03/30 00:59:39 ofung Exp $
 * $Revision: 1.4 $
 * $Date: 2006/03/30 00:59:39 $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at https://glassfish.dev.java.net/public/CDDLv1.0.html.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */
package javax.xml.soap;

/**
* The access point for the implementation classes of the factories defined in the
* SAAJ API. All of the <code>newInstance</code> methods defined on factories in 
* SAAJ 1.3 defer to instances of this class to do the actual object creation. 
* The implementations of <code>newInstance()</code> methods (in SOAPFactory and MessageFactory)
* that existed in SAAJ 1.2 have been updated to also delegate to the SAAJMetaFactory when the SAAJ 1.2
* defined lookup fails to locate the Factory implementation class name. 
* 
* <p>
* SAAJMetaFactory is a service provider interface. There are no public methods on this
* class.
*
* @author SAAJ RI Development Team
* @since SAAJ 1.3
*/

public abstract class SAAJMetaFactory {
    static private final String META_FACTORY_CLASS_PROPERTY =
        "javax.xml.soap.MetaFactory";
    static private final String DEFAULT_META_FACTORY_CLASS =
        "com.sun.xml.internal.messaging.saaj.soap.SAAJMetaFactoryImpl";

    /**
     * Creates a new instance of a concrete <code>SAAJMetaFactory</code> object. 
     * The SAAJMetaFactory is an SPI, it pulls the creation of the other factories together into a 
     * single place. Changing out the SAAJMetaFactory has the effect of changing out the entire SAAJ 
     * implementation. Service providers provide the name of their <code>SAAJMetaFactory</code>
     * implementation. 
     *
     * This method uses the following ordered lookup procedure to determine the SAAJMetaFactory implementation class to load:
     * <UL>
     *  <LI> Use the javax.xml.soap.MetaFactory system property.
     *  <LI> Use the properties file "lib/jaxm.properties" in the JRE directory. This configuration file is in standard 
     * java.util.Properties format and contains the fully qualified name of the implementation class with the key being the 
     * system property defined above. 
     *  <LI> Use the Services API (as detailed in the JAR specification), if available, to determine the classname. The Services API 
     * will look for a classname in the file META-INF/services/javax.xml.soap.MetaFactory in jars available to the runtime.
     *  <LI> Default to com.sun.xml.internal.messaging.saaj.soap.SAAJMetaFactoryImpl.
     * </UL>
     *
     * @return a concrete <code>SAAJMetaFactory</code> object
     * @exception SOAPException if there is an error in creating the <code>SAAJMetaFactory</code>
     */
    static SAAJMetaFactory getInstance() throws SOAPException {
            try {
                SAAJMetaFactory instance =
                    (SAAJMetaFactory) FactoryFinder.find(
                        META_FACTORY_CLASS_PROPERTY,
                        DEFAULT_META_FACTORY_CLASS);
                return instance;
            } catch (Exception e) {
                throw new SOAPException(
                    "Unable to create SAAJ meta-factory" + e.getMessage());
            }
    }

    protected SAAJMetaFactory() { }

     /**
      * Creates a <code>MessageFactory</code> object for 
      * the given <code>String</code> protocol. 
      *
      * @param protocol a <code>String</code> indicating the protocol
      * @exception SOAPException if there is an error in creating the
      *            MessageFactory
      * @see SOAPConstants#SOAP_1_1_PROTOCOL
      * @see SOAPConstants#SOAP_1_2_PROTOCOL
      * @see SOAPConstants#DYNAMIC_SOAP_PROTOCOL
      */
    protected abstract MessageFactory newMessageFactory(String protocol)
        throws SOAPException;

     /**
      * Creates a <code>SOAPFactory</code> object for 
      * the given <code>String</code> protocol. 
      *
      * @param protocol a <code>String</code> indicating the protocol
      * @exception SOAPException if there is an error in creating the
      *            SOAPFactory
      * @see SOAPConstants#SOAP_1_1_PROTOCOL
      * @see SOAPConstants#SOAP_1_2_PROTOCOL
      * @see SOAPConstants#DYNAMIC_SOAP_PROTOCOL
      */
    protected abstract SOAPFactory newSOAPFactory(String protocol)
        throws SOAPException;
}