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

**@Component:** Component is a graph. We build a component. Component will **provide injected instances by using modules**.

**@Component.Builder:** We might want to **bind some instance to Component**. In this case we can create an interface with **@Component.Builder** annotation and **add whatever method we want to add to builder**. 
 In my case I wanted to add Application to my AppComponent.

>Note: If you want to create a Builder for your Component, your Builder interface has to has a build(); method which returns your Component.


### Inject Into AppComponent
we can **bind our application instance** to our Dagger graph.
![bind instance](https://github.com/anjandebnath/ChatModule/blob/master/img/Bindinstance.PNG)


### Application Component
This Component is root of our dagger graph. Application component is providing 3 module in our app.

**AndroidInjectionModule** : 
- We didn’t create this. It is an internal class in Dagger 2.10. 
- The AndroidInjectionModule helps handling the android framework classes like Activity, Fragment, Service and Broadcasts.
- *AndroidInjectionModule* binds your **app.fragment** to dagger. But If you want to use injection in **v4.fragment** then you should add **AndroidSupportInjectionModule** to your AppComponent modules.

**ActivityBuilder** : 
- To map all activity and pass it to Dagger2 we will create the ActivityBuilderModule. 
- We created this module. This is a given module to dagger. We map all our activities here. And Dagger know our activities in compile time.

![ac](https://github.com/anjandebnath/ChatModule/blob/master/img/ActivityBuilder1.PNG)

**AppModule**: 
- We provide retrofit, okhttp, persistence db, shared pref etc here. 
- **We have to add our subcomponents to AppModule**. So our dagger graph will understand that.
![Apmodule](https://github.com/anjandebnath/ChatModule/blob/master/img/AppModule1.PNG)


### Activity Components
Each Activity has module and component but the components are Subcomponents that we define in AppModule
- For **MainActivity** it will have **MainActivityModule** and **MainActivityComponent**

**MainActivityModule**: This module provides main activity related instances.

**MainActivityComponent**: This component is just a bridge to **MainActivityModule**. We don’t add **inject()** and **build()** method to this component. MainActivityComponent has these methods from ancestor **AndroidInjector** class. AndroidInjector class is new dagger-android class which exist in dagger-android framework. 

> Note: We create our MainActivityComponent with our *MainActivity* class. So dagger will attach our activity to it’s graph.

![](https://github.com/anjandebnath/ChatModule/blob/master/img/ActivityComponent1.PNG)


### Fragment Components
 - **Application** knows **Activities** with a mapping module(**ActivityBuilder** module and add as module to AppComponent). And we add our activities to AppModule as subcomponent.
 - Same relationship between **Activity** and its **Fragments**. We will create a **FragmentBuilder** module and add as module to **@SubComponent** MainActivityComponent.
 
 ### DispatchingAndroidInjector<T>
 Performs **members-injection on instances** of core Android types (e.g. Activity, Fragment) that are constructed by the Android framework and not by Dagger.
 [link1](https://medium.com/@iammert/new-android-injector-with-dagger-2-part-1-8baa60152abe) described more