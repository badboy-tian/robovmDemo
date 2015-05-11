package net.mwplay.utils;

public interface HttpProtocol{
	public void onReceivedResults(String result);
	public void onFaile(String msg);
}