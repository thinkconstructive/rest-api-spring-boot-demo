package com.thinkconstructive.restdemo.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Controller
public class DummyServiceHealthCheck implements HealthIndicator {

    @Autowired
    private Environment env;

    @Override
    public Health health()
    {
        try {
            if (isServiceUp()) {
                return Health.up().withDetail("Dummy Service", "is working good").build();
            } else {
                return Health.down().withDetail("Dummy Service", "is DOWN").build();
            }
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
        }
        return null;
    }

    private boolean isServiceUp() throws IOException {
        String address = env.getProperty("dummyService.address");
        String port = env.getProperty("dummyService.port");

        return isAddressReachable(address, Integer.parseInt(port), 3000);
    }

    private static boolean isAddressReachable(String address, int port, int timeout)
            throws IOException {
        Socket isSocket = new Socket();
        try {
            // Connects this socket to the server with a specified timeout value.
            isSocket.connect(new InetSocketAddress(address, port), timeout);
            // Return true if connection successful
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            // Return false if connection fails
            return false;
        } finally {
            isSocket.close();
        }
    }
}
