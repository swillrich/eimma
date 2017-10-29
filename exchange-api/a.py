from urllib.parse import urlparse, parse_qs

query = parse_qs(urlparse("http://ad.de/index.html?test=2012-12-12").query)
print(query)
a = str(query['test']).replace('[','').replace(']','').replace('\'','')
print(a)
aa = a.split('-')
print(aa[0])
arr = [int(aa[0]),int(aa[1]),int(aa[2])]
print(arr)