#-*-coding=utf8-*-
import urllib2,sys,os
link="http://web.nvd.nist.gov/view/vuln/detail?vulnId="
def getHtml(no):
	tempLink=link+no
	page=urllib2.urlopen(tempLink)
	html=page.read()
	f=open(sys.path[0]+'/'+no+'.txt',"w+")
	f.write(html)
	print no+'load successfully'
	f.close()
if __name__=="__main__":
	noList=[
			"CVE-2011-2416","CVE-2011-2415","CVE-2011-2414","CVE-2011-2410","CVE-2011-2139","CVE-2011-2138","CVE-2011-2136",\
			"CVE-2011-2135","CVE-2011-2137","CVE-2011-2134","CVE-2011-2130","CVE-2008-7298","CVE-2011-2344","CVE-2011-1001",\
			"CVE-2011-2110","CVE-2011-1823","CVE-2010-4804","CVE-2011-2107","CVE-2011-0628","CVE-2011-0419","CVE-2011-0627",\
			"CVE-2011-0626","CVE-2011-0625","CVE-2011-0624","CVE-2011-0623","CVE-2011-0622","CVE-2011-0621","CVE-2011-0620",\
			"CVE-2011-0619","CVE-2011-0618","CVE-2011-0579","CVE-2011-1840","CVE-2011-1149","CVE-2011-1717","CVE-2011-0611",\
			"CVE-2011-0609","CVE-2011-0680","CVE-2010-0113","CVE-2010-4214","CVE-2010-4213","CVE-2010-4212","CVE-2010-3652",\
			"CVE-2010-3650","CVE-2010-3649","CVE-2010-3648","CVE-2010-3647","CVE-2010-3646","CVE-2010-3645","CVE-2010-3644",\
			"CVE-2010-3643","CVE-2010-3642","CVE-2010-3641","CVE-2010-3640","CVE-2010-3639","CVE-2010-3636","CVE-2010-3654",\
			"CVE-2010-2884","CVE-2010-1807","CVE-2009-3698","CVE-2009-2999","CVE-2009-2692","CVE-2009-2656","CVE-2009-2348",\
			"CVE-2009-1754","CVE-2009-1442","CVE-2009-0608","CVE-2009-0607","CVE-2009-0606","CVE-2009-0475","CVE-2008-0985",\
			"CVE-2008-0986","CVE-2007-5266","CVE-2007-5267","CVE-2007-5268","CVE-2007-5269","CVE-2007-2445","CVE-2006-5793"]	
	for no in noList:
		getHtml(no)

		
