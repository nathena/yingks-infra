package com.yingks.infra.utils;


public final class ArrayUtil  {

  public byte[] append(byte[] oldData, byte[] newData) 
  {
    return append(oldData, newData, 0, newData.length);
  }

  public byte[] append(byte[] oldData, byte[] newData, int offset, int length) 
  {
    if(newData.length < (offset + length)) return oldData;
    int totalSize = oldData.length + length;
    if(totalSize<1) return oldData;

    byte[] ret = new byte[totalSize];
    int idx=0;
    for(int ii=0; ii<oldData.length; ii++, idx++) {
      ret[idx] = oldData[ii];
    }

    for(int ii=0; ii<length; ii++, idx++) {
      ret[idx] = newData[ii + offset];
    }
    oldData = null;
    newData = null;
    return ret;
  }
  
  public static boolean isEmpty(Object[] arrays)
  {
	  if( null == arrays || arrays.length == 0 )
			return false;
	  
	  return true;
  }
}
