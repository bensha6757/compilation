/***********/
/* PACKAGE */
/***********/
package TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import IR.Register_Allocation;

/*******************/

public class TEMP
{
	private int serial=0;
	
	public TEMP(int serial)
	{
		this.serial = serial;
	}
	
	public int getSerialNumber()
	{
		return serial;
	}

    public int getRealSerialNumber() {
        //return getSerialNumber();
        return Register_Allocation.getPhysicalRegisterSerialNumber(this.getSerialNumber());
    }

}
