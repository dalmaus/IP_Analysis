package logic;

import java.util.ArrayList;

public class IpAddress{
	
	private ArrayList<Integer> octets;
	private int mask;
	
	public IpAddress(int oct1, int oct2, int oct3, int oct4, int mask) {
		
		octets = new ArrayList<Integer>();
		
		octets.add(oct1);
		octets.add(oct2);
		octets.add(oct3);
		octets.add(oct4);
		
		this.mask = mask;
		
	}
	
	public IpAddress(ArrayList<Integer> ip, int mask){
		
		if(ip != null && ip.size() == 4) {
		
			octets = ip;
		
		}else{
			
			octets = new ArrayList<Integer>();
			
			octets.add(192);
			octets.add(168);
			octets.add(0);
			octets.add(0);
			
		}
		
		this.mask = mask;
	}
	
	
	public ArrayList<Integer> getOctets(){
		
		return octets;
	}
	
	public int getMask() {
		
		return mask;
	}
	
	public void setMask(int mask) {
		
		this.mask = mask;
	}
	
	public int getOctet(int index) {
		
		return octets.get(index);
	}
	
	public int getMaxHosts() {
		
		return (int)(Math.pow(2, (IpMath.IP_LENGTH - mask))) - 2;
	}
	
	public String toString() {
		
		return octets.get(0) + "." + octets.get(1) + "." + octets.get(2) + "." + octets.get(3) + " /"+mask;
	}
	public String toText() {//Formato para JLabel de aplicación
		
		return octets.get(0) + "." + octets.get(1) + "." + octets.get(2) + "." + octets.get(3);
	}
}
