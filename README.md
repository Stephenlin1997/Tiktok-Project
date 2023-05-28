# Tiktok-Project
Tech Stack Used: 
Front End: UniApp, VueJs, HTML, JavaScript, CSS, Flex, UniCloud
BackEnd: SprintBoot, Redis, Minio, TencentCloud
DB: MangoDb, MariaDB(MySQL)
                 
<img width="745" alt="截屏2023-05-28 下午4 05 34" src="https://github.com/Stephenlin1997/Tiktok-Project/assets/80722360/c5cfea48-a8b1-4e92-96db-77623df33fd5">

## Architecture
In the diagram, the access layer represents the user side, where users can access and initiate requests to the backend through a mobile app. Although we have both iPhone and Android phones, our API interfaces are unified. If there is a mini-program, a web page, or even an app related to the HarmonyOS, they can also make requests to our API.

- Nginx (or Alibaba's Tengine) serves as the gateway, providing high availability and load balancing by distributing requests to the backend cluster services, which are the actual components we need to develop.
- Service Cluster: Developed using the Spring Boot, Spring, and MyBatis frameworks, the final deployment takes the form of a cluster.
- Redis: Distributed cache.
- RabbitMQ: Distributed message middleware.
- MinIO: Distributed object storage.
- Tencent Cloud SMS: Integrated to send SMS verification codes.
- Nacos Cluster: Registers services and acts as a configuration center.
- Data Layer: Mysql/MariaDB, MongoDB.

## Phone number registration function flowchat: 
<img width="741" alt="截屏2023-05-28 下午4 08 49" src="https://github.com/Stephenlin1997/Tiktok-Project/assets/80722360/eca1219c-2ba0-4d62-a2e6-2adb536137bd">

## CDN for Short Video Files upload
<img width="793" alt="截屏2023-05-28 下午4 14 03" src="https://github.com/Stephenlin1997/Tiktok-Project/assets/80722360/96258455-5ee0-4270-8512-de25304f412f">
<img width="772" alt="截屏2023-05-28 下午4 14 53" src="https://github.com/Stephenlin1997/Tiktok-Project/assets/80722360/04e15ed4-04a4-4764-9e91-dbddf779f090">


CDNs have servers distributed across various geographical locations. When you upload your content to a CDN, it gets replicated and stored on multiple servers. This enables the CDN to deliver the content from the server closest to the end user, reducing latency and improving overall performance. Users can access the content faster, resulting in a better user experience.

## APP Function Display
![61604a7009da821706401136](https://github.com/Stephenlin1997/Tiktok-Project/assets/80722360/79f95ac6-753e-4691-ae8d-e8c4ee7913aa)>
### Comment Module
![6160498f09e3306b06401136](https://github.com/Stephenlin1997/Tiktok-Project/assets/80722360/0c4499a7-195d-4d12-99b0-9ccf290689d2)
### User Personal Info Setting
![61604a7b0937214806401136](https://github.com/Stephenlin1997/Tiktok-Project/assets/80722360/6a42a904-ba68-4b02-abfd-9d669866693a)

## Function Components:
- Video module
- Message module
- Fans module
- Notification module
- User module



