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

If we want to *attach* our **activity** to dagger graph *to get instances from ancestor*, we simply create a **@Subcomponent** for it.
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
- Map MainActivity to ActivityBuilder(**So dagger can understand MainActivity will be injected.**)

        @Module
        public abstract class ActivityBuilder {
    
            @Binds
            @IntoMap
            @ActivityKey(MainActivity.class)
            abstract AndroidInjector.Factory<? extends Activity> bindMainActivity(MainActivityComponent.Builder builder);
        }

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
 
 Dagger 2 includes **HasActivityInjector** as a new ( that provides **AndroidInjector** ) and should implement in Application class, also we should inject **DispatchingAndroidInjector** that’s mean return **activityInjector**, in this way we’ll use **AndroidInjection.inject()** for activity and fragment.
 
 ![Dispatch](https://github.com/anjandebnath/ChatModule/blob/master/img/DispatchInjector.PNG)
 
 We can use AndroidInjection.inject(this) in activity after inject DispatchingAndroidInjector in application.
 AndroidInjection.inject(this) should calls in onCreate method before super. That’s all!!
 
 - Application has activities. That is why Application class implement **HasActivityInjector** interface.
 - Activity has fragments. That is why Activity class implement **HasFragmentInjector** interface.
 
 We call **AndroidInjection.inject(this)** in MainActivity and **provide whatever instance** we want in **MainActivityModule**.
 [link1](https://medium.com/@iammert/new-android-injector-with-dagger-2-part-1-8baa60152abe) described more
 
 ## Notes
 
 > Repetitive Task
 - **UI subcomponents** (MainActivityComponent) are just like bridge in the graph.
 - Whenever we add our UI component as new subcomponent, we have to map our activity in **ActivityBuilder module**. This is also repetitive task.
 
 ## Don’t Repeat Yourself
 
 ### Attach Activity to Dagger in a new way
 We can easily attach activities/fragments to dagger graph *to get instances from ancestor*, with new annotation **@ContributesAndroidInjector**
 The new graph can be depicted as below 
 
 ![gr](https://github.com/anjandebnath/ChatModule/blob/master/img/graph2.png)
 
 So **ActivityBuilder** Module code will be changed 
 from 
 
         @Module
         public abstract class ActivityBuilder {
             
             @Binds
             @IntoMap
             @ActivityKey(MainActivity.class)
             abstract AndroidInjector.Factory<? extends Activity> bindMainActivity(MainActivityComponent.Builder builder);
         }
         
 to
         
         @Module
         public abstract class ActivityBuilder {
         
             @ContributesAndroidInjector(modules = MainActivityModule.class)
             abstract MainActivity bindMainActivity();
         
         }
         
         
### AndroidInjector helps us to simplify our App Component

We can use **AndroidInjector<T>** in your dagger components to reduce boilerplate.
- **build()** and **seedInstance()** is already defined in **AndroidInjector.Builder** class. So we can get rid of them and extend our Builder from **AndroidInjection.Builder<Application>**.
- AndroidInjector interface has **inject()** method in it. So we can remove **inject()** method and extend our AppComponent interface from **AndroidInjector<Application>**

So **AppComponent** code will be changed 
from

    @Component(modules = {
            AndroidInjectionModule.class,
            AppModule.class,
            ActivityBuilder.class})
    public interface AppComponent {
    
        @Component.Builder
        interface Builder {
            @BindsInstance Builder application(Application application);
            AppComponent build();
        }
    
        void inject(AndroidSampleApp app);
    }
    
to

    @Component(modules = {
             AndroidInjectionModule.class,
             AppModule.class,
             ActivityBuilder.class})
    interface AppComponent extends AndroidInjector<AndroidSampleApp> {
         @Component.Builder
         abstract class Builder extends AndroidInjector.Builder<AndroidSampleApp> {}
    }       


###  DaggerActivity, DaggerFragment, DaggerApplication
We can use **DaggerActivity**, **DaggerFragment**, **DaggerApplication** to reduce boilerplate in our Activity/Fragment/Application.

We need to extend our Application class from DaggerApplication.
So **Application** class will be changed 
from 

    public class AndroidSampleApp extends Application implements HasActivityInjector {
    
        @Inject
        DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;
    
        @Override
        public void onCreate() {
            super.onCreate();
            DaggerAppComponent
                    .builder()
                    .application(this)
                    .build()
                    .inject(this);
        }
    
        @Override
        public DispatchingAndroidInjector<Activity> activityInjector() {
            return activityDispatchingAndroidInjector;
        }
    }
to

    public class AndroidSampleApp extends DaggerApplication {
    
        @Override
                public void onCreate() {
                    super.onCreate();
         
                }
                
        @Override
        protected AndroidInjector<? extends AndroidSampleApp> applicationInjector() {
            return DaggerAppComponent.builder().create(this);
        }
    }    
    
    

Reference 
- https://medium.com/@iammert/new-android-injector-with-dagger-2-part-1-8baa60152abe
- https://medium.com/@iammert/new-android-injector-with-dagger-2-part-2-4af05fd783d0
- https://android.jlelse.eu/new-android-injector-with-dagger-2-part-3-fe3924df6a89    