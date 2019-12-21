/*---------------------------------------------------------------------------
* Copyright (c) 2006 苏洋
*----------------------------------------------------------------------------
* RELEASE:
* REVISION:     1.0
*----------------------------------------------------------------------------
* PURPOSE:

*  IntelHexFmt.cpp: implementation of the IntelHexFmt class.
*
*
* NOTE: 请保存文件的完整性。
*
*
------------------------------------------------------------------------------*/
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "chb.h"
#include "IntelHexFmt.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

IntelHexFmt::IntelHexFmt()
{

}

IntelHexFmt::~IntelHexFmt()
{

}
IntelHexFmt::IntelHexFmt(std::string s)
{
	buf=s;
}

char IntelHexFmt::GetSig(void)
{
	return buf.at(0);
}

short IntelHexFmt::GetDataLen(void)
{
	std::string tmp=buf.substr(1,2);
	return((short)strtoul(tmp.c_str(),NULL,16));
}

unsigned int IntelHexFmt::GetStartAdr(void)
{
	std::string tmp=buf.substr(3,4);
	return((unsigned int) strtoul(tmp.c_str(),NULL,16));
}

int IntelHexFmt::GetDataType(void)
{
	std::string tmp=buf.substr(7,2);
	return(atoi(tmp.c_str()));
}

void IntelHexFmt::GetDt(char *dt)
{
	int j,i=GetDataLen();

	for(j=0;j<2*i;j++)
	{
		//std::string tmp=buf.substr(2*j+9,2*j+10);
		dt[j]=buf[j+9];
		//dt[j]=(char)atoi(tmp.c_str());	
	}
}

void IntelHexFmt::operator=(std::string s)
{
	buf=s;
}
