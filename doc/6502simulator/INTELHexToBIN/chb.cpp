
/*---------------------------------------------------------------------------
* Copyright (c) 2006 苏洋
*----------------------------------------------------------------------------
* RELEASE:
* REVISION:     
*----------------------------------------------------------------------------
* PURPOSE:

*  chb.cpp : Defines the entry point for the console application.
*
*
* NOTE: 请保存文件的完整性。
*
*
------------------------------------------------------------------------------*/

#include "stdafx.h"
#include "chb.h"

#include<fstream>
#include<sstream>
#include<string>
#include"IntelHexFmt.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif



/////////////////////////////////////////////////////////////////////////////
// The one and only application object

CWinApp theApp;

using namespace std;

int _tmain(int argc, TCHAR* argv[], TCHAR* envp[])
{
	int nRetCode = 0;

	CString m_inFile,m_outFile;
	ifstream inf; 
	ofstream outf;

	long SegAdr=0,LineAdr=0,LastWriteAdr=0,finalAdr=0;
	std::string src;
	std::stringstream ostr;
	IntelHexFmt ihf;	//intel hex文件记录对象

	// initialize MFC and print and error on failure
	if (!AfxWinInit(::GetModuleHandle(NULL), NULL, ::GetCommandLine(), 0))
	{
		// TODO: change error code to suit your needs
		cerr << _T("Fatal Error: MFC initialization failed") << endl;
		nRetCode = 1;
	}
	else
	{
		// TODO: code your application's behavior here.
		
		CFileDialog idlg(TRUE,"hex",NULL,NULL,"hex文件(*.hex)|*.hex|");
		idlg.DoModal();
		m_inFile=idlg.GetPathName();

		CFileDialog odlg(TRUE,"bin",NULL,NULL,"Bin文件(*.Bin)|*.Bin|");
		odlg.DoModal();
		m_outFile=odlg.GetPathName();

	//	std::string s_in(m_inFile.GetBuffer(1));   //CString 转std::string
	//	m_inFile.ReleaseBuffer();
	//	std::string s_out(m_outFile.GetBuffer(1));   //CString 转std::string
	//	m_inFile.ReleaseBuffer();
		if(m_inFile=="" ||m_outFile=="")
		{	
			AfxMessageBox("源,目的文件名不能为空");
			return 0;
		}

		inf.open(m_inFile.operator LPCTSTR());
		if(!inf.is_open())
		{
			AfxMessageBox("源文件打开失败");
			return 0;
		}

		outf.open(m_outFile.operator LPCTSTR(),ios::binary);
		if(!outf.is_open())
		{
			AfxMessageBox("目的文件打开失败");
			return 0;
		}


		while(getline(inf,src))  //读源文件的一行
		{
			ihf=src;
			int rcdtype;
			rcdtype=ihf.GetDataType();  
			if(rcdtype== LINERCD)
			{
				char ladr[2];
				ihf.GetDt(ladr);
				LineAdr=strtoul(ladr,NULL,16); //得到线性地址
			}
			else if(rcdtype==SEGRCD)
			{
				char sadr[2];
				ihf.GetDt(sadr);
				SegAdr=strtoul(sadr,NULL,16);  //得到段地址

			}
			else if(rcdtype==DATARCD)
			{
				finalAdr=( LineAdr<<16)+(SegAdr<<4) + ihf.GetStartAdr(); //算出最终地址
				long diffSpace=finalAdr-LastWriteAdr;

				while(diffSpace--)
				{
					ostr<<"00"	;			 //在地址空隙填充0
				}
				short len = ihf.GetDataLen(); 
				char * dt= new char[2*len]; 
				ihf.GetDt(dt);				 //得到数据记录中的数据
				for(int i=0;i<len;i++)
				{					
					ostr <<dt[2*i]<<dt[2*i+1];//向流中写入数据
				}
				delete []dt;
				LastWriteAdr=finalAdr+len;

			}
			else  //此时则为结束记录,内容写入Bin文件
			{
				string c=ostr.str();
				string tmp;
				long len=0;
				char hexdt=0;
				len=c.length()/2;
				for(long i=0;i<len;i++)
				{
					tmp=c.substr(2*i,2);
					hexdt=strtoul(tmp.c_str(),NULL,16);
					outf.write(&hexdt,1);
				}

				inf.close();
				outf.close();
				AfxMessageBox("转换完成!");
					
			}
		}


		


	}

	return nRetCode;
}


