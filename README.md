# moviesAppChallenge

#### **Architecture**
I am using Clean Architecture. I think it is a good choice because it isolates all different layers of an application. It is also a good choice to follow the SOLID principles.

For the UI, I'm using the Model-View-Presenter approach. It helps to separate the view logic from the business logic which easier to test.

#### **Storage**

Since it is only needed to store a bunch of Movies with no complicated queries, I'm storing them as json on the SharedPreferences.

#### **Testing**
Using UnitTests to test the business logic. Since there is no dependencies on the Android framework, all the logic can be unit tested (Repositories, Presenters, etc...).

Using InstrumentationTests to test the UI with Espresso (Activities)  

#### **Libraries**
Apart from the usual libraries every application may contain, I'm using the following libraries:

- KodeIn: Dependency Injection for Kotlin
- RxKotlin, RxAndroid and RxBindings: to use reactive programming.

#### **Other**

- I'm using RxJava also for the asynchronous tasks since Kotlin's coroutines are still experimental.
- Sorry for the design, I'm not good with colors :) I prefer to trust on a good designer.

