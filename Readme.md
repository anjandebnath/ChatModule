### DI core principal
The core principal of dependency injection is `A class shouldn’t know anything about how it is injected.`

- Each project should contain an Application class.
- Application class build a graph using **AppComponent**.


-  AppComponent has **@Component** annotation top of its class. 
-  When AppComponent is build with its **modules**, we have a graph with all *provided instances* in our graph.
-  For instance, If **AppModule** provides ApiService, we will have ApiService instance when we build component which has app module.
