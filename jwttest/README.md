# used for testing jwt algorithm

# sapmle input
HTTP GET 127.0.0.1:8080/test?algo=hmac&secretKeyBitLen=256&hashLen=256&times=10  

* algo: hmac, rsa, ecdsa
* hashLen: 256, 384, 512

* secretKeyBitLen: the bit len you want to test
* times: loop times

# sapmle output
```
2020-10-28 20:59:51.861  INFO 16652 --- [nio-8080-exec-1] com.bewindweb.jwt.algtest.AlgController  : HS256 encoding, hashLen=256, secretKeyBitLen=256, startTime=1603889991452, costTime=40.9
2020-10-28 20:59:51.863  INFO 16652 --- [nio-8080-exec-1] com.bewindweb.jwt.algtest.AlgController  : signlen=43, token for HS256 is eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImtpZCI6InJhbmRvbUtJRCJ9.eyJhdWQiOiJhdWRpZW5jZSIsInN1YiI6ImNsaWVudGlkMTIzIiwiaXNzIjoiaXNzdWVyIiwiZXhwIjoxNjAzOTc2MzkxLCJpYXQiOjE2MDM4ODk5OTEsInVybCI6Ind3dy5iZXdpbmRvd2ViLmNvbSJ9.XvHzClsatSEFlzTD-g1lq1jJ7x8CNNuJcMTs0vlKb7o
2020-10-28 20:59:51.886  INFO 16652 --- [nio-8080-exec-1] com.bewindweb.jwt.algtest.AlgController  : HS256 decoding, hashLen=256, secretKeyBitLen=256, startTime=1603889991863, costTime=2.3
```

# key generation
```
// rsa
openssl genrsa -out RSA_512_pri.pem 512
openssl rsa -in RSA_512_pri.pem -out RSA_512_pub.txt -pubout
openssl genrsa -out RSA_1024_pri.pem 1024
openssl rsa -in RSA_1024_pri.pem -out RSA_1024_pub.txt -pubout
openssl genrsa -out RSA_2048_pri.pem 2048
openssl rsa -in RSA_2048_pri.pem -out RSA_2048_pub.txt -pubout
openssl pkcs8 -topk8 -inform PEM -in RSA_512_pri.pem -outform PEM -nocrypt > RSA_512_pri.txt
openssl pkcs8 -topk8 -inform PEM -in RSA_1024_pri.pem -outform PEM -nocrypt > RSA_1024_pri.txt
openssl pkcs8 -topk8 -inform PEM -in RSA_2048_pri.pem -outform PEM -nocrypt > RSA_2048_pri.txt

// ecdsa
openssl ecparam -name prime256v1 -genkey -out ECDSA_p256_pri.pem
openssl ec -in ECDSA_p256_pri.pem -pubout -out ECDSA_p256_pub.txt
openssl pkcs8 -topk8 -inform PEM -in ECDSA_p256_pri.pem -outform PEM -nocrypt >ECDSA_p256_pri.txt



```