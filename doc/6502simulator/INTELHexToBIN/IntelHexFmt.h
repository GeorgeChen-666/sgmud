
/*---------------------------------------------------------------------------
* Copyright (c) 2006 苏洋
*----------------------------------------------------------------------------
* RELEASE:
* REVISION:     1.0
*----------------------------------------------------------------------------
* PURPOSE:
*  IntelHexFmt.h: interface for the IntelHexFmt class.
*
*
* NOTE: 请保存文件的完整性。
*
*
------------------------------------------------------------------------------*/
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_INTELHEXFMT_H__8891BBEE_B2E9_47F8_B7C9_EF5B90DEED52__INCLUDED_)
#define AFX_INTELHEXFMT_H__8891BBEE_B2E9_47F8_B7C9_EF5B90DEED52__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <string>

#define DATARCD 0
#define EOFRCD 1
#define SEGRCD 2
#define LINERCD 4

class IntelHexFmt  
{
private:
	std::string buf;		
public:		
	IntelHexFmt();
	IntelHexFmt(std::string s); 
	virtual ~IntelHexFmt();
	char GetSig(void);
	short GetDataLen(void);
	unsigned int GetStartAdr(void);
	int GetDataType(void);
	void GetDt(char *);
	void operator=(std::string);
};

#endif // !defined(AFX_INTELHEXFMT_H__8891BBEE_B2E9_47F8_B7C9_EF5B90DEED52__INCLUDED_)
