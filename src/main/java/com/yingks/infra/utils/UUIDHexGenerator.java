package com.yingks.infra.utils;

import com.alibaba.fastjson.JSONObject;

import java.net.InetAddress;
import java.util.Arrays;

public class UUIDHexGenerator
{

    private String sep = "";
    
    private static final int IP;
    
    static {
        int ipadd;
        try {
            ipadd = BytesHelper.toInt( InetAddress.getLocalHost().getAddress() );
        }
        catch (Exception e) {
            ipadd = 0;
        }
        IP = ipadd;
    }
    
    private static short counter = (short) 0;
    private static final int JVM = (int) ( System.currentTimeMillis() >>> 8 );

    public UUIDHexGenerator() {
    }
    
    public static String generator()
    {
    	return new UUIDHexGenerator().generateUUID();
    }
    
    /**
     * Unique across JVMs on this machine (unless they load this class
     * in the same quater second - very unlikely)
     */
    protected int getJVM() {
        return JVM;
    }

    /**
     * Unique in a millisecond for this JVM instance (unless there
     * are > Short.MAX_VALUE instances created in a millisecond)
     */
    protected short getCount() {
        synchronized(UUIDHexGenerator.class) {
            if (counter<0) counter=0;
            return counter++;
        }
    }

    /**
     * Unique in a local network
     */
    protected int getIP() {
        return IP;
    }

    /**
     * Unique down to millisecond
     */
    protected short getHiTime() {
        return (short) ( System.currentTimeMillis() >>> 32 );
    }
    protected int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    protected String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace( 8-formatted.length(), 8, formatted );
        return buf.toString();
    }

    protected String format(short shortval) {
        String formatted = Integer.toHexString(shortval);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace( 4-formatted.length(), 4, formatted );
        return buf.toString();
    }

    public String generateUUID() {
        return new StringBuffer(36)
            .append( format( getIP() ) ).append(sep)
            .append( format( getJVM() ) ).append(sep)
            .append( format( getHiTime() ) ).append(sep)
            .append( format( getLoTime() ) ).append(sep)
            .append( format( getCount() ) )
            .toString();
    }
    
    public static void main( String[] args ) throws Exception {
        
        UUIDHexGenerator gen = new UUIDHexGenerator();
        System.out.println(gen.generateUUID());

        System.out.println(JSONObject.toJSONString(Arrays.asList("312", "ase")));
    }
}
