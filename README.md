# SpringBoot_Microservices_ApacheCamel_Consul_REST_DSL_Aggregation
Apache Camel Rest Integration with spring boot and consul

This project holds the complete source code for below scenarios. Before importing these projects into your IDE, you have to do is, you need to download consul and start it by using below command.
```
C:\Users\subbu\Downloads\consul_1.2.2_windows_amd64>consul agent -dev
```

**About this project:**
1. Have created few spring boot projects for *Customer, Bank* and *Address*. *CommonBeans* project has commonly used POJO classes. **BankCustomerIntegration** project holds the actual logic of Apache Camel.
2. Customer, Bank and Address data holds temporarly in ArrayList in each controllers.
3. *CamelPullingData.java* file contains the logic for REST DSL Aggregation of GET method.
4. *CamelPushingData.java* file contains the logic for making REST POST calls. It also holds the logic for reverting first call data when second call is failed.
5. It also has the **DynamicRouting** concept in post call failure fore reverting the first call parsed data.

**Order to follow executing the projects:**
1. As I already mentioend, we need to start Consul with the helo of above command.
2. AddressApplication
3. BankApplication
4. CustomerApplication
5. BankCustomerIntegration

**Note**: You can see total how many number of services are running in Consul(http://localhost:8500), as on when you have started each service.
