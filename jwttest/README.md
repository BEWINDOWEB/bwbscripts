# used for test jwt algorithm

# try
HTTP GET 127.0.0.1:8080/test?algo=hmac&secretKeyBitLen=256&hashLen=256&times=10

2020-10-28 20:59:51.861  INFO 16652 --- [nio-8080-exec-1] com.bewindweb.jwt.algtest.AlgController  : HS256 encoding, hashLen=256, secretKeyBitLen=256, startTime=1603889991452, costTime=40.9
2020-10-28 20:59:51.863  INFO 16652 --- [nio-8080-exec-1] com.bewindweb.jwt.algtest.AlgController  : signlen=43, token for HS256 is eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImtpZCI6InJhbmRvbUtJRCJ9.eyJhdWQiOiJhdWRpZW5jZSIsInN1YiI6ImNsaWVudGlkMTIzIiwiaXNzIjoiaXNzdWVyIiwiZXhwIjoxNjAzOTc2MzkxLCJpYXQiOjE2MDM4ODk5OTEsInVybCI6Ind3dy5iZXdpbmRvd2ViLmNvbSJ9.XvHzClsatSEFlzTD-g1lq1jJ7x8CNNuJcMTs0vlKb7o
2020-10-28 20:59:51.886  INFO 16652 --- [nio-8080-exec-1] com.bewindweb.jwt.algtest.AlgController  : HS256 decoding, hashLen=256, secretKeyBitLen=256, startTime=1603889991863, costTime=2.3