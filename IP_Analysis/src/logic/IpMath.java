package logic;

import java.util.ArrayList;

public class IpMath {
	
	private static final int LAST_COMBINATION = 255;
	public static final int IP_LENGTH = 32;
	private static final int OCTET_LENGTH = 8;
	private static final int NUMBER_OF_IP_OCTETS = 4;
	private static final int BYTE_LENGTH = 8;
	
	public static final int NETWORK_ADDRESS = 1;
	public static final int BROADCAST_ADDRESS = 2;
	public static final int FIRST_ASSIGNABLE_ADDRESS = 3;
	public static final int LAST_ASSIGNABLE_ADDRESS = 4;
	
	public static ArrayList<Integer> convertRawIp(int rawIp) {
		
		ArrayList<Integer> ip = new ArrayList<Integer>();
		
		int k = IP_LENGTH - 1; //para coger la posición del integer de 32 bits
		
		for(int i = 0; i < NUMBER_OF_IP_OCTETS; i++) {
			
			int octet = 0;
			
			for(int j = BYTE_LENGTH - 1; j >= 0; j--) {
			
				if(((rawIp >>> k) & 1) != 0){//Si en la posición j en el número n no hay un 0... true
				
					octet += 1 << j;
				}
				
				k--;
			}
			
			ip.add(octet);
		}
		
		return ip;
	}
	
	public static int getRawIp(IpAddress ip) { //Devuelve la ip transformada en un entero de 32 bits para una más fácil manipulación.
		
		int rawIp = 0;
		int k = IP_LENGTH - 1; //la longitud de la ip ajustada
		
		for(int i = 0; i < NUMBER_OF_IP_OCTETS; i++) {
			
			int n = ip.getOctet(i);
			
			for(int j = BYTE_LENGTH - 1; j >= 0; j--) {
			
				if(((n >>> j) & 1) != 0){//Si en la posición j en el número n no hay un 0... true
				
					rawIp += 1 << k;
				}
				
				k--;
			}
		}
		
		return rawIp;
	}
	
	public static int getRawMask(IpAddress ip) {
		
		int simplifiedMask = ip.getMask();
		int pos = IP_LENGTH - 1; 
		int rawMask = 0;
		
		for(int i = simplifiedMask; i > 0; i--) {
			
			rawMask += 1 << pos--;
		}
		
		return rawMask;
	}
	
	
	public static int getReversedRawMask(IpAddress ip) { //Devuelve la máscara invertida (con los unos al final). Útil para averiguar broadcast.
														 //Este método también se puede utilizar de algún modo para averiguar la cantidad de hosts.
		int rawMask = IpMath.getRawMask(ip);
		
		return ~rawMask;
	}
	
	public static int findMask(int hostNumber) { //Returns the smallest mask capable of hosting the requested number of hosts
		
		int multiples = 0;
		int hostBits = 0;
		
		while(multiples < hostNumber) {
			
			multiples = (int)Math.pow(2, ++hostBits) - 2;
			
		}
		
		return IP_LENGTH - hostBits;
	}
	
	public static int getMaxHosts(IpAddress ip) { //Returns the maximum assignables ips //Todos estos métodos se pueden meter en un solo método con un switch
		
		return (int)(Math.pow(2, (IpMath.IP_LENGTH - ip.getMask()))) - 2; //2^bits de hosts - 2 -> para averiguar el número de hosts.
	}
	
	public static int getRawBroadcast(IpAddress ip) {
		
		int rawAddress = IpMath.getRawAddress(ip);
		int reversedRawMask = IpMath.getReversedRawMask(ip);
		
		return rawAddress + reversedRawMask;
	}
	
	public static IpAddress getFirstAssignableAddress(IpAddress ip) {
		
		int rawAddress = IpMath.getRawAddress(ip);
		
		return new IpAddress(IpMath.convertRawIp(rawAddress + 1), ip.getMask());
	}
	
	public static IpAddress getBroadcast(IpAddress ip) {
		
		int rawBroadcast = IpMath.getRawBroadcast(ip);
		
		return new IpAddress(IpMath.convertRawIp(rawBroadcast), ip.getMask());
	}
	
	public static IpAddress getLastAssignableAddress(IpAddress ip) {
		
		int rawBroadcast = IpMath.getRawBroadcast(ip);
		
		return new IpAddress(IpMath.convertRawIp(rawBroadcast - 1), ip.getMask());
	}
	
	public static IpAddress getNetworkAddress(IpAddress ip) {
		
		int rawAddress = IpMath.getRawAddress(ip);
		
		return new IpAddress(IpMath.convertRawIp(rawAddress), ip.getMask());
	}
	
	public static int getRawAddress(IpAddress ip) { //Returns the network address in a 32 bit integer
		
		int rawIp = IpMath.getRawIp(ip);
		int rawMask = IpMath.getRawMask(ip);
		
		return rawMask & rawIp;
		
	}
	public static int getRawFirstAssignableAddress(IpAddress ip) {
		
		return IpMath.getRawAddress(ip) + 1;
		
	}
	
	public static int getRawLastAssignableAddress(IpAddress ip) {
		
		return IpMath.getRawBroadcast(ip) - 1;
		
	}
	
	public static IpAddress getAddress(IpAddress ip, int option) {
		
		int rawAddress = 0;
		
		switch(option) {
			
			case 1:
				
				rawAddress = IpMath.getRawAddress(ip);
				
				break;
				
			case 2:
				
				rawAddress = IpMath.getRawBroadcast(ip);
				
				break;
				
			case 3:
				
				rawAddress = IpMath.getRawFirstAssignableAddress(ip);
				
				break;
				
			case 4:
				
				rawAddress = IpMath.getRawLastAssignableAddress(ip);
				
				break;
		}
		
		return new IpAddress(IpMath.convertRawIp(rawAddress), ip.getMask());
	}
	
}
