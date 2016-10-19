# org.doggateway.addons.upnp.advertizer
A very simple UPnP advertizer for the Dog 

It describes, and advertizes, a Dog gateway as a UPnP Basic device (no services) and exploits the presentation URL field to let applications discover the Dog REST APIs endpoint through UPnP

The bundle depends on a UPnP base driver implementation. It has been tested by exploiting the [Cling OSGi base driver] (http://4thline.org/m2/org/fourthline/cling/cling-osgi-basedriver/2.0-alpha3/cling-osgi-basedriver-2.0-alpha3.jar), also available from (https://github.com/jomarmar/cling-osgi). As the bundle is implemented on the basis of the OSGi specification only, it should work with any other implementation of the OSGi UPnP base driver.
