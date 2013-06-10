package za.co.johanmynhardt.remotestorage.impl;

import za.co.johanmynhardt.remotestorage.service.HelloService;

/**
 * @author Johan Mynhardt
 */
public class HelloServiceImpl implements HelloService {
	public String getHello(String name) {
		return "Hello " + name;
	}
}
