### DI core principal
The core principal of dependency injection is `A class shouldn’t know anything about how it is injected.`

- Each project should contain an Application class.
- Application class build a graph using **AppComponent**.


-  AppComponent has **@Component** annotation top of its class. 
-  When AppComponent is build with its **modules**, we have a graph with all *provided instances* in our graph.
-  For instance, If **AppModule** provides ApiService, we will have ApiService instance when we build component which has app module.


dagger graph with visual graphic is depicted here 
![graph](https://github.com/anjandebnath/ChatModule/blob/master/img/graph1.png)

#### Attach Activity to Dagger

If we want to attach our **activity** to dagger graph to get instances from ancestor, we simply create a **@Subcomponent** for it.
Then, last step we have to take, we need to tell ancestor about subcomponent info. So all subcomponents have to be known by its ancestor.

**Package Structure and Activity attach**

![subcomponent](https://github.com/anjandebnath/ChatModule/blob/master/img/package.png)

### @Component and @Component.Builder
![component](https://github.com/anjandebnath/ChatModule/blob/master/img/component.PNG)