package com.xnx3;

import java.util.List;

public class LotteryDataEntry
{
  public String Code;
  public Codes Codes;
  public String Msg;
  
  public String getStrFromArray(int paramInt)
  {
    paramInt -= 1;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append((String)this.Codes.Code1.get(paramInt));
    localStringBuilder.append((String)this.Codes.Code2.get(paramInt));
    localStringBuilder.append((String)this.Codes.Code3.get(paramInt));
    localStringBuilder.append((String)this.Codes.Code4.get(paramInt));
    localStringBuilder.append((String)this.Codes.Code5.get(paramInt));
    localStringBuilder.append((String)this.Codes.Code6.get(paramInt));
    localStringBuilder.append((String)this.Codes.Code7.get(paramInt));
    localStringBuilder.append((String)this.Codes.Code8.get(paramInt));
    return localStringBuilder.toString();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("LotteryDataEntry{Code='");
    localStringBuilder.append(this.Code);
    localStringBuilder.append('\'');
    localStringBuilder.append(", Msg='");
    localStringBuilder.append(this.Msg);
    localStringBuilder.append('\'');
    localStringBuilder.append(", Codes=");
    localStringBuilder.append(this.Codes);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public class Codes
  {
    public List<String> Code1;
    public List<String> Code2;
    public List<String> Code3;
    public List<String> Code4;
    public List<String> Code5;
    public List<String> Code6;
    public List<String> Code7;
    public List<String> Code8;
    
    public Codes() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Codes{Code1=");
      localStringBuilder.append(this.Code1);
      localStringBuilder.append(", Code2=");
      localStringBuilder.append(this.Code2);
      localStringBuilder.append(", Code3=");
      localStringBuilder.append(this.Code3);
      localStringBuilder.append(", Code4=");
      localStringBuilder.append(this.Code4);
      localStringBuilder.append(", Code5=");
      localStringBuilder.append(this.Code5);
      localStringBuilder.append(", Code6=");
      localStringBuilder.append(this.Code6);
      localStringBuilder.append(", Code7=");
      localStringBuilder.append(this.Code7);
      localStringBuilder.append(", Code8=");
      localStringBuilder.append(this.Code8);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
}
