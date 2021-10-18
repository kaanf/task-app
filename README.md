# 🦄 Mobiroller Task Project

A simple e-commerce application that implements MVVM architecture.


## User Interfaces
Splash Screen              |  Login Screen                |  Register Screen
:-------------------------:|:-------------------------:|:-------------------------:
![](/images/splash_screen.png)  |  ![](/images/login_screen.png)  |  ![](/images/register_screen.png)

Home Screen                |  Add Product Screen            |  Product Details Screen
:-------------------------:|:------------------------------:|:-------------------------:
![](/images/home_screen.png)  |  ![](/images/add_product.png)  |  ![](/images/product_details.png)

## Software Architecture
Task project has many external frameworks and libraries such as Firebase (Real Time Database, Authentication, Storage Libraries), Picasso. The system works with some of these frameworks and libraries in Android Studio (Kotlin). System uses Firebase Realtime Database for storage all information about products including its pictures.

<center>
    <img src="/images/schema_first.jpg" alt="drawing" width="600"/> 
</center>

## Firebase Architecture
Task project’s firebase architecture consists of several components. These are Firebase Authentication, Firebase Functions, Firebase Database, Firebase Storage.

<center>
    <img src="/images/schema_two.jpg" alt="drawing" width="300"/> 
</center>

## 🏗️ Structure

### Model

**Product**: ` Contains product data model. `

### Repositories

**FirebaseRepository**: `Firebase Authentication functions.`

**ProductRepository**: `Product updates and CRUD operations.`

### User Interface (UI)

**BaseFragment & ViewModelFactory**: `Classes to fragment management.`

**AddProductFragment**: `Fragment that product information is saved into the database.`

**BottomSheetFragment**: `BottomSheetDialog fragment used for product updates.`

**HomeActivity**: `Container activity that hosts product fragments.`

**Login Fragment**: `Fragment that users can login with their Firebase account.`

**MainActivity**: `Container activity that hosts login and register fragments.`

**ProductDetailsFragment**: `Fragment that you can update and delete products.`

**ProductFragment**: `Fragment that data retrieved from Firebase Database is displayed in Recyclerview.`

**RegisterFragment**: `Fragment that users are registered to the application with their e-mail and password via Firebase Authentication.`

**SplashActivity**: `Fragment that Party Corgi greets you.`

### Utils

**AuthenticationState**: `Enum class that declares the user's authentication state in the application.`

**Extensions**: `Object class written to make Toast easier to use.`

**Router**: `Class that hosts intent usage.`

**UserLiveData**: `Authentication LiveData functions.`

**Validation**: `Object class that controls inputs entered by the user.`

### ViewModels

**AddProductViewModel**: `Connection between ProductRepository and AddProductFragment. `

**LoginViewModel**: `ViewModel that determines the user's Authentication State. (Connection between FirebaseRepository and LoginFragment.)`

**RegisterViewModel**: `Connection between FirebaseRepository and RegisterFragment`

**ProductViewModel**: `Empty.`

**ProductDetailsViewModel**: `Empty.`

- Users can register and login to the app.
- Users can view the products saved in the database.
- Users can save their favorite e-commerce products with product images to the database.
- Users can update and delete the products they want.


## 🖊️ Note

When I tried to sort datas using Realtime Database Queries, I got an error and I tried to solve that error for a long time. However, I uploaded it to the Github repository without sorting, unfortunately, in order not to overdue the assignment.
As a result of this project, I learned a lot of things. I may have mistakes in code design, I tried to do my best.
_Thank you for your understanding._
