# spring-boot-file-handling
This Spring Boot based Spring MVC WebService handles file upload. The file is stored in the Temp directory on the host machine and the file's metadata is stored in the In-Memory HSQL Database

Files should be posted to this URL
http://localhost:8080/file

File can be downloaded using the URL with a GET Request
http://localhost:8080/file/<fileId>

File's Metadata can be retrieved using the following URL
http://localhost:8080/file/<fileId>/metadata
