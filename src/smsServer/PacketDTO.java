package smsServer;

import java.net.InetAddress;

public class PacketDTO {
	InetAddress srcAddress;
	int srcPort;
	int length;
	int offset;
	long lastRegistrationTime;
}
