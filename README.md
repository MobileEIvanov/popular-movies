# Popular Movies

This project resolves the decade long problem when you sit at home and wonder what to watch. 

## Project Architecture

The project is build following the **Model-View-Presenter** Architecture and uses repository pattern for its data layer.

The project information is loaded from the [link The Movie Database](https://www.themoviedb.org/documentation/api?language=en).

### Prerequisites

You will have to create your own account in order to request information from the API.
The API Key should be placed in **RestClient.java** which is responsible for creating the requests.

### Coding style 
The project follows the general coding style for Java and Android.


## Built With

*[Retrofit2](http://square.github.io/retrofit/) - Networking library from Square
*[JakeWharton's RxJava2 for Android](https://github.com/ReactiveX/RxAndroid) - Reactive solution for Android framework
*[Picasso](http://square.github.io/picasso/) - Image processing library
*[Stetho Logger](http://facebook.github.io/stetho/) - Debug bridge from Facebook. Used for network request debugging.

## Contributing

Please read [CONTRIBUTING.md] for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

* **EIvanov** - *Initial work*

## Acknowledgments

* [Circular Reveal Animation](https://stackoverflow.com/questions/41132475/translation-animation-starts-off-screen-instead-of-where-it-belongs) 
* [Endless scroll for Recycle view](https://stackoverflow.com/questions/35673854/how-to-implement-infinite-scroll-in-gridlayout-recylcerview)
* [Grid Items size](https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns)
* [Network Connection status](https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details


