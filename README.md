# 🦄 Firebase E-Commerce Project

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

## Code Review

### ProductRepository.kt

```kotlin title="ProductRepository.kt"
private const val PRODUCT_REFERENCE = "products"
private val firebaseDatabase = FirebaseDatabase.getInstance()  
private val firebaseStorage = FirebaseStorage.getInstance()  
private val databaseReference = firebaseDatabase.getReference(PRODUCT_REFERENCE)
```
The **firebaseDatabase** and **firebaseStorage** objects are the main entry point to database.  Also, I used `PRODUCT_REFERENCE` constant in retrieving reference from the database.

```kotlin
fun addProduct(product: Product, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {  
    val productReference = firebaseDatabase.getReference(PRODUCT_REFERENCE)  
    product.key = productReference.push().key ?: ""  
    product.timestamp = getCurrentTime()  
    val storageReference = firebaseStorage.reference.child("images/${imageNameFormatter()}")  
    storageReference.putFile(product.image.toUri())  
        .addOnSuccessListener {
	        it.storage.downloadUrl.addOnCompleteListener { url ->  
		        val imgLink = url.result.toString()  
		        product.image = imgLink  
		        productReference.child(product.key)  
                    .setValue(product)  
                    .addOnSuccessListener { onSuccessAction() }  
                    .addOnFailureListener { onFailureAction() }  
          }
        }
}
```
Products will be added as a child of the `PRODUCT_REFERENCE` node. I used `push()` to create an empty node with an **auto-generated key.** Next, I created a Product instance in `AddProductViewModel.kt` that I’ll save to the database and I stored the key of that product so I can refer to it later. Finally, I used `setValue()` to save the product.

```kotlin
private val productValues = MutableLiveData<List<Product>>()
```
I used _LiveData_ to notify the observers about product changes.

```kotlin
private lateinit var productsValueEventListener: ValueEventListener  
```
This is where I stored my event listener.

```kotlin
private fun listenForProductsValueChanges() {  
    productsValueEventListener = object : ValueEventListener {  
        override fun onCancelled(databaseError: DatabaseError) {}  
        override fun onDataChange(dataSnapshot: DataSnapshot) {  
            if (dataSnapshot.exists()) {  
                val products = dataSnapshot.children.mapNotNull {
	                it.getValue(Product::class.java)  
                }.toList()  
                productValues.postValue(products)  
            } else productValues.postValue(emptyList())  
        }  
    }  
    databaseReference.addValueEventListener(productsValueEventListener)  
}
```
I added _ValueListener_ as an anonymous inner class and I assigned it to `productsValueEventListener` field. `onDataChange(dataSnapshot: DataSnapshot)` gets triggered whenever data under the reference I attached the listener to gets changed; either new data is added or existing data is updated or deleted. Next, by calling `exists()` on the _DataSnapshot_ object I checked if the snapshot contains a **non-null value.** If there is data in the snapshot I get all of the direct children of the snapshot and I map each one to the **Product** object by calling `getValue(Product::class.java)` on a child. If data doesn’t exist I set an empty list as the new value of *LiveData*.

```kotlin
fun onProductsValuesChange(): LiveData<List<Product>> {  
    listenForProductsValueChanges()  
    return productValues  
}
```
This method just calls the function that attaches the listener and returns *LiveData*.

```kotlin
fun removeProductsValuesChangesListener() {  
    databaseReference.removeEventListener(productsValueEventListener)  
}
```
I want to listen for product updates when user on the home screen only. So, this method removes the passed in event listener, by calling `removeEventListener()`.

```kotlin
fun updateProduct(key: String, product: Product) {  
    databaseReference.child(key).setValue(product)  
}
``` 
Here I use the key to access the location of the product I want to update. I called `setValue()` with new product to update the content of the product.

```kotlin
fun deleteProduct(key: String) {  
    databaseReference.child(key).removeValue()  
}
```

I called `removeValue()` to delete product.

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

