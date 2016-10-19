/*
 * Dog - Addons - UPnP Advertizer
 * 
 * Copyright (c) 2013-2016 Dario Bonino
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package org.doggateway.addons.upnp.advertizer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPIcon;
import org.osgi.service.upnp.UPnPService;

/**
 * This simple service implements a Basic UPnP device whose purpose is to
 * support automatic discovery of the Dog REST APIs through the device
 * presentation URL.
 * 
 * TODO: we need to evaluate if its is worth exposing APIs in this way or if it
 * is better to exposes the administration interface through the device
 * presentation URL and the REST APIs as single services offered by the gateway.
 * 
 * TODO: check whether is nice to export the subset of managed devices having a
 * UPnP specification as UPnP devices.
 * 
 * @author <a href="mailto:dario.bonino@gmail.com">Dario Bonino</a>
 *
 */
public class DogUPNPDevice implements UPnPDevice
{
	// endpoint addresses
	// TODO: get these from configuration of the REST API bundle
	private static String API_ENDPOINT = "/api/v1/";
	@SuppressWarnings("unused")
	private static String ADMIN_UI_ENDPOINT = "/admin/ui/index.html";

	// port
	// TODO: get this from the "org.osgi.service.http.port" property
	private static int PORT = 8080;

	// the Dictionary containing the properties used to describe the device in
	// UPnP
	private Dictionary<String, Object> descriptions;

	// the unique device udn, shall be consistent across different runs
	private String deviceId;

	// the url of the Dog REST APIs
	private String dogURL;

	// the OSGi service registration object needed to handle the service life
	// cycle
	private ServiceRegistration<UPnPDevice> upnpReg;

	/**
	 * @throws UnknownHostException
	 * 
	 */
	public DogUPNPDevice()
	{
		// the device udn, shall be consisten across runs
		// TODO: make this consistent
		this.deviceId = UUID.randomUUID().toString();

		// try to get the address of the machine hosting Dog and use it to
		// compose the API address
		// please notice that the API port is currently hard coded, shall be
		// derived from the "org.osgi.service.http.port" property
		try
		{
			this.dogURL = "http://" + InetAddress.getLocalHost().getHostAddress()
					+ ":" + DogUPNPDevice.PORT + DogUPNPDevice.API_ENDPOINT;
		}
		catch (UnknownHostException e)
		{
			// revert to the loopback address (will not be usable)
			this.dogURL = InetAddress.getLoopbackAddress().getHostName();
		}

		// Prepare the descriptions dictionary
		this.descriptions = new Hashtable<String, Object>();

		// flag the service to be exported as UPnP device
		this.descriptions.put(UPnPDevice.UPNP_EXPORT, "");

		// set the device category to match the UPnP device category as per the
		// OSGi specification
		this.descriptions.put(org.osgi.service.device.Constants.DEVICE_CATEGORY,
				new String[] { UPnPDevice.DEVICE_CATEGORY });

		// add the friendly name of this device
		this.descriptions.put(UPnPDevice.FRIENDLY_NAME,
				"The Dog Gateway UPNP endpoint");
		// set the manufacturer
		this.descriptions.put(UPnPDevice.MANUFACTURER, "dog-gateway");

		// set the manufacturer url, the dog github site in our case
		this.descriptions.put(UPnPDevice.MANUFACTURER_URL,
				"http://dog-gateway.github.io");
		// set the description of the device
		this.descriptions.put(UPnPDevice.MODEL_DESCRIPTION, "The Dog gateway");

		// set the model name
		this.descriptions.put(UPnPDevice.MODEL_NAME, "Balto");

		// set the model number
		this.descriptions.put(UPnPDevice.MODEL_NUMBER, "3.0");

		// set the url pointing at the given dog model (currently the dog
		// github)
		// TODO: start assigning names to the Dog releases
		this.descriptions.put(UPnPDevice.MODEL_URL,
				"http://dog-gateway.github.io");
		// the gateway serial number, also this information shall be generated
		// to be consistent across runs, or delegated to configuration
		this.descriptions.put(UPnPDevice.SERIAL_NUMBER, "1000001");

		// set the type of device at basic, i.e., no services
		this.descriptions.put(UPnPDevice.TYPE,
				"urn:schemas-upnp-org:device:Basic:1.0");
		// set the device UDN
		this.descriptions.put(UPnPDevice.UDN, this.deviceId);
		// set the device UPC
		// TODO: see how to correctly generate this
		this.descriptions.put(UPnPDevice.UPC, "012345678905");
		// set the gateway presentation url
		this.descriptions.put(UPnPDevice.PRESENTATION_URL, this.dogURL);
	}

	@Override
	public UPnPService getService(String serviceId)
	{
		// returns the main service of the device or null if no service is
		// provided
		return null;
	}

	@Override
	public UPnPService[] getServices()
	{
		// returns all services offered by this device
		return null;
	}

	@Override
	public UPnPIcon[] getIcons(String locale)
	{
		// returns the icon representing this device or null, if not defined
		return null;
	}

	@Override
	public Dictionary<String, Object> getDescriptions(String locale)
	{
		// return the descriptions of this device characteristics localized
		// according to the given locale.
		return descriptions;
	}

	@SuppressWarnings("unchecked")
	protected void activate(BundleContext ctx)
	{
		//register the UPnP device service on activation
		this.upnpReg = (ServiceRegistration<UPnPDevice>) ctx.registerService(
				UPnPDevice.class.getName(), this, this.descriptions);
	}

	protected void deactivate()
	{
		//de-register the UPnP device service on de-activation
		if (this.upnpReg != null)
			this.upnpReg.unregister();
	}
}
